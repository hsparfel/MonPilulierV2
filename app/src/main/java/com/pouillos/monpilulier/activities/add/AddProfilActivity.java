package com.pouillos.monpilulier.activities.add;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.orm.SugarRecord;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.AccueilActivity;
import com.pouillos.monpilulier.activities.MainActivity;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.recherche.ChercherContactActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.Profil;
import com.pouillos.monpilulier.entities.Region;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragment;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
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

import static java.lang.Math.*;

public class AddProfilActivity extends NavDrawerActivity implements Serializable, BasicUtils {

    @State
    Utilisateur activeUser;
    @State
    Profil profilToCreate;
    @State
    Profil lastProfil;
    @State
    Date dateProfil;

    List<Profil> listProfil;
    List<Profil> listProfilExistant;

    @BindView(R.id.floating_action_button)
    FloatingActionButton fab;
    @BindView(R.id.textDate)
    TextInputEditText textDate;
    @BindView(R.id.layoutDate)
    TextInputLayout layoutDate;
    @BindView(R.id.textImc)
    TextInputEditText textImc;
    @BindView(R.id.layoutImc)
    TextInputLayout layoutImc;
    @BindView(R.id.textPoids)
    TextInputEditText textPoids;
    @BindView(R.id.layoutPoids)
    TextInputLayout layoutPoids;
    @BindView(R.id.textTaille)
    TextInputEditText textTaille;
    @BindView(R.id.layoutTaille)
    TextInputLayout layoutTaille;
    @BindView(R.id.sliderTaille)
    Slider sliderTaille;
    @BindView(R.id.sliderPoids)
    Slider sliderPoids;
    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_add_profil);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        AddProfilActivity.AsyncTaskRunnerBD runnerBD = new AddProfilActivity.AsyncTaskRunnerBD();
        runnerBD.execute();

        sliderTaille.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                profilToCreate.setTaille(round(sliderTaille.getValue()));
                profilToCreate.setImc(floatArrondi(profilToCreate.calculerImc(),2));
                textImc.setText(""+floatArrondi(profilToCreate.getImc(),2));
                textTaille.setText(""+profilToCreate.getTaille()+" cm");
            }
        });

        sliderPoids.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                profilToCreate.setPoids(floatArrondi(sliderPoids.getValue(),2));
                profilToCreate.setImc(floatArrondi(profilToCreate.calculerImc(),2));
                textImc.setText(""+floatArrondi(profilToCreate.getImc(),2));
                textPoids.setText(""+floatArrondi(profilToCreate.getPoids(),2)+" kg");
            }
        });

        textDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(view);
                    textDate.clearFocus();
                }
            }
        });
    }

    public class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(50);
            listProfil = Profil.findWithQuery(Profil.class, "SELECT * FROM PROFIL WHERE UTILISATEUR = ? ORDER BY DATE DESC", ""+activeUser.getId());
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);

            profilToCreate = new Profil();
            profilToCreate.setUtilisateur(activeUser);
            profilToCreate.setDate(new Date());
            new DateUtils();
            String dateString = DateUtils.ecrireDate(profilToCreate.getDate());
            textDate.setText(dateString);
            profilToCreate.setPoids(sliderPoids.getValue());
            profilToCreate.setTaille(round(sliderTaille.getValue()));
            profilToCreate.setImc(profilToCreate.calculerImc());
            textImc.setText(""+profilToCreate.getImc());
            textPoids.setText(""+profilToCreate.getPoids()+" kg");
            textTaille.setText(""+profilToCreate.getTaille()+" cm");
            if (listProfil.size() !=0){
                lastProfil = listProfil.get(0);
                dateString = DateUtils.ecrireDate(lastProfil.getDate());
                textDate.setText(dateString);
                sliderTaille.setValue(lastProfil.getTaille());
                sliderPoids.setValue((lastProfil.getPoids()));
                textImc.setText(""+lastProfil.getImc());
                textPoids.setText(""+lastProfil.getPoids()+" kg");
                textTaille.setText(""+lastProfil.getTaille()+" cm");
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
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
                textDate.setText(dateString);
                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                try{
                    dateProfil = df.parse(dateString);
                    profilToCreate.setDate(dateProfil);
                }catch(ParseException e){
                    System.out.println("ERROR");
                }
            }
        });
    }

    @OnClick(R.id.floating_action_button)
    public void fabClick() {
        if (isExistant(profilToCreate)) {
            new MaterialAlertDialogBuilder(AddProfilActivity.this)
                    .setTitle(R.string.dialog_overwrite_title)
                    .setMessage(R.string.dialog_overwrite_message)
                    .setNegativeButton(R.string.dialog_overwrite_negative, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AddProfilActivity.this, R.string.dialog_overwrite_negative_toast, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setPositiveButton(R.string.dialog_overwrite_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AddProfilActivity.this, R.string.dialog_overwrite_positive_toast, Toast.LENGTH_LONG).show();
                            profilToCreate.setId(listProfilExistant.get(0).getId());
                            profilToCreate.save();
                            Toast.makeText(AddProfilActivity.this, R.string.modification_saved, Toast.LENGTH_LONG).show();

                            ouvrirActiviteSuivante(AddProfilActivity.this, AccueilActivity.class,true);
                        }
                    })
                    .show();
        } else {
            profilToCreate.setId(null);
            profilToCreate.save();
            Toast.makeText(AddProfilActivity.this, R.string.modification_saved, Toast.LENGTH_LONG).show();
            ouvrirActiviteSuivante(AddProfilActivity.this, AccueilActivity.class,true);
        }
        //profilToCreate.save();
        //ouvrirActiviteSuivante(AddProfilActivity.this, AccueilActivity.class);
    }

    public boolean isExistant(Profil profil) {
        listProfilExistant = Profil.findWithQuery(Profil.class, "SELECT * FROM PROFIL WHERE UTILISATEUR = ? AND DATE = ?", ""+activeUser.getId(), ""+profilToCreate.getDate().getTime());
        if (listProfilExistant.size()>0) {
            return true;
        } else {
            return false;
        }
    }



}

