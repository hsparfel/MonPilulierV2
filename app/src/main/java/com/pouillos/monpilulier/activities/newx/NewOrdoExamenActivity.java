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
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.OrdoExamen;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewOrdoExamenActivity extends AppCompatActivity implements BasicUtils {

    private ImageButton buttonValider;
    private ImageButton buttonAnnuler;
    private ImageButton buttonAddExamen;
    private ImageButton buttonAddCabinet;
    private TextView textDescription;
    private Spinner spinnerExamen;
    private Spinner spinnerCabinet;
    private Class<?> activitySource;
    private OrdoExamen ordoExamenAModif;
    private OrdoExamen ordoExamen;
    private Intent intent;
    private Examen examen;
    private Cabinet cabinet;
    private Ordonnance ordonnanceSauvegarde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ordo_examen);

        buttonValider = (ImageButton) findViewById(R.id.buttonValider);
        buttonAnnuler= (ImageButton) findViewById(R.id.buttonAnnuler);
        buttonAddExamen = (ImageButton) findViewById(R.id.buttonAddExamen);
        buttonAddCabinet = (ImageButton) findViewById(R.id.buttonAddCabinet);
        textDescription = findViewById(R.id.textDescription);
        spinnerExamen = (Spinner) findViewById(R.id.spinnerExamen);
        spinnerCabinet = (Spinner) findViewById(R.id.spinnerCabinet);

        createSpinners();

        traiterIntent();

        buttonAddExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ouvrirActivityAddExamen();
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
        if (intent.hasExtra("ordoExamenAModifId")) {
            Long ordoExamenAModifId = intent.getLongExtra("ordoExamenAModifId", 0);
            ordoExamenAModif = OrdoExamen.findById(OrdoExamen.class, ordoExamenAModifId);
            ordonnanceSauvegarde = Ordonnance.findById(Ordonnance.class, ordoExamenAModif.getOrdonnance().getId());
            textDescription.setText(ordoExamenAModif.getDetail());
            spinnerCabinet.setSelection(getIndex(spinnerCabinet, ordoExamenAModif.getCabinet().getName()));
            spinnerExamen.setSelection(getIndex(spinnerExamen, ordoExamenAModif.getExamen().getName()));
        }
    }

    @Override
    public void retourPagePrecedente() {
        Intent nextActivity = new Intent(NewOrdoExamenActivity.this, NewOrdonnanceActivity.class);
        nextActivity.putExtra("activitySource", NewOrdoExamenActivity.class);
        nextActivity.putExtra("ordonnanceSauvegardeId", ordonnanceSauvegarde.getId());
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void saveToDb(TextView... args) {
        examen = (Examen) Examen.find(Examen.class,"name = ?", spinnerExamen.getSelectedItem().toString()).get(0);
        cabinet = (Cabinet) Cabinet.find(Cabinet.class,"name = ?", spinnerCabinet.getSelectedItem().toString()).get(0);
        if (ordoExamenAModif != null) {
            ordoExamenAModif.setCabinet(cabinet);
            ordoExamenAModif.setExamen(examen);
            ordoExamenAModif.setDetail(textDescription.getText().toString());
            ordoExamenAModif.save();
        } else {
            ordoExamen = new OrdoExamen();
            ordoExamen.setCabinet(cabinet);
            ordoExamen.setExamen(examen);
            ordoExamen.setDetail(textDescription.getText().toString());
            ordoExamen.setOrdonnance(ordonnanceSauvegarde);
            ordoExamen.setId(ordoExamen.save());
        }
    }



    @Override
    public void createSpinners() {
        //Examen
        List<Examen> listAllExamen = Examen.listAll(Examen.class,"name");
        List<String> listExamenName = new ArrayList<String>();
        listExamenName.add("sélectionner");
        for (Examen examen : listAllExamen) {
            listExamenName.add(examen.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listExamenName) {
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
        spinnerExamen.setAdapter(adapter);

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
