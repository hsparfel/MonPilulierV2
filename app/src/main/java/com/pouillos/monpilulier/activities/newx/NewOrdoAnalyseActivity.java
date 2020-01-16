package com.pouillos.monpilulier.activities.newx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.OrdoAnalyse;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Ordonnance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewOrdoAnalyseActivity extends AppCompatActivity {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private ImageButton buttonAddAnalyse;
    private ImageButton buttonAddCabinet;
    private TextView textDescription;
    private Spinner spinnerAnalyse;
    private Spinner spinnerCabinet;
    private Class<?> activitySource;
    private OrdoAnalyse ordoAnalyseAModif;
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

                //enregistrer en bdd
                saveToDb(textDescription);

                //retour
                retourPagePrecedente();
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
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("ordoAnalyseAModifId")) {

            Long ordoAnalyseAModifId = (Long) intent.getLongExtra("ordoAnalyseAModifId",0);
            ordoAnalyseAModif = OrdoAnalyse.findById(OrdoAnalyse.class,ordoAnalyseAModifId);

            textDescription.setText(ordoAnalyseAModif.getDetail());
            spinnerAnalyse.setSelection(getIndex(spinnerAnalyse, ordoAnalyseAModif.getAnalyse().getName()));
            spinnerCabinet.setSelection(getIndex(spinnerCabinet, ordoAnalyseAModif.getCabinet().getName()));

        } else {
            ordoAnalyseAModif = new OrdoAnalyse();}
        if (intent.hasExtra("ordonnanceToUpdate")) {
                ordonnanceToUpdate = (Ordonnance) intent.getSerializableExtra("ordonnanceToUpdate");
        } else {ordonnanceToUpdate = new Ordonnance();}
    }

    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewOrdoAnalyseActivity.this, activitySource);
        nextActivity.putExtra("activitySource", NewOrdoAnalyseActivity.class);
        startActivity(nextActivity);
        finish();
    }

    public void ouvrirActivityAddAnalyse() {
        Intent nextActivity = new Intent(NewOrdoAnalyseActivity.this,NewAnalyseActivity.class);
        nextActivity.putExtra("activitySource", NewOrdoAnalyseActivity.class);
        ordoAnalyseAModif.setDetail(textDescription.getText().toString());
        nextActivity.putExtra("ordoAnalyseAModif", ordoAnalyseAModif);
        startActivity(nextActivity);
        finish();
    }

    public void ouvrirActivityAddCabinet() {
        Intent nextActivity = new Intent(NewOrdoAnalyseActivity.this,NewAnalyseActivity.class);
        nextActivity.putExtra("activitySource", NewOrdoAnalyseActivity.class);
        ordoAnalyseAModif.setDetail(textDescription.getText().toString());
        nextActivity.putExtra("ordoAnalyseAModif", ordoAnalyseAModif);
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

    public void saveToDb(TextView textDescription) {
        Analyse analyse = (Analyse) Analyse.find(Analyse.class,"name = ?", spinnerAnalyse.getSelectedItem().toString()).get(0);
        Cabinet cabinet = (Cabinet) Cabinet.find(Cabinet.class,"name = ?", spinnerCabinet.getSelectedItem().toString()).get(0);
        if (ordoAnalyseAModif.getId()==null) {
            OrdoAnalyse ordoAnalyse = new OrdoAnalyse(analyse, textDescription.getText().toString(), ordonnanceToUpdate, cabinet);
            ordoAnalyse.save();
        } else {
            OrdoAnalyse ordoAnalyse;
            if (ordoAnalyseAModif.getId()!=null) {
                ordoAnalyse = (OrdoAnalyse.find(OrdoAnalyse.class, "id = ?", ordoAnalyseAModif.getId().toString())).get(0);
            } else {
                ordoAnalyse = new OrdoAnalyse();
            }

            ordoAnalyse.setDetail(textDescription.getText().toString());

            ordoAnalyse.setAnalyse(analyse);
            ordoAnalyse.save();
        }
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
