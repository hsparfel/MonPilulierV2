package com.pouillos.monpilulier.activities.newx;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

public class NewUserActivity extends AppCompatActivity {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private TextView textNom;
    private TextView textSexe;
    private TextView textDate;
    private String sexe;
    private Date date;
    private Class<?> activitySource;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        textNom = findViewById(R.id.textNom);
        textSexe = findViewById(R.id.textSexe);
        textDate = findViewById(R.id.textDate);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);


        traiterIntent();

        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retourPagePrecedente();
            }
        });

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //rediger les verifs de remplissage des champs
                if (!isRempli(textNom)) {
                    return;
                }
                if (isExistant(textNom)) {
                    return;
                }
                if (!isRempli(textSexe, sexe)) {
                    return;
                }
                if (!isRempli(textDate, date)) {
                    return;
                }

                //enregistrer en bdd
                saveToDb(textNom, date, sexe);


                //retour
                retourPagePrecedente();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragment.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMaxDate(new Date().getTime());
                TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()<10) {
                    dateMois = "0"+dateMois;
                }
                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                tv1.setText("date de naissance: "+dateString);
                textDate.setError(null);
                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                try{
                    date = df.parse(dateString);
                }catch(ParseException e){
                    System.out.println("ERROR");
                }
            }
        });
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonHomme:
                if (checked)
                    sexe="Homme";
                textSexe.setError(null);
                break;
            case R.id.radioButtonFemme:
                if (checked)
                    sexe="Femme";
                textSexe.setError(null);
                break;
        }
    }

    public void traiterIntent() {
        intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewUserActivity.this, activitySource);
        nextActivity.putExtra("activitySource", NewUserActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public boolean isRempli(TextView textView) {
        if (TextUtils.isEmpty(textView.getText())) {
            textView.requestFocus();
            textView.setError("Saisie Obligatoire");
            return false;
        } else {
            return true;
        }
    }

    public boolean isRempli(TextView textView, String string) {
        if (TextUtils.isEmpty(string)) {
            textView.setError("");
            return false;
        } else {
            return true;
        }
    }

    public boolean isRempli(TextView textView, Date date) {
        if (date == null) {
            textView.setError("Sélection Obligatoire");
            return false;
        } else {
            return true;
        }
    }

    public boolean isExistant(TextView textView) {
        List<Utilisateur> listAllUtilisateur = Utilisateur.listAll(Utilisateur.class);
        boolean reponse = false;
        for (Utilisateur utilisateur : listAllUtilisateur) {
            if (textView.getText().toString().equals(utilisateur.getName())) {
                textView.requestFocus();
                textView.setError("l'utilisateur existe déjà");
                reponse = true;
            }
        }
        return reponse;
    }

    public void saveToDb(TextView textNom, Date date, String sexe) {
        Utilisateur utilisateur = new Utilisateur(textNom.getText().toString(), date, sexe);
        utilisateur.setActif(false);
        utilisateur.save();
    }

}
