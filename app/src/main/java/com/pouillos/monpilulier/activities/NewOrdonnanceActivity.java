package com.pouillos.monpilulier.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
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
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.fragments.DatePickerFragment;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewOrdonnanceActivity extends AppCompatActivity {

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
    private Class<?> cible;
    private Ordonnance ordonnanceToUpdate;
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

        createSpinnerMedecin();

        traiterIntent();

        //masquer les 3 boutons
        afficherBoutons(false);

        buttonOrdoPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ouvrirActivityOrdoPrescription();
            }
        });

        buttonOrdoExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ouvrirActivityOrdoExamen();
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
                ouvrirActivityAdd();
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

                if (!isRempli(textDate, date)) {
                    return;
                }

                //enregistrer en bdd
                saveToDb(textDescription);

                afficherBoutons(true);
                //retour
                //retourPagePrecedente();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragmentDateJour newFragment = new DatePickerFragmentDateJour();
        newFragment.show(getSupportFragmentManager(), "buttonDate");
        newFragment.setOnDateClickListener(new DatePickerFragmentDateJour.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //datePicker.setMaxDate(new Date().getTime());
                TextView tv1= (TextView) findViewById(R.id.textDate);
                String dateJour = ""+datePicker.getDayOfMonth();
                String dateMois = ""+(datePicker.getMonth()+1);
                String dateAnnee = ""+datePicker.getYear();
                if (datePicker.getDayOfMonth()<10) {
                    dateJour = "0"+datePicker.getDayOfMonth();
                }
                if (datePicker.getMonth()<10) {
                    dateMois = "0"+datePicker.getMonth();
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

    public void traiterIntent() {
        Intent intent = getIntent();
        cible = (Class<?>) intent.getSerializableExtra("precedent");
        if (intent.hasExtra("ordonnanceToUpdate")) {
            ordonnanceToUpdate = (Ordonnance) intent.getSerializableExtra("ordonnanceToUpdate");
            textDescription.setText(ordonnanceToUpdate.getDetail());
            medecin = ordonnanceToUpdate.getMedecin();
            date = ordonnanceToUpdate.getDate();
            textDate.setText(ordonnanceToUpdate.EcrireDate(ordonnanceToUpdate.getDate()));
            spinnerMedecin.setSelection(getIndex(spinnerMedecin, medecin.getName()));
        } else {ordonnanceToUpdate = new Ordonnance();}
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this,cible);
        nextActivity.putExtra("precedent", NewOrdonnanceActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public void afficherBoutons(boolean bool) {
        if (bool) {
            buttonOrdoPrescription.setVisibility(View.VISIBLE);
            buttonOrdoExamen.setVisibility(View.VISIBLE);
            buttonOrdoAnalyse.setVisibility(View.VISIBLE);
            buttonValider.setVisibility(View.INVISIBLE);
            buttonAnnuler.setVisibility(View.INVISIBLE);
        } else {
            buttonOrdoPrescription.setVisibility(View.INVISIBLE);
            buttonOrdoExamen.setVisibility(View.INVISIBLE);
            buttonOrdoAnalyse.setVisibility(View.INVISIBLE);
            buttonValider.setVisibility(View.VISIBLE);
            buttonAnnuler.setVisibility(View.VISIBLE);
        }
    }

    public void ouvrirActivityAdd() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this,NewMedecinActivity.class);
        nextActivity.putExtra("precedent", NewOrdonnanceActivity.class);
        ordonnanceToUpdate.setDetail(textDescription.getText().toString());
        //ordonnanceToUpdate.setMedecin(textTelephone.getText().toString());
        //ordonnanceToUpdate.setDate(textEmail.getText().toString());
        nextActivity.putExtra("ordonnanceToUpdate", ordonnanceToUpdate);
        startActivity(nextActivity);
        finish();
    }

    public void ouvrirActivityListAllOrdoAnalyse() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this,ListAllOrdoAnalyseActivity.class);
        nextActivity.putExtra("precedent", NewOrdonnanceActivity.class);
        ordonnanceToUpdate.setDetail(textDescription.getText().toString());
        ordonnanceToUpdate.setMedecin(medecin);
        ordonnanceToUpdate.setDate(date);
        nextActivity.putExtra("ordonnanceToUpdate", ordonnanceToUpdate);
        startActivity(nextActivity);
        finish();
    }

    //creer les classes puis enlever commentaires
   /* public void ouvrirActivityOrdoExamen() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this,NewOrdoExamenActivity.class);
        nextActivity.putExtra("precedent", NewOrdonnanceActivity.class);
        ordonnanceToUpdate.setDetail(textDescription.getText().toString());
        ordonnanceToUpdate.setMedecin(medecin);
        ordonnanceToUpdate.setDate(date);
        nextActivity.putExtra("ordonnanceToUpdate", ordonnanceToUpdate);
        startActivity(nextActivity);
        finish();
    }

    public void ouvrirActivityOrdoPrescription() {
        Intent nextActivity = new Intent(NewOrdonnanceActivity.this,NewOrdoPrescriptionActivity.class);
        nextActivity.putExtra("precedent", NewOrdonnanceActivity.class);
        ordonnanceToUpdate.setDetail(textDescription.getText().toString());
        ordonnanceToUpdate.setMedecin(medecin);
        ordonnanceToUpdate.setDate(date);
        nextActivity.putExtra("ordonnanceToUpdate", ordonnanceToUpdate);
        startActivity(nextActivity);
        finish();
    }*/

    public boolean isExistant(TextView textView) {
        boolean reponse = false;
        List<Ordonnance> listAllOrdonnance = Ordonnance.listAll(Ordonnance.class);
        for (Ordonnance ordonnance : listAllOrdonnance) {
            if (ordonnanceToUpdate == null) {
                if (textView.getText().toString().equals(ordonnance.getName())) {
                    textView.requestFocus();
                    textView.setError("l'ordonnance existe déjà");
                    reponse = true;
                }
            } else {
                if (!ordonnanceToUpdate.getName().equals(ordonnance.getName()) && textView.getText().toString().equals(ordonnance.getName())) {
                    textView.requestFocus();
                    textView.setError("l'ordonnance existe déjà");
                    reponse = true;
                }
            }
        }
        return reponse;
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

    public boolean isRempli(TextView textView, Date date) {
        if (date == null) {
            textView.setError("Sélection Obligatoire");
            return false;
        } else {
            return true;
        }
    }

    public void saveToDb(TextView textDescription) {
        medecin = (Medecin) Medecin.find(Medecin.class,"name = ?", spinnerMedecin.getSelectedItem().toString()).get(0);
        if (ordonnanceToUpdate.getId() == null) {
            Ordonnance ordonnance = new Ordonnance(textDescription.getText().toString(), utilisateur, medecin, date);
            ordonnance.setCreationDate(new Date());
            Long Id = ordonnance.save();
            ordonnanceToUpdate = (Ordonnance) Ordonnance.find(Ordonnance.class,"id = ?", Id.toString()).get(0);
        } else {
            Ordonnance ordonnance;
            if (ordonnanceToUpdate.getId()!=null) {
                ordonnance = (Ordonnance.find(Ordonnance.class, "id = ?", ordonnanceToUpdate.getId().toString())).get(0);
            } else {
                ordonnance = new Ordonnance();
            }
            ordonnance.setDetail(textDescription.getText().toString());
            ordonnance.setMedecin(medecin);
            ordonnance.setDate(date);
            Long Id = ordonnance.save();
            ordonnanceToUpdate = (Ordonnance) Ordonnance.find(Ordonnance.class,"id = ?", Id.toString()).get(0);
        }
    }

    public static boolean isEmailAdress(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    public void createSpinnerMedecin() {
        List<Medecin> listAllMedecin = Medecin.listAll(Medecin.class,"name");
        List<String> listMedecinName = new ArrayList<String>();
        for (Medecin medecin : listAllMedecin) {
            listMedecinName.add(medecin.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMedecinName);
        //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedecin.setAdapter(adapter);
    }
}
