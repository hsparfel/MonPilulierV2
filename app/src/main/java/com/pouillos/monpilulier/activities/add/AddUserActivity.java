package com.pouillos.monpilulier.activities.add;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;

import com.pouillos.monpilulier.activities.MainActivity;
import com.pouillos.monpilulier.activities.recherche.ChercherMedecinOfficielActivity;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragment;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AddUserActivity extends AppCompatActivity implements Serializable, AdapterView.OnItemClickListener, BasicUtils {

    @State
    Utilisateur activeUser;
    @State
    Date dateOfBirth;
    @State
    Departement departement;
    @State
    Utilisateur userToCreate;

    private List<Utilisateur> listUserBD;
    private List<Departement> listDepartementBD;

    @BindView(R.id.selectDepartement)
    AutoCompleteTextView selectedDepartement;
    @BindView(R.id.listDepartement)
    TextInputLayout listDepartement;
    @BindView(R.id.floating_action_button)
    FloatingActionButton fab;
    @BindView(R.id.textName)
    TextInputEditText textName;
    @BindView(R.id.layoutName)
    TextInputLayout layoutName;
    @BindView(R.id.chipMan)
    Chip chipMan;
    @BindView(R.id.chipWoman)
    Chip chipWoman;
    @BindView(R.id.chipSexe)
    com.google.android.material.chip.ChipGroup chipSexe;
    @BindView(R.id.textBirthday)
    TextInputEditText textBirthday;
    @BindView(R.id.layoutBirthday)
    TextInputLayout layoutBirthday;


    /*
    private Date date;
    private Class<?> activitySource;
    private Intent intent;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_add_user);

        listUserBD = Utilisateur.listAll(Utilisateur.class);

        AddUserActivity.AsyncTaskRunnerDepartement runnerDepartement = new AddUserActivity.AsyncTaskRunnerDepartement();
        runnerDepartement.execute();

        ButterKnife.bind(this);
        selectedDepartement.setOnItemClickListener(this);
        textBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(view);
                    textBirthday.clearFocus();
                }
            }
        });
    }

    @OnClick(R.id.floating_action_button)
    public void fabClick() {
        //TODO verif si existant
        boolean fieldsCompleted = checkFields();


        if (fieldsCompleted) {
            //TODO lancer creation profil
            //Toast toast = Toast.makeText(AddUserActivity.this, "valide", Toast.LENGTH_LONG);
            //toast.show();
            userToCreate = new Utilisateur();
            userToCreate.setName(textName.getText().toString());
            if (chipMan.isChecked()) {
                userToCreate.setSexe("Homme");
            } else {
                userToCreate.setSexe("Femme");
            }
            userToCreate.setDateDeNaissance(dateOfBirth);
            userToCreate.setDepartement(departement);
            userToCreate.setActif(true);
            for (Utilisateur currentUser : listUserBD) {
                currentUser.setActif(false);
                currentUser.save();
            }
            Long userToCreateId = userToCreate.save();
            Intent intent = new Intent(AddUserActivity.this, AddProfilActivity.class);
            intent.putExtra("userId", userToCreateId);
            startActivity(intent);
            finish();
        } else {
            Toast toast = Toast.makeText(AddUserActivity.this, "Saisie non valide", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean checkFields(){
        boolean bool;
        if (!isFilled(textName)) {
            layoutName.setError("Obligatoire");
        } else {
            layoutName.setError(null);
        }
        if (isChecked(chipSexe)){
            chipMan.setError(null);
            chipWoman.setError(null);
        } else {
            chipMan.setError("error");
            chipWoman.setError("error");
        }
        if (!isFilled(dateOfBirth)){
            layoutBirthday.setError("Obligatoire");
        } else {
            layoutBirthday.setError(null);
        }
        if (!isFilled(departement)){
            selectedDepartement.setError("Obligatoire");
        } else {
            selectedDepartement.setError(null);
        }

        boolean isNotExistant = true;
        for (Utilisateur currentUser : listUserBD) {
            if (currentUser.getName().equalsIgnoreCase(textName.getText().toString())) {
                isNotExistant = false;
            }
        }
        if (isNotExistant) {
            layoutName.setError(null);
        } else {
            layoutName.setError("Déjà Existant");
        }
        bool = isFilled(textName) && isChecked(chipSexe) && isFilled(dateOfBirth) && isFilled(departement) && isNotExistant;
        return bool;
    }


    public class AsyncTaskRunnerDepartement extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            listDepartementBD = Departement.listAll(Departement.class);
            return null;
        }

        protected void onPostExecute(Void result) {
            if (listDepartementBD.size() == 0) {
                listDepartement.setVisibility(View.INVISIBLE);
            } else {
                List<String> listDepartementString = new ArrayList<>();
                String[] listDeroulanteDepartement;
                listDeroulanteDepartement = new String[listDepartementBD.size()];

                for (Departement departement : listDepartementBD) {
                    listDepartementString.add(departement.getNumero()+" - "+departement.getNom());
                }
                listDepartementString.toArray(listDeroulanteDepartement);
                ArrayAdapter adapter = new ArrayAdapter(AddUserActivity.this, R.layout.list_item, listDeroulanteDepartement);
                selectedDepartement.setAdapter(adapter);
                listDepartement.setVisibility(View.VISIBLE);
            }
        }
    }



    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragment.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMaxDate(new Date().getTime());
                //TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()+1<10) {
                    dateMois = "0"+dateMois;
                }
                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                textBirthday.setText(dateString);
                //textBirthday?.setText("date de naissance: "+dateString);
                //textDate.setError(null);
                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                try{
                    dateOfBirth = df.parse(dateString);
                }catch(ParseException e){
                    System.out.println("ERROR");
                }
            }
        });
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //masquer clavier
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString().substring(0,2);
        List<Departement> listDepartementSelected = Departement.find(Departement.class, "numero = ?", item);
        if (listDepartementSelected.size() !=0){
            departement = listDepartementSelected.get(0);
        }
    }
}

