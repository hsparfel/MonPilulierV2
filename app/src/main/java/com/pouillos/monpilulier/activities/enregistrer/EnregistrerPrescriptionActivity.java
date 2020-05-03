package com.pouillos.monpilulier.activities.enregistrer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.AssociationFormeDose;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Dose;
import com.pouillos.monpilulier.entities.Medicament;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.ArrayList;
import java.util.List;

public class EnregistrerPrescriptionActivity extends AppCompatActivity implements BasicUtils, AdapterView.OnItemClickListener  {

    private Intent intent;
    private Utilisateur utilisateur;
    private Contact mContact;
    private boolean booleanMatin = false;
    private boolean booleanMidi = false;
    private boolean booleanSoir = false;
    private boolean booleanAvantRepas = false;
    private boolean booleanPendantRepas = false;
    private boolean booleanApresRepas = false;
    private boolean booleanLundi = false;
    private boolean booleanMardi = false;
    private boolean booleanMercredi = false;
    private boolean booleanJeudi = false;
    private boolean booleanVendredi = false;
    private boolean booleanSamedi = false;
    private boolean booleanDimanche = false;
    //private EditText nbDose;
    private List<Medicament> listMedicament;
    private List<Dose> listDose = new ArrayList<>();
    private AutoCompleteTextView textRechercheMedicament;
private AutoCompleteTextView selectionDose;
    private Button buttonPlus;
    private Button buttonMoins;

