package com.pouillos.monpilulier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.listallx.ListAllAnalyseActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllCabinetActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllDoseActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllDureeActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllExamenActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllMedecinActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllMedicamentActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllOrdoAnalyseActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllOrdoExamenActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllOrdonnanceActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllProfilActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllSpecialiteActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllUserActivity;
import com.pouillos.monpilulier.activities.listmyx.ListMyMedecinActivity;
import com.pouillos.monpilulier.activities.listmyx.ListMyProfilActivity;
import com.pouillos.monpilulier.activities.newx.NewAnalyseActivity;
import com.pouillos.monpilulier.activities.newx.NewCabinetActivity;
import com.pouillos.monpilulier.activities.newx.NewDoseActivity;
import com.pouillos.monpilulier.activities.newx.NewDureeActivity;
import com.pouillos.monpilulier.activities.newx.NewExamenActivity;
import com.pouillos.monpilulier.activities.newx.NewMedecinActivity;
import com.pouillos.monpilulier.activities.newx.NewMedicamentActivity;
import com.pouillos.monpilulier.activities.newx.NewOrdonnanceActivity;
import com.pouillos.monpilulier.activities.newx.NewRdvActivity;
import com.pouillos.monpilulier.activities.newx.NewSpecialiteActivity;
import com.pouillos.monpilulier.activities.newx.NewUserActivity;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Cabinet;
import com.pouillos.monpilulier.entities.Dose;
import com.pouillos.monpilulier.entities.Duree;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.Medecin;
import com.pouillos.monpilulier.entities.Medicament;
import com.pouillos.monpilulier.entities.Specialite;
import com.pouillos.monpilulier.entities.Utilisateur;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        TextView textUser = (TextView) findViewById(R.id.textUser);
        Button buttonListAllUser = (Button) findViewById(R.id.buttonListAllUser);
        Button buttonNewUser = (Button) findViewById(R.id.buttonNewUser);
        Button buttonListAllMedicament = (Button) findViewById(R.id.buttonListAllMedicament);
        Button buttonNewMedicament = (Button) findViewById(R.id.buttonNewMedicament);
        Button buttonListAllAnalyse = (Button) findViewById(R.id.buttonListAllAnalyse);
        Button buttonNewAnalyse = (Button) findViewById(R.id.buttonNewAnalyse);
        Button buttonListAllDose = (Button) findViewById(R.id.buttonListAllDose);
        Button buttonNewDose = (Button) findViewById(R.id.buttonNewDose);
        Button buttonListAllExamen = (Button) findViewById(R.id.buttonListAllExamen);
        Button buttonNewExamen = (Button) findViewById(R.id.buttonNewExamen);
        Button buttonListAllSpecialite = (Button) findViewById(R.id.buttonListAllSpecialite);
        Button buttonNewSpecialite = (Button) findViewById(R.id.buttonNewSpecialite);
        Button buttonListAllCabinet = (Button) findViewById(R.id.buttonListAllCabinet);
        Button buttonNewCabinet = (Button) findViewById(R.id.buttonNewCabinet);
        Button buttonListAllDuree = (Button) findViewById(R.id.buttonListAllDuree);
        Button buttonNewDuree = (Button) findViewById(R.id.buttonNewDuree);
        Button buttonListAllMedecin = (Button) findViewById(R.id.buttonListAllMedecin);
        Button buttonNewMedecin = (Button) findViewById(R.id.buttonNewMedecin);
        Button buttonListMyMedecin = (Button) findViewById(R.id.buttonListMyMedecin);
        Button buttonNewOrdonnance = (Button) findViewById(R.id.buttonNewOrdonnance);
        Button buttonListAllOrdonnance = (Button) findViewById(R.id.buttonListAllOrdonnance);
        Button buttonListAllOrdoAnalyse = (Button) findViewById(R.id.buttonListAllOrdoAnalyse);
        Button buttonListAllOrdoExamen = (Button) findViewById(R.id.buttonListAllOrdoExamen);
        ImageButton buttonDeleteAllUser = (ImageButton) findViewById(R.id.buttonDeleteAllUser);
        Button buttonRAZ = (Button) findViewById(R.id.buttonRAZ);
        Button buttonNewRdv = (Button) findViewById(R.id.buttonNewRdv);
        Button buttonListAllRdv = (Button) findViewById(R.id.buttonListAllRdv);
        Button buttonListMyRdv = (Button) findViewById(R.id.buttonListMyRdv);
        Button buttonProfil = (Button) findViewById(R.id.buttonProfil);
        Button buttonListAllProfil = (Button) findViewById(R.id.buttonListAllProfil);
        Button buttonListMyProfil = (Button) findViewById(R.id.buttonListMyProfil);

        //remplir BD avec valeur par defaut
        remplirDefaultBD();
        remplirExempleBD();

        //afficher le user actif
        Utilisateur utilisateur = (new Utilisateur()).findActifUser();
        if (utilisateur.getId() == null) {
            textUser.setText("user inconnu");
        } else {
            textUser.setText(utilisateur.getName());
        }

        buttonNewDuree.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewDureeActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewMedecin.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewMedecinActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonListAllProfil.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, ListAllProfilActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonListMyProfil.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, ListMyProfilActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewSpecialite.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewSpecialiteActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewCabinet.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewCabinetActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewMedicament.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewMedicamentActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewAnalyse.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewAnalyseActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewDose.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewDoseActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewExamen.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewExamenActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewUser.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, NewUserActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonProfil.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, MyProfilActivity.class);
            myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonRAZ.setOnClickListener(v -> {

           //RAZ A LA DEMANDE
             //SugarRecord.executeQuery("DROP TABLE PATIENT_MEDECIN");
            //SugarRecord.executeQuery("DELETE FROM RDV");
            //SugarRecord.executeQuery("DELETE FROM ASSOCIATION");

            //RAZ TOUT
            SugarContext.terminate();
            SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
            schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
            SugarContext.init(getApplicationContext());
            schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
        });

        buttonListAllUser.setOnClickListener(v -> {

            Intent listAllUserActivity = new Intent(MainActivity.this, ListAllUserActivity.class);
            listAllUserActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllUserActivity);
        });

        buttonDeleteAllUser.setOnClickListener(v -> {

            Utilisateur.deleteAll(Utilisateur.class);
            recreate();
            //Intent listAllUserActivity = new Intent(MainActivity.this, ListAllUserActivity.class);
            //   listAllUserActivity.putExtra("precedent",MainActivity.class);
            //   startActivity(listAllUserActivity);
        });


        buttonListAllMedicament.setOnClickListener(v -> {
            Intent listAllMedicamentActivity = new Intent(MainActivity.this, ListAllMedicamentActivity.class);
            listAllMedicamentActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllMedicamentActivity);
        });


        buttonListAllAnalyse.setOnClickListener(v -> {
            Intent listAllAnalyseActivity = new Intent(MainActivity.this, ListAllAnalyseActivity.class);
            listAllAnalyseActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllAnalyseActivity);
        });


        buttonListAllDose.setOnClickListener(v -> {
            Intent listAllDoseActivity = new Intent(MainActivity.this, ListAllDoseActivity.class);
            listAllDoseActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllDoseActivity);
        });


        buttonListAllExamen.setOnClickListener(v -> {
            Intent listAllExamenActivity = new Intent(MainActivity.this, ListAllExamenActivity.class);
            listAllExamenActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllExamenActivity);
        });


        buttonListAllSpecialite.setOnClickListener(v -> {
            Intent listAllSpecialiteActivity = new Intent(MainActivity.this, ListAllSpecialiteActivity.class);
            listAllSpecialiteActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllSpecialiteActivity);
        });


        buttonListAllCabinet.setOnClickListener(v -> {
            Intent listAllCabinetActivity = new Intent(MainActivity.this, ListAllCabinetActivity.class);
            listAllCabinetActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllCabinetActivity);
        });


        buttonListAllDuree.setOnClickListener(v -> {
            Intent listAllDureeActivity = new Intent(MainActivity.this, ListAllDureeActivity.class);
            listAllDureeActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllDureeActivity);
        });


        buttonListAllMedecin.setOnClickListener(v -> {
            Intent listAllMedecinActivity = new Intent(MainActivity.this, ListAllMedecinActivity.class);
            listAllMedecinActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllMedecinActivity);
        });

        buttonListMyMedecin.setOnClickListener(v -> {
            Intent listMyMedecinActivity = new Intent(MainActivity.this, ListMyMedecinActivity.class);
            listMyMedecinActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listMyMedecinActivity);
        });

        buttonNewRdv.setOnClickListener(v -> {
            Intent newRdvActivity = new Intent(MainActivity.this, NewRdvActivity.class);
            newRdvActivity.putExtra("activitySource", MainActivity.class);
            startActivity(newRdvActivity);
        });

        buttonListAllRdv.setOnClickListener(v -> {
            Intent ListAllRdvActivity = new Intent(MainActivity.this, com.pouillos.monpilulier.activities.listallx.ListAllRdvActivity.class);
            ListAllRdvActivity.putExtra("activitySource", MainActivity.class);
            startActivity(ListAllRdvActivity);
        });

        buttonListMyRdv.setOnClickListener(v -> {
            Intent ListMyRdvActivity = new Intent(MainActivity.this, com.pouillos.monpilulier.activities.listmyx.ListMyRdvActivity.class);
            ListMyRdvActivity.putExtra("activitySource", MainActivity.class);
            startActivity(ListMyRdvActivity);
        });

        buttonNewOrdonnance.setOnClickListener(v -> {
            Intent newOrdonnanceActivity = new Intent(MainActivity.this, NewOrdonnanceActivity.class);
            newOrdonnanceActivity.putExtra("activitySource", MainActivity.class);
            startActivity(newOrdonnanceActivity);

        });

        buttonListAllOrdonnance.setOnClickListener(v -> {
            Intent listAllOrdonnanceActivity = new Intent(MainActivity.this, ListAllOrdonnanceActivity.class);
            listAllOrdonnanceActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllOrdonnanceActivity);
        });

        buttonListAllOrdoAnalyse.setOnClickListener(v -> {
            Intent listAllOrdoAnalyseActivity = new Intent(MainActivity.this, ListAllOrdoAnalyseActivity.class);
            listAllOrdoAnalyseActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllOrdoAnalyseActivity);
        });

        buttonListAllOrdoExamen.setOnClickListener(v -> {
            Intent listAllOrdoExamenActivity = new Intent(MainActivity.this, ListAllOrdoExamenActivity.class);
            listAllOrdoExamenActivity.putExtra("activitySource", MainActivity.class);
            startActivity(listAllOrdoExamenActivity);
        });
    }

    public void remplirDefaultBD() {
        List<Dose> listDose = Dose.listAll(Dose.class);
        if (listDose.size()==0) {
            new Dose("comprimé","desc comprime").save();
            new Dose("sachet","desc sachet").save();
            new Dose("cuillère à café","desc cac").save();
            new Dose("cuillère à soupe","desc cas").save();
            new Dose("ampoule","desc ampoule").save();
        }

        List<Duree> listDuree = Duree.listAll(Duree.class);
        if (listDuree.size()==0) {
            new Duree("jour").save();
            new Duree("semaine").save();
            new Duree("mois").save();
            new Duree("an").save();
        }

        List<Analyse> listAnalyse = Analyse.listAll(Analyse.class);
        if (listAnalyse.size()==0) {
            new Analyse("sang","desc sang").save();
            new Analyse("urine","desc urine").save();
            new Analyse("selle","desc selle").save();
        }
    }

    public void remplirExempleBD() {

        List<Utilisateur> listUtilisateur = Utilisateur.listAll(Utilisateur.class);
        if (listUtilisateur.size()==0) {
            new Utilisateur("Bob",new Date(),"desc Bob").save();
            new Utilisateur("John",new Date(),"desc John").save();
            new Utilisateur("Bill",new Date(),"desc Bill").save();
        }

        List<Medicament> listMedicament = Medicament.listAll(Medicament.class);
        if (listMedicament.size()==0) {
            new Medicament("doliprane","desc doliprane").save();
            new Medicament("neomercazole","desc neomercazole").save();
            new Medicament("levothyrox","desc levothyrox").save();
        }

        List<Examen> listExamen = Examen.listAll(Examen.class);
        if (listExamen.size()==0) {
            new Examen("radiologie","desc radiologie").save();
            new Examen("irm","desc irm").save();
            new Examen("scanner","desc scanner").save();
        }

        List<Cabinet> listCabinet = Cabinet.listAll(Cabinet.class);
        if (listCabinet.size()==0) {
            new Cabinet("hopital 1","desc hopital1","adresse1","11111","ville1").save();
            new Cabinet("hopital 2","desc hopital2","adresse2","22222","ville2").save();
            new Cabinet("hopital 3","desc hopital3","adresse3","33333","ville3").save();
        }

        List<Specialite> listSpecialite = Specialite.listAll(Specialite.class);
        if (listSpecialite.size()==0) {
            new Specialite("radiologue","desc radiologue").save();
            new Specialite("generaliste","desc generaliste").save();
            new Specialite("endocrinologue","desc endocrinologue").save();
        }

        List<Medecin> listMedecin = Medecin.listAll(Medecin.class);
        if (listMedecin.size()==0) {
            new Medecin("dr alfred","desc alfred", Specialite.findById(Specialite.class,1), "1111111111","email1@email1.fr").save();
            new Medecin("dr raoul","desc raoul", Specialite.findById(Specialite.class,2), "2222222222","email2@email2.fr").save();
            new Medecin("dr charles","desc charles", Specialite.findById(Specialite.class,3), "3333333333","email3@email3.fr").save();
        }

    }
}
