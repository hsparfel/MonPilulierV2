package com.pouillos.monpilulier.activities.add;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.MainActivity;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Profil;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragment;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() !=0){
            activeUser = listUserActif.get(0);
        }



        //List<Profil> listProfil = Profil.find(Profil.class, "utilisateur = ?", ""+activeUser.getId(),null,"date",null);
        //List<Profil> listProfil = Profil.find(Profil.class, "utilisateur = ?", ""+activeUser.getId());
        List<Profil> listProfil = Profil.findWithQuery(Profil.class, "SELECT * FROM PROFIL WHERE UTILISATEUR = ? ORDER BY DATE DESC", ""+activeUser.getId());

        if (listProfil.size() !=0){
            lastProfil = listProfil.get(0);
            String dateString = DateUtils.ecrireDate(lastProfil.getDate());
            textDate.setText(dateString);
            sliderTaille.setValue(lastProfil.getTaille());
            sliderPoids.setValue((lastProfil.getPoids()));
            textImc.setText(""+lastProfil.getImc());
            textPoids.setText(""+lastProfil.getPoids());
            textTaille.setText(""+lastProfil.getTaille());
        }
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

        //TODO corriger les sliders pour actualiser imc en direct
        sliderTaille.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                    profilToCreate.setTaille(round(sliderTaille.getValue()));
                    profilToCreate.setImc(profilToCreate.calculerImc());
                    textImc.setText(""+profilToCreate.getImc());
                //textPoids.setText(""+profilToCreate.getPoids()+" kg");
                textTaille.setText(""+profilToCreate.getTaille()+" cm");
            }
        });

        sliderPoids.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                profilToCreate.setPoids(sliderPoids.getValue());
                profilToCreate.setImc(profilToCreate.calculerImc());
                textImc.setText(""+profilToCreate.getImc());
                textPoids.setText(""+profilToCreate.getPoids()+" kg");
                //textTaille.setText(""+profilToCreate.getTaille()+" cm");
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

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
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
        Long profilToCreateId = profilToCreate.save();
        Intent intent = new Intent(AddProfilActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showTimePickerDialog(View view) {
        //non necessaire dans cette classe mais à cause de l'id textHeure dans les layout je dois la declarer à priori
    }
}

