package com.pouillos.monpilulier.activities.newx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.listallx.ListAllOrdoAnalyseActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllOrdoExamenActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllOrdoPrescriptionActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewOrdonnanceActivity extends AppCompatActivity implements Serializable, BasicUtils {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private ImageButton buttonAddMedecin;
    private Button buttonOrdoPrescription;
    private Button buttonOrdoExamen;
    private Button buttonOrdoAnalyse;
    private TextView textDescription;
    private Spinner spinnerMedecin;
    private Medecin medecin;
    private TextView textDate;
    private Date date;
    private Class<?> activitySource;
    private Ordonnance ordonnanceAModif;
    private Ordonnance ordonnanceSauvegarde;
    private Intent intent;
    private Utilisateur utilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ordonnance);

        utilisateur = (new Utilisateur()).findActifUser();

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);
        buttonAddMedecin = (ImageButton) findViewById(R.id.buttonAddMedecin);
        buttonOrdoPrescription = (Button) findViewById(R.id.buttonOrdoPrescription);
        buttonOrdoExamen = (Button) findViewById(R.id.buttonOrdoExamen);
        buttonOrdoAnalyse = (Button) findViewById(R.id.buttonOrdoAnalyse);
        textDescription = findViewById(R.id.textDescription);
        spinnerMedecin = (Spinner) findViewById(R.id.spinnerMedecin);
        textDate = findViewById(R.id.textDate);

        //masquer les 3 boutons
        afficherBoutons(false);

        createSpinners();

        traiterIntent();



        buttonOrdoPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityListAllOrdoPrescription();
            }
        });

        buttonOrdoExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ouvrirActivityListAllOrdoExamen();
            }
        });

        buttonOrdoAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityListAllOrdoAnalyse();
            }
        });

        buttonAddMedecin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ouvrirActivityAdd();
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
                if (!isRempli(spinnerMedecin)){
                    return;
                }

                if (!isRempli(textDate, date)) {
                    return;
                }

                //enregistrer en bdd
                saveToDb(textDescription);

                afficherBoutons(true);

            }
        });
    }

    @Override
    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
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
                if (datePicker.getMonth()+1<10) {
                    dateMois = "0"+dateMois;
                }
                String dateString = dateJour+"/"+dateMois+"/"+dateAnnee;
                tv1.setText("date : "+dateString);
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
        Intent intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");

        if (intent.hasExtra("ordonnanceSauvegardeId")) {
            Long ordonnanceSauvegardeId = intent.getLongExtra("ordonnanceSauvegardeId",0);
            ordonnanceSauvegarde = Ordonnance.findById(Ordonnance.class,ordonnanceSauvegardeId);
            textDescription.setText(ordonnanceSauvegarde.getDetail());
            medecin = ordonnanceSauvegarde.getMedecin();
            date = ordonnanceSauvegarde.getDate();
            textDate.setText(new DateUtils().ecrireDate(ordonnanceSauvegarde.getDate()));
            spinnerMedecin.setSelection(getIndex(spinnerMedecin, medecin.getName()));
            afficherBoutons(true);
        }





        if (intent.hasExtra("ordonnanceAModifId")) {

            Long ordonnanceAModifId = intent.getLongExtra("ordonnanceAModifId",0);
            ordonnanceAModif = Ordonnance.findById(Ordonnance.class,ordonnanceAModifId);
            ordonnanceSauvegarde = ordonnanceAModif;
            textDescription.setText(ordonnanceAModif.getDetail());
            medecin = ordonnanceAModif.getMedecin();
            date = ordonnanceAModif.getDate();
            textDate.setText(new DateUtils().ecrireDate(ordonnanceAModif.getDate()));
            spinnerMedecin.setSelection(getIndex(spinnerMedecin, medecin.getName()));
            afficherBoutons(true);
        } else {
            ordonnanceAModif = new Ordonnance();}
        if (intent.hasExtra("medecinToUpdate")) {
            medecin = (Medecin) intent.getSerializableExtra("medecinToUpdate");
            spinnerMedecin.setSelection(getIndex(spinnerMedecin, medecin.getName()));
        }
    }

    @Override
    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this, activitySource);
        nextActivity.putExtra("activitySource", NewOrdonnanceActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public void afficherBoutons(boolean bool) {
        if (bool) {
            buttonOrdoPrescription.setVisibility(View.VISIBLE);
            buttonOrdoExamen.setVisibility(View.VISIBLE);
            buttonOrdoAnalyse.setVisibility(View.VISIBLE);
        } else {
            buttonOrdoPrescription.setVisibility(View.INVISIBLE);
            buttonOrdoExamen.setVisibility(View.INVISIBLE);
            buttonOrdoAnalyse.setVisibility(View.INVISIBLE);
        }
    }



    public void ouvrirActivityListAllOrdoExamen() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this, ListAllOrdoExamenActivity.class);
        nextActivity.putExtra("activitySource", NewOrdonnanceActivity.class);
        ordonnanceSauvegarde.setDetail(textDescription.getText().toString());
        ordonnanceSauvegarde.setMedecin(medecin);
        ordonnanceSauvegarde.setDate(date);
        ordonnanceSauvegarde.setId(ordonnanceSauvegarde.save());
        nextActivity.putExtra("ordonnanceSauvegardeId", ordonnanceSauvegarde.getId());
        startActivity(nextActivity);
        finish();
    }

    public void ouvrirActivityListAllOrdoAnalyse() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this, ListAllOrdoAnalyseActivity.class);
        nextActivity.putExtra("activitySource", NewOrdonnanceActivity.class);
        ordonnanceSauvegarde.setDetail(textDescription.getText().toString());
        ordonnanceSauvegarde.setMedecin(medecin);
        ordonnanceSauvegarde.setDate(date);
        ordonnanceSauvegarde.setId(ordonnanceSauvegarde.save());
        nextActivity.putExtra("ordonnanceSauvegardeId", ordonnanceSauvegarde.getId());
        startActivity(nextActivity);
        finish();

    }

    public void ouvrirActivityListAllOrdoPrescription() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this, ListAllOrdoPrescriptionActivity.class);
        nextActivity.putExtra("activitySource", NewOrdonnanceActivity.class);
        ordonnanceSauvegarde.setDetail(textDescription.getText().toString());
        ordonnanceSauvegarde.setMedecin(medecin);
        ordonnanceSauvegarde.setDate(date);
        ordonnanceSauvegarde.setId(ordonnanceSauvegarde.save());
        nextActivity.putExtra("ordonnanceSauvegardeId", ordonnanceSauvegarde.getId());
        startActivity(nextActivity);
        finish();

    }








    public void alertOnSpinners() {
        TextView errorText = (TextView)spinnerMedecin.getSelectedView();
        errorText.setTextColor(Color.RED);
    }

    public void alertOffSpinners() {
        TextView errorText = (TextView)spinnerMedecin.getSelectedView();
        errorText.setTextColor(Color.BLACK);
    }

    @Override
    public void saveToDb(TextView... args) {
        medecin = (Medecin) Medecin.find(Medecin.class,"name = ?", spinnerMedecin.getSelectedItem().toString()).get(0);
        if (ordonnanceAModif.getId() == null) {
            Ordonnance ordonnance = new Ordonnance(args[0].getText().toString(), utilisateur, medecin, date);
            ordonnanceSauvegarde = ordonnance;
            ordonnanceSauvegarde.setId(ordonnance.save());
        } else {
            Ordonnance ordonnance;
            if (ordonnanceAModif.getId()!=null) {
                ordonnance = (Ordonnance.find(Ordonnance.class, "id = ?", ordonnanceAModif.getId().toString())).get(0);
            } else {
                ordonnance = new Ordonnance();
            }
            ordonnance.setDetail(args[0].getText().toString());
            ordonnance.setMedecin(medecin);
            ordonnance.setDate(date);
            Long Id = ordonnance.save();
            ordonnanceAModif = (Ordonnance) Ordonnance.find(Ordonnance.class,"id = ?", Id.toString()).get(0);
        }
    }


    @Override
    public void createSpinners() {
        List<Medecin> listAllMedecin = Medecin.listAll(Medecin.class,"name");
        List<String> listMedecinName = new ArrayList<String>();
        listMedecinName.add("sélectionner");
        for (Medecin medecin : listAllMedecin) {
            listMedecinName.add(medecin.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMedecinName) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==0) {
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedecin.setAdapter(adapter);
    }
}
