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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.OrdoAnalyse;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.OrdoExamen;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewOrdoAnalyseActivity extends AppCompatActivity implements BasicUtils {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private ImageButton buttonAddAnalyse;
    private ImageButton buttonAddCabinet;
    private TextView textDescription;
    private Spinner spinnerAnalyse;
    private Spinner spinnerCabinet;
    private Class<?> activitySource;
    private OrdoAnalyse ordoAnalyseAModif;
    private OrdoAnalyse ordoAnalyse;
    private Intent intent;
    private Analyse analyse;
    private Cabinet cabinet;
    private Ordonnance ordonnanceSauvegarde;

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
                //ouvrirActivityAddAnalyse();
            }
        });

        buttonAddCabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ouvrirActivityAddCabinet();
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
    public void traiterIntent() {
        intent = getIntent();
        activitySource = (Class<?>) intent.getSerializableExtra("activitySource");
        if (intent.hasExtra("ordonnanceSauvegardeId")) {
            Long ordonnanceSauvegardeId = intent.getLongExtra("ordonnanceSauvegardeId", 0);
            ordonnanceSauvegarde = Ordonnance.findById(Ordonnance.class, ordonnanceSauvegardeId);
        }
        if (intent.hasExtra("ordoAnalyseAModifId")) {
            Long ordoAnalyseAModifId = intent.getLongExtra("ordoAnalyseAModifId", 0);
            ordoAnalyseAModif = OrdoAnalyse.findById(OrdoAnalyse.class, ordoAnalyseAModifId);
            ordonnanceSauvegarde = Ordonnance.findById(Ordonnance.class, ordoAnalyseAModif.getOrdonnance().getId());
            textDescription.setText(ordoAnalyseAModif.getDetail());
            spinnerCabinet.setSelection(getIndex(spinnerCabinet, ordoAnalyseAModif.getCabinet().getName()));
            spinnerAnalyse.setSelection(getIndex(spinnerAnalyse, ordoAnalyseAModif.getAnalyse().getName()));
        }
    }

    @Override
    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewOrdoAnalyseActivity.this, NewOrdonnanceActivity.class);
        nextActivity.putExtra("activitySource", NewOrdoAnalyseActivity.class);
        nextActivity.putExtra("ordonnanceSauvegardeId", ordonnanceSauvegarde.getId());
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void saveToDb(TextView... args) {
        analyse = (Analyse) Analyse.find(Analyse.class,"name = ?", spinnerAnalyse.getSelectedItem().toString()).get(0);
        cabinet = (Cabinet) Cabinet.find(Cabinet.class,"name = ?", spinnerCabinet.getSelectedItem().toString()).get(0);
        if (ordoAnalyseAModif != null) {
            ordoAnalyseAModif.setCabinet(cabinet);
            ordoAnalyseAModif.setAnalyse(analyse);
            ordoAnalyseAModif.setDetail(textDescription.getText().toString());
            ordoAnalyseAModif.save();
        } else {
            ordoAnalyse = new OrdoAnalyse();
            ordoAnalyse.setCabinet(cabinet);
            ordoAnalyse.setAnalyse(analyse);
            ordoAnalyse.setDetail(textDescription.getText().toString());
            ordoAnalyse.setOrdonnance(ordonnanceSauvegarde);
            ordoAnalyse.setId(ordoAnalyse.save());
        }
    }



    @Override
    public void createSpinners() {
        //Examen
        List<Analyse> listAllAnalyse = Analyse.listAll(Analyse.class,"name");
        List<String> listAnalyseName = new ArrayList<String>();
        listAnalyseName.add("sélectionner");
        for (Analyse analyse : listAllAnalyse) {
            listAnalyseName.add(analyse.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAnalyseName) {
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
        spinnerAnalyse.setAdapter(adapter);

        //Cabinet
        List<Cabinet> listAllCabinet = Cabinet.listAll(Cabinet.class,"name");
        List<String> listCabinetName = new ArrayList<String>();
        listCabinetName.add("sélectionner");
        for (Cabinet cabinet : listAllCabinet) {
            listCabinetName.add(cabinet.getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCabinetName) {
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
        spinnerCabinet.setAdapter(adapter);
    }

}
