package com.pouillos.monpilulier.activities.add;

import android.content.Context;
import android.content.Intent;
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
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.entities.Medicament;
import com.pouillos.monpilulier.entities.MedicamentLight;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.Duree;
import com.pouillos.monpilulier.enumeration.Frequence;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AddPrescriptionActivity extends NavDrawerActivity implements Serializable, BasicUtils {
    @State
    Utilisateur activeUser;
    @State
    Date date;
    @State
    Medicament medicamentSelected;
    @State
    Ordonnance ordonnance;

    //pr eviter erreur compile ene tattendan de creer le champ
    //EditText textDate;

    @BindView(R.id.textMedicament)
    AutoCompleteTextView selectedMedicament;
    @BindView(R.id.layoutMedicament)
    TextInputLayout listMedicament;

    @BindView(R.id.layoutFrequence)
    TextInputLayout layoutFrequence;
    @BindView(R.id.textFrequence)
    TextInputEditText textFrequence;

    @BindView(R.id.layoutDuree)
    TextInputLayout layoutDuree;
    @BindView(R.id.textDuree)
    TextInputEditText textDuree;

    List<MedicamentLight> listMedicamentLightBD;

    @BindView(R.id.layoutDate)
    TextInputLayout layoutDate;
    @BindView(R.id.textDate)
    TextInputEditText textDate;

    boolean whenNeeded = false;

    public Frequence frequence;
    public Duree duree;

  //  @BindView(R.id.fragment_list_frequence)
   // Fragment fragment_list_frequence;
    Fragment fragmentListFrequence;
    Fragment fragmentListDuree;

    @BindView(R.id.fabSave)
    FloatingActionButton fabSave;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.rbEveryDay)
    RadioButton rbEveryDay;

    @BindView(R.id.rbEveryDayByHour)
    RadioButton rbEveryDayByHour;

    @BindView(R.id.rbEveryXDays)
    RadioButton rbEveryXDays;

    @BindView(R.id.rbChosenDays)
    RadioButton rbChosenDays;

    @BindView(R.id.rbCycle)
    RadioButton rbCycle;

    @BindView(R.id.rbNoEnding)
    RadioButton rbNoEnding;

    @BindView(R.id.rbUntilDate)
    RadioButton rbUntilDate;

    @BindView(R.id.rbDuringDays)
    RadioButton rbDuringDays;

    @BindView(R.id.chipLundi)
    Chip chipLundi;
    @BindView(R.id.chipMardi)
    Chip chipMardi;
    @BindView(R.id.chipMercredi)
    Chip chipMercredi;
    @BindView(R.id.chipJeudi)
    Chip chipJeudi;
    @BindView(R.id.chipVendredi)
    Chip chipVendredi;
    @BindView(R.id.chipSamedi)
    Chip chipSamedi;
    @BindView(R.id.chipDimanche)
    Chip chipDimanche;

    @BindView(R.id.numberPickerFrequence)
    NumberPicker numberPickerFrequence;

    @BindView(R.id.numberPickerDuree)
    NumberPicker numberPickerDuree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);
        fragmentListFrequence = (Fragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_list_frequence);
        fragmentListDuree = (Fragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_list_duree);

        progressBar.setVisibility(View.VISIBLE);

        AddPrescriptionActivity.AsyncTaskRunnerBDMedicament runnerBDMedicament = new AddPrescriptionActivity.AsyncTaskRunnerBDMedicament();
        runnerBDMedicament.execute();

        updateDisplay();

        setTitle("+ Prescription");

     //   hideAll(true);
        traiterIntent();
        //selectedContact.setOnItemClickListener(this);
        selectedMedicament.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
               // hideAll(true);
                MedicamentLight medicamentLight = listMedicamentLightBD.get(position);
                medicamentSelected = Medicament.findById(Medicament.class,medicamentLight.getId());
                //selectedMedicament.setText(null);

                updateDisplay();
            }
        });

        numberPickerFrequence.setMinValue(1);
        numberPickerFrequence.setMaxValue(365);
        numberPickerFrequence.setOnValueChangedListener(onValueChangeListenerFrequence);
        numberPickerDuree.setMinValue(1);
        numberPickerDuree.setMaxValue(15);
        numberPickerDuree.setOnValueChangedListener(onValueChangeListenerDuree);
    }

    public void traiterIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("ordonnanceId")) {
            Long ordonnanceId = intent.getLongExtra("ordonnanceId", 0);
            ordonnance = Ordonnance.findById(Ordonnance.class, ordonnanceId);
        }
    }

    NumberPicker.OnValueChangeListener onValueChangeListenerFrequence =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddPrescriptionActivity.this,"selected number freq"+numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            };

    NumberPicker.OnValueChangeListener onValueChangeListenerDuree =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(AddPrescriptionActivity.this,"selected number duree"+numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                }
            };


    public void onRGFrequenceClick(View view){
        if (rbEveryDay.isChecked()) {
            frequence = Frequence.EveryDay;
        } else if (rbEveryDayByHour.isChecked()) {
            frequence = Frequence.EveryDayByHour;
        } else if (rbEveryXDays.isChecked()) {
            frequence = Frequence.EveryXDays;
        } else if (rbChosenDays.isChecked()) {
            frequence = Frequence.ChosenDays;
        } else if (rbCycle.isChecked()) {
            frequence = Frequence.Cycle;
        }
        textFrequence.setText(frequence.toString());
        fragmentListFrequence.getView().setVisibility(View.GONE);
    }

    public void onRGDureeClick(View view){
        if (rbNoEnding.isChecked()) {
            duree = Duree.NoEnding;
        } else if (rbUntilDate.isChecked()) {
            duree = Duree.UntilDate;
        } else if (rbDuringDays.isChecked()) {
            duree = Duree.DuringDays;
        }
        textDuree.setText(duree.toString());
        fragmentListDuree.getView().setVisibility(View.GONE);
    }

    @OnClick(R.id.fabSave)
    public void fabSaveClick() {
        saveToDb();
    }

    @OnClick(R.id.textFrequence)
    public void textFrequenceClick() {
        fragmentListFrequence.getView().setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.textDuree)
    public void textDureeClick() {
        fragmentListDuree.getView().setVisibility(View.VISIBLE);
    }


    private void hideAll(boolean bool){
        fabSave.setVisibility(View.GONE);
        fragmentListFrequence.getView().setVisibility(View.GONE);
        layoutFrequence.setVisibility(View.GONE);
        fragmentListDuree.getView().setVisibility(View.GONE);
        layoutDuree.setVisibility(View.GONE);
    }

    public void updateDisplay() {
        if (selectedMedicament != null) {
            layoutFrequence.setVisibility(View.VISIBLE);
        }
    }

    public class AsyncTaskRunnerBDMedicament extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(50);

            listMedicamentLightBD = MedicamentLight.listAll(MedicamentLight.class);

            Collections.sort(listMedicamentLightBD);
            publishProgress(100);
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
                buildDropdownMenu(listMedicamentLightBD, AddPrescriptionActivity.this,selectedMedicament);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @Override
    public void saveToDb() {
        Prescription prescription = new Prescription();
        prescription.setMedicament(medicamentSelected);
        prescription.setWhenNeeded(whenNeeded);
        // A FAIRE prescription.setDuree();
        // A FAIRE prescription.setFrequence();
        prescription.setOrdonnance(ordonnance);
        prescription.setId(prescription.save());
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

    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        //newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.show(getSupportFragmentManager(), "editTexteDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMaxDate(new Date().getTime());
               // TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()+1<10) {
                    dateMois = "0"+dateMois;
                }
                Calendar c1 = Calendar.getInstance();
                // set Month
                // MONTH starts with 0 i.e. ( 0 - Jan)
                c1.set(Calendar.MONTH, datePicker.getMonth());
                // set Date
                c1.set(Calendar.DATE, datePicker.getDayOfMonth());
                // set Year
                c1.set(Calendar.YEAR, datePicker.getYear());
                // creating a date object with specified time.
                date = c1.getTime();

                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                //tv1.setText("date: "+dateString);
                textDate.setText(dateString);
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

}