    private TextView nbDose;
    private float valeurDose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_prescription);

        utilisateur = (new Utilisateur()).findActifUser();

        Chip chipMatin = (Chip) findViewById(R.id.chipMatin);
        Chip chipMidi = (Chip) findViewById(R.id.chipMidi);
        Chip chipSoir = (Chip) findViewById(R.id.chipSoir);
        Chip chipAvantRepas = (Chip) findViewById(R.id.chipAvantRepas);
        Chip chipPendantRepas = (Chip) findViewById(R.id.chipPendantRepas);
        Chip chipApresRepas = (Chip) findViewById(R.id.chipApresRepas);
        Chip chipLundi = (Chip) findViewById(R.id.chipLundi);
        Chip chipMardi = (Chip) findViewById(R.id.chipMardi);
        Chip chipMercredi = (Chip) findViewById(R.id.chipMercredi);
        Chip chipJeudi = (Chip) findViewById(R.id.chipJeudi);
        Chip chipVendredi = (Chip) findViewById(R.id.chipVendredi);
        Chip chipSamedi = (Chip) findViewById(R.id.chipSamedi);
        Chip chipDimanche = (Chip) findViewById(R.id.chipDimanche);
        textRechercheMedicament = (AutoCompleteTextView) findViewById(R.id.textRechercheMedicament);

        selectionDose = (AutoCompleteTextView) findViewById(R.id.selectionDose);
        TextInputLayout listDose = (TextInputLayout) findViewById(R.id.listDose);

        buttonPlus = (Button) findViewById(R.id.buttonPlusDose);
        buttonMoins = (Button) findViewById(R.id.buttonMoinsDose);
        nbDose = (TextView) findViewById(R.id.textNbDose);

        //remplir l'autocomplete
        AsyncTaskRunnerMedicament runnerMedicament = new AsyncTaskRunnerMedicament();
        runnerMedicament.execute();

        traiterIntent();

        buttonMoins.setOnClickListener(v -> {
                    valeurDose = Float.parseFloat(nbDose.getText().toString());
                    valeurDose = valeurDose-0.5f;
                    nbDose.setText(""+valeurDose);
                    if (valeurDose == 0) {
                        buttonMoins.setClickable(false);
                    }
                }
        );

        buttonPlus.setOnClickListener(v -> {
                    valeurDose = Float.parseFloat(nbDose.getText().toString());
                    valeurDose = valeurDose+0.5f;
                    nbDose.setText(""+valeurDose);
                    buttonMoins.setClickable(true);
                }
        );

        chipMatin.setOnClickListener(v -> {
                    booleanMatin = chipMatin.isChecked();
                }
        );
        chipMidi.setOnClickListener(v -> {
                    booleanMidi = chipMidi.isChecked();
                }
        );
        chipSoir.setOnClickListener(v -> {
                    booleanSoir = chipSoir.isChecked();
                }
        );
        chipAvantRepas.setOnClickListener(v -> {
                    booleanAvantRepas = chipAvantRepas.isChecked();
                }
        );
        chipPendantRepas.setOnClickListener(v -> {
                    booleanPendantRepas = chipPendantRepas.isChecked();
                }
        );
        chipApresRepas.setOnClickListener(v -> {
                    booleanApresRepas = chipApresRepas.isChecked();
                }
        );
        chipLundi.setOnClickListener(v -> {
                    booleanLundi = chipLundi.isChecked();
                }
        );
        chipMardi.setOnClickListener(v -> {
                    booleanLundi = chipMardi.isChecked();
                }
        );
        chipMercredi.setOnClickListener(v -> {
                    booleanLundi = chipMercredi.isChecked();
                }
        );
        chipJeudi.setOnClickListener(v -> {
                    booleanLundi = chipJeudi.isChecked();
                }
        );
        chipVendredi.setOnClickListener(v -> {
                    booleanLundi = chipVendredi.isChecked();
                }
        );
        chipSamedi.setOnClickListener(v -> {
                    booleanLundi = chipSamedi.isChecked();
                }
        );
        chipDimanche.setOnClickListener(v -> {
                    booleanLundi = chipDimanche.isChecked();
                }
        );

    }

    @Override
    public void retourPagePrecedente() {
        finish();
    }



    @Override
    public void saveToDb() {

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
        if (intent.hasExtra("medecinOfficiel")) {
            Long medecinOfficielId = intent.getLongExtra("medecinOfficiel", 0);
            //rdvAModif = Rdv.findById(Rdv.class,rdvAModifId);
            mContact = Contact.findById(Contact.class, medecinOfficielId);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //MedicamentOfficiel medicamentSelectionne = (MedicamentOfficiel) parent.getItemAtPosition(position);
        String item = parent.getItemAtPosition(position).toString();
        //MedecinOfficiel medecinOfficiel = (MedecinOfficiel) parent.getItemAtPosition((position));

        // create Toast with user selected value
        Toast.makeText(EnregistrerPrescriptionActivity.this, "Selected : \t" + item, Toast.LENGTH_SHORT).show();

        Medicament medicamentSelectionne = Medicament.find(Medicament.class,"denomination = ?",item).get(0);

        /*String requete = "SELECT * FROM DOSE AS D JOIN ASSOCIATION_FORME_DOSE AS A ON D.NAME = A.DOSE WHERE A.FORME_PHARMACEUTIQUE = ?";
        //requete += medicamentSelectionne.getFormePharmaceutique().getName();
        listDose = Dose.findWithQuery(Dose.class, requete,medicamentSelectionne.getFormePharmaceutique().getId().toString());
*/
        List<AssociationFormeDose> listAssoc= AssociationFormeDose.find(AssociationFormeDose.class,"forme_pharmaceutique = ?", medicamentSelectionne.getFormePharmaceutique().getId().toString());
        listDose.clear();
        for (AssociationFormeDose assoc : listAssoc) {
            listDose.add(assoc.getDose());
        }
        List<String> listDoseString = new ArrayList<>();
        String[] listDeroulanteDose = new String[listDose.size()];
        for (Dose dose : listDose) {
            listDoseString.add(dose.getName());
        }
        listDoseString.toArray(listDeroulanteDose);
        selectionDose.setText(listDeroulanteDose[0], false);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listDeroulanteDose);
        selectionDose.setAdapter(adapter);
    }

    private class AsyncTaskRunnerMedicament extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            listMedicament = Medicament.listAll(Medicament.class);
            return null;
        }

        protected void onPostExecute(Void result) {
           // Toast toast = Toast.makeText(EnregistrerPrescriptionActivity.this, "Import fini", Toast.LENGTH_LONG);
            //toast.show();
            if (listMedicament.size() == 0) {
                Toast toast = Toast.makeText(EnregistrerPrescriptionActivity.this, "Aucune correspondance", Toast.LENGTH_LONG);
                toast.show();
            } else {
                List<String> listMedicamentString = new ArrayList<>();
                String[] listDeroulanteMedicament = new String[listMedicament.size()];
                for (Medicament medicament : listMedicament) {
                    String affichageMedecinOfficiel = medicament.getDenomination();
                    listMedicamentString.add(affichageMedecinOfficiel);
                }
                listMedicamentString.toArray(listDeroulanteMedicament);
                ArrayAdapter adapter = new ArrayAdapter(EnregistrerPrescriptionActivity.this, R.layout.list_item, listDeroulanteMedicament);
                textRechercheMedicament.setAdapter(adapter);
                //textRechercheIntervenant.setOnItemClickListener(this);
                //textRechercheIntervenant.setOnItemClickListener((AdapterView.OnItemClickListener) this);
                // Set the minimum number of characters, to show suggestions
                //textRechercheIntervenant.setThreshold(3);
                //TODO revoir le clic sur liste
                textRechercheMedicament.setOnItemClickListener(EnregistrerPrescriptionActivity.this);
                textRechercheMedicament.setThreshold(1);
                Toast toast = Toast.makeText(EnregistrerPrescriptionActivity.this, "import fini", Toast.LENGTH_LONG);
                toast.show();
            }
        }


    }
}
