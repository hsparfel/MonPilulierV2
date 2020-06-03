package com.pouillos.monpilulier.activities.add;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;

import com.pouillos.monpilulier.activities.AccueilActivity;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.RdvContact;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragment;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AddUserActivity extends NavDrawerActivity implements Serializable, AdapterView.OnItemClickListener, BasicUtils {

    @State
    Utilisateur userToModify;
    @State
    Date dateOfBirth;
    @State
    Departement departement;
    @State
    Utilisateur userToCreate;

    private List<Utilisateur> listUserBD;
    private List<Departement> listDepartementBD;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.selectDepartement)
    AutoCompleteTextView selectedDepartement;
    @BindView(R.id.listDepartement)
    TextInputLayout listDepartement;
    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;
    @BindView(R.id.fabCancel)
    FloatingActionButton fabCancel;
    @BindView(R.id.fabEdit)
    FloatingActionButton fabEdit;
    @BindView(R.id.fabDelete)
    FloatingActionButton fabDelete;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_add_user);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        activeUser=findActiveUser();

        setTitle(getResources().getString(R.string.title_connexion));
        //traiterIntent();
        AddUserActivity.AsyncTaskRunnerDepartement runnerDepartement = new AddUserActivity.AsyncTaskRunnerDepartement();
        runnerDepartement.execute();

        fabCancel.hide();

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



    @Override
    public void enableItems(Boolean bool) {
        if (bool) {
            chipMan.setEnabled(true);
            chipWoman.setEnabled(true);
            listDepartement.setEnabled(true);
            layoutBirthday.setEnabled(true);
            fabEdit.hide();
            fabDelete.hide();
            fabSave.show();
            fabCancel.show();
        } else {
            chipMan.setEnabled(false);
            chipWoman.setEnabled(false);
            listDepartement.setEnabled(false);
            layoutBirthday.setEnabled(false);
            fabEdit.show();
            fabDelete.show();
            fabSave.hide();
            fabCancel.hide();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.fabCancel)
    public void setfabCancelClick() {
        dateOfBirth= userToModify.getDateDeNaissance();
        textName.setText(userToModify.getName());

        textBirthday.setText(DateUtils.ecrireDate(userToModify.getDateDeNaissance()));

        int position = 0;
        for (Departement departement : listDepartementBD) {
            if (departement.getId().longValue() != userToModify.getDepartement().getId()) {
                position ++;
            } else {
                break;
            }
        }
        selectedDepartement.setText(selectedDepartement.getAdapter().getItem(position).toString(), false);

        if (userToModify.getSexe().equalsIgnoreCase(getResources().getString(R.string.text_man))) {
            chipMan.setChecked(true);
            chipWoman.setChecked(false);
        } else {
            chipMan.setChecked(false);
            chipWoman.setChecked(true);
        }

        enableItems(false);
    }

    @OnClick(R.id.fabDelete)
    public void setfabDeleteClick() {
        deleteItem(AddUserActivity.this, userToModify, AccueilActivity.class,true);

        /*new MaterialAlertDialogBuilder(AddUserActivity.this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
        .setNegativeButton(R.string.dialog_delete_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AddUserActivity.this, R.string.dialog_delete_negative_toast, Toast.LENGTH_LONG).show();
            }
        })
        .setPositiveButton(R.string.dialog_delete_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AddUserActivity.this, R.string.dialog_delete_positive_toast, Toast.LENGTH_LONG).show();
                userToModify.delete();
                ouvrirActiviteSuivante(AddUserActivity.this, AccueilActivity.class);
            }
        })
        .show();*/
    }

    @OnClick(R.id.fabEdit)
    public void setFabEditClick() {
       enableItems(true);
    }

    @OnClick(R.id.fabSave)
    public void fabSaveClick() {
        boolean fieldsValidated = checkFields();
        Long userToCreateId = null;
        if (fieldsValidated) {
            if(userToModify != null) {
                userToModify.setName(textName.getText().toString());
                if (chipMan.isChecked()) {
                    userToModify.setSexe(getResources().getString(R.string.text_man));
                } else {
                    userToModify.setSexe(getResources().getString(R.string.text_woman));
                }
                userToModify.setDateDeNaissance(dateOfBirth);
                userToModify.setDepartement(departement);
                userToModify.save();
        } else {
                userToCreate = new Utilisateur();
                userToCreate.setName(textName.getText().toString());
                if (chipMan.isChecked()) {
                    userToCreate.setSexe(getResources().getString(R.string.text_man));
                } else {
                    userToCreate.setSexe(getResources().getString(R.string.text_woman));
                }
                userToCreate.setDateDeNaissance(dateOfBirth);
                userToCreate.setDepartement(departement);
                userToCreate.setActif(true);
                for (Utilisateur currentUser : listUserBD) {
                    currentUser.setActif(false);
                    currentUser.save();
                }
                userToCreateId = userToCreate.save();
            }
            if (userToModify != null) {
                ouvrirActiviteSuivante(AddUserActivity.this, AccueilActivity.class,true);
            } else {
                ouvrirActiviteSuivante(AddUserActivity.this, AddProfilActivity.class, getResources().getString(R.string.id_user), userToCreateId,true);
            }

        } else {
            Toast.makeText(AddUserActivity.this, getResources().getString(R.string.text_no_validation), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean checkFields(){
        boolean bool;
        if (!isFilled(textName)) {
            layoutName.setError(getResources().getString(R.string.text_needed));
        } else {
            layoutName.setError(null);
        }
        if (isChecked(chipSexe)){
            chipMan.setError(null);
            chipWoman.setError(null);
        } else {
            chipMan.setError(getResources().getString(R.string.error));
            chipWoman.setError(getResources().getString(R.string.error));
        }
        if (!isFilled(dateOfBirth)){
            layoutBirthday.setError(getResources().getString(R.string.text_needed));
        } else {
            layoutBirthday.setError(null);
        }
        if (!isFilled(departement)){
            selectedDepartement.setError(getResources().getString(R.string.text_needed));
        } else {
            selectedDepartement.setError(null);
        }

        boolean isNotExistant = true;
        if (userToModify == null) {
            for (Utilisateur currentUser : listUserBD) {
                if (currentUser.getName().equalsIgnoreCase(textName.getText().toString())) {
                    isNotExistant = false;
                }
            }
        }

        if (isNotExistant) {
            layoutName.setError(null);
        } else {
            layoutName.setError(getResources().getString(R.string.text_already_exist));
        }
        bool = isFilled(textName) && isChecked(chipSexe) && isFilled(dateOfBirth) && isFilled(departement) && isNotExistant;
        return bool;
    }


    public class AsyncTaskRunnerDepartement extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            listUserBD = Utilisateur.listAll(Utilisateur.class);
            listDepartementBD = Departement.listAll(Departement.class);
            Collections.sort(listDepartementBD);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            if (listDepartementBD.size() == 0) {
                listDepartement.setVisibility(View.INVISIBLE);
            } else {
                buildDropdownMenu(listDepartementBD,AddUserActivity.this,selectedDepartement);
                listDepartement.setVisibility(View.VISIBLE);

                traiterIntent();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
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
                DateFormat df = new SimpleDateFormat(getResources().getString(R.string.format_date));
                try{
                    dateOfBirth = df.parse(dateString);
                }catch(ParseException e){
                    System.out.println(getResources().getString(R.string.error));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString().substring(0,2);
        List<Departement> listDepartementSelected = Departement.find(Departement.class, "numero = ?", item);
        if (listDepartementSelected.size() !=0){
            departement = listDepartementSelected.get(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void traiterIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(getResources().getString(R.string.id_user))) {
            Long userId = intent.getLongExtra(getResources().getString(R.string.id_user), 0);
            userToModify = Utilisateur.findById(Utilisateur.class, userId);
            dateOfBirth= userToModify.getDateDeNaissance();
            textName.setText(userToModify.getName());
            layoutName.setEnabled(false);
            textBirthday.setText(DateUtils.ecrireDate(userToModify.getDateDeNaissance()));
            layoutBirthday.setEnabled(false);
            int position = 0;
            for (Departement departement : listDepartementBD) {
                if (departement.getId().longValue() != userToModify.getDepartement().getId()) {
                    position ++;
                } else {
                    break;
                }
            }
            selectedDepartement.setText(selectedDepartement.getAdapter().getItem(position).toString(), false);
            listDepartement.setEnabled(false);

            if (userToModify.getSexe().equalsIgnoreCase(getResources().getString(R.string.text_man))) {
                chipMan.setChecked(true);
                chipWoman.setChecked(false);
            } else {
                chipMan.setChecked(false);
                chipWoman.setChecked(true);
            }
            chipMan.setEnabled(false);
            chipWoman.setEnabled(false);

            fabSave.hide();
            fabCancel.hide();
            fabEdit.show();
            fabDelete.show();
        } else {
            fabSave.show();
            fabEdit.hide();
            fabDelete.hide();
            getSupportActionBar().hide();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}

