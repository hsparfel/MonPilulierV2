package com.pouillos.monpilulier.activities.enregistrer;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.MedecinOfficiel;
import com.pouillos.monpilulier.entities.RdvOfficiel;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EnregistrerRdvMedecinOfficielActivity extends AppCompatActivity implements BasicUtils {

    private MedecinOfficiel medecinOfficiel;
    private TextView textDate;

    private TextView textNote;
    private TextView textMedecin;
    private TextView textProfession;
    private TextView textSavoirFaire;
    private TextView textCabinet;
    private TextView textComplement;
    private TextView textAdresse1;
    private TextView textAdresse2;
    private TextView textTel;
    private TextView textFax;
    private TextView textEmail;


    private Date date = new Date();
    private Intent intent;
    private RdvOfficiel rdv;
    private Utilisateur utilisateur;

    TimePickerDialog picker;
    EditText editTextHeure;
    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_rdv);

        utilisateur = (new Utilisateur()).findActifUser();


        editTextDate= findViewById(R.id.editTextDate);
        editTextHeure= findViewById(R.id.editTextHeure);
        //editTextHeure.setInputType(InputType.TYPE_NULL);



        ImageButton buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        ImageButton buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);

        textNote = findViewById(R.id.textNote);
        textMedecin = findViewById(R.id.textMedecin);
        textProfession = findViewById(R.id.textProfession);
        textSavoirFaire = findViewById(R.id.textSavoirFaire);
        textCabinet = findViewById(R.id.textCabinet);
        textComplement = findViewById(R.id.textComplement);
        textAdresse1 = findViewById(R.id.textAdresse1);
        textAdresse2 = findViewById(R.id.textAdresse2);
        textTel = findViewById(R.id.textTel);
        textFax = findViewById(R.id.textFax);
        textEmail = findViewById(R.id.textEmail);
        //textDate = findViewById(R.id.textDate);

        traiterIntent();

        editTextHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                //int hour = cldr.get(Calendar.HOUR_OF_DAY);
                //int minutes = cldr.get(Calendar.MINUTE);
                int hour = 8;
                int minutes = 0;
                // time picker dialog
                picker = new TimePickerDialog(EnregistrerRdvMedecinOfficielActivity.this,
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
        RdvOfficiel rdv = new RdvOfficiel();
        rdv.setNote(textNote.getText().toString());
        rdv.setMedecinOfficiel(medecinOfficiel);
        rdv.setUtilisateur(utilisateur);
        rdv.setDate(date);
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
        if (intent.hasExtra("medecinOfficiel")) {

            Long medecinOfficielId = intent.getLongExtra("medecinOfficiel",0);
            //rdvAModif = Rdv.findById(Rdv.class,rdvAModifId);
            medecinOfficiel = MedecinOfficiel.findById(MedecinOfficiel.class,medecinOfficielId);
            //textDescription.setText(rdvAModif.getDetail());
            //textDate.setText(DateUtils.ecrireDate(rdvAModif.getDate()));
            //date = rdvAModif.getDate();
            String nomMedecin = "";
            if (medecinOfficiel.getCodeCivilite() !=null){
                nomMedecin += medecinOfficiel.getCodeCivilite();
            }
            nomMedecin += medecinOfficiel.getNom()+" "+medecinOfficiel.getPrenom();
            textMedecin.setText(nomMedecin);
            if (medecinOfficiel.getSavoirFaire() != null) {
                textSavoirFaire.setText(medecinOfficiel.getSavoirFaire().getName());
                textProfession.setVisibility(View.GONE);
            } else {
                textSavoirFaire.setVisibility(View.GONE);
                textProfession.setText(medecinOfficiel.getProfession().getName());
            }


            if (!medecinOfficiel.getComplement().equals("")) {
                textCabinet.setText(medecinOfficiel.getRaisonSocial());
            } else {
                textCabinet.setVisibility(View.GONE);
            }



            if (!medecinOfficiel.getComplement().equals("")) {
                textComplement.setText(medecinOfficiel.getComplement());
            } else {
                textComplement.setVisibility(View.GONE);
            }


            if (!medecinOfficiel.getAdresse().equals("")) {
                textAdresse1.setText(medecinOfficiel.getAdresse());
            } else {
                textAdresse1.setVisibility(View.GONE);
            }

            if (!medecinOfficiel.getCp().equals("") & !medecinOfficiel.getVille().equals("")) {
                textAdresse2.setText(medecinOfficiel.getCp()+" "+medecinOfficiel.getVille());
            } else {
                textAdresse2.setVisibility(View.GONE);
            }

            if (!medecinOfficiel.getTelephone().equals("")) {
                textTel.setText("Tel: "+medecinOfficiel.getTelephone());
            } else {
                textTel.setVisibility(View.GONE);
            }

            if (!medecinOfficiel.getFax().equals("")) {
                textFax.setText("Fax: "+medecinOfficiel.getFax());
            } else {
                textFax.setVisibility(View.GONE);
            }

            if (!medecinOfficiel.getEmail().equals("")) {
                textEmail.setText("@: "+medecinOfficiel.getEmail());
            } else {
                textEmail.setVisibility(View.GONE);
            }

        }
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
