package com.pouillos.monpilulier.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.SugarRecord;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Utilisateur;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    private TextView textUser;
    private Button buttonListAllUser;
    private ImageButton buttonDeleteAllUser;
    private Button buttonListAllMedicament;
    private Button buttonListAllAnalyse;
    private Button buttonListAllDose;
    private Button buttonListAllExamen;
    private Button buttonListAllSpecialite;
    private Button buttonListAllCabinet;
    private Button buttonListAllDuree;
    private Button buttonListAllMedecin;
    private Button buttonListMyMedecin;
    private Button buttonNewOrdonnance;
    private Button buttonListAllOrdonnance;
    private Button buttonListAllOrdoAnalyse;
    private Button buttonRAZ;


    private Utilisateur utilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        textUser = (TextView) findViewById(R.id.textUser);
        buttonListAllUser = (Button) findViewById(R.id.buttonListAllUser);
        buttonListAllMedicament = (Button) findViewById(R.id.buttonListAllMedicament);
        buttonListAllAnalyse = (Button) findViewById(R.id.buttonListAllAnalyse);
        buttonListAllDose = (Button) findViewById(R.id.buttonListAllDose);
        buttonListAllExamen = (Button) findViewById(R.id.buttonListAllExamen);
        buttonListAllSpecialite = (Button) findViewById(R.id.buttonListAllSpecialite);
        buttonListAllCabinet = (Button) findViewById(R.id.buttonListAllCabinet);
        buttonListAllDuree = (Button) findViewById(R.id.buttonListAllDuree);
        buttonListAllMedecin = (Button) findViewById(R.id.buttonListAllMedecin);
        buttonListMyMedecin = (Button) findViewById(R.id.buttonListMyMedecin);
        buttonNewOrdonnance = (Button) findViewById(R.id.buttonNewOrdonnance);
        buttonListAllOrdonnance = (Button) findViewById(R.id.buttonListAllOrdonnance);
        buttonListAllOrdoAnalyse = (Button) findViewById(R.id.buttonListAllOrdoAnalyse);
        buttonDeleteAllUser = (ImageButton) findViewById(R.id.buttonDeleteAllUser);
        buttonRAZ = (Button) findViewById(R.id.buttonRAZ);

        //afficher le user actif
        utilisateur = (new Utilisateur()).findActifUser();
        if (utilisateur.getId() == null) {
            textUser.setText("user inconnu");
        } else {
            textUser.setText(utilisateur.getName());
        }

        buttonRAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //RAZ A LA DEMANDE
                // SugarRecord.executeQuery("DROP TABLE PATIENT_MEDECIN");


                //RAZ TOUT
             /*           SugarContext.terminate();
               SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
                schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
                SugarContext.init(getApplicationContext());
               schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());*/
            }
        });

        buttonListAllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllUserActivity = new Intent(MainActivity.this, ListAllUserActivity.class);
                listAllUserActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllUserActivity);
            }
        });

        buttonDeleteAllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilisateur.deleteAll(Utilisateur.class);
                recreate();
                //Intent listAllUserActivity = new Intent(MainActivity.this, ListAllUserActivity.class);
                //   listAllUserActivity.putExtra("precedent",MainActivity.class);
                //   startActivity(listAllUserActivity);
            }
        });


        buttonListAllMedicament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllMedicamentActivity = new Intent(MainActivity.this, ListAllMedicamentActivity.class);
                listAllMedicamentActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllMedicamentActivity);
            }
        });


        buttonListAllAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllAnalyseActivity = new Intent(MainActivity.this, ListAllAnalyseActivity.class);
                listAllAnalyseActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllAnalyseActivity);
            }
        });


        buttonListAllDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllDoseActivity = new Intent(MainActivity.this, ListAllDoseActivity.class);
                listAllDoseActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllDoseActivity);
            }
        });


        buttonListAllExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllExamenActivity = new Intent(MainActivity.this, ListAllExamenActivity.class);
                listAllExamenActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllExamenActivity);
            }
        });


        buttonListAllSpecialite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllSpecialiteActivity = new Intent(MainActivity.this, ListAllSpecialiteActivity.class);
                listAllSpecialiteActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllSpecialiteActivity);
            }
        });


        buttonListAllCabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllCabinetActivity = new Intent(MainActivity.this, ListAllCabinetActivity.class);
                listAllCabinetActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllCabinetActivity);
            }
        });


        buttonListAllDuree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllDureeActivity = new Intent(MainActivity.this, ListAllDureeActivity.class);
                listAllDureeActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllDureeActivity);
            }
        });


        buttonListAllMedecin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllMedecinActivity = new Intent(MainActivity.this, ListAllMedecinActivity.class);
                listAllMedecinActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllMedecinActivity);
            }
        });

        buttonListMyMedecin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listMyMedecinActivity = new Intent(MainActivity.this, ListMyMedecinActivity.class);
                listMyMedecinActivity.putExtra("precedent", MainActivity.class);
                startActivity(listMyMedecinActivity);
            }
        });

        buttonNewOrdonnance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newOrdonnanceActivity = new Intent(MainActivity.this, NewOrdonnanceActivity.class);
                newOrdonnanceActivity.putExtra("precedent", MainActivity.class);
                startActivity(newOrdonnanceActivity);

            }
        });

        buttonListAllOrdonnance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllOrdonnanceActivity = new Intent(MainActivity.this, ListAllOrdonnanceActivity.class);
                listAllOrdonnanceActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllOrdonnanceActivity);
            }
        });

        buttonListAllOrdoAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllOrdoAnalyseActivity = new Intent(MainActivity.this, ListAllOrdoAnalyseActivity.class);
                listAllOrdoAnalyseActivity.putExtra("precedent", MainActivity.class);
                startActivity(listAllOrdoAnalyseActivity);
            }
        });

       /* buttonListAllOrdonnance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllOrdonnanceActivity = new Intent(MainActivity.this, ListAllOrdonnanceActivity.class);
                listAllOrdonnanceActivity.putExtra("precedent",MainActivity.class);
                startActivity(listAllOrdonnanceActivity);
            }
        });

        buttonListAllOrdonnance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listAllOrdonnanceActivity = new Intent(MainActivity.this, ListAllOrdonnanceActivity.class);
                listAllOrdonnanceActivity.putExtra("precedent",MainActivity.class);
                startActivity(listAllOrdonnanceActivity);
            }
        });*/


    }
}
