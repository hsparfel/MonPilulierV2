package com.pouillos.monpilulier.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.stetho.Stetho;
import com.google.android.material.button.MaterialButton;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvAnalyseActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvContactActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvExamenActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.AssociationFormeDose;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Dose;
import com.pouillos.monpilulier.entities.ImportEtablissement;
import com.pouillos.monpilulier.entities.TypeEtablissement;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.FormePharmaceutique;
import com.pouillos.monpilulier.entities.ImportContact;
import com.pouillos.monpilulier.entities.Ordonnance;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.RdvAnalyse;
import com.pouillos.monpilulier.entities.RdvContact;
import com.pouillos.monpilulier.entities.RdvExamen;
import com.pouillos.monpilulier.entities.Region;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.interfaces.BasicUtils;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterPrescription;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRdvAnalyse;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRdvContact;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRdvExamen;
import com.pouillos.monpilulier.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;

public class AccueilActivity extends NavDrawerActivity implements BasicUtils, RecyclerAdapterRdvContact.Listener, RecyclerAdapterRdvAnalyse.Listener, RecyclerAdapterRdvExamen.Listener, RecyclerAdapterPrescription.Listener {

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.list_prescription)
    RecyclerView listPrescription;
    @BindView(R.id.button_list_prescription)
    MaterialButton buttonListPrescription;
    private boolean prescriptionDisplay = true;
    private RecyclerAdapterPrescription adapterPrescription;
    private List<Prescription> listePrescription = new ArrayList<>();

    @BindView(R.id.list_rdv_contact)
    RecyclerView listRdvContact;
    @BindView(R.id.button_list_rdv_contact)
    MaterialButton buttonListRdvContact;
    private boolean rdvContactDisplay = true;
    private RecyclerAdapterRdvContact adapterRdvContact;
    private List<RdvContact> listeRdvContact = new ArrayList<>();

    @BindView(R.id.list_rdv_analyse)
    RecyclerView listRdvAnalyse;
    @BindView(R.id.button_list_rdv_analyse)
    MaterialButton buttonListRdvAnalyse;
    private boolean rdvAnalyseDisplay = true;
    private RecyclerAdapterRdvAnalyse adapterRdvAnalyse;
    private List<RdvAnalyse> listeRdvAnalyse = new ArrayList<>();

    @BindView(R.id.list_rdv_examen)
    RecyclerView listRdvExamen;
    @BindView(R.id.button_list_rdv_examen)
    MaterialButton buttonListRdvExamen;
    private boolean rdvExamenDisplay = true;
    private RecyclerAdapterRdvExamen adapterRdvExamen;
    private List<RdvExamen> listeRdvExamen = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Stetho.initializeWithDefaults(this);

        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        textView.setText(DateUtils.ecrireDate(new Date()));

        //RAZ de secours
       /* SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
*/


       AccueilActivity.AsyncTaskRunnerBD runnerBD = new AccueilActivity.AsyncTaskRunnerBD();
       runnerBD.execute();

    }

    public void remplirImportContactBD() {
        Long count = ImportContact.count(ImportContact.class);
        if (count == 0) {
            new ImportContact("PS_LibreAcces_Personne_activite_0.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_1.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_2.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_3.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_4.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_5.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_6.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_7.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_8.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_9.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_10.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_11.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_12.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_13.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_14.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_15.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_16.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_17.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_18.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_19.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_20.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_21.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_22.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_23.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_24.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_25.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_26.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_27.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_28.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_29.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_30.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_31.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_32.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_33.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_34.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_35.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_36.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_37.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_38.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_39.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_40.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_41.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_42.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_43.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_44.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_45.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_46.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_47.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_48.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_49.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_50.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_51.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_52.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_53.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_54.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_55.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_56.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_57.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_58.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_59.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_60.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_61.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_62.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_63.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_64.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_65.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_66.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_67.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_68.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_69.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_70.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_71.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_72.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_73.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_74.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_75.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_76.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_77.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_78.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_79.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_80.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_81.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_82.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_83.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_84.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_85.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_86.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_87.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_88.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_89.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_90.csv", false).save();
            new ImportContact("PS_LibreAcces_Personne_activite_91.csv", false).save();

        }
    }

    public void remplirImportEtablissementBD() {
        Long count = ImportEtablissement.count(ImportEtablissement.class);
        if (count == 0) {
            new ImportEtablissement("etablissement.txt", false).save();
        }
    }

    @Override
    public void onClickPrescriptionButton(int position) {
        Toast.makeText(AccueilActivity.this, "click sur prescription", Toast.LENGTH_LONG).show();
        Prescription prescription = listePrescription.get(position);
        //creer la classe
        //ouvrirActiviteSuivante(this, AfficherPrescriptionActivity.class,"rdvContactId",prescription.getId(),false);
    }

    @Override
    public void onClickRdvContactButton(int position) {
        Toast.makeText(AccueilActivity.this, "click sur rdv contact", Toast.LENGTH_LONG).show();
        RdvContact rdvContact = listeRdvContact.get(position);
        ouvrirActiviteSuivante(this, AfficherRdvContactActivity.class,"rdvContactId",rdvContact.getId(),false);
    }

    @Override
    public void onClickRdvAnalyseButton(int position) {
        Toast.makeText(AccueilActivity.this, "click sur rdv analyse", Toast.LENGTH_LONG).show();
        RdvAnalyse rdvAnalyse = listeRdvAnalyse.get(position);
        ouvrirActiviteSuivante(this, AfficherRdvAnalyseActivity.class,"rdvAnalyseId",rdvAnalyse.getId(),false);
    }

    @Override
    public void onClickRdvExamenButton(int position) {
        Toast.makeText(AccueilActivity.this, "click sur rdv examen", Toast.LENGTH_LONG).show();
        RdvExamen rdvExamen = listeRdvExamen.get(position);
        ouvrirActiviteSuivante(this, AfficherRdvExamenActivity.class,"rdvExamenId",rdvExamen.getId(),false);
    }

    @OnClick(R.id.button_list_prescription)
    public void buttonListPrescriptionClick() {
        if (!prescriptionDisplay) {
            listPrescription.setVisibility(View.VISIBLE);
        } else {
            listPrescription.setVisibility(View.GONE);
        }
        prescriptionDisplay = !prescriptionDisplay;
    }
    @OnClick(R.id.button_list_rdv_contact)
    public void buttonListRdvContactClick() {
        if (!rdvContactDisplay) {
            listRdvContact.setVisibility(View.VISIBLE);
        } else {
            listRdvContact.setVisibility(View.GONE);
        }
        rdvContactDisplay = !rdvContactDisplay;
    }
    @OnClick(R.id.button_list_rdv_analyse)
    public void buttonListRdvAnalyseClick() {
        if (!rdvAnalyseDisplay) {
            listRdvAnalyse.setVisibility(View.VISIBLE);
        } else {
            listRdvAnalyse.setVisibility(View.GONE);
        }
        rdvAnalyseDisplay = !rdvAnalyseDisplay;
    }
    @OnClick(R.id.button_list_rdv_examen)
    public void buttonListRdvExamenClick() {
        if (!rdvExamenDisplay) {
            listRdvExamen.setVisibility(View.VISIBLE);
        } else {
            listRdvExamen.setVisibility(View.GONE);
        }
        rdvExamenDisplay = !rdvExamenDisplay;
    }

    private class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            activeUser = findActiveUser();
            publishProgress(5);
            //remplir BD avec valeur par defaut
            remplirFormePharmaceutiqueBD();
            publishProgress(10);
            remplirTypeEtablissementBD();
            publishProgress(15);
            remplirDefaultBD();
            publishProgress(20);
            remplirProfessionBD();
            publishProgress(30);
            remplirSavoirFaireBD();
            publishProgress(40);
            remplirRegionBD();
            publishProgress(50);
            remplirDepartementBD();
            publishProgress(70);
            remplirImportContactBD();
            publishProgress(80);
            remplirImportEtablissementBD();
            publishProgress(85);

            if (activeUser != null) {
                ActualiserListPrescription();
            }
            publishProgress(82);
            if (activeUser != null) {
                ActualiserListRdvContact();
            }

            publishProgress(84);
            if (activeUser != null) {
                ActualiserListRdvAnalyse();
            }

            publishProgress(88);
            if (activeUser != null) {
                ActualiserListRdvExamen();
            }

            publishProgress(92);
            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(AccueilActivity.this, R.string.text_DB_created, Toast.LENGTH_LONG).show();

            if (activeUser == null){
                ouvrirActiviteSuivante(AccueilActivity.this, AuthentificationActivity.class,true);
            } else {
                setTitle(getResources().getString(R.string.welcome)+" "+activeUser.getName());
            }

            configureRecyclerView();
            configureOnClickRecyclerView();
            if (listePrescription.size() == 0) {
                buttonListPrescription.setVisibility(View.GONE);
            }
            if (listeRdvContact.size() == 0) {
                buttonListRdvContact.setVisibility(View.GONE);
            }
            if (listeRdvAnalyse.size() == 0) {
                buttonListRdvAnalyse.setVisibility(View.GONE);
            }
            if (listeRdvExamen.size() == 0) {
                buttonListRdvExamen.setVisibility(View.GONE);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }
    private void ActualiserListPrescription() {
        List<Ordonnance> listAllOrdonnance = Ordonnance.find(Ordonnance.class,"utilisateur = ?",activeUser.getId().toString());
        for (Ordonnance currentOrdonnance : listAllOrdonnance) {
            List<Prescription> listAllPrescription = Prescription.find(Prescription.class,"ordonnance = ?",currentOrdonnance.getId().toString());
            for (Prescription currentPrescription : listAllPrescription) {
                if (currentPrescription.getDateFin().after(DateUtils.ajouterJour(new Date(),1))) {
                    listePrescription.add(currentPrescription);
                }
            }
        }

        Collections.sort(listePrescription);
    }
    private void ActualiserListRdvContact() {
        List<RdvContact> listAllRdvContact = RdvContact.find(RdvContact.class,"utilisateur = ?",activeUser.getId().toString());
        for (RdvContact currentContact : listAllRdvContact) {
            if (currentContact.getDate().after(new Date())) {
                listeRdvContact.add(currentContact);
            }
        }
        Collections.sort(listeRdvContact);
    }
    private void ActualiserListRdvAnalyse() {
        List<RdvAnalyse> listAllRdvAnalyse = RdvAnalyse.find(RdvAnalyse.class,"utilisateur = ?",activeUser.getId().toString());
        for (RdvAnalyse currentAnalyse : listAllRdvAnalyse) {
            if (currentAnalyse.getDate().after(new Date())) {
                listeRdvAnalyse.add(currentAnalyse);
            }
        }
        Collections.sort(listeRdvAnalyse);
    }
    private void ActualiserListRdvExamen() {
        List<RdvExamen> listAllRdvExamen = RdvExamen.find(RdvExamen.class,"utilisateur = ?",activeUser.getId().toString());
        for (RdvExamen currentExamen : listAllRdvExamen) {
            if (currentExamen.getDate().after(new Date())) {
                listeRdvExamen.add(currentExamen);
            }
        }
        Collections.sort(listeRdvExamen);
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(listPrescription, R.layout.recycler_list_prescription)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                    }
                });
        ItemClickSupport.addTo(listRdvContact, R.layout.recycler_list_rdv_contact)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                    }
                });
        ItemClickSupport.addTo(listRdvAnalyse, R.layout.recycler_list_rdv_analyse)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                    }
                });
        ItemClickSupport.addTo(listRdvExamen, R.layout.recycler_list_rdv_examen)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                    }
                });
    }

    public void configureRecyclerView() {
        adapterPrescription = new RecyclerAdapterPrescription(listePrescription, this);
        listPrescription.setAdapter(adapterPrescription);
        listPrescription.setLayoutManager(new LinearLayoutManager(this));

        adapterRdvContact = new RecyclerAdapterRdvContact(listeRdvContact, this);
        listRdvContact.setAdapter(adapterRdvContact);
        listRdvContact.setLayoutManager(new LinearLayoutManager(this));

        adapterRdvAnalyse = new RecyclerAdapterRdvAnalyse(listeRdvAnalyse, this);
        listRdvAnalyse.setAdapter(adapterRdvAnalyse);
        listRdvAnalyse.setLayoutManager(new LinearLayoutManager(this));

        adapterRdvExamen = new RecyclerAdapterRdvExamen(listeRdvExamen, this);
        listRdvExamen.setAdapter(adapterRdvExamen);
        listRdvExamen.setLayoutManager(new LinearLayoutManager(this));
    }

    public void remplirDepartementBD() {
        Long count = Departement.count(Departement.class);
        if (count ==0) {
            new Departement("01","Ain", Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("02","Aisne",Region.find(Region.class,"nom = ?","Hauts-de-France").get(0)).save();
            new Departement("03","Allier",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("04","Alpes-de-Haute-Provence",Region.find(Region.class,"nom = ?","Provence-Alpes-Côte d'Azur").get(0)).save();
            new Departement("05","Hautes-Alpes",Region.find(Region.class,"nom = ?","Provence-Alpes-Côte d'Azur").get(0)).save();
            new Departement("06","Alpes-Maritimes",Region.find(Region.class,"nom = ?","Provence-Alpes-Côte d'Azur").get(0)).save();
            new Departement("07","Ardèche",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("08","Ardennes",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("09","Ariège",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("10","Aube",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("11","Aude",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("12","Aveyron",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("13","Bouches-du-Rhône",Region.find(Region.class,"nom = ?","Provence-Alpes-Côte d'Azur").get(0)).save();
            new Departement("14","Calvados",Region.find(Region.class,"nom = ?","Normandie").get(0)).save();
            new Departement("15","Cantal",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("16","Charente",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("17","Charente-Maritime",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("18","Cher",Region.find(Region.class,"nom = ?","Centre-Val de Loire").get(0)).save();
            new Departement("19","Corrèze",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("20","Corse",Region.find(Region.class,"nom = ?","Corse").get(0)).save();
            new Departement("21","Côte-d'Or",Region.find(Region.class,"nom = ?","Bourgogne-Franche-Comté").get(0)).save();
            new Departement("22","Côtes d'Armor",Region.find(Region.class,"nom = ?","Bretagne").get(0)).save();
            new Departement("23","Creuse",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("24","Dordogne",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("25","Doubs",Region.find(Region.class,"nom = ?","Bourgogne-Franche-Comté").get(0)).save();
            new Departement("26","Drôme",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("27","Eure",Region.find(Region.class,"nom = ?","Normandie").get(0)).save();
            new Departement("28","Eure-et-Loir",Region.find(Region.class,"nom = ?","Centre-Val de Loire").get(0)).save();
            new Departement("29","Finistère",Region.find(Region.class,"nom = ?","Bretagne").get(0)).save();
            new Departement("30","Gard",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("31","Haute-Garonne",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("32","Gers",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("33","Gironde",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("34","Hérault",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("35","Ille-et-Vilaine",Region.find(Region.class,"nom = ?","Bretagne").get(0)).save();
            new Departement("36","Indre",Region.find(Region.class,"nom = ?","Centre-Val de Loire").get(0)).save();
            new Departement("37","Indre-et-Loire",Region.find(Region.class,"nom = ?","Centre-Val de Loire").get(0)).save();
            new Departement("38","Isère",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("39","Jura",Region.find(Region.class,"nom = ?","Bourgogne-Franche-Comté").get(0)).save();
            new Departement("40","Landes",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("41","Loir-et-Cher",Region.find(Region.class,"nom = ?","Centre-Val de Loire").get(0)).save();
            new Departement("42","Loire",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("43","Haute-Loire",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("44","Loire-Atlantique",Region.find(Region.class,"nom = ?","Pays de la Loire").get(0)).save();
            new Departement("45","Loiret",Region.find(Region.class,"nom = ?","Centre-Val de Loire").get(0)).save();
            new Departement("46","Lot",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("47","Lot-et-Garonne",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("48","Lozère",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("49","Maine-et-Loire",Region.find(Region.class,"nom = ?","Pays de la Loire").get(0)).save();
            new Departement("50","Manche",Region.find(Region.class,"nom = ?","Normandie").get(0)).save();
            new Departement("51","Marne",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("52","Haute-Marne",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("53","Mayenne",Region.find(Region.class,"nom = ?","Pays de la Loire").get(0)).save();
            new Departement("54","Meurthe-et-Moselle",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("55","Meuse",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("56","Morbihan",Region.find(Region.class,"nom = ?","Bretagne").get(0)).save();
            new Departement("57","Moselle",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("58","Nièvre",Region.find(Region.class,"nom = ?","Bourgogne-Franche-Comté").get(0)).save();
            new Departement("59","Nord",Region.find(Region.class,"nom = ?","Hauts-de-France").get(0)).save();
            new Departement("60","Oise",Region.find(Region.class,"nom = ?","Hauts-de-France").get(0)).save();
            new Departement("61","Orne",Region.find(Region.class,"nom = ?","Normandie").get(0)).save();
            new Departement("62","Pas-de-Calais",Region.find(Region.class,"nom = ?","Hauts-de-France").get(0)).save();
            new Departement("63","Puy-de-Dôme",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("64","Pyrénées-Atlantiques",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("65","Hautes-Pyrénées",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("66","Pyrénées-Orientales",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("67","Bas-Rhin",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("68","Haut-Rhin",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("69","Rhône",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("70","Haute-Saône",Region.find(Region.class,"nom = ?","Bourgogne-Franche-Comté").get(0)).save();
            new Departement("71","Saône-et-Loire",Region.find(Region.class,"nom = ?","Bourgogne-Franche-Comté").get(0)).save();
            new Departement("72","Sarthe",Region.find(Region.class,"nom = ?","Pays de la Loire").get(0)).save();
            new Departement("73","Savoie",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("74","Haute-Savoie",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
            new Departement("75","Paris",Region.find(Region.class,"nom = ?","Ile-de-France").get(0)).save();
            new Departement("76","Seine-Maritime",Region.find(Region.class,"nom = ?","Normandie").get(0)).save();
            new Departement("77","Seine-et-Marne",Region.find(Region.class,"nom = ?","Ile-de-France").get(0)).save();
            new Departement("78","Yvelines",Region.find(Region.class,"nom = ?","Ile-de-France").get(0)).save();
            new Departement("79","Deux-Sèvres",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("80","Somme",Region.find(Region.class,"nom = ?","Hauts-de-France").get(0)).save();
            new Departement("81","Tarn",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("82","Tarn-et-Garonne",Region.find(Region.class,"nom = ?","Occitanie").get(0)).save();
            new Departement("83","Var",Region.find(Region.class,"nom = ?","Provence-Alpes-Côte d'Azur").get(0)).save();
            new Departement("84","Vaucluse",Region.find(Region.class,"nom = ?","Provence-Alpes-Côte d'Azur").get(0)).save();
            new Departement("85","Vendée",Region.find(Region.class,"nom = ?","Pays de la Loire").get(0)).save();
            new Departement("86","Vienne",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("87","Haute-Vienne",Region.find(Region.class,"nom = ?","Nouvelle-Aquitaine").get(0)).save();
            new Departement("88","Vosges",Region.find(Region.class,"nom = ?","Grand Est").get(0)).save();
            new Departement("89","Yonne",Region.find(Region.class,"nom = ?","Bourgogne-Franche-Comté").get(0)).save();
            new Departement("90","Territoire-de-Belfort",Region.find(Region.class,"nom = ?","Bourgogne-Franche-Comté").get(0)).save();
            new Departement("91","Essonne",Region.find(Region.class,"nom = ?","Ile-de-France").get(0)).save();
            new Departement("92","Hauts-de-Seine",Region.find(Region.class,"nom = ?","Ile-de-France").get(0)).save();
            new Departement("93","Seine-Saint-Denis",Region.find(Region.class,"nom = ?","Ile-de-France").get(0)).save();
            new Departement("94","Val-de-Marne",Region.find(Region.class,"nom = ?","Ile-de-France").get(0)).save();
            new Departement("95","Val-D'Oise",Region.find(Region.class,"nom = ?","Ile-de-France").get(0)).save();
            new Departement("97","Outre-Mer",Region.find(Region.class,"nom = ?","Dom-Tom").get(0)).save();
            new Departement("98","Autre",Region.find(Region.class,"nom = ?","Autre").get(0)).save();
            new Departement("XX","Indéfini",Region.find(Region.class,"nom = ?","Indéfini").get(0)).save();
        }
    }

    public void remplirRegionBD() {
        Long count = Region.count(Region.class);
        if (count ==0) {
            new Region("Auvergne-Rhône-Alpes").save();
            new Region("Bourgogne-Franche-Comté").save();
            new Region("Bretagne").save();
            new Region("Centre-Val de Loire").save();
            new Region("Corse").save();
            new Region("Grand Est").save();
            new Region("Hauts-de-France").save();
            new Region("Ile-de-France").save();
            new Region("Normandie").save();
            new Region("Nouvelle-Aquitaine").save();
            new Region("Occitanie").save();
            new Region("Pays de la Loire").save();
            new Region("Provence-Alpes-Côte d'Azur").save();
            new Region("Dom-Tom").save();
            new Region("Autre").save();
            new Region("Indéfini").save();
        }
    }

    public void remplirSavoirFaireBD() {
        Long count = SavoirFaire.count(SavoirFaire.class);
        if (count ==0) {
            new SavoirFaire("Allergologie").save();
            new SavoirFaire("Anatomie et cytologie pathologiques").save();
            new SavoirFaire("Anesthesie-réanimation").save();
            new SavoirFaire("Biologie médicale").save();
            new SavoirFaire("Cardiologie et maladies vasculaires").save();
            new SavoirFaire("Chirurgie générale").save();
            new SavoirFaire("Chirurgie infantile").save();
            new SavoirFaire("Chirurgie maxillo-faciale").save();
            new SavoirFaire("Chirurgie maxillo-faciale (réforme 2017)").save();
            new SavoirFaire("Chirurgie maxillo-faciale et stomatologie").save();
            new SavoirFaire("Chirurgie Orale").save();
            new SavoirFaire("Chirurgie orthopédique et traumatologie").save();
            new SavoirFaire("Chirurgie plastique reconstructrice et esthétique").save();
            new SavoirFaire("Chirurgie thoracique et cardio-vasculaire").save();
            new SavoirFaire("Chirurgie urologique").save();
            new SavoirFaire("Chirurgie vasculaire").save();
            new SavoirFaire("Chirurgie viscérale et digestive").save();
            new SavoirFaire("Dermatologie et vénéréologie").save();
            new SavoirFaire("Endocrinologie et métabolisme").save();
            new SavoirFaire("Endocrinologie, diabétologie, nutrition").save();
            new SavoirFaire("Gastro-entérologie et hépatologie").save();
            new SavoirFaire("Génétique médicale").save();
            new SavoirFaire("Gériatrie").save();
            new SavoirFaire("Gynécologie médicale").save();
            new SavoirFaire("Gynécologie médicale et obstétrique").save();
            new SavoirFaire("Gynécologie-obstétrique").save();
            new SavoirFaire("Gynéco-obstétrique et Gynéco médicale option Gynéco-médicale").save();
            new SavoirFaire("Gynéco-obstétrique et Gynéco médicale option Gynéco-obst").save();
            new SavoirFaire("Hématologie").save();
            new SavoirFaire("Hématologie (option Maladie du sang)").save();
            new SavoirFaire("Hématologie (option Onco-hématologie)").save();
            new SavoirFaire("Hématologie (réforme 2017)").save();
            new SavoirFaire("Maladies infectieuses et tropicales").save();
            new SavoirFaire("Médecine Bucco-Dentaire").save();
            new SavoirFaire("Médecine du travail").save();
            new SavoirFaire("Médecine d'urgence").save();
            new SavoirFaire("Médecine Générale").save();
            new SavoirFaire("Médecine intensive-réanimation").save();
            new SavoirFaire("Médecine interne").save();
            new SavoirFaire("Médecine interne et immunologie clinique").save();
            new SavoirFaire("Médecine légale et expertises médicales").save();
            new SavoirFaire("Médecine nucléaire").save();
            new SavoirFaire("Médecine physique et réadaptation").save();
            new SavoirFaire("Médecine vasculaire").save();
            new SavoirFaire("Néphrologie").save();
            new SavoirFaire("Neuro-chirurgie").save();
            new SavoirFaire("Neurologie").save();
            new SavoirFaire("Neuro-psychiatrie").save();
            new SavoirFaire("O.R.L et chirurgie cervico faciale").save();
            new SavoirFaire("Obstétrique").save();
            new SavoirFaire("Oncologie (option onco-hématologie)").save();
            new SavoirFaire("Oncologie option médicale").save();
            new SavoirFaire("Oncologie option radiothérapie").save();
            new SavoirFaire("Ophtalmologie").save();
            new SavoirFaire("Orthopédie dento-faciale").save();
            new SavoirFaire("Oto-rhino-laryngologie").save();
            new SavoirFaire("Pédiatrie").save();
            new SavoirFaire("Pneumologie").save();
            new SavoirFaire("Psychiatrie").save();
            new SavoirFaire("Psychiatrie option enfant & adolescent").save();
            new SavoirFaire("Qualification PAC").save();
            new SavoirFaire("Radio-diagnostic").save();
            new SavoirFaire("Radio-thérapie ").save();
            new SavoirFaire("Recherche médicale").save();
            new SavoirFaire("Rhumatologie").save();
            new SavoirFaire("Santé publique et médecine sociale").save();
            new SavoirFaire("Stomatologie").save();
            new SavoirFaire("Urologie").save();
        }
    }

    public void remplirProfessionBD() {
        Long count = Profession.count(Profession.class);
        if (count ==0) {
            new Profession("Audioprothésiste").save();
            new Profession("Chirurgien-Dentiste").save();
            new Profession("Diététicien").save();
            new Profession("Epithésiste").save();
            new Profession("Ergothérapeute").save();
            new Profession("Infirmier").save();
            new Profession("Manipulateur ERM").save();
            new Profession("Masseur-Kinésithérapeute").save();
            new Profession("Médecin").save();
            new Profession("Oculariste").save();
            new Profession("Opticien-Lunetier").save();
            new Profession("Orthopédiste-Orthésiste").save();
            new Profession("Orthophoniste").save();
            new Profession("Orthoprothésiste").save();
            new Profession("Orthoptiste").save();
            new Profession("Pédicure-Podologue").save();
            new Profession("Pharmacien").save();
            new Profession("Podo-Orthésiste").save();
            new Profession("Psychomotricien").save();
            new Profession("Sage-Femme").save();
            new Profession("Technicien de laboratoire médical").save();
        }
    }

    public void remplirFormePharmaceutiqueBD() {
        Long count = FormePharmaceutique.count(FormePharmaceutique.class);
        if (count ==0) {
            new FormePharmaceutique("comprimé et solution(s) et granules et poudre").save();
            new FormePharmaceutique("comprimé et solution(s) et granules et poudre et pommade").save();
            new FormePharmaceutique("crème et solution et granules et poudre").save();
            new FormePharmaceutique("crème et solution et granules et poudre et pommade").save();
            new FormePharmaceutique("dispositif(s)").save();
            new FormePharmaceutique("solution et granules et poudre et pommade").save();
            new FormePharmaceutique("bain de bouche").save();
            new FormePharmaceutique("bâton pour application").save();
            new FormePharmaceutique("bâton pour usage dentaire").save();
            new FormePharmaceutique("bâton pour usage urétral").save();
            new FormePharmaceutique("capsule").save();
            new FormePharmaceutique("capsule molle").save();
            new FormePharmaceutique("capsule molle ou").save();
            new FormePharmaceutique("capsule pour inhalation par vapeur").save();
            new FormePharmaceutique("cartouche pour inhalation").save();
            new FormePharmaceutique("collutoire").save();
            new FormePharmaceutique("collyre").save();
            new FormePharmaceutique("collyre à libération prolongée").save();
            new FormePharmaceutique("collyre en émulsion").save();
            new FormePharmaceutique("collyre en solution").save();
            new FormePharmaceutique("collyre en suspension").save();
            new FormePharmaceutique("collyre pour solution").save();
            new FormePharmaceutique("compresse et solution(s) et générateur radiopharmaceutique").save();
            new FormePharmaceutique("compresse imprégné(e)").save();
            new FormePharmaceutique("compresse imprégné(e) pour usage dentaire").save();
            new FormePharmaceutique("comprimé").save();
            new FormePharmaceutique("comprimé à croquer").save();
            new FormePharmaceutique("comprimé à croquer à sucer ou dispersible").save();
            new FormePharmaceutique("comprimé à croquer ou à sucer").save();
            new FormePharmaceutique("comprimé à croquer ou dispersible").save();
            new FormePharmaceutique("comprimé à libération modifiée").save();
            new FormePharmaceutique("comprimé à libération prolongée").save();
            new FormePharmaceutique("comprimé à mâcher").save();
            new FormePharmaceutique("comprimé à sucer").save();
            new FormePharmaceutique("comprimé à sucer ou à croquer").save();
            new FormePharmaceutique("comprimé à sucer sécable").save();
            new FormePharmaceutique("comprimé dispersible").save();
            new FormePharmaceutique("comprimé dispersible et orodispersible").save();
            new FormePharmaceutique("comprimé dispersible orodispersible").save();
            new FormePharmaceutique("comprimé dispersible ou à croquer").save();
            new FormePharmaceutique("comprimé dispersible sécable").save();
            new FormePharmaceutique("comprimé dragéifié").save();
            new FormePharmaceutique("comprimé effervescent(e)").save();
            new FormePharmaceutique("comprimé effervescent(e) sécable").save();
            new FormePharmaceutique("comprimé enrobé").save();
            new FormePharmaceutique("comprimé enrobé à croquer").save();
            new FormePharmaceutique("comprimé enrobé à libération prolongée").save();
            new FormePharmaceutique("comprimé enrobé et  comprimé enrobé").save();
            new FormePharmaceutique("comprimé enrobé et  comprimé enrobé enrobé").save();
            new FormePharmaceutique("comprimé enrobé et  comprimé enrobé et  comprimé enrobé").save();
            new FormePharmaceutique("comprimé enrobé et  comprimé enrobé et  comprimé enrobé enrobé").save();
            new FormePharmaceutique("comprimé enrobé gastro-résistant(e)").save();
            new FormePharmaceutique("comprimé enrobé sécable").save();
            new FormePharmaceutique("comprimé et  comprimé").save();
            new FormePharmaceutique("comprimé et  comprimé et  comprimé").save();
            new FormePharmaceutique("comprimé et  comprimé pelliculé").save();
            new FormePharmaceutique("comprimé et  gélule").save();
            new FormePharmaceutique("comprimé gastro-résistant(e)").save();
            new FormePharmaceutique("comprimé muco-adhésif").save();
            new FormePharmaceutique("comprimé orodispersible").save();
            new FormePharmaceutique("comprimé orodispersible sécable").save();
            new FormePharmaceutique("comprimé osmotique").save();
            new FormePharmaceutique("comprimé osmotique pelliculé à libération prolongée").save();
            new FormePharmaceutique("comprimé pelliculé").save();
            new FormePharmaceutique("comprimé pelliculé à libération modifiée").save();
            new FormePharmaceutique("comprimé pelliculé à libération prolongée").save();
            new FormePharmaceutique("comprimé pelliculé dispersible").save();
            new FormePharmaceutique("comprimé pelliculé et  comprimé pelliculé").save();
            new FormePharmaceutique("comprimé pelliculé et  comprimé pelliculé et  comprimé pelliculé").save();
            new FormePharmaceutique("comprimé pelliculé et  comprimé pelliculé pelliculé").save();
            new FormePharmaceutique("comprimé pelliculé et  granulés effervescent(e)").save();
            new FormePharmaceutique("comprimé pelliculé gastro-résistant(e)").save();
            new FormePharmaceutique("comprimé pelliculé quadrisécable").save();
            new FormePharmaceutique("comprimé pelliculé sécable").save();
            new FormePharmaceutique("comprimé pelliculé sécable à libération prolongée").save();
            new FormePharmaceutique("comprimé pour solution buvable").save();
            new FormePharmaceutique("comprimé pour suspension buvable").save();
            new FormePharmaceutique("comprimé quadrisécable").save();
            new FormePharmaceutique("comprimé sécable").save();
            new FormePharmaceutique("comprimé sécable à libération modifiée").save();
            new FormePharmaceutique("comprimé sécable à libération prolongée").save();
            new FormePharmaceutique("comprimé sécable pelliculé").save();
            new FormePharmaceutique("comprimé sécable pour suspension buvable").save();
            new FormePharmaceutique("comprimé(s) pelliculé").save();
            new FormePharmaceutique("cône pour usage dentaire").save();
            new FormePharmaceutique("crème").save();
            new FormePharmaceutique("crème épaisse pour application").save();
            new FormePharmaceutique("crème et solution et granules et poudre unidose").save();
            new FormePharmaceutique("crème pour application").save();
            new FormePharmaceutique("crème pour usage dentaire").save();
            new FormePharmaceutique("crème stérile").save();
            new FormePharmaceutique("dispersion liposomale à diluer injectable").save();
            new FormePharmaceutique("dispersion pour perfusion").save();
            new FormePharmaceutique("dispositif").save();
            new FormePharmaceutique("dispositif et  dispositif").save();
            new FormePharmaceutique("dispositif et  gel").save();
            new FormePharmaceutique("dispositif pour application").save();
            new FormePharmaceutique("éluat et  solution").save();
            new FormePharmaceutique("emplâtre").save();
            new FormePharmaceutique("emplâtre adhésif(ve)").save();
            new FormePharmaceutique("emplâtre médicamenteux(se)").save();
            new FormePharmaceutique("émulsion et  solution et  solution pour perfusion").save();
            new FormePharmaceutique("émulsion fluide pour application").save();
            new FormePharmaceutique("émulsion injectable").save();
            new FormePharmaceutique("émulsion injectable ou pour perfusion").save();
            new FormePharmaceutique("émulsion injectable pour perfusion").save();
            new FormePharmaceutique("émulsion pour application").save();
            new FormePharmaceutique("émulsion pour inhalation par fumigation").save();
            new FormePharmaceutique("émulsion pour perfusion").save();
            new FormePharmaceutique("éponge pour usage dentaire").save();
            new FormePharmaceutique("film orodispersible").save();
            new FormePharmaceutique("gaz").save();
            new FormePharmaceutique("gaz pour inhalation").save();
            new FormePharmaceutique("gel").save();
            new FormePharmaceutique("gel buvable").save();
            new FormePharmaceutique("gel dentifrice").save();
            new FormePharmaceutique("gel et").save();
            new FormePharmaceutique("gel intestinal").save();
            new FormePharmaceutique("gel pour application").save();
            new FormePharmaceutique("gel pour lavement").save();
            new FormePharmaceutique("gel pour usage dentaire").save();
            new FormePharmaceutique("gel stérile").save();
            new FormePharmaceutique("gelée").save();
            new FormePharmaceutique("gélule").save();
            new FormePharmaceutique("gélule à libération modifiée").save();
            new FormePharmaceutique("gélule à libération prolongée").save();
            new FormePharmaceutique("gélule et  gélule").save();
            new FormePharmaceutique("gélule et  gélule gastro-résistant(e)").save();
            new FormePharmaceutique("gélule gastro-résistant(e)").save();
            new FormePharmaceutique("gélule gastro-soluble et  gélule gastro-résistant(e)").save();
            new FormePharmaceutique("générateur radiopharmaceutique").save();
            new FormePharmaceutique("gomme").save();
            new FormePharmaceutique("gomme à mâcher").save();
            new FormePharmaceutique("gomme à mâcher médicamenteux(se)").save();
            new FormePharmaceutique("graines").save();
            new FormePharmaceutique("granules").save();
            new FormePharmaceutique("granulés").save();
            new FormePharmaceutique("granulés à croquer").save();
            new FormePharmaceutique("granulés à libération prolongée").save();
            new FormePharmaceutique("granulés buvable pour solution").save();
            new FormePharmaceutique("granulés buvable pour suspension").save();
            new FormePharmaceutique("granulés effervescent(e)").save();
            new FormePharmaceutique("granulés effervescent(e) pour solution buvable").save();
            new FormePharmaceutique("granulés en gélule").save();
            new FormePharmaceutique("granulés enrobé").save();
            new FormePharmaceutique("granulés enrobé en vrac").save();
            new FormePharmaceutique("granules et  crème et  solution en gouttes en gouttes").save();
            new FormePharmaceutique("granulés et  granulés pour solution buvable").save();
            new FormePharmaceutique("granules et  pommade et  solution en gouttes en gouttes").save();
            new FormePharmaceutique("granules et  poudre et  solution en gouttes en gouttes").save();
            new FormePharmaceutique("granules et  solution en gouttes en gouttes").save();
            new FormePharmaceutique("granules et  solution en gouttes en gouttes et  crème").save();
            new FormePharmaceutique("granules et  solution en gouttes en gouttes et  poudre").save();
            new FormePharmaceutique("granulés et  solvant pour suspension buvable").save();
            new FormePharmaceutique("granulés gastro-résistant(e)").save();
            new FormePharmaceutique("granulés gastro-résistant(e) pour suspension buvable").save();
            new FormePharmaceutique("granulés pour solution buvable").save();
            new FormePharmaceutique("granules pour suspension buvable").save();
            new FormePharmaceutique("granulés pour suspension buvable").save();
            new FormePharmaceutique("implant").save();
            new FormePharmaceutique("implant injectable").save();
            new FormePharmaceutique("insert").save();
            new FormePharmaceutique("liquide").save();
            new FormePharmaceutique("liquide par vapeur pour inhalation").save();
            new FormePharmaceutique("liquide pour application").save();
            new FormePharmaceutique("liquide pour inhalation par fumigation").save();
            new FormePharmaceutique("liquide pour inhalation par vapeur").save();
            new FormePharmaceutique("lotion").save();
            new FormePharmaceutique("lotion pour application").save();
            new FormePharmaceutique("lyophilisat").save();
            new FormePharmaceutique("lyophilisat et  poudre pour préparation injectable").save();
            new FormePharmaceutique("lyophilisat et  poudre pour usage parentéral").save();
            new FormePharmaceutique("lyophilisat et  solution pour préparation injectable").save();
            new FormePharmaceutique("lyophilisat et  solution pour usage parentéral").save();
            new FormePharmaceutique("lyophilisat et  solvant pour collyre").save();
            new FormePharmaceutique("lyophilisat pour préparation injectable").save();
            new FormePharmaceutique("lyophilisat pour usage parentéral").save();
            new FormePharmaceutique("lyophilisat pour usage parentéral pour perfusion").save();
            new FormePharmaceutique("matrice").save();
            new FormePharmaceutique("matrice pour colle").save();
            new FormePharmaceutique("mélange de plantes pour tisane").save();
            new FormePharmaceutique("microgranule à libération prolongée en gélule").save();
            new FormePharmaceutique("microgranule en comprimé").save();
            new FormePharmaceutique("microgranule gastro-résistant(e)").save();
            new FormePharmaceutique("microgranule gastro-résistant(e) en gélule").save();
            new FormePharmaceutique("microsphère et  solution pour usage parentéral à libération prolongée").save();
            new FormePharmaceutique("mousse").save();
            new FormePharmaceutique("mousse pour application").save();
            new FormePharmaceutique("ovule").save();
            new FormePharmaceutique("ovule à libération prolongée").save();
            new FormePharmaceutique("pansement adhésif(ve)").save();
            new FormePharmaceutique("pansement médicamenteux(se)").save();
            new FormePharmaceutique("pastille").save();
            new FormePharmaceutique("pastille à sucer").save();
            new FormePharmaceutique("pâte").save();
            new FormePharmaceutique("pâte à sucer").save();
            new FormePharmaceutique("pâte dentifrice").save();
            new FormePharmaceutique("pâte pour application").save();
            new FormePharmaceutique("pâte pour usage dentaire").save();
            new FormePharmaceutique("plante(s) pour tisane").save();
            new FormePharmaceutique("plante(s) pour tisane en vrac").save();
            new FormePharmaceutique("pommade").save();
            new FormePharmaceutique("pommade pour application").save();
            new FormePharmaceutique("pommade pour application et").save();
            new FormePharmaceutique("poudre").save();
            new FormePharmaceutique("poudre à diluer à diluer et  diluant pour solution pour perfusion").save();
            new FormePharmaceutique("poudre à diluer à diluer et  solution pour solution pour solution").save();
            new FormePharmaceutique("poudre à diluer pour solution pour perfusion").save();
            new FormePharmaceutique("poudre buvable effervescent(e) pour suspension").save();
            new FormePharmaceutique("poudre buvable pour solution").save();
            new FormePharmaceutique("poudre buvable pour suspension").save();
            new FormePharmaceutique("poudre buvable pour suspension en pot").save();
            new FormePharmaceutique("poudre effervescent(e) pour solution buvable").save();
            new FormePharmaceutique("poudre effervescent(e) pour suspension buvable").save();
            new FormePharmaceutique("poudre et  dispersion et  solvant pour solution à diluer pour dispersion pour perfusion").save();
            new FormePharmaceutique("poudre et  poudre").save();
            new FormePharmaceutique("poudre et  poudre pour solution buvable").save();
            new FormePharmaceutique("poudre et  poudre pour solution injectable").save();
            new FormePharmaceutique("poudre et  solution pour préparation injectable").save();
            new FormePharmaceutique("poudre et  solution pour solution injectable").save();
            new FormePharmaceutique("poudre et  solution pour usage parentéral").save();
            new FormePharmaceutique("poudre et  solution pour usage parentéral à diluer").save();
            new FormePharmaceutique("poudre et  solvant").save();
            new FormePharmaceutique("poudre et  solvant et  matrice pour matrice implantable").save();
            new FormePharmaceutique("poudre et  solvant et  solvant pour solution injectable").save();
            new FormePharmaceutique("poudre et  solvant pour dispersion injectable").save();
            new FormePharmaceutique("poudre et  solvant pour inhalation par nébuliseur").save();
            new FormePharmaceutique("poudre et  solvant pour préparation injectable").save();
            new FormePharmaceutique("poudre et  solvant pour solution").save();
            new FormePharmaceutique("poudre et  solvant pour solution à diluer pour perfusion").save();
            new FormePharmaceutique("poudre et  solvant pour solution injectable").save();
            new FormePharmaceutique("poudre et  solvant pour solution injectable et pour perfusion").save();
            new FormePharmaceutique("poudre et  solvant pour solution injectable ou pour perfusion").save();
            new FormePharmaceutique("poudre et  solvant pour solution injectable pour perfusion").save();
            new FormePharmaceutique("poudre et  solvant pour solution injectable pour perfusion ou buvable").save();
            new FormePharmaceutique("poudre et  solvant pour solution pour inhalation").save();
            new FormePharmaceutique("poudre et  solvant pour solution pour perfusion").save();
            new FormePharmaceutique("poudre et  solvant pour solution pour pulvérisation").save();
            new FormePharmaceutique("poudre et  solvant pour suspension buvable").save();
            new FormePharmaceutique("poudre et  solvant pour suspension injectable").save();
            new FormePharmaceutique("poudre et  solvant pour suspension injectable à libération prolongée").save();
            new FormePharmaceutique("poudre et  solvant pour suspension pour administration intravésicale").save();
            new FormePharmaceutique("poudre et  solvant pour suspension pour instillation").save();
            new FormePharmaceutique("poudre et  solvant pour usage parentéral").save();
            new FormePharmaceutique("poudre et  suspension pour suspension injectable").save();
            new FormePharmaceutique("poudre pour aérosol et pour usage parentéral").save();
            new FormePharmaceutique("poudre pour application").save();
            new FormePharmaceutique("poudre pour concentré pour solution pour perfusion").save();
            new FormePharmaceutique("poudre pour dispersion pour perfusion").save();
            new FormePharmaceutique("poudre pour inhalation").save();
            new FormePharmaceutique("poudre pour inhalation en gélule").save();
            new FormePharmaceutique("poudre pour inhalation et  poudre pour inhalation").save();
            new FormePharmaceutique("poudre pour inhalation et  poudre pour inhalation pour inhalation").save();
            new FormePharmaceutique("poudre pour injection").save();
            new FormePharmaceutique("poudre pour préparation injectable").save();
            new FormePharmaceutique("poudre pour solution").save();
            new FormePharmaceutique("poudre pour solution à diluer injectable ou pour perfusion").save();
            new FormePharmaceutique("poudre pour solution à diluer injectable pour perfusion").save();
            new FormePharmaceutique("poudre pour solution à diluer pour injection ou pour perfusion").save();
            new FormePharmaceutique("poudre pour solution à diluer pour perfusion").save();
            new FormePharmaceutique("poudre pour solution à diluer pour perfusion ou buvable").save();
            new FormePharmaceutique("poudre pour solution buvable").save();
            new FormePharmaceutique("poudre pour solution buvable entéral(e)").save();
            new FormePharmaceutique("poudre pour solution buvable et entéral(e)").save();
            new FormePharmaceutique("poudre pour solution buvable et gastro-entérale").save();
            new FormePharmaceutique("poudre pour solution injectable").save();
            new FormePharmaceutique("poudre pour solution injectable et pour perfusion").save();
            new FormePharmaceutique("poudre pour solution injectable ou pour perfusion").save();
            new FormePharmaceutique("poudre pour solution injectable pour perfusion").save();
            new FormePharmaceutique("poudre pour solution injectable pour perfusion ou buvable").save();
            new FormePharmaceutique("poudre pour solution pour inhalation par nébuliseur").save();
            new FormePharmaceutique("poudre pour solution pour injection ou pour perfusion").save();
            new FormePharmaceutique("poudre pour solution pour irrigation vésical(e)").save();
            new FormePharmaceutique("poudre pour solution pour perfusion").save();
            new FormePharmaceutique("poudre pour suspension buvable").save();
            new FormePharmaceutique("poudre pour suspension et").save();
            new FormePharmaceutique("poudre pour suspension injectable").save();
            new FormePharmaceutique("poudre pour suspension injectable pour perfusion").save();
            new FormePharmaceutique("poudre pour suspension ou").save();
            new FormePharmaceutique("poudre pour suspension pour administration intravésicale").save();
            new FormePharmaceutique("poudre pour usage diagnostic").save();
            new FormePharmaceutique("poudre pour usage parentéral").save();
            new FormePharmaceutique("précurseur radiopharmaceutique").save();
            new FormePharmaceutique("précurseur radiopharmaceutique en solution").save();
            new FormePharmaceutique("shampooing").save();
            new FormePharmaceutique("sirop").save();
            new FormePharmaceutique("solution").save();
            new FormePharmaceutique("solution à diluer et  diluant pour solution pour perfusion").save();
            new FormePharmaceutique("solution à diluer et  solvant à diluer pour solution injectable").save();
            new FormePharmaceutique("solution à diluer et  solvant injectable injectable").save();
            new FormePharmaceutique("solution à diluer et  solvant pour solution à diluer pour perfusion").save();
            new FormePharmaceutique("solution à diluer injectable").save();
            new FormePharmaceutique("solution à diluer injectable ou pour perfusion").save();
            new FormePharmaceutique("solution à diluer injectable pour perfusion").save();
            new FormePharmaceutique("solution à diluer pour perfusion").save();
            new FormePharmaceutique("solution à diluer pour solution buvable").save();
            new FormePharmaceutique("solution à diluer pour solution injectable pour perfusion").save();
            new FormePharmaceutique("solution buvable").save();
            new FormePharmaceutique("solution buvable à diluer").save();
            new FormePharmaceutique("solution buvable en gouttes").save();
            new FormePharmaceutique("solution buvable et  comprimé pelliculé buvable pelliculé").save();
            new FormePharmaceutique("solution buvable et injectable").save();
            new FormePharmaceutique("solution buvable gouttes").save();
            new FormePharmaceutique("solution buvable ou").save();
            new FormePharmaceutique("solution concentré(e) à diluer pour perfusion").save();
            new FormePharmaceutique("solution concentré(e) à diluer pour solution pour perfusion").save();
            new FormePharmaceutique("solution et").save();
            new FormePharmaceutique("solution et  émulsion et  solution pour perfusion").save();
            new FormePharmaceutique("solution et  poudre pour injection").save();
            new FormePharmaceutique("solution et  solution buvable").save();
            new FormePharmaceutique("solution et  solution et  émulsion pour perfusion").save();
            new FormePharmaceutique("solution et  solution injectable").save();
            new FormePharmaceutique("solution et  solution pour application").save();
            new FormePharmaceutique("solution et  solution pour colle").save();
            new FormePharmaceutique("solution et  solution pour dialyse péritonéale").save();
            new FormePharmaceutique("solution et  solution pour hémodialyse et pour hémofiltration").save();
            new FormePharmaceutique("solution et  solution pour hémodialyse pour hémofiltration").save();
            new FormePharmaceutique("solution et  solution pour hémofiltration et pour hémodialyse").save();
            new FormePharmaceutique("solution et  solution pour hémofiltration pour hémodialyse et pour hémodiafiltration").save();
            new FormePharmaceutique("solution et  solution pour marquage").save();
            new FormePharmaceutique("solution et  solution pour perfusion").save();
            new FormePharmaceutique("solution et  suspension pour suspension injectable").save();
            new FormePharmaceutique("solution filmogène pour application").save();
            new FormePharmaceutique("solution gouttes").save();
            new FormePharmaceutique("solution injectable").save();
            new FormePharmaceutique("solution injectable à diluer ou pour perfusion").save();
            new FormePharmaceutique("solution injectable à diluer pour perfusion").save();
            new FormePharmaceutique("solution injectable à libération prolongée").save();
            new FormePharmaceutique("solution injectable et buvable").save();
            new FormePharmaceutique("solution injectable hypertonique pour perfusion").save();
            new FormePharmaceutique("solution injectable isotonique").save();
            new FormePharmaceutique("solution injectable ou").save();
            new FormePharmaceutique("solution injectable ou à diluer pour perfusion").save();
            new FormePharmaceutique("solution injectable ou pour perfusion").save();
            new FormePharmaceutique("solution injectable pour perfusion").save();
            new FormePharmaceutique("solution injectable pour usage dentaire").save();
            new FormePharmaceutique("solution moussant(e)").save();
            new FormePharmaceutique("solution oculaire pour lavage").save();
            new FormePharmaceutique("solution ou").save();
            new FormePharmaceutique("solution ou injectable pour perfusion").save();
            new FormePharmaceutique("solution pour administration endonasale").save();
            new FormePharmaceutique("solution pour administration intravésicale").save();
            new FormePharmaceutique("solution pour application").save();
            new FormePharmaceutique("solution pour application à diluer").save();
            new FormePharmaceutique("solution pour application et  solution pour application").save();
            new FormePharmaceutique("solution pour application moussant(e)").save();
            new FormePharmaceutique("solution pour application stérile").save();
            new FormePharmaceutique("solution pour bain de bouche").save();
            new FormePharmaceutique("solution pour dialyse péritonéale").save();
            new FormePharmaceutique("solution pour gargarisme ou pour bain de bouche").save();
            new FormePharmaceutique("solution pour hémodialyse pour hémodialyse et  solution pour hémodialyse pour hémodialyse").save();
            new FormePharmaceutique("solution pour hémofiltration").save();
            new FormePharmaceutique("solution pour inhalation").save();
            new FormePharmaceutique("solution pour inhalation par fumigation").save();
            new FormePharmaceutique("solution pour inhalation par nébuliseur").save();
            new FormePharmaceutique("solution pour injection").save();
            new FormePharmaceutique("solution pour injection ou pour perfusion").save();
            new FormePharmaceutique("solution pour instillation").save();
            new FormePharmaceutique("solution pour irrigation oculaire").save();
            new FormePharmaceutique("solution pour lavage").save();
            new FormePharmaceutique("solution pour marquage").save();
            new FormePharmaceutique("solution pour perfusion").save();
            new FormePharmaceutique("solution pour préparation injectable").save();
            new FormePharmaceutique("solution pour préparation injectable ou pour perfusion").save();
            new FormePharmaceutique("solution pour préparation parentérale").save();
            new FormePharmaceutique("solution pour prick-test").save();
            new FormePharmaceutique("solution pour pulvérisation").save();
            new FormePharmaceutique("solution pour pulvérisation endo-buccal(e)").save();
            new FormePharmaceutique("solution pour pulvérisation ou").save();
            new FormePharmaceutique("solution pour usage dentaire").save();
            new FormePharmaceutique("solvant et solution et poudre(s) pour colle").save();
            new FormePharmaceutique("solvant pour préparation parentérale").save();
            new FormePharmaceutique("solvant(s) et poudre(s) pour colle").save();
            new FormePharmaceutique("solvant(s) et poudre(s) pour solution injectable").save();
            new FormePharmaceutique("substitut de tissu vivant").save();
            new FormePharmaceutique("suppositoire").save();
            new FormePharmaceutique("suppositoire effervescent(e)").save();
            new FormePharmaceutique("suppositoire sécable").save();
            new FormePharmaceutique("suspension").save();
            new FormePharmaceutique("suspension à diluer pour perfusion").save();
            new FormePharmaceutique("suspension buvable").save();
            new FormePharmaceutique("suspension buvable à diluer").save();
            new FormePharmaceutique("suspension buvable ou").save();
            new FormePharmaceutique("suspension buvable ou pour instillation").save();
            new FormePharmaceutique("suspension colloidale injectable").save();
            new FormePharmaceutique("suspension et  granulés effervescent(e) pour suspension buvable").save();
            new FormePharmaceutique("suspension et  solvant pour usage dentaire").save();
            new FormePharmaceutique("suspension injectable").save();
            new FormePharmaceutique("suspension injectable à libération prolongée").save();
            new FormePharmaceutique("suspension par nébuliseur pour inhalation").save();
            new FormePharmaceutique("suspension pour application").save();
            new FormePharmaceutique("suspension pour inhalation").save();
            new FormePharmaceutique("suspension pour inhalation par nébuliseur").save();
            new FormePharmaceutique("suspension pour injection").save();
            new FormePharmaceutique("suspension pour instillation").save();
            new FormePharmaceutique("suspension pour pulvérisation").save();
            new FormePharmaceutique("système de diffusion").save();
            new FormePharmaceutique("tampon imprégné(e) pour inhalation").save();
            new FormePharmaceutique("tampon imprégné(e) pour inhalation par fumigation").save();
            new FormePharmaceutique("trousse").save();
            new FormePharmaceutique("trousse et  trousse et  trousse pour préparation radiopharmaceutique").save();
            new FormePharmaceutique("trousse et  trousse et  trousse pour préparation radiopharmaceutique pour perfusion").save();
            new FormePharmaceutique("trousse et  trousse pour préparation radiopharmaceutique").save();
            new FormePharmaceutique("trousse et  trousse pour préparation radiopharmaceutique pour injection").save();
            new FormePharmaceutique("trousse et  trousse radiopharmaceutique").save();
            new FormePharmaceutique("trousse pour préparation radiopharmaceutique").save();
            new FormePharmaceutique("trousse pour préparation radiopharmaceutique et  trousse pour préparation radiopharmaceutique").save();
            new FormePharmaceutique("trousse radiopharmaceutique").save();
            new FormePharmaceutique("vernis à ongles médicamenteux(se)").save();
        }
    }

    public void remplirTypeEtablissementBD() {
        Long count = TypeEtablissement.count(TypeEtablissement.class);
        if (count ==0) {
            new TypeEtablissement("Aire Station Nomades").save();
            new TypeEtablissement("Appartement de Coordination Thérapeutique (A.C.T.)").save();
            new TypeEtablissement("Appartement Thérapeutique").save();
            new TypeEtablissement("Atelier Thérapeutique").save();
            new TypeEtablissement("Autre Centre d'Accueil").save();
            new TypeEtablissement("Autre Etablissement Loi Hospitalière").save();
            new TypeEtablissement("Autre Laboratoire de Biologie Médicale sans FSE").save();
            new TypeEtablissement("Autre Résidence Sociale (hors Maison Relais, Pension de Fami").save();
            new TypeEtablissement("Bureau d'Aide Psychologique Universitaire (B.A.P.U.)").save();
            new TypeEtablissement("Centre Accueil Demandeurs Asile (C.A.D.A.)").save();
            new TypeEtablissement("Centre Action Médico-Sociale Précoce (C.A.M.S.P.)").save();
            new TypeEtablissement("Centre Circonscription Sanitaire et Sociale").save();
            new TypeEtablissement("Centre Crise Accueil Permanent").save();
            new TypeEtablissement("Centre d'Accueil Familial Spécialisé").save();
            new TypeEtablissement("Centre d'Accueil Thérapeutique à temps partiel (C.A.T.T.P.)").save();
            new TypeEtablissement("Centre d'Action Educative (C.A.E.)").save();
            new TypeEtablissement("Centre de Consultations Cancer").save();
            new TypeEtablissement("Centre de dialyse").save();
            new TypeEtablissement("Centre de Jour pour Personnes Agées").save();
            new TypeEtablissement("Centre de Lutte Contre Cancer").save();
            new TypeEtablissement("Centre de Médecine collective").save();
            new TypeEtablissement("Centre de Médecine Sportive").save();
            new TypeEtablissement("Centre de Médecine Universitaire").save();
            new TypeEtablissement("Centre de Pré orientation pour Handicapés").save();
            new TypeEtablissement("Centre de Santé").save();
            new TypeEtablissement("Centre de Services pour Associations").save();
            new TypeEtablissement("Centre de soins et de prévention").save();
            new TypeEtablissement("Centre de Vaccination BCG").save();
            new TypeEtablissement("Centre d'Examens de Santé").save();
            new TypeEtablissement("Centre Hébergement & Réinsertion Sociale (C.H.R.S.)").save();
            new TypeEtablissement("Centre Hospitalier (C.H.)").save();
            new TypeEtablissement("Centre Hospitalier Régional (C.H.R.)").save();
            new TypeEtablissement("Centre Hospitalier Spécialisé lutte Maladies Mentales").save();
            new TypeEtablissement("Centre hospitalier, ex Hôpital local").save();
            new TypeEtablissement("Centre Médico-Psychologique (C.M.P.)").save();
            new TypeEtablissement("Centre Médico-Psycho-Pédagogique (C.M.P.P.)").save();
            new TypeEtablissement("Centre Médico-Scolaire").save();
            new TypeEtablissement("Centre Placement Familial Socio-Educatif (C.P.F.S.E.)").save();
            new TypeEtablissement("Centre Planification ou Education Familiale").save();
            new TypeEtablissement("Centre Postcure Malades Mentaux").save();
            new TypeEtablissement("Centre Provisoire Hébergement (C.P.H.)").save();
            new TypeEtablissement("Centre Rééducation Professionnelle").save();
            new TypeEtablissement("Centre Social").save();
            new TypeEtablissement("Centre soins accompagnement prévention addictologie (CSAPA)").save();
            new TypeEtablissement("Centres de Ressources S.A.I. (Sans Aucune Indication)").save();
            new TypeEtablissement("Centres Locaux Information Coordination P.A .(C.L.I.C.)").save();
            new TypeEtablissement("Club Equipe de Prévention").save();
            new TypeEtablissement("Communautés professionnelles territoriales de santé (CPTS)").save();
            new TypeEtablissement("Ctre.Accueil/ Accomp.Réduc.Risq.Usag. Drogues (C.A.A.R.U.D.)").save();
            new TypeEtablissement("Dispensaire Antihansénien").save();
            new TypeEtablissement("Dispensaire Antituberculeux").save();
            new TypeEtablissement("Dispensaire Antivénérien").save();
            new TypeEtablissement("Ecole des Hautes Etudes en Santé Publique (E.H.E.S.P.)").save();
            new TypeEtablissement("Ecoles Formant aux Professions Sanitaires").save();
            new TypeEtablissement("Ecoles Formant aux Professions Sanitaires et Sociales").save();
            new TypeEtablissement("Ecoles Formant aux Professions Sociales").save();
            new TypeEtablissement("EHPA ne percevant pas des crédits d'assurance maladie").save();
            new TypeEtablissement("EHPA percevant des crédits d'assurance maladie").save();
            new TypeEtablissement("Entité Ayant Autorisation").save();
            new TypeEtablissement("Entreprise adaptée").save();
            new TypeEtablissement("Etab.Acc.Médicalisé en tout ou partie personnes handicapées").save();
            new TypeEtablissement("Etab.Accueil Non Médicalisé pour personnes handicapées").save();
            new TypeEtablissement("Etablissement Consultation Protection Infantile").save();
            new TypeEtablissement("Etablissement d'Accueil Mère-Enfant").save();
            new TypeEtablissement("Etablissement d'Accueil Temporaire d'Enfants Handicapés").save();
            new TypeEtablissement("Etablissement d'Accueil Temporaire pour Adultes Handicapés").save();
            new TypeEtablissement("Etablissement de Consultation Pré et Post-natale").save();
            new TypeEtablissement("Etablissement de santé privé autorisé en SSR").save();
            new TypeEtablissement("Etablissement de Soins Chirurgicaux").save();
            new TypeEtablissement("Etablissement de Soins du Service de Santé des Armées").save();
            new TypeEtablissement("Etablissement de Soins Longue Durée").save();
            new TypeEtablissement("Etablissement de Soins Médicaux").save();
            new TypeEtablissement("Etablissement de Soins Pluridisciplinaire").save();
            new TypeEtablissement("Etablissement de Transfusion Sanguine").save();
            new TypeEtablissement("Etablissement d'hébergement pour personnes âgées dépendantes").save();
            new TypeEtablissement("Etablissement et Service d'Aide par le Travail (E.S.A.T.)").save();
            new TypeEtablissement("Etablissement Expérimental Autres Adultes").save();
            new TypeEtablissement("Etablissement Expérimental Enfance Protégée").save();
            new TypeEtablissement("Etablissement Expérimental pour Adultes Handicapés").save();
            new TypeEtablissement("Etablissement Expérimental pour Enfance Handicapée").save();
            new TypeEtablissement("Etablissement Expérimental pour Personnes Agées").save();
            new TypeEtablissement("Etablissement Expérimental pour personnes handicapées").save();
            new TypeEtablissement("Etablissement Information Consultation Conseil Familial").save();
            new TypeEtablissement("Etablissement pour Enfants ou Adolescents Polyhandicapés").save();
            new TypeEtablissement("Etablissement Sanitaire des Prisons").save();
            new TypeEtablissement("Etablissement Soins Obstétriques Chirurgico-Gynécologiques").save();
            new TypeEtablissement("Etablissement Thermal").save();
            new TypeEtablissement("Foyer Club Restaurant").save();
            new TypeEtablissement("Foyer d'Accueil Médicalisé pour Adultes Handicapés (F.A.M.)").save();
            new TypeEtablissement("Foyer d'Accueil Polyvalent pour Adultes Handicapés").save();
            new TypeEtablissement("Foyer d'Action Educative (F.A.E.)").save();
            new TypeEtablissement("Foyer de Jeunes Travailleurs (résidence sociale ou non)").save();
            new TypeEtablissement("Foyer de l'Enfance").save();
            new TypeEtablissement("Foyer de Vie pour Adultes Handicapés").save();
            new TypeEtablissement("Foyer Hébergement Adultes Handicapés").save();
            new TypeEtablissement("Foyer Hébergement Enfants et Adolescents Handicapés").save();
            new TypeEtablissement("Foyer Travailleurs Migrants non transformé en Résidence Soc.").save();
            new TypeEtablissement("Groupement de coopération sanitaire - Etablissement de santé").save();
            new TypeEtablissement("Groupement de coopération sanitaire de moyens").save();
            new TypeEtablissement("Groupement de coopération sanitaire de moyens - Exploitant").save();
            new TypeEtablissement("Hôpital des armées").save();
            new TypeEtablissement("Hospitalisation à Domicile").save();
            new TypeEtablissement("Installation autonome de chirurgie esthétique").save();
            new TypeEtablissement("Institut d'éducation motrice").save();
            new TypeEtablissement("Institut d'Education Sensorielle Sourd/Aveugle").save();
            new TypeEtablissement("Institut Médico-Educatif (I.M.E.)").save();
            new TypeEtablissement("Institut pour Déficients Auditifs").save();
            new TypeEtablissement("Institut pour Déficients Visuels").save();
            new TypeEtablissement("Institut Thérapeutique Éducatif et Pédagogique (I.T.E.P.)").save();
            new TypeEtablissement("Intermédiaire de Placement Social").save();
            new TypeEtablissement("Jardin d'Enfants Spécialisé").save();
            new TypeEtablissement("Laboratoire d'Analyses").save();
            new TypeEtablissement("Laboratoire de Biologie Médicale").save();
            new TypeEtablissement("Laboratoire pharmaceutique préparant délivrant allergènes").save();
            new TypeEtablissement("Lieux de vie").save();
            new TypeEtablissement("Lits d'Accueil Médicalisés (L.A.M.)").save();
            new TypeEtablissement("Lits Halte Soins Santé (L.H.S.S.)").save();
            new TypeEtablissement("Logement Foyer non Spécialisé").save();
            new TypeEtablissement("Maison d'Accueil Spécialisée (M.A.S.)").save();
            new TypeEtablissement("Maison de naissance").save();
            new TypeEtablissement("Maison de santé (L.6223-3)").save();
            new TypeEtablissement("Maison de Santé pour Maladies Mentales").save();
            new TypeEtablissement("Maison d'Enfants à Caractère Social").save();
            new TypeEtablissement("Maisons d'accueil hospitalières (M.A.H.)").save();
            new TypeEtablissement("Maisons Relais - Pensions de Famille").save();
            new TypeEtablissement("Pharmacie d'Officine").save();
            new TypeEtablissement("Pharmacie Minière").save();
            new TypeEtablissement("Pharmacie Mutualiste").save();
            new TypeEtablissement("Pouponnière à Caractère Social").save();
            new TypeEtablissement("Propharmacie").save();
            new TypeEtablissement("Protection Maternelle et Infantile (P.M.I.)").save();
            new TypeEtablissement("Résidences autonomie").save();
            new TypeEtablissement("Service Action Educative en Milieu Ouvert (A.E.M.O.)").save();
            new TypeEtablissement("Service d'Accompagnement à la Vie Sociale (S.A.V.S.)").save();
            new TypeEtablissement("Service d'accompagnement médico-social adultes handicapés").save();
            new TypeEtablissement("Service d'Aide aux Familles en Difficulté").save();
            new TypeEtablissement("Service d'Aide aux Personnes Agées").save();
            new TypeEtablissement("Service d'Aide et d'Accompagnement à Domicile (S.A.A.D.)").save();
            new TypeEtablissement("Service d'Aide Ménagère à Domicile").save();
            new TypeEtablissement("Service de Réparation Pénale").save();
            new TypeEtablissement("Service de Repas à Domicile").save();
            new TypeEtablissement("Service de Soins Infirmiers A Domicile (S.S.I.A.D)").save();
            new TypeEtablissement("Service de Travailleuses Familiales").save();
            new TypeEtablissement("Service dédié mesures d'accompagnement social personnalisé").save();
            new TypeEtablissement("Service d'Éducation Spéciale et de Soins à Domicile").save();
            new TypeEtablissement("Service délégué aux prestations familiales").save();
            new TypeEtablissement("Service d'Enquêtes Sociales (S.E.S.)").save();
            new TypeEtablissement("Service d'information et de soutien aux tuteurs familiaux").save();
            new TypeEtablissement("Service Educatif Auprès des Tribunaux (S.E.A.T.)").save();
            new TypeEtablissement("Service Investigation Orientation Educative (S.I.O.E.)").save();
            new TypeEtablissement("Service mandataire judiciaire à la protection des majeurs").save();
            new TypeEtablissement("Service Médico-Psychologique Régional (S.M.P.R.)").save();
            new TypeEtablissement("Service Polyvalent Aide et Soins A Domicile (S.P.A.S.A.D.)").save();
            new TypeEtablissement("Service Social Polyvalent de Secteur").save();
            new TypeEtablissement("Service Social Spécialisé ou Polyvalent de Catégorie").save();
            new TypeEtablissement("Service Tutelle Prestation Sociale").save();
            new TypeEtablissement("Structure d'Alternative à la dialyse en centre").save();
            new TypeEtablissement("Structure Dispensatrice à domicile d'Oxygène à usage médical").save();
            new TypeEtablissement("Syndicat Inter Hospitalier (S.I.H.)").save();
            new TypeEtablissement("Traitements Spécialisés à Domicile").save();
            new TypeEtablissement("Unités Evaluation Réentraînement et d'Orient. Soc. et Pro.").save();
            new TypeEtablissement("Village d'Enfants").save();


        }
    }

    public void remplirDefaultBD() {
        /*Long count = Duree.count(Duree.class);
        if (count ==0) {
            new Duree("jour").save();
            new Duree("semaine").save();
            new Duree("mois").save();
            new Duree("an").save();
        }*/
        Long count = Analyse.count(Analyse.class);
        if (count ==0) {
            new Analyse("sang").save();
            new Analyse("urine").save();
            new Analyse("selle").save();
            new Analyse("adn").save();
            new Analyse("autre").save();
        }
        count = Dose.count(Dose.class);
        if (count ==0) {
            new Dose("application").save();
            new Dose("bâton").save();
            new Dose("capsule").save();
            new Dose("cartouche").save();
            new Dose("compresse").save();
            new Dose("comprimé").save();
            new Dose("cuillère à café").save();
            new Dose("cuillère à soupe").save();
            new Dose("dispositif").save();
            new Dose("emplâtre").save();
            new Dose("gélule").save();
            new Dose("gomme").save();
            new Dose("goutte").save();
            new Dose("graine").save();
            new Dose("granulés").save();
            new Dose("indéfini").save();
            new Dose("pansement").save();
            new Dose("pastille").save();
            new Dose("sachet").save();
            new Dose("suppositoire").save();
            new Dose("tampon").save();
            new Dose("trousse").save();
        }
        count = Examen.count(Examen.class);
        if (count ==0) {
            new Examen("radiologie").save();
            new Examen("échographie").save();
            new Examen("irm").save();
            new Examen("scanner").save();
            new Examen("électrocardiogramme (ECG)").save();
            new Examen("électroencéphalogramme (EEG)").save();
            new Examen("tomographie à émission de positron").save();
            new Examen("urographie intra-veineuse").save();
            new Examen("ultrasons").save();
            new Examen("doppler").save();
            new Examen("endoscopie").save();
            new Examen("lavement").save();
            new Examen("biopsie").save();
            new Examen("ponction lombaire").save();
            new Examen("autre").save();
        }
        count = AssociationFormeDose.count(AssociationFormeDose.class);
        if (count ==0) {
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre","comprimé").save();
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre et pommade","comprimé").save();
            new AssociationFormeDose("crème et solution et granules et poudre","application").save();
            new AssociationFormeDose("crème et solution et granules et poudre et pommade","application").save();
            new AssociationFormeDose("dispositif(s)","dispositif").save();
            new AssociationFormeDose("solution et granules et poudre et pommade","indéfini").save();
            new AssociationFormeDose("bain de bouche","application").save();
            new AssociationFormeDose("bâton pour application","bâton").save();
            new AssociationFormeDose("bâton pour usage dentaire","bâton").save();
            new AssociationFormeDose("bâton pour usage urétral","bâton").save();
            new AssociationFormeDose("capsule","capsule").save();
            new AssociationFormeDose("capsule molle","capsule").save();
            new AssociationFormeDose("capsule molle ou","capsule").save();
            new AssociationFormeDose("capsule pour inhalation par vapeur","capsule").save();
            new AssociationFormeDose("cartouche pour inhalation","cartouche").save();
            new AssociationFormeDose("collutoire","indéfini").save();
            new AssociationFormeDose("collyre","goutte").save();
            new AssociationFormeDose("collyre à libération prolongée","goutte").save();
            new AssociationFormeDose("collyre en émulsion","goutte").save();
            new AssociationFormeDose("collyre en solution","goutte").save();
            new AssociationFormeDose("collyre en suspension","goutte").save();
            new AssociationFormeDose("collyre pour solution","goutte").save();
            new AssociationFormeDose("compresse et solution(s) et générateur radiopharmaceutique","compresse").save();
            new AssociationFormeDose("compresse imprégné(e)","compresse").save();
            new AssociationFormeDose("compresse imprégné(e) pour usage dentaire","compresse").save();
            new AssociationFormeDose("comprimé","comprimé").save();
            new AssociationFormeDose("comprimé à croquer","comprimé").save();
            new AssociationFormeDose("comprimé à croquer à sucer ou dispersible","comprimé").save();
            new AssociationFormeDose("comprimé à croquer ou à sucer","comprimé").save();
            new AssociationFormeDose("comprimé à croquer ou dispersible","comprimé").save();
            new AssociationFormeDose("comprimé à libération modifiée","comprimé").save();
            new AssociationFormeDose("comprimé à libération prolongée","comprimé").save();
            new AssociationFormeDose("comprimé à mâcher","comprimé").save();
            new AssociationFormeDose("comprimé à sucer","comprimé").save();
            new AssociationFormeDose("comprimé à sucer ou à croquer","comprimé").save();
            new AssociationFormeDose("comprimé à sucer sécable","comprimé").save();
            new AssociationFormeDose("comprimé dispersible","comprimé").save();
            new AssociationFormeDose("comprimé dispersible et orodispersible","comprimé").save();
            new AssociationFormeDose("comprimé dispersible orodispersible","comprimé").save();
            new AssociationFormeDose("comprimé dispersible ou à croquer","comprimé").save();
            new AssociationFormeDose("comprimé dispersible sécable","comprimé").save();
            new AssociationFormeDose("comprimé dragéifié","comprimé").save();
            new AssociationFormeDose("comprimé effervescent(e)","comprimé").save();
            new AssociationFormeDose("comprimé effervescent(e) sécable","comprimé").save();
            new AssociationFormeDose("comprimé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé à croquer","comprimé").save();
            new AssociationFormeDose("comprimé enrobé à libération prolongée","comprimé").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé et  comprimé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé et  comprimé enrobé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé gastro-résistant(e)","comprimé").save();
            new AssociationFormeDose("comprimé enrobé sécable","comprimé").save();
            new AssociationFormeDose("comprimé et  comprimé","comprimé").save();
            new AssociationFormeDose("comprimé et  comprimé et  comprimé","comprimé").save();
            new AssociationFormeDose("comprimé et  comprimé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé et  gélule","comprimé").save();
            new AssociationFormeDose("comprimé gastro-résistant(e)","comprimé").save();
            new AssociationFormeDose("comprimé muco-adhésif","comprimé").save();
            new AssociationFormeDose("comprimé orodispersible","comprimé").save();
            new AssociationFormeDose("comprimé orodispersible sécable","comprimé").save();
            new AssociationFormeDose("comprimé osmotique","comprimé").save();
            new AssociationFormeDose("comprimé osmotique pelliculé à libération prolongée","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé à libération modifiée","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé à libération prolongée","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé dispersible","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé et  comprimé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé et  comprimé pelliculé et  comprimé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé et  comprimé pelliculé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé et  granulés effervescent(e)","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé gastro-résistant(e)","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé quadrisécable","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé sécable","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé sécable à libération prolongée","comprimé").save();
            new AssociationFormeDose("comprimé pour solution buvable","comprimé").save();
            new AssociationFormeDose("comprimé pour suspension buvable","comprimé").save();
            new AssociationFormeDose("comprimé quadrisécable","comprimé").save();
            new AssociationFormeDose("comprimé sécable","comprimé").save();
            new AssociationFormeDose("comprimé sécable à libération modifiée","comprimé").save();
            new AssociationFormeDose("comprimé sécable à libération prolongée","comprimé").save();
            new AssociationFormeDose("comprimé sécable pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé sécable pour suspension buvable","comprimé").save();
            new AssociationFormeDose("comprimé(s) pelliculé","comprimé").save();
            new AssociationFormeDose("cône pour usage dentaire","application").save();
            new AssociationFormeDose("crème","application").save();
            new AssociationFormeDose("crème épaisse pour application","application").save();
            new AssociationFormeDose("crème et solution et granules et poudre unidose","application").save();
            new AssociationFormeDose("crème pour application","application").save();
            new AssociationFormeDose("crème pour usage dentaire","application").save();
            new AssociationFormeDose("crème stérile","application").save();
            new AssociationFormeDose("dispersion liposomale à diluer injectable","indéfini").save();
            new AssociationFormeDose("dispersion pour perfusion","indéfini").save();
            new AssociationFormeDose("dispositif","dispositif").save();
            new AssociationFormeDose("dispositif et  dispositif","dispositif").save();
            new AssociationFormeDose("dispositif et  gel","dispositif").save();
            new AssociationFormeDose("dispositif pour application","dispositif").save();
            new AssociationFormeDose("éluat et  solution","indéfini").save();
            new AssociationFormeDose("emplâtre","emplâtre").save();
            new AssociationFormeDose("emplâtre adhésif(ve)","emplâtre").save();
            new AssociationFormeDose("emplâtre médicamenteux(se)","emplâtre").save();
            new AssociationFormeDose("émulsion et  solution et  solution pour perfusion","indéfini").save();
            new AssociationFormeDose("émulsion fluide pour application","indéfini").save();
            new AssociationFormeDose("émulsion injectable","indéfini").save();
            new AssociationFormeDose("émulsion injectable ou pour perfusion","indéfini").save();
            new AssociationFormeDose("émulsion injectable pour perfusion","indéfini").save();
            new AssociationFormeDose("émulsion pour application","indéfini").save();
            new AssociationFormeDose("émulsion pour inhalation par fumigation","indéfini").save();
            new AssociationFormeDose("émulsion pour perfusion","indéfini").save();
            new AssociationFormeDose("éponge pour usage dentaire","application").save();
            new AssociationFormeDose("film orodispersible","indéfini").save();
            new AssociationFormeDose("gaz","indéfini").save();
            new AssociationFormeDose("gaz pour inhalation","indéfini").save();
            new AssociationFormeDose("gel","application").save();
            new AssociationFormeDose("gel buvable","application").save();
            new AssociationFormeDose("gel dentifrice","application").save();
            new AssociationFormeDose("gel et","application").save();
            new AssociationFormeDose("gel intestinal","application").save();
            new AssociationFormeDose("gel pour application","application").save();
            new AssociationFormeDose("gel pour lavement","application").save();
            new AssociationFormeDose("gel pour usage dentaire","application").save();
            new AssociationFormeDose("gel stérile","application").save();
            new AssociationFormeDose("gelée","indéfini").save();
            new AssociationFormeDose("gélule","gélule").save();
            new AssociationFormeDose("gélule à libération modifiée","gélule").save();
            new AssociationFormeDose("gélule à libération prolongée","gélule").save();
            new AssociationFormeDose("gélule et  gélule","gélule").save();
            new AssociationFormeDose("gélule et  gélule gastro-résistant(e)","gélule").save();
            new AssociationFormeDose("gélule gastro-résistant(e)","gélule").save();
            new AssociationFormeDose("gélule gastro-soluble et  gélule gastro-résistant(e)","gélule").save();
            new AssociationFormeDose("générateur radiopharmaceutique","indéfini").save();
            new AssociationFormeDose("gomme","gomme").save();
            new AssociationFormeDose("gomme à mâcher","gomme").save();
            new AssociationFormeDose("gomme à mâcher médicamenteux(se)","gomme").save();
            new AssociationFormeDose("graines","graine").save();
            new AssociationFormeDose("granules","granulés").save();
            new AssociationFormeDose("granulés","granulés").save();
            new AssociationFormeDose("granulés à croquer","granulés").save();
            new AssociationFormeDose("granulés à libération prolongée","granulés").save();
            new AssociationFormeDose("granulés buvable pour solution","granulés").save();
            new AssociationFormeDose("granulés buvable pour suspension","granulés").save();
            new AssociationFormeDose("granulés effervescent(e)","granulés").save();
            new AssociationFormeDose("granulés effervescent(e) pour solution buvable","granulés").save();
            new AssociationFormeDose("granulés en gélule","granulés").save();
            new AssociationFormeDose("granulés enrobé","granulés").save();
            new AssociationFormeDose("granulés enrobé en vrac","granulés").save();
            new AssociationFormeDose("granules et  crème et  solution en gouttes en gouttes","granulés").save();
            new AssociationFormeDose("granulés et  granulés pour solution buvable","granulés").save();
            new AssociationFormeDose("granules et  pommade et  solution en gouttes en gouttes","granulés").save();
            new AssociationFormeDose("granules et  poudre et  solution en gouttes en gouttes","granulés").save();
            new AssociationFormeDose("granules et  solution en gouttes en gouttes","granulés").save();
            new AssociationFormeDose("granules et  solution en gouttes en gouttes et  crème","granulés").save();
            new AssociationFormeDose("granules et  solution en gouttes en gouttes et  poudre","granulés").save();
            new AssociationFormeDose("granulés et  solvant pour suspension buvable","granulés").save();
            new AssociationFormeDose("granulés gastro-résistant(e)","granulés").save();
            new AssociationFormeDose("granulés gastro-résistant(e) pour suspension buvable","granulés").save();
            new AssociationFormeDose("granulés pour solution buvable","granulés").save();
            new AssociationFormeDose("granules pour suspension buvable","granulés").save();
            new AssociationFormeDose("granulés pour suspension buvable","granulés").save();
            new AssociationFormeDose("implant","indéfini").save();
            new AssociationFormeDose("implant injectable","indéfini").save();
            new AssociationFormeDose("insert","indéfini").save();
            new AssociationFormeDose("liquide","indéfini").save();
            new AssociationFormeDose("liquide par vapeur pour inhalation","indéfini").save();
            new AssociationFormeDose("liquide pour application","indéfini").save();
            new AssociationFormeDose("liquide pour inhalation par fumigation","indéfini").save();
            new AssociationFormeDose("liquide pour inhalation par vapeur","indéfini").save();
            new AssociationFormeDose("lotion","application").save();
            new AssociationFormeDose("lotion pour application","application").save();
            new AssociationFormeDose("lyophilisat","indéfini").save();
            new AssociationFormeDose("lyophilisat et  poudre pour préparation injectable","indéfini").save();
            new AssociationFormeDose("lyophilisat et  poudre pour usage parentéral","indéfini").save();
            new AssociationFormeDose("lyophilisat et  solution pour préparation injectable","indéfini").save();
            new AssociationFormeDose("lyophilisat et  solution pour usage parentéral","indéfini").save();
            new AssociationFormeDose("lyophilisat et  solvant pour collyre","indéfini").save();
            new AssociationFormeDose("lyophilisat pour préparation injectable","indéfini").save();
            new AssociationFormeDose("lyophilisat pour usage parentéral","indéfini").save();
            new AssociationFormeDose("lyophilisat pour usage parentéral pour perfusion","indéfini").save();
            new AssociationFormeDose("matrice","indéfini").save();
            new AssociationFormeDose("matrice pour colle","indéfini").save();
            new AssociationFormeDose("mélange de plantes pour tisane","indéfini").save();
            new AssociationFormeDose("microgranule à libération prolongée en gélule","gélule").save();
            new AssociationFormeDose("microgranule en comprimé","comprimé").save();
            new AssociationFormeDose("microgranule gastro-résistant(e)","indéfini").save();
            new AssociationFormeDose("microgranule gastro-résistant(e) en gélule","gélule").save();
            new AssociationFormeDose("microsphère et  solution pour usage parentéral à libération prolongée","indéfini").save();
            new AssociationFormeDose("mousse","application").save();
            new AssociationFormeDose("mousse pour application","application").save();
            new AssociationFormeDose("ovule","indéfini").save();
            new AssociationFormeDose("ovule à libération prolongée","indéfini").save();
            new AssociationFormeDose("pansement adhésif(ve)","pansement").save();
            new AssociationFormeDose("pansement médicamenteux(se)","pansement").save();
            new AssociationFormeDose("pastille","pastille").save();
            new AssociationFormeDose("pastille à sucer","pastille").save();
            new AssociationFormeDose("pâte","application").save();
            new AssociationFormeDose("pâte à sucer","application").save();
            new AssociationFormeDose("pâte dentifrice","application").save();
            new AssociationFormeDose("pâte pour application","application").save();
            new AssociationFormeDose("pâte pour usage dentaire","application").save();
            new AssociationFormeDose("plante(s) pour tisane","indéfini").save();
            new AssociationFormeDose("plante(s) pour tisane en vrac","indéfini").save();
            new AssociationFormeDose("pommade","application").save();
            new AssociationFormeDose("pommade pour application","application").save();
            new AssociationFormeDose("pommade pour application et","application").save();
            new AssociationFormeDose("poudre","sachet").save();
            new AssociationFormeDose("poudre à diluer à diluer et  diluant pour solution pour perfusion","sachet").save();
            new AssociationFormeDose("poudre à diluer à diluer et  solution pour solution pour solution","sachet").save();
            new AssociationFormeDose("poudre à diluer pour solution pour perfusion","sachet").save();
            new AssociationFormeDose("poudre buvable effervescent(e) pour suspension","sachet").save();
            new AssociationFormeDose("poudre buvable pour solution","sachet").save();
            new AssociationFormeDose("poudre buvable pour suspension","sachet").save();
            new AssociationFormeDose("poudre buvable pour suspension en pot","sachet").save();
            new AssociationFormeDose("poudre effervescent(e) pour solution buvable","sachet").save();
            new AssociationFormeDose("poudre effervescent(e) pour suspension buvable","sachet").save();
            new AssociationFormeDose("poudre et  dispersion et  solvant pour solution à diluer pour dispersion pour perfusion","sachet").save();
            new AssociationFormeDose("poudre et  poudre","sachet").save();
            new AssociationFormeDose("poudre et  poudre pour solution buvable","sachet").save();
            new AssociationFormeDose("poudre et  poudre pour solution injectable","sachet").save();
            new AssociationFormeDose("poudre et  solution pour préparation injectable","sachet").save();
            new AssociationFormeDose("poudre et  solution pour solution injectable","sachet").save();
            new AssociationFormeDose("poudre et  solution pour usage parentéral","sachet").save();
            new AssociationFormeDose("poudre et  solution pour usage parentéral à diluer","sachet").save();
            new AssociationFormeDose("poudre et  solvant","sachet").save();
            new AssociationFormeDose("poudre et  solvant et  matrice pour matrice implantable","sachet").save();
            new AssociationFormeDose("poudre et  solvant et  solvant pour solution injectable","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour dispersion injectable","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour inhalation par nébuliseur","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour préparation injectable","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution à diluer pour perfusion","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable et pour perfusion","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable ou pour perfusion","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable pour perfusion","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable pour perfusion ou buvable","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution pour inhalation","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution pour perfusion","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour solution pour pulvérisation","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour suspension buvable","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour suspension injectable","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour suspension injectable à libération prolongée","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour suspension pour administration intravésicale","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour suspension pour instillation","sachet").save();
            new AssociationFormeDose("poudre et  solvant pour usage parentéral","sachet").save();
            new AssociationFormeDose("poudre et  suspension pour suspension injectable","sachet").save();
            new AssociationFormeDose("poudre pour aérosol et pour usage parentéral","sachet").save();
            new AssociationFormeDose("poudre pour application","sachet").save();
            new AssociationFormeDose("poudre pour concentré pour solution pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour dispersion pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour inhalation","sachet").save();
            new AssociationFormeDose("poudre pour inhalation en gélule","sachet").save();
            new AssociationFormeDose("poudre pour inhalation et  poudre pour inhalation","sachet").save();
            new AssociationFormeDose("poudre pour inhalation et  poudre pour inhalation pour inhalation","sachet").save();
            new AssociationFormeDose("poudre pour injection","sachet").save();
            new AssociationFormeDose("poudre pour préparation injectable","sachet").save();
            new AssociationFormeDose("poudre pour solution","sachet").save();
            new AssociationFormeDose("poudre pour solution à diluer injectable ou pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour solution à diluer injectable pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour solution à diluer pour injection ou pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour solution à diluer pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour solution à diluer pour perfusion ou buvable","sachet").save();
            new AssociationFormeDose("poudre pour solution buvable","sachet").save();
            new AssociationFormeDose("poudre pour solution buvable entéral(e)","sachet").save();
            new AssociationFormeDose("poudre pour solution buvable et entéral(e)","sachet").save();
            new AssociationFormeDose("poudre pour solution buvable et gastro-entérale","sachet").save();
            new AssociationFormeDose("poudre pour solution injectable","sachet").save();
            new AssociationFormeDose("poudre pour solution injectable et pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour solution injectable ou pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour solution injectable pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour solution injectable pour perfusion ou buvable","sachet").save();
            new AssociationFormeDose("poudre pour solution pour inhalation par nébuliseur","sachet").save();
            new AssociationFormeDose("poudre pour solution pour injection ou pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour solution pour irrigation vésical(e)","sachet").save();
            new AssociationFormeDose("poudre pour solution pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour suspension buvable","sachet").save();
            new AssociationFormeDose("poudre pour suspension et","sachet").save();
            new AssociationFormeDose("poudre pour suspension injectable","sachet").save();
            new AssociationFormeDose("poudre pour suspension injectable pour perfusion","sachet").save();
            new AssociationFormeDose("poudre pour suspension ou","sachet").save();
            new AssociationFormeDose("poudre pour suspension pour administration intravésicale","sachet").save();
            new AssociationFormeDose("poudre pour usage diagnostic","sachet").save();
            new AssociationFormeDose("poudre pour usage parentéral","sachet").save();
            new AssociationFormeDose("précurseur radiopharmaceutique","indéfini").save();
            new AssociationFormeDose("précurseur radiopharmaceutique en solution","indéfini").save();
            new AssociationFormeDose("shampooing","application").save();
            new AssociationFormeDose("sirop","cuillère à café").save();
            new AssociationFormeDose("sirop","cuillère à soupe").save();
            new AssociationFormeDose("solution","indéfini").save();
            new AssociationFormeDose("solution à diluer et  diluant pour solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution à diluer et  solvant à diluer pour solution injectable","indéfini").save();
            new AssociationFormeDose("solution à diluer et  solvant injectable injectable","indéfini").save();
            new AssociationFormeDose("solution à diluer et  solvant pour solution à diluer pour perfusion","indéfini").save();
            new AssociationFormeDose("solution à diluer injectable","indéfini").save();
            new AssociationFormeDose("solution à diluer injectable ou pour perfusion","indéfini").save();
            new AssociationFormeDose("solution à diluer injectable pour perfusion","indéfini").save();
            new AssociationFormeDose("solution à diluer pour perfusion","indéfini").save();
            new AssociationFormeDose("solution à diluer pour solution buvable","indéfini").save();
            new AssociationFormeDose("solution à diluer pour solution injectable pour perfusion","indéfini").save();
            new AssociationFormeDose("solution buvable","indéfini").save();
            new AssociationFormeDose("solution buvable à diluer","indéfini").save();
            new AssociationFormeDose("solution buvable en gouttes","indéfini").save();
            new AssociationFormeDose("solution buvable et  comprimé pelliculé buvable pelliculé","indéfini").save();
            new AssociationFormeDose("solution buvable et injectable","indéfini").save();
            new AssociationFormeDose("solution buvable gouttes","indéfini").save();
            new AssociationFormeDose("solution buvable ou","indéfini").save();
            new AssociationFormeDose("solution concentré(e) à diluer pour perfusion","indéfini").save();
            new AssociationFormeDose("solution concentré(e) à diluer pour solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et","indéfini").save();
            new AssociationFormeDose("solution et  émulsion et  solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  poudre pour injection","indéfini").save();
            new AssociationFormeDose("solution et  solution buvable","indéfini").save();
            new AssociationFormeDose("solution et  solution et  émulsion pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  solution injectable","indéfini").save();
            new AssociationFormeDose("solution et  solution pour application","indéfini").save();
            new AssociationFormeDose("solution et  solution pour colle","indéfini").save();
            new AssociationFormeDose("solution et  solution pour dialyse péritonéale","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémodialyse et pour hémofiltration","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémodialyse pour hémofiltration","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémofiltration et pour hémodialyse","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémofiltration pour hémodialyse et pour hémodiafiltration","indéfini").save();
            new AssociationFormeDose("solution et  solution pour marquage","indéfini").save();
            new AssociationFormeDose("solution et  solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  suspension pour suspension injectable","indéfini").save();
            new AssociationFormeDose("solution filmogène pour application","indéfini").save();
            new AssociationFormeDose("solution gouttes","indéfini").save();
            new AssociationFormeDose("solution injectable","indéfini").save();
            new AssociationFormeDose("solution injectable à diluer ou pour perfusion","indéfini").save();
            new AssociationFormeDose("solution injectable à diluer pour perfusion","indéfini").save();
            new AssociationFormeDose("solution injectable à libération prolongée","indéfini").save();
            new AssociationFormeDose("solution injectable et buvable","indéfini").save();
            new AssociationFormeDose("solution injectable hypertonique pour perfusion","indéfini").save();
            new AssociationFormeDose("solution injectable isotonique","indéfini").save();
            new AssociationFormeDose("solution injectable ou","indéfini").save();
            new AssociationFormeDose("solution injectable ou à diluer pour perfusion","indéfini").save();
            new AssociationFormeDose("solution injectable ou pour perfusion","indéfini").save();
            new AssociationFormeDose("solution injectable pour perfusion","indéfini").save();
            new AssociationFormeDose("solution injectable pour usage dentaire","indéfini").save();
            new AssociationFormeDose("solution moussant(e)","indéfini").save();
            new AssociationFormeDose("solution oculaire pour lavage","indéfini").save();
            new AssociationFormeDose("solution ou","indéfini").save();
            new AssociationFormeDose("solution ou injectable pour perfusion","indéfini").save();
            new AssociationFormeDose("solution pour administration endonasale","indéfini").save();
            new AssociationFormeDose("solution pour administration intravésicale","indéfini").save();
            new AssociationFormeDose("solution pour application","indéfini").save();
            new AssociationFormeDose("solution pour application à diluer","indéfini").save();
            new AssociationFormeDose("solution pour application et  solution pour application","indéfini").save();
            new AssociationFormeDose("solution pour application moussant(e)","indéfini").save();
            new AssociationFormeDose("solution pour application stérile","indéfini").save();
            new AssociationFormeDose("solution pour bain de bouche","indéfini").save();
            new AssociationFormeDose("solution pour dialyse péritonéale","indéfini").save();
            new AssociationFormeDose("solution pour gargarisme ou pour bain de bouche","indéfini").save();
            new AssociationFormeDose("solution pour hémodialyse pour hémodialyse et  solution pour hémodialyse pour hémodialyse","indéfini").save();
            new AssociationFormeDose("solution pour hémofiltration","indéfini").save();
            new AssociationFormeDose("solution pour inhalation","indéfini").save();
            new AssociationFormeDose("solution pour inhalation par fumigation","indéfini").save();
            new AssociationFormeDose("solution pour inhalation par nébuliseur","indéfini").save();
            new AssociationFormeDose("solution pour injection","indéfini").save();
            new AssociationFormeDose("solution pour injection ou pour perfusion","indéfini").save();
            new AssociationFormeDose("solution pour instillation","indéfini").save();
            new AssociationFormeDose("solution pour irrigation oculaire","indéfini").save();
            new AssociationFormeDose("solution pour lavage","indéfini").save();
            new AssociationFormeDose("solution pour marquage","indéfini").save();
            new AssociationFormeDose("solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution pour préparation injectable","indéfini").save();
            new AssociationFormeDose("solution pour préparation injectable ou pour perfusion","indéfini").save();
            new AssociationFormeDose("solution pour préparation parentérale","indéfini").save();
            new AssociationFormeDose("solution pour prick-test","indéfini").save();
            new AssociationFormeDose("solution pour pulvérisation","indéfini").save();
            new AssociationFormeDose("solution pour pulvérisation endo-buccal(e)","indéfini").save();
            new AssociationFormeDose("solution pour pulvérisation ou","indéfini").save();
            new AssociationFormeDose("solution pour usage dentaire","indéfini").save();
            new AssociationFormeDose("solvant et solution et poudre(s) pour colle","indéfini").save();
            new AssociationFormeDose("solvant pour préparation parentérale","indéfini").save();
            new AssociationFormeDose("solvant(s) et poudre(s) pour colle","indéfini").save();
            new AssociationFormeDose("solvant(s) et poudre(s) pour solution injectable","indéfini").save();
            new AssociationFormeDose("substitut de tissu vivant","indéfini").save();
            new AssociationFormeDose("suppositoire","suppositoire").save();
            new AssociationFormeDose("suppositoire effervescent(e)","suppositoire").save();
            new AssociationFormeDose("suppositoire sécable","suppositoire").save();
            new AssociationFormeDose("suspension","sachet").save();
            new AssociationFormeDose("suspension à diluer pour perfusion","sachet").save();
            new AssociationFormeDose("suspension buvable","sachet").save();
            new AssociationFormeDose("suspension buvable à diluer","sachet").save();
            new AssociationFormeDose("suspension buvable ou","sachet").save();
            new AssociationFormeDose("suspension buvable ou pour instillation","sachet").save();
            new AssociationFormeDose("suspension colloidale injectable","sachet").save();
            new AssociationFormeDose("suspension et  granulés effervescent(e) pour suspension buvable","sachet").save();
            new AssociationFormeDose("suspension et  solvant pour usage dentaire","sachet").save();
            new AssociationFormeDose("suspension injectable","sachet").save();
            new AssociationFormeDose("suspension injectable à libération prolongée","sachet").save();
            new AssociationFormeDose("suspension par nébuliseur pour inhalation","sachet").save();
            new AssociationFormeDose("suspension pour application","sachet").save();
            new AssociationFormeDose("suspension pour inhalation","sachet").save();
            new AssociationFormeDose("suspension pour inhalation par nébuliseur","sachet").save();
            new AssociationFormeDose("suspension pour injection","sachet").save();
            new AssociationFormeDose("suspension pour instillation","sachet").save();
            new AssociationFormeDose("suspension pour pulvérisation","sachet").save();
            new AssociationFormeDose("système de diffusion","indéfini").save();
            new AssociationFormeDose("tampon imprégné(e) pour inhalation","tampon").save();
            new AssociationFormeDose("tampon imprégné(e) pour inhalation par fumigation","tampon").save();
            new AssociationFormeDose("trousse","trousse").save();
            new AssociationFormeDose("trousse et  trousse et  trousse pour préparation radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse et  trousse et  trousse pour préparation radiopharmaceutique pour perfusion","trousse").save();
            new AssociationFormeDose("trousse et  trousse pour préparation radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse et  trousse pour préparation radiopharmaceutique pour injection","trousse").save();
            new AssociationFormeDose("trousse et  trousse radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse pour préparation radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse pour préparation radiopharmaceutique et  trousse pour préparation radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse radiopharmaceutique","trousse").save();
            new AssociationFormeDose("vernis à ongles médicamenteux(se)","application").save();
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre","indéfini").save();
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre et pommade","indéfini").save();
            new AssociationFormeDose("crème et solution et granules et poudre","indéfini").save();
            new AssociationFormeDose("crème et solution et granules et poudre et pommade","indéfini").save();
            new AssociationFormeDose("solution et granules et poudre et pommade","granulés").save();
            new AssociationFormeDose("compresse et solution(s) et générateur radiopharmaceutique","indéfini").save();
            new AssociationFormeDose("comprimé dispersible et orodispersible","indéfini").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé et  comprimé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé et  comprimé enrobé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé et  comprimé","comprimé").save();
            new AssociationFormeDose("comprimé et  comprimé et  comprimé","comprimé").save();
            new AssociationFormeDose("comprimé et  comprimé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé et  gélule","gélule").save();
            new AssociationFormeDose("comprimé pelliculé et  comprimé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé et  comprimé pelliculé et  comprimé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé et  comprimé pelliculé pelliculé","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé et  granulés effervescent(e)","granulés").save();
            new AssociationFormeDose("crème et solution et granules et poudre unidose","indéfini").save();
            new AssociationFormeDose("dispositif et  dispositif","dispositif").save();
            new AssociationFormeDose("dispositif et  gel","application").save();
            new AssociationFormeDose("éluat et  solution","indéfini").save();
            new AssociationFormeDose("émulsion et  solution et  solution pour perfusion","indéfini").save();
            new AssociationFormeDose("gélule et  gélule","gélule").save();
            new AssociationFormeDose("gélule et  gélule gastro-résistant(e)","gélule").save();
            new AssociationFormeDose("gélule gastro-soluble et  gélule gastro-résistant(e)","gélule").save();
            new AssociationFormeDose("granules et  crème et  solution en gouttes en gouttes","application").save();
            new AssociationFormeDose("granulés et  granulés pour solution buvable","granulés").save();
            new AssociationFormeDose("granules et  pommade et  solution en gouttes en gouttes","application").save();
            new AssociationFormeDose("granules et  poudre et  solution en gouttes en gouttes","sachet").save();
            new AssociationFormeDose("granules et  solution en gouttes en gouttes","indéfini").save();
            new AssociationFormeDose("granules et  solution en gouttes en gouttes et  crème","indéfini").save();
            new AssociationFormeDose("granules et  solution en gouttes en gouttes et  poudre","indéfini").save();
            new AssociationFormeDose("granulés et  solvant pour suspension buvable","indéfini").save();
            new AssociationFormeDose("lyophilisat et  poudre pour préparation injectable","sachet").save();
            new AssociationFormeDose("lyophilisat et  poudre pour usage parentéral","sachet").save();
            new AssociationFormeDose("lyophilisat et  solution pour préparation injectable","indéfini").save();
            new AssociationFormeDose("lyophilisat et  solution pour usage parentéral","indéfini").save();
            new AssociationFormeDose("lyophilisat et  solvant pour collyre","indéfini").save();
            new AssociationFormeDose("microsphère et  solution pour usage parentéral à libération prolongée","indéfini").save();
            new AssociationFormeDose("poudre à diluer à diluer et  diluant pour solution pour perfusion","indéfini").save();
            new AssociationFormeDose("poudre à diluer à diluer et  solution pour solution pour solution","indéfini").save();
            new AssociationFormeDose("poudre et  dispersion et  solvant pour solution à diluer pour dispersion pour perfusion","indéfini").save();
            new AssociationFormeDose("poudre et  poudre","sachet").save();
            new AssociationFormeDose("poudre et  poudre pour solution buvable","sachet").save();
            new AssociationFormeDose("poudre et  poudre pour solution injectable","sachet").save();
            new AssociationFormeDose("poudre et  solution pour préparation injectable","indéfini").save();
            new AssociationFormeDose("poudre et  solution pour solution injectable","indéfini").save();
            new AssociationFormeDose("poudre et  solution pour usage parentéral","indéfini").save();
            new AssociationFormeDose("poudre et  solution pour usage parentéral à diluer","indéfini").save();
            new AssociationFormeDose("poudre et  solvant","indéfini").save();
            new AssociationFormeDose("poudre et  solvant et  matrice pour matrice implantable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant et  solvant pour solution injectable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour dispersion injectable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour inhalation par nébuliseur","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour préparation injectable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution à diluer pour perfusion","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable et pour perfusion","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable ou pour perfusion","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable pour perfusion","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable pour perfusion ou buvable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution pour inhalation","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution pour perfusion","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution pour pulvérisation","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour suspension buvable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour suspension injectable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour suspension injectable à libération prolongée","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour suspension pour administration intravésicale","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour suspension pour instillation","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour usage parentéral","indéfini").save();
            new AssociationFormeDose("poudre et  suspension pour suspension injectable","sachet").save();
            new AssociationFormeDose("poudre pour aérosol et pour usage parentéral","sachet").save();
            new AssociationFormeDose("poudre pour inhalation et  poudre pour inhalation","sachet").save();
            new AssociationFormeDose("poudre pour inhalation et  poudre pour inhalation pour inhalation","sachet").save();
            new AssociationFormeDose("poudre pour solution buvable et entéral(e)","indéfini").save();
            new AssociationFormeDose("poudre pour solution buvable et gastro-entérale","indéfini").save();
            new AssociationFormeDose("poudre pour solution injectable et pour perfusion","indéfini").save();
            new AssociationFormeDose("solution à diluer et  diluant pour solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution à diluer et  solvant à diluer pour solution injectable","indéfini").save();
            new AssociationFormeDose("solution à diluer et  solvant injectable injectable","indéfini").save();
            new AssociationFormeDose("solution à diluer et  solvant pour solution à diluer pour perfusion","indéfini").save();
            new AssociationFormeDose("solution buvable et  comprimé pelliculé buvable pelliculé","comprimé").save();
            new AssociationFormeDose("solution buvable et injectable","indéfini").save();
            new AssociationFormeDose("solution et  émulsion et  solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  poudre pour injection","sachet").save();
            new AssociationFormeDose("solution et  solution buvable","indéfini").save();
            new AssociationFormeDose("solution et  solution et  émulsion pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  solution injectable","indéfini").save();
            new AssociationFormeDose("solution et  solution pour application","indéfini").save();
            new AssociationFormeDose("solution et  solution pour colle","indéfini").save();
            new AssociationFormeDose("solution et  solution pour dialyse péritonéale","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémodialyse et pour hémofiltration","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémodialyse pour hémofiltration","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémofiltration et pour hémodialyse","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémofiltration pour hémodialyse et pour hémodiafiltration","indéfini").save();
            new AssociationFormeDose("solution et  solution pour marquage","indéfini").save();
            new AssociationFormeDose("solution et  solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  suspension pour suspension injectable","sachet").save();
            new AssociationFormeDose("solution injectable et buvable","indéfini").save();
            new AssociationFormeDose("solution pour application et  solution pour application","indéfini").save();
            new AssociationFormeDose("solution pour hémodialyse pour hémodialyse et  solution pour hémodialyse pour hémodialyse","indéfini").save();
            new AssociationFormeDose("solvant et solution et poudre(s) pour colle","indéfini").save();
            new AssociationFormeDose("solvant(s) et poudre(s) pour colle","sachet").save();
            new AssociationFormeDose("solvant(s) et poudre(s) pour solution injectable","sachet").save();
            new AssociationFormeDose("suspension et  granulés effervescent(e) pour suspension buvable","granulés").save();
            new AssociationFormeDose("suspension et  solvant pour usage dentaire","indéfini").save();
            new AssociationFormeDose("trousse et  trousse et  trousse pour préparation radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse et  trousse et  trousse pour préparation radiopharmaceutique pour perfusion","trousse").save();
            new AssociationFormeDose("trousse et  trousse pour préparation radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse et  trousse pour préparation radiopharmaceutique pour injection","trousse").save();
            new AssociationFormeDose("trousse et  trousse radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse pour préparation radiopharmaceutique et  trousse pour préparation radiopharmaceutique","trousse").save();
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre","granulés").save();
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre et pommade","granulés").save();
            new AssociationFormeDose("crème et solution et granules et poudre","granulés").save();
            new AssociationFormeDose("crème et solution et granules et poudre et pommade","granulés").save();
            new AssociationFormeDose("solution et granules et poudre et pommade","sachet").save();
            new AssociationFormeDose("compresse et solution(s) et générateur radiopharmaceutique","indéfini").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé et  comprimé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé enrobé et  comprimé enrobé et  comprimé enrobé enrobé","comprimé").save();
            new AssociationFormeDose("comprimé et  comprimé et  comprimé","comprimé").save();
            new AssociationFormeDose("comprimé pelliculé et  comprimé pelliculé et  comprimé pelliculé","comprimé").save();
            new AssociationFormeDose("crème et solution et granules et poudre unidose","granulés").save();
            new AssociationFormeDose("émulsion et  solution et  solution pour perfusion","indéfini").save();
            new AssociationFormeDose("granules et  crème et  solution en gouttes en gouttes","indéfini").save();
            new AssociationFormeDose("granules et  pommade et  solution en gouttes en gouttes","indéfini").save();
            new AssociationFormeDose("granules et  poudre et  solution en gouttes en gouttes","indéfini").save();
            new AssociationFormeDose("granules et  solution en gouttes en gouttes et  crème","application").save();
            new AssociationFormeDose("granules et  solution en gouttes en gouttes et  poudre","sachet").save();
            new AssociationFormeDose("poudre et  dispersion et  solvant pour solution à diluer pour dispersion pour perfusion","indéfini").save();
            new AssociationFormeDose("poudre et  solvant et  matrice pour matrice implantable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant et  solvant pour solution injectable","indéfini").save();
            new AssociationFormeDose("poudre et  solvant pour solution injectable et pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  émulsion et  solution pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  solution et  émulsion pour perfusion","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémodialyse et pour hémofiltration","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémofiltration et pour hémodialyse","indéfini").save();
            new AssociationFormeDose("solution et  solution pour hémofiltration pour hémodialyse et pour hémodiafiltration","indéfini").save();
            new AssociationFormeDose("solvant et solution et poudre(s) pour colle","indéfini").save();
            new AssociationFormeDose("trousse et  trousse et  trousse pour préparation radiopharmaceutique","trousse").save();
            new AssociationFormeDose("trousse et  trousse et  trousse pour préparation radiopharmaceutique pour perfusion","trousse").save();
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre","sachet").save();
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre et pommade","sachet").save();
            new AssociationFormeDose("crème et solution et granules et poudre","sachet").save();
            new AssociationFormeDose("crème et solution et granules et poudre et pommade","sachet").save();
            new AssociationFormeDose("solution et granules et poudre et pommade","application").save();
            new AssociationFormeDose("crème et solution et granules et poudre unidose","sachet").save();
            new AssociationFormeDose("comprimé et solution(s) et granules et poudre et pommade","application").save();
            new AssociationFormeDose("crème et solution et granules et poudre et pommade","application").save();
        }

    }
}
