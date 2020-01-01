package com.pouillos.monpilulier.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.abstraite.GestionDate;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.OrdoAnalyse;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.fragments.DatePickerFragmentDateJour;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewOrdoAnalyseActivity extends AppCompatActivity implements GestionDate{

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private ImageButton buttonAddAnalyse;
    private ImageButton buttonAddCabinet;
    private TextView textDescription;
    private Spinner spinnerAnalyse;
    private Spinner spinnerCabinet;
    private TextView textDate;
    private Date date;
    private Class<?> pagePrecedente;
    private OrdoAnalyse ordoAnalyseToUpdate;
    private Intent intent;
    private Analyse analyse;
    private Cabinet cabinet;
    private Ordonnance ordonnanceToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ordo_analyse);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);
        buttonAddAnalyse = (ImageButton) findViewById(R.id.buttonAddAnalyse);
        buttonAddCabinet = (ImageButton) findViewById(R.id.buttonAddCabinet);
        textDescription = findViewById(R.id.textDescription);
        spinnerAnalyse = (Spinner) findViewById(R.id.spinnerAnalyse);
        spinnerCabinet = (Spinner) findViewById(R.id.spinnerCabinet);
        textDate = findViewById(R.id.textDate);

        createSpinners();

        traiterIntent();

        buttonAddAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityAddAnalyse();
            }
        });

        buttonAddCabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirActivityAddCabinet();
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

                //TODO rajouter obligation description

                if (!isRempli(textDate, date)) {
                    return;
                }

                //enregistrer en bdd
                saveToDb(textDescription, date);

                //retour
                retourPagePrecedente();
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
                    dateJour = "0"+dateJour;
                }
                if (datePicker.getMonth()<10) {
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

    public void traiterIntent() {
        intent = getIntent();
        pagePrecedente = (Class<?>) intent.getSerializableExtra("precedent");
        if (intent.hasExtra("ordoAnalyseToUpdate")) {
            ordoAnalyseToUpdate = (OrdoAnalyse) intent.getSerializableExtra("ordoAnalyseToUpdate");
            textDescription.setText(ordoAnalyseToUpdate.getDetail());
            spinnerAnalyse.setSelection(getIndex(spinnerAnalyse, ordoAnalyseToUpdate.getAnalyse().getName()));
            spinnerCabinet.setSelection(getIndex(spinnerCabinet, ordoAnalyseToUpdate.getCabinet().getName()));
            textDate.setText(ordoAnalyseToUpdate.EcrireDate(ordoAnalyseToUpdate.getDate()));
            date = ordoAnalyseToUpdate.getDate();

        } else {ordoAnalyseToUpdate = new OrdoAnalyse();}
        if (intent.hasExtra("ordonnanceToUpdate")) {
                ordonnanceToUpdate = (Ordonnance) intent.getSerializableExtra("ordonnanceToUpdate");
        } else {ordonnanceToUpdate = new Ordonnance();}
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewOrdoAnalyseActivity.this,pagePrecedente);
        nextActivity.putExtra("precedent", NewOrdoAnalyseActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public void ouvrirActivityAddAnalyse() {
        Intent nextActivity = new Intent(NewOrdoAnalyseActivity.this,NewAnalyseActivity.class);
        nextActivity.putExtra("precedent", NewOrdoAnalyseActivity.class);
        ordoAnalyseToUpdate.setDetail(textDescription.getText().toString());
        nextActivity.putExtra("ordoAnalyseToUpdate", ordoAnalyseToUpdate);
        startActivity(nextActivity);
        finish();
    }

    public void ouvrirActivityAddCabinet() {
        Intent nextActivity = new Intent(NewOrdoAnalyseActivity.this,NewAnalyseActivity.class);
        nextActivity.putExtra("precedent", NewOrdoAnalyseActivity.class);
        ordoAnalyseToUpdate.setDetail(textDescription.getText().toString());
        nextActivity.putExtra("ordoAnalyseToUpdate", ordoAnalyseToUpdate);
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





    public boolean isRempli(TextView textView, Date date) {
        if (date == null) {
            textView.setError("Sélection Obligatoire");
            return false;
        } else {
            return true;
        }
    }

    public void saveToDb(TextView textDescription, Date date) {
        Analyse analyse = (Analyse) Analyse.find(Analyse.class,"name = ?", spinnerAnalyse.getSelectedItem().toString()).get(0);
        Cabinet cabinet = (Cabinet) Cabinet.find(Cabinet.class,"name = ?", spinnerCabinet.getSelectedItem().toString()).get(0);
        if (ordoAnalyseToUpdate.getId()==null) {
            OrdoAnalyse ordoAnalyse = new OrdoAnalyse(analyse, textDescription.getText().toString(), ordonnanceToUpdate, cabinet, date);
            ordoAnalyse.save();
        } else {
            OrdoAnalyse ordoAnalyse;
            if (ordoAnalyseToUpdate.getId()!=null) {
                ordoAnalyse = (OrdoAnalyse.find(OrdoAnalyse.class, "id = ?", ordoAnalyseToUpdate.getId().toString())).get(0);
            } else {
                ordoAnalyse = new OrdoAnalyse();
            }

            ordoAnalyse.setDetail(textDescription.getText().toString());

            ordoAnalyse.setAnalyse(analyse);
            ordoAnalyse.save();
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

    public void createSpinners() {
        //Analyse
        List<Analyse> listAllAnalyse = Analyse.listAll(Analyse.class,"name");
        List<String> listAnalyseName = new ArrayList<String>();
        for (Analyse analyse : listAllAnalyse) {
            listAnalyseName.add(analyse.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAnalyseName);
        //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnalyse.setAdapter(adapter);

        //Cabinet
        List<Cabinet> listAllCabinet = Cabinet.listAll(Cabinet.class,"name");
        List<String> listCabinetName = new ArrayList<String>();
        for (Cabinet cabinet : listAllCabinet) {
            listCabinetName.add(cabinet.getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCabinetName);
        //Le layout par défaut est android.R.layout.simple_spinner_dropdown_item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCabinet.setAdapter(adapter);
    }
}
