package com.pouillos.monpilulier.activities.recherche;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.MainActivity;
import com.pouillos.monpilulier.activities.enregistrer.EnregistrerPrescriptionActivity;
import com.pouillos.monpilulier.activities.enregistrer.EnregistrerRdvMedecinOfficielActivity;
import com.pouillos.monpilulier.entities.AssociationOfficielle;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.MedecinOfficiel;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.Region;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.util.ArrayList;
import java.util.List;

public class ChercherMedecinOfficielActivity extends AppCompatActivity implements BasicUtils, AdapterView.OnItemClickListener  {

    private Class<?> activitySource;
    private Intent intent;
    private Utilisateur utilisateur;
    private boolean booleanMesIntervenants;
    private boolean booleanMedecin;
    private boolean booleanAutre;
    private boolean booleanSpecialite;
    private boolean booleanActivite;
    private boolean booleanDepartement;
    private boolean booleanRegion;

    private List<Profession> listProfession;
    private List<SavoirFaire> listSavoirFaire;
    private List<Departement> listDepartement;
    private List<Region> listRegion;
    private Departement utilisateurDepartement;
    private Region utilisateurRegion;
    private AutoCompleteTextView textRechercheIntervenant;
    private MedecinOfficiel medecinOfficielSelectionne;
    private Button buttonAssocier;
    private Button buttonRdv;
    private Button buttonPrescription;
    private AutoCompleteTextView selectionMesIntervenants;
    //TextView tvDisplay;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chercher_medecin_officiel);


        utilisateur = (new Utilisateur()).findActifUser();
        utilisateurDepartement = utilisateur.getDepartement();
        utilisateurRegion = utilisateur.getDepartement().getRegion();

        Chip chipMesIntervenants = (Chip) findViewById(R.id.chipMesIntervenants);
        Chip chipMedecin = (Chip) findViewById(R.id.chipMedecin);
        Chip chipAutre = (Chip) findViewById(R.id.chipAutre);
        Chip chipSpecialite = (Chip) findViewById(R.id.chipSpecialite);
        Chip chipActivite = (Chip) findViewById(R.id.chipActivite);
        Chip chipDepartement = (Chip) findViewById(R.id.chipDepartement);
        Chip chipRegion = (Chip) findViewById(R.id.chipRegion);
        textRechercheIntervenant = (AutoCompleteTextView) findViewById(R.id.textRechercheIntervenant);
        Button buttonAppliquerFiltre = (Button) findViewById((R.id.buttonAppliquerFiltre));
        Button buttonRazFiltre = (Button) findViewById((R.id.buttonRazFiltre));
        AutoCompleteTextView selectionMetier = (AutoCompleteTextView) findViewById(R.id.selectionMetier);
        TextInputLayout listMetier = (TextInputLayout) findViewById(R.id.listMetier);
        AutoCompleteTextView selectionGeo = (AutoCompleteTextView) findViewById(R.id.selectionGeo);
        TextInputLayout listGeo = (TextInputLayout) findViewById(R.id.listGeo);
        buttonAssocier = (Button) findViewById(R.id.buttonAssocier);
        buttonRdv = (Button) findViewById(R.id.buttonRdv);
        buttonPrescription = (Button) findViewById(R.id.buttonPrescription);
        TextInputLayout listMesIntervenants = (TextInputLayout) findViewById(R.id.listMesIntervenants);
        selectionMesIntervenants = (AutoCompleteTextView) findViewById(R.id.selectionMesIntervenants);


        listProfession = Profession.listAll(Profession.class);
        listSavoirFaire = SavoirFaire.listAll(SavoirFaire.class);
        listDepartement = Departement.listAll(Departement.class);
        listRegion = Region.listAll(Region.class);

        buttonPrescription.setOnClickListener(v -> {
            //TODO ouvrir nouvelle activite rdv
            ouvrirActiviteSuivante(EnregistrerPrescriptionActivity.class, "medecinOfficiel", medecinOfficielSelectionne.getId());
        });


        buttonRdv.setOnClickListener(v -> {
            //TODO ouvrir nouvelle activite rdv
            ouvrirActiviteSuivante(EnregistrerRdvMedecinOfficielActivity.class, "medecinOfficiel", medecinOfficielSelectionne.getId());
        });

