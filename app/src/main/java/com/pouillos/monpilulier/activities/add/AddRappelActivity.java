package com.pouillos.monpilulier.activities.add;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvAnalyseActivity;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Dose;
import com.pouillos.monpilulier.entities.Medicament;
import com.pouillos.monpilulier.entities.MedicamentLight;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.entities.Rappel;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.Duree;
import com.pouillos.monpilulier.enumeration.Frequence;
import com.pouillos.monpilulier.enumeration.QuantiteDose;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AddRappelActivity extends NavDrawerActivity implements Serializable, BasicUtils, AdapterView.OnItemClickListener {
    @State
    Utilisateur activeUser;
    @State
    String heure;
    @State
    Dose doseSelected;
    @State
    Prescription prescription;
    @State
    Rappel rappel;

    @BindView(R.id.selectionDose)
    AutoCompleteTextView selectedDose;
    @BindView(R.id.listDose)
    TextInputLayout listDose;

    List<Dose> listDoseBD;

    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.numberPickerHeure)
    com.shawnlin.numberpicker.NumberPicker numberPickerHeure;
    @BindView(R.id.numberPickerMinute)
    com.shawnlin.numberpicker.NumberPicker numberPickerMinute;
    @BindView(R.id.numberPickerQuantiteDose)
    com.shawnlin.numberpicker.NumberPicker numberPickerQuantiteDose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_add_rappel);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);
        traiterIntent();
        AddRappelActivity.AsyncTaskRunnerBDDose runnerBDDose = new AddRappelActivity.AsyncTaskRunnerBDDose();
        runnerBDDose.execute();
        //hideAll(true);
        //updateDisplay();

        setTitle("+ Rappel");
        listDose.setEnabled(false);
        //   hideAll(true);

        //selectedContact.setOnItemClickListener(this);
        selectedDose.setOnItemClickListener(this);


        fillPickers();

        numberPickerHeure.setOnValueChangedListener(onValueChangeListenerHeure);
        numberPickerMinute.setOnValueChangedListener(onValueChangeListenerMinute);
        numberPickerQuantiteDose.setOnValueChangedListener(onValueChangeListenerQuantiteDose);

    }

    public void traiterIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("prescriptionId")) {
            Long prescriptionId = intent.getLongExtra("prescriptionId", 0);
            prescription = Prescription.findById(Prescription.class, prescriptionId);
        }
    }

    com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener onValueChangeListenerHeure =
            new 	com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddRappelActivity.this,"selected Heure"+numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            };

    com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener onValueChangeListenerMinute =
            new 	com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddRappelActivity.this,"selected minute"+numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            };

    com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener onValueChangeListenerQuantiteDose =
            new 	com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddRappelActivity.this,"selected qte dose"+numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            };




    @OnClick(R.id.fabSave)
    public void fabSaveClick() {
        saveToDb();
        //revenirActivitePrecedente("prescription","id",prescription.getId());
        finish();
    }

    private void fillPickers() {
        String[] displayedValuesHeure  = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        numberPickerHeure.setMinValue(1);
        numberPickerHeure.setMaxValue(displayedValuesHeure.length);
        numberPickerHeure.setDisplayedValues(displayedValuesHeure);
        numberPickerHeure.setValue(9);

        String[] displayedValuesMinute  = {"00","15","30","45"};
        numberPickerMinute.setMinValue(1);
        numberPickerMinute.setMaxValue(displayedValuesMinute.length);
        numberPickerMinute.setDisplayedValues((displayedValuesMinute));
        numberPickerMinute.setValue(1);

        List<QuantiteDose> listQteDose = Arrays.asList(QuantiteDose.values());
        String[] displayedValuesQteDose  = new String[listQteDose.size()];
        int i = 0;
        for (QuantiteDose qteDose : listQteDose) {
            displayedValuesQteDose[i] = qteDose.toString();
            i++;
        }
        numberPickerQuantiteDose.setMinValue(1);
        numberPickerQuantiteDose.setMaxValue(displayedValuesQteDose.length);
        numberPickerQuantiteDose.setDisplayedValues((displayedValuesQteDose));
        numberPickerQuantiteDose.setValue(4);
    }

    private void hideAll(boolean bool){


        fabSave.hide();
    }

    public void updateDisplay() {


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        doseSelected = listDoseBD.get(position);
    }

    public class AsyncTaskRunnerBDDose extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            // activeUser = findActiveUser();
            publishProgress(50);

            //listDoseBD = Dose.listAll(Dose.class);
            String requete = "";
            requete += "SELECT d.* FROM DOSE as d LEFT JOIN ASSOCIATION_FORME_DOSE as afd ON afd.DOSE = d.ID ";
            requete += "LEFT JOIN MEDICAMENT as m ON m.FORME_PHARMACEUTIQUE = afd.FORME_PHARMACEUTIQUE ";
            requete += "WHERE m.ID = "+prescription.getMedicament().getId();
            listDoseBD = Dose.findWithQuery(Dose.class,requete);
            Collections.sort(listDoseBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);

            if (listDoseBD.size() == 0) {
                Toast.makeText(AddRappelActivity.this, R.string.text_no_matching, Toast.LENGTH_LONG).show();
                listDose.setVisibility(View.GONE);
            } else {
                buildDropdownMenu(listDoseBD, AddRappelActivity.this,selectedDose);
                listDose.setEnabled(true);
                selectedDose.setText(listDoseBD.get(0).getName());
                doseSelected = listDoseBD.get(0);
            }



        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }



    @Override
    public void saveToDb() {
        if (rappel == null) {
            rappel = new Rappel();
        }

        rappel.setDose(doseSelected);
        //numberPickerQuantiteDose.getDisplayedValues();
        String qte = numberPickerQuantiteDose.getDisplayedValues()[numberPickerQuantiteDose.getValue()-1];
        Double qteDouble = Double.valueOf(qte);
        rappel.setQuantiteDose(qteDouble);
        rappel.setHeure(numberPickerHeure.getDisplayedValues()[numberPickerHeure.getValue()-1]+":"+numberPickerMinute.getDisplayedValues()[numberPickerMinute.getValue()-1]);
        rappel.setPrescription(prescription);
        rappel.setId(rappel.save());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
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

}
