package com.pouillos.monpilulier.activities.enregistrer;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.RdvActeMedical;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EnregistrerRdvAutreActivity extends AppCompatActivity implements BasicUtils {

    private TextView textNote;

    private Date date = new Date();
    private Intent intent;
    private RdvActeMedical rdv;
    private Utilisateur utilisateur;

    TimePickerDialog picker;
    EditText editTextHeure;
    EditText editTextDate;

    private boolean booleanAnalyse;
    private boolean booleanExamen;
    private List<Analyse> listAnalyses;
    private List<Examen> listExamens;
    private AutoCompleteTextView selectionAnalyse;
    private AutoCompleteTextView selectionExamen;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_rdv);

        utilisateur = (new Utilisateur()).findActifUser();

        editTextDate= findViewById(R.id.editTextDate);
        editTextHeure= findViewById(R.id.editTextHeure);

        ImageButton buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        ImageButton buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);

        textNote = findViewById(R.id.textNote);

        Chip chipAnalyse = (Chip) findViewById(R.id.chipAnalyse);
        Chip chipExamen = (Chip) findViewById(R.id.chipExamen);

        TextInputLayout listAnalyse = (TextInputLayout) findViewById(R.id.listAnalyse);
        selectionAnalyse = (AutoCompleteTextView) findViewById(R.id.selectionAnalyse);
        TextInputLayout listExamen = (TextInputLayout) findViewById(R.id.listExamen);
        selectionExamen = (AutoCompleteTextView) findViewById(R.id.selectionExamen);

        listAnalyses = Analyse.listAll(Analyse.class);
        listExamens = Examen.listAll(Examen.class);

        traiterIntent();

        chipAnalyse.setOnClickListener(v -> {
                if (booleanAnalyse) {
                    listAnalyse.setVisibility(View.GONE);
                    selectionAnalyse.setText("",false);
                } else {
                    listAnalyse.setVisibility(View.VISIBLE);
                }
                booleanAnalyse = !booleanAnalyse;
            List<String> listAnalyseString = new ArrayList<>();
            String[] listDeroulanteAnalyse = new String[listAnalyses.size()];
            for (Analyse analyse : listAnalyses) {
                listAnalyseString.add(analyse.getName());
            }
            listAnalyseString.toArray(listDeroulanteAnalyse);
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listDeroulanteAnalyse);
            selectionAnalyse.setAdapter(adapter);
        });

        chipExamen.setOnClickListener(v -> {
            if (booleanExamen) {
                listExamen.setVisibility(View.GONE);
                selectionExamen.setText("",false);
            } else {
                listExamen.setVisibility(View.VISIBLE);
            }
            booleanExamen = !booleanExamen;
            List<String> listExamenString = new ArrayList<>();
            String[] listDeroulanteExamen = new String[listExamens.size()];
            for (Examen examen : listExamens) {
                listExamenString.add(examen.getName());
            }
            listExamenString.toArray(listDeroulanteExamen);
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listDeroulanteExamen);
            selectionExamen.setAdapter(adapter);
        });

        editTextHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                //int hour = cldr.get(Calendar.HOUR_OF_DAY);
                //int minutes = cldr.get(Calendar.MINUTE);
                int hour = 8;
                int minutes = 0;
                // time picker dialog
                picker = new TimePickerDialog(EnregistrerRdvAutreActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String hour = "";
                                String minute = "";
                                if (sHour<10){
                                    hour+="0";
                                }
                                if (sMinute<10){
                                    minute+="0";
                                }
                                hour+=sHour;
                                minute+=sMinute;

                                editTextHeure.setText(hour + ":" + minute);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                calendar.add(Calendar.HOUR_OF_DAY, sHour);
                                calendar.add(Calendar.MINUTE, sMinute);
                                date = calendar.getTime();
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

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
                //if (!isRempli(textDate, date)) {
                //    return;
                //}

                //enregistrer en bdd
                //saveToDb(textDescription);
                saveToDb();
                //retour
                retourPagePrecedente();
            }
        });
    }

    @Override
    public void retourPagePrecedente() {
        finish();
    }

    @Override
    public void saveToDb() {
        RdvActeMedical rdv = new RdvActeMedical();
        rdv.setNote(textNote.getText().toString());

        rdv.setUtilisateur(utilisateur);
        rdv.setDate(date);

        if (selectionAnalyse != null) {
            Analyse analyse = Analyse.find(Analyse.class,"name = ?", selectionAnalyse.getText().toString()).get(0);
            rdv.setAnalyse(analyse);
        }
        if (selectionExamen != null) {
            Examen examen = Examen.find(Examen.class,"name = ?", selectionExamen.getText().toString()).get(0);
            rdv.setExamen(examen);
        }

        rdv.save();
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
    public void traiterIntent() {
        intent = getIntent();
      //  activitySource = (Class<?>) intent.getSerializableExtra("activitySource");

    }


    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        //newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.show(getSupportFragmentManager(), "editTexteDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePicker.setMinDate(new Date().getTime());
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
                editTextDate.setText(dateString);
                editTextDate.setError(null);
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