        buttonAssocier.setOnClickListener(v -> {
            if (medecinOfficielSelectionne != null) {
                AssociationOfficielle associationOfficielle = new AssociationOfficielle(utilisateur, medecinOfficielSelectionne);
                if (!associationOfficielle.isExistante()) {
                    associationOfficielle.save();
                }
                Toast toast = Toast.makeText(ChercherMedecinOfficielActivity.this, utilisateur.getName()+" & "+medecinOfficielSelectionne.getNom()+" ont été associés", Toast.LENGTH_LONG);
                toast.show();
                ouvrirActiviteSuivante(MainActivity.class);
            }
        });

        chipMesIntervenants.setOnClickListener(v -> {
            if (chipMesIntervenants.isCheckable()) {
                textRechercheIntervenant.setVisibility(View.GONE);
                if (booleanMesIntervenants && !booleanMedecin && !booleanAutre) {
                    buttonAppliquerFiltre.setVisibility(View.GONE);
                    //chipMedecin.setChecked(false);
                    // booleanMedecin = false;
                    // chipAutre.setChecked(false);
                    // booleanAutre = false;
                } else {
                    buttonAppliquerFiltre.setVisibility(View.VISIBLE);
                    //  chipMedecin.setChecked(true);
                    //  booleanMedecin = true;
                    //  chipAutre.setChecked(true);
                    //  booleanAutre = true;
                }
                booleanMesIntervenants = !booleanMesIntervenants;
            }
        });

        chipDepartement.setOnClickListener(v -> {
            if (chipDepartement.isCheckable()) {
                textRechercheIntervenant.setVisibility(View.GONE);
                if (booleanDepartement && !booleanMesIntervenants) {
                    buttonAppliquerFiltre.setVisibility(View.GONE);
                } else {
                    buttonAppliquerFiltre.setVisibility(View.VISIBLE);
                }
                if (!booleanDepartement) {
                    chipRegion.setVisibility(View.GONE);
                    listGeo.setVisibility(View.VISIBLE);
                } else {
                    chipRegion.setVisibility(View.VISIBLE);
                    listGeo.setVisibility(View.GONE);
                }
                booleanDepartement = !booleanDepartement;
                List<String> listGeoString = new ArrayList<>();
                String[] listDeroulanteGeo = new String[listDepartement.size()];
                for (Departement departement : listDepartement) {
                    listGeoString.add(departement.getNom() + " (" + departement.getNumero() + ")");
                }
                listGeoString.toArray(listDeroulanteGeo);
                selectionGeo.setText(utilisateurDepartement.getNom() + " (" + utilisateurDepartement.getNumero() + ")", false);
                ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listDeroulanteGeo);
                selectionGeo.setAdapter(adapter);
            }
        });

        chipRegion.setOnClickListener(v -> {
            if (chipRegion.isCheckable()) {
                textRechercheIntervenant.setVisibility(View.GONE);
                if (booleanRegion && !booleanMesIntervenants) {
                    buttonAppliquerFiltre.setVisibility(View.GONE);
                } else {
                    buttonAppliquerFiltre.setVisibility(View.VISIBLE);
                }
                if (!booleanRegion) {
                    chipDepartement.setVisibility(View.GONE);
                    listGeo.setVisibility(View.VISIBLE);
                } else {
                    chipDepartement.setVisibility(View.VISIBLE);
                    listGeo.setVisibility(View.GONE);
                }
                booleanRegion = !booleanRegion;
                List<String> listGeoString = new ArrayList<>();
                String[] listDeroulanteGeo = new String[listRegion.size()];
                for (Region region : listRegion) {
                    listGeoString.add(region.getNom());
                }
                listGeoString.toArray(listDeroulanteGeo);
                selectionGeo.setText(utilisateurRegion.getNom(), false);
                ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listDeroulanteGeo);
                selectionGeo.setAdapter(adapter);
            }
        });

        chipSpecialite.setOnClickListener(v -> {
            if (chipSpecialite.isCheckable()) {
                textRechercheIntervenant.setVisibility(View.GONE);

                if (booleanSpecialite) {
                    listMetier.setVisibility(View.GONE);
                } else {
                    listMetier.setVisibility(View.VISIBLE);
                }
                booleanSpecialite = !booleanSpecialite;
                List<String> listMetierString = new ArrayList<>();
                String[] listDeroulanteMetier = new String[listSavoirFaire.size()];
                for (SavoirFaire savoirFaire : listSavoirFaire) {
                    listMetierString.add(savoirFaire.getName());
                }
                listMetierString.toArray(listDeroulanteMetier);
                selectionMetier.setText("Médecine Générale", false);
                ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listDeroulanteMetier);
                selectionMetier.setAdapter(adapter);
            }
        });

        chipActivite.setOnClickListener(v -> {
            if (chipActivite.isCheckable()) {
                textRechercheIntervenant.setVisibility(View.GONE);
                if (booleanActivite) {
                    listMetier.setVisibility(View.GONE);
                } else {
                    listMetier.setVisibility(View.VISIBLE);
                }
                booleanActivite = !booleanActivite;
                List<String> listMetierString = new ArrayList<>();
                String[] listDeroulanteMetier = new String[listProfession.size()];
                for (Profession profession : listProfession) {
                    listMetierString.add(profession.getName());
                }
                listMetierString.toArray(listDeroulanteMetier);
                selectionMetier.setText("Infirmier", false);
                ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listDeroulanteMetier);
                selectionMetier.setAdapter(adapter);
            }
        });

        chipAutre.setOnClickListener(v -> {
            if (chipAutre.isCheckable()) {
                textRechercheIntervenant.setVisibility(View.GONE);
                if (booleanAutre && !booleanMesIntervenants) {
                    chipActivite.setVisibility(View.GONE);
                    chipActivite.setChecked(false);
                    chipMedecin.setVisibility(View.VISIBLE);
                    chipDepartement.setVisibility(View.GONE);
                    chipDepartement.setChecked(false);
                    chipRegion.setVisibility(View.GONE);
                    chipRegion.setChecked(false);
                    buttonAppliquerFiltre.setVisibility(View.GONE);
                } else {
                    chipActivite.setVisibility(View.VISIBLE);
                    chipMedecin.setVisibility(View.GONE);
                    chipMedecin.setChecked(false);
                    chipSpecialite.setVisibility(View.GONE);
                    chipSpecialite.setChecked(false);
                    chipDepartement.setVisibility(View.VISIBLE);
                    chipRegion.setVisibility(View.VISIBLE);
                    //buttonAppliquerFiltre.setVisibility(View.VISIBLE);
                }
                booleanDepartement = false;
                booleanRegion = false;
                booleanActivite = false;
                listMetier.setVisibility(View.GONE);
                listGeo.setVisibility(View.GONE);
                booleanAutre = !booleanAutre;
            }
        });

        chipMedecin.setOnClickListener(v -> {
            if (chipMedecin.isCheckable()) {
                textRechercheIntervenant.setVisibility(View.GONE);
                if (booleanMedecin && !booleanMesIntervenants) {
                    chipSpecialite.setVisibility(View.GONE);
                    chipSpecialite.setChecked(false);
                    chipAutre.setVisibility(View.VISIBLE);
                    chipDepartement.setVisibility(View.GONE);
                    chipDepartement.setChecked(false);
                    chipRegion.setVisibility(View.GONE);
                    chipRegion.setChecked(false);
                    buttonAppliquerFiltre.setVisibility(View.GONE);
                } else {
                    chipSpecialite.setVisibility(View.VISIBLE);
                    chipAutre.setVisibility(View.GONE);
                    chipAutre.setChecked(false);
                    chipActivite.setVisibility(View.GONE);
                    chipActivite.setChecked(false);
                    chipDepartement.setVisibility(View.VISIBLE);
                    chipRegion.setVisibility(View.VISIBLE);
                    //buttonAppliquerFiltre.setVisibility(View.VISIBLE);
                }
                booleanDepartement = false;
                booleanRegion = false;
                booleanSpecialite = false;
                listMetier.setVisibility(View.GONE);
                listGeo.setVisibility(View.GONE);
                booleanMedecin = !booleanMedecin;
            }
        });

        buttonAppliquerFiltre.setOnClickListener(v -> {

            booleanMesIntervenants=chipMesIntervenants.isChecked();
            booleanMedecin=chipMedecin.isChecked();
            booleanAutre=chipAutre.isChecked();
            booleanSpecialite=chipSpecialite.isChecked();
            booleanActivite=chipActivite.isChecked();
            booleanDepartement=chipDepartement.isChecked();
            booleanRegion=chipRegion.isChecked();
            buttonRazFiltre.setVisibility(View.VISIBLE);

            String requete = "";

            List<MedecinOfficiel> listMedecinOfficiel = new ArrayList<>();

            if (booleanMesIntervenants) {

                listMesIntervenants.setVisibility(View.VISIBLE);
                requete += "SELECT * FROM MEDECIN_OFFICIEL AS MO INNER JOIN ASSOCIATION_OFFICIELLE AS AO ON MO.ID = AO.MEDECIN_OFFICIEL WHERE AO.UTILISATEUR = "+utilisateur.getId().toString();
                if (booleanMedecin) {
                    requete += "AND MO.CODE_CIVILITE <>\"\" ";
                    chipMedecin.setCheckable(false);
                }
                if (booleanAutre) {
                    requete += "AND MO.CODE_CIVILITE =\"\" ";
                    chipAutre.setCheckable(false);
                }
                if (booleanSpecialite) {
                    requete += "AND MO.SAVOIR_FAIRE = ";
                    String metier = selectionMetier.getText().toString();
                    SavoirFaire savoirFaire = SavoirFaire.find(SavoirFaire.class, "name = ?", metier).get(0);
                    requete += savoirFaire.getId().toString();
                    chipSpecialite.setCheckable(false);
                    listMetier.setClickable(false);
                }
                if (booleanActivite) {
                    requete += "AND MO.PROFESSION = ";
                    String metier = selectionMetier.getText().toString();
                    Profession profession = Profession.find(Profession.class, "name = ?", metier).get(0);
                    requete += profession.getId().toString();
                    chipActivite.setCheckable(false);
                    listMetier.setClickable(false);
                }
                if (booleanDepartement) {
                    requete += " AND MO.DEPARTEMENT = ";
                    String departement = selectionGeo.getText().toString();
                    departement = departement.substring(departement.length() - 3, departement.length() - 1);
                    Departement dep = Departement.find(Departement.class, "numero = ?", departement).get(0);
                    requete += dep.getId().toString();
                    chipDepartement.setCheckable(false);
                    listGeo.setClickable(false);
                }
                if (booleanRegion) {
                    requete += " AND MO.REGION = ";
                    String region = selectionGeo.getText().toString();
                    Region reg = Region.find(Region.class, "nom = ?", region).get(0);
                    requete += reg.getId().toString();
                    chipRegion.setCheckable(false);
                    listGeo.setClickable(false);
                }

                listMedecinOfficiel = MedecinOfficiel.findWithQuery(MedecinOfficiel.class, requete);
                if (listMedecinOfficiel.size() == 0) {
                    Toast toast = Toast.makeText(ChercherMedecinOfficielActivity.this, "Aucune correspondance, modifier puis appliquer filtre", Toast.LENGTH_LONG);
                    toast.show();
                    listMesIntervenants.setVisibility(View.GONE);
                } else {

                    List<String> listMedecinOfficielString = new ArrayList<>();
                    String[] listDeroulanteMedecinOfficiel = new String[listMedecinOfficiel.size()];
                    for (MedecinOfficiel medecinOfficiel : listMedecinOfficiel) {
                        String affichageMedecinOfficiel = "";
                        affichageMedecinOfficiel += medecinOfficiel.getNom() + ", " + medecinOfficiel.getPrenom() + " (";

                        if (medecinOfficiel.getSavoirFaire() != null) {
                            affichageMedecinOfficiel += medecinOfficiel.getSavoirFaire().getName() + ") * ";
                        } else {
                            affichageMedecinOfficiel += medecinOfficiel.getProfession().getName() + ") * ";
                        }
                        affichageMedecinOfficiel += medecinOfficiel.getVille();
                        listMedecinOfficielString.add(affichageMedecinOfficiel);
                    }
                    listMedecinOfficielString.toArray(listDeroulanteMedecinOfficiel);
                    //selectionMetier.setText("Infirmier", false);
                    //selectionMesIntervenants.setText(listDeroulanteMedecinOfficiel[0], false);
                    ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, listDeroulanteMedecinOfficiel);
                    selectionMesIntervenants.setAdapter(adapter);
                    //textRechercheIntervenant.setOnItemClickListener(this);
                    //textRechercheIntervenant.setOnItemClickListener((AdapterView.OnItemClickListener) this);
                    // Set the minimum number of characters, to show suggestions
                    //textRechercheIntervenant.setThreshold(3);
                    //TODO revoir le clic sur liste
                    selectionMesIntervenants.setOnItemClickListener(this);
                }

            } else {
                textRechercheIntervenant.setVisibility(View.VISIBLE);
                requete += "SELECT * FROM MEDECIN_OFFICIEL ";
                if (booleanMedecin) {
                    requete += "WHERE CODE_CIVILITE <>\"\" ";
                    chipMedecin.setCheckable(false);
                }
                if (booleanAutre) {
                    requete += "WHERE CODE_CIVILITE =\"\" ";
                    chipAutre.setCheckable(false);
                }
                if (booleanSpecialite) {
                    requete += "AND SAVOIR_FAIRE = ";
                    String metier = selectionMetier.getText().toString();
                    SavoirFaire savoirFaire = SavoirFaire.find(SavoirFaire.class, "name = ?", metier).get(0);
                    requete += savoirFaire.getId().toString();
                    chipSpecialite.setCheckable(false);
                    listMetier.setClickable(false);
                }
                if (booleanActivite) {
                    requete += "AND PROFESSION = ";
                    String metier = selectionMetier.getText().toString();
                    Profession profession = Profession.find(Profession.class, "name = ?", metier).get(0);
                    requete += profession.getId().toString();
                    chipActivite.setCheckable(false);
                    listMetier.setClickable(false);
                }
                if (booleanDepartement) {
                    requete += " AND DEPARTEMENT = ";
                    String departement = selectionGeo.getText().toString();
                    departement = departement.substring(departement.length() - 3, departement.length() - 1);
                    Departement dep = Departement.find(Departement.class, "numero = ?", departement).get(0);
                    requete += dep.getId().toString();
                    chipDepartement.setCheckable(false);
                    listGeo.setClickable(false);
                }
                if (booleanRegion) {
                    requete += " AND REGION = ";
                    String region = selectionGeo.getText().toString();
                    Region reg = Region.find(Region.class, "nom = ?", region).get(0);
                    requete += reg.getId().toString();
                    chipRegion.setCheckable(false);
                    listGeo.setClickable(false);
                }

                listMedecinOfficiel = MedecinOfficiel.findWithQuery(MedecinOfficiel.class, requete);
                if (listMedecinOfficiel.size() == 0) {
                    Toast toast = Toast.makeText(ChercherMedecinOfficielActivity.this, "Aucune correspondance, modifier puis appliquer filtre", Toast.LENGTH_LONG);
                    toast.show();
                    textRechercheIntervenant.setVisibility(View.GONE);
                }


                List<String> listMedecinOfficielString = new ArrayList<>();
                String[] listAutocompletion = new String[listMedecinOfficiel.size()];
                for (MedecinOfficiel medecinOfficiel : listMedecinOfficiel) {
                    String affichageMedecinOfficiel = "";
                    affichageMedecinOfficiel += medecinOfficiel.getNom() + ", " + medecinOfficiel.getPrenom() + " (";
                    if (medecinOfficiel.getSavoirFaire() !=null) {
                        affichageMedecinOfficiel += medecinOfficiel.getSavoirFaire().getName() + ") * ";
                    } else {
                        affichageMedecinOfficiel += medecinOfficiel.getProfession().getName() + ") * ";
                    }
                    affichageMedecinOfficiel += medecinOfficiel.getVille();
                    listMedecinOfficielString.add(affichageMedecinOfficiel);
                }
                listMedecinOfficielString.toArray(listAutocompletion);
                ArrayAdapter adapterMedecins = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listAutocompletion);
                textRechercheIntervenant.setAdapter(adapterMedecins);
                textRechercheIntervenant.setOnItemClickListener(this);
                //textRechercheIntervenant.setOnItemClickListener((AdapterView.OnItemClickListener) this);
                // Set the minimum number of characters, to show suggestions
                textRechercheIntervenant.setThreshold(3);
            }
            //Masquer & raz tout le reste
            if (listMedecinOfficiel.size()!=0) {
                //booleanMesIntervenants = false;
                chipMesIntervenants.setVisibility(View.GONE);
                //booleanMedecin = false;
                chipMedecin.setVisibility(View.GONE);
                //booleanAutre = false;
                chipMesIntervenants.setVisibility(View.GONE);
                //booleanSpecialite = false;
                chipSpecialite.setVisibility(View.GONE);
                //booleanActivite = false;
                chipActivite.setVisibility(View.GONE);
                //booleanDepartement = false;
                chipDepartement.setVisibility(View.GONE);
                //booleanRegion = false;
                chipRegion.setVisibility(View.GONE);
            }




        });

        buttonRazFiltre.setOnClickListener(v -> {
            textRechercheIntervenant.setVisibility(View.GONE);
            buttonAppliquerFiltre.setVisibility((View.GONE));
            buttonRazFiltre.setVisibility(View.GONE);

            //Masquer & raz tout le reste
            booleanMesIntervenants = false;
            chipMesIntervenants.setCheckable(true);
            chipMesIntervenants.setChecked(false);
            chipMesIntervenants.setVisibility(View.VISIBLE);
            booleanMedecin = false;
            chipMedecin.setCheckable(true);
            chipMedecin.setChecked(false);
            chipMedecin.setVisibility(View.VISIBLE);
            booleanAutre = false;
            chipAutre.setCheckable(true);
            chipAutre.setChecked(false);
            chipAutre.setVisibility(View.VISIBLE);
            booleanSpecialite = false;
            chipSpecialite.setCheckable(true);
            chipSpecialite.setChecked(false);
            chipSpecialite.setVisibility(View.GONE);
            booleanActivite = false;
            chipActivite.setCheckable(true);
            chipActivite.setChecked(false);
            chipActivite.setVisibility(View.GONE);
            booleanDepartement = false;
            chipDepartement.setCheckable(true);
            chipDepartement.setChecked(false);
            chipDepartement.setVisibility(View.GONE);
            booleanRegion = false;
            chipRegion.setCheckable(true);
            chipRegion.setChecked(false);
            chipRegion.setVisibility(View.GONE);
            listMetier.setVisibility(View.GONE);
            listGeo.setVisibility(View.GONE);
            textRechercheIntervenant.setText(null);
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // fetch the user selected value
        String item = parent.getItemAtPosition(position).toString();
        //MedecinOfficiel medecinOfficiel = (MedecinOfficiel) parent.getItemAtPosition((position));

        // create Toast with user selected value
        Toast.makeText(ChercherMedecinOfficielActivity.this, "Selected : \t" + item, Toast.LENGTH_SHORT).show();
        //Toast.makeText(ChercherMedecinOfficielActivity.this, "Selected Item2 is: \t" + medecinOfficiel.getPrenom()+medecinOfficiel.getNom(), Toast.LENGTH_LONG).show();
        // set user selected value to the TextView
        //tvDisplay.setText(item);


        if (!booleanMesIntervenants) {
            textRechercheIntervenant.setFocusable(false);
            int positionVirgule = textRechercheIntervenant.getText().toString().indexOf(",");
            int positionParentheseOuverte = textRechercheIntervenant.getText().toString().indexOf("(");
            int positionParentheseFermee = textRechercheIntervenant.getText().toString().indexOf(")");
            int positionEtoile = textRechercheIntervenant.getText().toString().indexOf("*");

            String nom = textRechercheIntervenant.getText().toString().substring(0, positionVirgule);
            String prenom = textRechercheIntervenant.getText().toString().substring(positionVirgule + 2, positionParentheseOuverte - 1);
            String metier = textRechercheIntervenant.getText().toString().substring(positionParentheseOuverte + 1, positionParentheseFermee);
            String ville = textRechercheIntervenant.getText().toString().substring(positionEtoile + 2);

            if (booleanAutre) {
                Profession profession = Profession.find(Profession.class, "name = ?", metier).get(0);
                medecinOfficielSelectionne = MedecinOfficiel.find(MedecinOfficiel.class, "nom = ? and prenom = ? and profession = ? and ville = ?", nom, prenom, profession.getId().toString(), ville).get(0);
                Toast.makeText(ChercherMedecinOfficielActivity.this, "Selected Item is: \t" + medecinOfficielSelectionne.getPrenom() + medecinOfficielSelectionne.getNom(), Toast.LENGTH_LONG).show();
            } else if (booleanMedecin) {
                SavoirFaire savoirFaire = SavoirFaire.find(SavoirFaire.class, "name = ?", metier).get(0);
                medecinOfficielSelectionne = MedecinOfficiel.find(MedecinOfficiel.class, "nom = ? and prenom = ? and savoir_faire = ? and ville = ?", nom, prenom, savoirFaire.getId().toString(), ville).get(0);
                Toast.makeText(ChercherMedecinOfficielActivity.this, "Selected Item is: \t" + medecinOfficielSelectionne.getPrenom() + medecinOfficielSelectionne.getNom(), Toast.LENGTH_LONG).show();

            }


            buttonAssocier.setVisibility(View.VISIBLE);
        } else {
            //TODO
            selectionMesIntervenants.setFocusable(false);
            int positionVirgule = selectionMesIntervenants.getText().toString().indexOf(",");
            int positionParentheseOuverte = selectionMesIntervenants.getText().toString().indexOf("(");
            int positionParentheseFermee = selectionMesIntervenants.getText().toString().indexOf(")");
            int positionEtoile = selectionMesIntervenants.getText().toString().indexOf("*");

            String nom = selectionMesIntervenants.getText().toString().substring(0, positionVirgule);
            String prenom = selectionMesIntervenants.getText().toString().substring(positionVirgule + 2, positionParentheseOuverte - 1);
            String metier = selectionMesIntervenants.getText().toString().substring(positionParentheseOuverte + 1, positionParentheseFermee);
            String ville = selectionMesIntervenants.getText().toString().substring(positionEtoile + 2);


            List <Profession> listProfession = Profession.find(Profession.class, "name = ?", metier);
            List <SavoirFaire> listSavoirFaire = SavoirFaire.find(SavoirFaire.class, "name = ?", metier);
            if (listProfession.size() !=0) {
                Profession profession = listProfession.get(0);
                medecinOfficielSelectionne = MedecinOfficiel.find(MedecinOfficiel.class, "nom = ? and prenom = ? and profession = ? and ville = ?", nom, prenom, profession.getId().toString(), ville).get(0);
                Toast.makeText(ChercherMedecinOfficielActivity.this, "Selected Item is: \t" + medecinOfficielSelectionne.getPrenom() + medecinOfficielSelectionne.getNom(), Toast.LENGTH_LONG).show();
            }
            if (listSavoirFaire.size() !=0) {
                SavoirFaire savoirFaire = listSavoirFaire.get(0);
                medecinOfficielSelectionne = MedecinOfficiel.find(MedecinOfficiel.class, "nom = ? and prenom = ? and savoir_faire = ? and ville = ?", nom, prenom, savoirFaire.getId().toString(), ville).get(0);
                Toast.makeText(ChercherMedecinOfficielActivity.this, "Selected Item is: \t" + medecinOfficielSelectionne.getPrenom() + medecinOfficielSelectionne.getNom(), Toast.LENGTH_LONG).show();
            }

        }
        if ((medecinOfficielSelectionne.getLongitude() == 0) && (medecinOfficielSelectionne.getLatitude() == 0)) {
            medecinOfficielSelectionne.enregisterCoordonnees(this);
            medecinOfficielSelectionne.save();
        }

        buttonRdv.setVisibility((View.VISIBLE));
        //ajouter parametre medecin ou autre
        buttonPrescription.setVisibility(View.VISIBLE);
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

    public void ouvrirActiviteSuivante(Class classe){
        Intent intent = new Intent(ChercherMedecinOfficielActivity.this, classe);
        startActivity(intent);
        finish();
    }

    public void ouvrirActiviteSuivante(Class classe, String nomExtra, Long objetIdExtra ) {
        Intent intent = new Intent(ChercherMedecinOfficielActivity.this, classe);
        intent.putExtra(nomExtra, objetIdExtra);
        startActivity(intent);
        finish();
    }




}
