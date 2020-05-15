package com.pouillos.monpilulier.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.facebook.stetho.Stetho;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.R;
///import com.pouillos.monpilulier.activities.listallx.ListAllProfilActivity;
//import com.pouillos.monpilulier.activities.newx.NewUserActivity;
import com.pouillos.monpilulier.activities.tools.MyNotificationBroadcastReceiver;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.AlarmRdv;

import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.AssociationFormeDose;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Dose;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.FormePharmaceutique;
import com.pouillos.monpilulier.entities.ContactLight;
import com.pouillos.monpilulier.entities.ImportContact;
import com.pouillos.monpilulier.entities.Medicament;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.Region;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.entities.Utilisateur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Serializable {

    private NotificationCompat.Builder notBuilder;
    private static final int MY_NOTIFICATION_ID = 12345;
    private static final int MY_REQUEST_CODE = 100;
    private ProgressBar progressBar;
    private TextView textUser;
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);


        Button buttonRAZ = (Button) findViewById(R.id.buttonRAZ);
        Button buttonCreerNotification = (Button) findViewById(R.id.buttonCreerNotification);
        Button buttonNewMedicamentOfficiel = (Button) findViewById(R.id.buttonMajMedicamentOfficiel);
        Button buttonNewMedecinOfficiel = (Button) findViewById(R.id.buttonMajMedecinOfficiel);
        Button buttonInfoDb = (Button) findViewById(R.id.buttonInfoDb);
        Button buttonAccueil = (Button) findViewById(R.id.buttonAccueil);
        Button buttonAlarm = (Button) findViewById(R.id.buttonAddAlarm);

        progressBar = (ProgressBar) findViewById(R.id.my_progressBar);


        progressBar.setVisibility(View.VISIBLE);

        AsyncTaskRunnerBD runnerBD = new AsyncTaskRunnerBD();
        runnerBD.execute();

        //notification
        //creation du channel
       /* createNotificationChannel();

        this.notBuilder = new NotificationCompat.Builder(this, "notifTest");
        // The message will automatically be canceled when the user clicks on Panel
        this.notBuilder.setAutoCancel(true);*/

        buttonCreerNotification.setOnClickListener(v -> {
            startNotification();
        });

        buttonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startAlert(DateUtils.ajouterSeconde(new Date(),15));
               // startAlert(DateUtils.ajouterSeconde(new Date(),30));
                Long requestCode = new Date().getTime();
                startAlert(DateUtils.ajouterSeconde(new Date(),5),requestCode.intValue());
                requestCode = new Date().getTime();
                startAlert(DateUtils.ajouterSeconde(new Date(),10),requestCode.intValue());
                requestCode = new Date().getTime();
                startAlert(DateUtils.ajouterSeconde(new Date(),15),requestCode.intValue());
            }
        });

        buttonNewMedicamentOfficiel.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            //remplirMedicamentOfficielBD();
            progressBar.setProgress(0);
            AsyncTaskRunnerMedicament runnerMedicament = new AsyncTaskRunnerMedicament();
            runnerMedicament.execute();
        });

        buttonNewMedecinOfficiel.setOnClickListener(v -> {
            //remplirMedecinOfficielBD();
            progressBar.setVisibility(View.VISIBLE);
            //remplirMedicamentOfficielBD();
            progressBar.setProgress(0);
            AsyncTaskRunnerContact runner = new AsyncTaskRunnerContact(this);
            runner.execute();
        });

        buttonAccueil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccueilActivity.class);
            startActivity(intent);
            finish();
        });

        buttonInfoDb.setOnClickListener(v -> {
            long size1 = Medicament.count(Medicament.class);
            long size2 = Contact.count(Contact.class);
            Toast toast = Toast.makeText(MainActivity.this, "nb medicament: "+size1+" - nb medecin: "+size2, Toast.LENGTH_LONG);
            toast.show();
        });

        buttonRAZ.setOnClickListener(v -> {
            //mettre en commentaire pr eviter suppression involontaire FAIRE ATTENTION
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            AsyncTaskRunnerRAZ runnerRAZ = new AsyncTaskRunnerRAZ();
            runnerRAZ.execute();
        });

    }

    public void startNotification() {
        // --------------------------
        // Prepare a notification
        // --------------------------

        this.notBuilder.setSmallIcon(R.mipmap.ic_launcher);
        this.notBuilder.setTicker("This is a ticker");
        this.notBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Set the time that the event occurred.
        // Notifications in the panel are sorted by this time.

        //Date dateJour = new Date();
        Date dateNotif = new Date();
        Long  longNotif = dateNotif.getTime();

        //  this.notBuilder.setWhen(new DateUtils().ajouterHeure(dateJour, 1).getTime() + 10*1000);

        // this.notBuilder.setWhen(System.currentTimeMillis()+ 300* 1000);
        this.notBuilder.setWhen(longNotif);

        //this.notBuilder.setShowWhen(false);
        this.notBuilder.setShowWhen(true);
        this.notBuilder.setContentTitle("This is title");
        String messageNotif = "";
        messageNotif += "prog: "+dateNotif.toString();

        this.notBuilder.setContentText(messageNotif);
        //this.notBuilder.setContentText("This is content text ....");

        // Create Intent
        Intent intent = new Intent(this, MainActivity.class);

        // PendingIntent.getActivity(..) will start an Activity, and returns PendingIntent object.
        // It is equivalent to calling Context.startActivity(Intent).
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MY_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        this.notBuilder.setContentIntent(pendingIntent);

        // Get a notification service (A service available on the system).
        NotificationManager notificationService  =
                (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Builds notification and issue it

        Notification notification =  notBuilder.build();
        notificationService.notify(MY_NOTIFICATION_ID, notification);
    }

   // public void startAlert(Date date) {
    public void startAlert(Date date,int requestCode) {
        //EditText text = findViewById(R.id.time);
        //int i = Integer.parseInt(text.getText().toString());
        //int i = 10;
        //Intent intent = new Intent(this, MyBroadcastReceiver.class);
        Intent intent = new Intent(this, MyNotificationBroadcastReceiver.class);

        intent.putExtra("testA","nom du contact");
        intent.putExtra("testB","dans une heure");

        //intent.putExtra("activity",MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);


        AlarmRdv alarmRdv = new AlarmRdv();
        alarmRdv.setClasse(MainActivity.class.getName());
        alarmRdv.setDate(date);
        alarmRdv.setDateString(date.toString());
        alarmRdv.setDetail("detail");
        alarmRdv.setEcheance("echeance");
        alarmRdv.setRequestCode(requestCode);
        alarmRdv.save();

        Toast.makeText(this, "Alarm set : " + date.toString(), Toast.LENGTH_LONG).show();
        //startNotification();
    }

    private class AsyncTaskRunnerRAZ extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);

            //RAZ A LA DEMANDE
            //SugarRecord.executeQuery("DROP TABLE PATIENT_MEDECIN");
            //SugarRecord.executeQuery("DELETE FROM RDV");
            //SugarRecord.executeQuery("DELETE FROM ASSOCIATION");
            //SugarRecord.executeQuery("DELETE FROM UTILISATEUR");
            //SugarRecord.executeQuery("DELETE FROM PROFIL");
            //SugarRecord.executeQuery("DELETE FROM DEPARTEMENT");
            //SugarRecord.executeQuery("DELETE FROM CONTACT");
            //SugarRecord.executeQuery("DROP TABLE CONTACT");
            //SugarRecord.executeQuery("DELETE FROM REGION");
            //SugarRecord.executeQuery("DROP TABLE MEDECIN");
            //SugarRecord.executeQuery("DROP TABLE MEDECIN_OFFICIEL");
            //SugarRecord.executeQuery("DROP TABLE MEDICAMENT");
            //SugarRecord.executeQuery("DROP TABLE MEDICAMENT_OFFICIEL");
           // SugarRecord.executeQuery("DROP TABLE ASSOCIATION");
            //SugarRecord.executeQuery("DROP TABLE ASSOCIATION_OFFICIELLE");
            //SugarRecord.executeQuery("DROP TABLE CABINET");
            //SugarRecord.executeQuery("DROP TABLE ORDONNANCE");
            //SugarRecord.executeQuery("DROP TABLE ORDO_PRESCRIPTION");
            //SugarRecord.executeQuery("DROP TABLE ORDO_ANALYSE");
            //SugarRecord.executeQuery("DROP TABLE ORDO_EXAMEN");
            //SugarRecord.executeQuery("DROP TABLE RDV");
            //SugarRecord.executeQuery("DROP TABLE RDV_AUTRE");
            //SugarRecord.executeQuery("DROP TABLE RDV_OFFICIEL");
           // SugarRecord.executeQuery("DROP TABLE SPECIALITE");

           // SugarRecord.executeQuery("DELETE FROM ALARM");
            //SugarRecord.executeQuery("DROP TABLE ALARM");
            //SugarRecord.executeQuery("DELETE FROM ALARM_RDV");
            //SugarRecord.executeQuery("DELETE FROM ASSOCIATION_ALARM_RDV");
           // SugarRecord.executeQuery("DELETE FROM RDV_ANALYSE");
            //SugarRecord.executeQuery("DELETE FROM RDV_EXAMEN");
            //SugarRecord.executeQuery("DELETE FROM RDV_CONTACT");
            //SugarRecord.executeQuery("DELETE FROM PHOTO");
            SugarRecord.executeQuery("DELETE FROM ORDONNANCE");
            SugarRecord.executeQuery("DELETE FROM PRESCRIPTION");




            //
           // SugarRecord.executeQuery("DROP TABLE RDV_ACTE_MEDICAL");
            //RAZ TOTAL
            /*SugarContext.terminate();

            publishProgress(20);
            SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
            publishProgress(40);
            schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
            publishProgress(60);
            SugarContext.init(getApplicationContext());
            publishProgress(80);

            schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());*/


            publishProgress(100);

            finish();
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "RAZ fini", Toast.LENGTH_LONG).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    private class AsyncTaskRunnerBD extends AsyncTask<Void, Integer, String> {

        protected String doInBackground(Void...voids) {
                    //remplir BD avec valeur par defaut

            remplirFormePharmaceutiqueBD();

            publishProgress(10);
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
                  // pas de creation de user  remplirExempleBD();
            publishProgress(90);

                    //afficher le user actif
                    //Utilisateur utilisateur = (new Utilisateur()).findActifUser();
            List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
            Utilisateur utilisateur = null;
            if (listUserActif.size() !=0){
                utilisateur = listUserActif.get(0);
            }
                    String userName;
                    if (utilisateur.getId() == null) {
                        userName = "user inconnu";
                    } else {
                        userName = utilisateur.getName();
                    }
            publishProgress(100);
            return userName;
        }

        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
          //  textUser.setText(result);
            Toast.makeText(MainActivity.this, "Creation BD fini", Toast.LENGTH_LONG).show();

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    public void remplirDepartementBD() {
        //remplir Departement
        Long count = Departement.count(Departement.class);
        if (count ==0) {
            new Departement("01","Ain",Region.find(Region.class,"nom = ?","Auvergne-Rhône-Alpes").get(0)).save();
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
        //remplir Region
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
        //remplir SavoirFaire
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
        //remplir FormeProfession
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

        //remplir FormePharmaceutique
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

    private class AsyncTaskRunnerMedicament extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {

                InputStream is = null;
                BufferedReader reader = null;

                try {
                    is = getAssets().open("CIS_bdpm.txt");
                    reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                    String line = null;

                    List<Medicament> listMedicament = Medicament.listAll(Medicament.class);
                    Map<Long, Medicament> mapMedicamentOfficiel = new HashMap<>();
                    for (Medicament medicament : listMedicament) {
                        mapMedicamentOfficiel.put(medicament.getCodeCIS(), medicament);
                    }

                    List<FormePharmaceutique> listFormePharamaceutique = FormePharmaceutique.listAll(FormePharmaceutique.class);
                    Map<String, FormePharmaceutique> mapFormePharamaceutique = new HashMap<>();
                    for (FormePharmaceutique formePharmaceutique : listFormePharamaceutique) {
                        mapFormePharamaceutique.put(formePharmaceutique.getName(), formePharmaceutique);
                    }

                    int readerSize = 15090;
                    int readerCount = 0;
                    int compteur = 0;
                    publishProgress(compteur);
                    while ((line = reader.readLine()) != null) {
                        readerCount ++;
                        compteur = readerCount*100/readerSize;
                        publishProgress(compteur);
                        final String SEPARATEUR = "\t";
                        String lineSplitted[] = line.split(SEPARATEUR);

                        Medicament medicament = new Medicament();
                        //verif si commercialise
                        if (!lineSplitted[6].equals("Commercialisée") ) {
                            continue;
                        }

                        //verif si existant
                        Medicament verifMedicament = null;
                        verifMedicament = mapMedicamentOfficiel.get(Long.parseLong(lineSplitted[0]));
                        if (verifMedicament != null){
                            continue;
                        }

                        medicament.setCodeCIS(Long.parseLong(lineSplitted[0]));

                        int positionVirgule = lineSplitted[1].indexOf(",");
                        if (positionVirgule >=0){
                            medicament.setDenomination(lineSplitted[1].substring(0,positionVirgule));
                        } else {
                            medicament.setDenomination(lineSplitted[1]);
                        }
                        if (lineSplitted[2].substring(0,1).equals(" ")) {
                            lineSplitted[2] = lineSplitted[2].substring(1,lineSplitted[2].length());
                        }

                        medicament.setFormePharmaceutique(mapFormePharamaceutique.get(lineSplitted[2]));

                        int positionPointVirgule = lineSplitted[10].indexOf(";");
                        if (positionPointVirgule >=0){
                            medicament.setTitulaire(lineSplitted[10].substring(0,positionPointVirgule));
                        } else {
                            medicament.setTitulaire(lineSplitted[10]);
                        }
                        medicament.save();
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ignored) {
                        }
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ignored) {
                        }
                    }
                }



            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            Toast toast = Toast.makeText(MainActivity.this, "Import fini", Toast.LENGTH_LONG);
            toast.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    private class AsyncTaskRunnerContact extends AsyncTask<Void, Integer, Void> {

        private Context context;
        public AsyncTaskRunnerContact(Context context) {
            this.context=context;
        }

        protected Void doInBackground(Void...voids) {

            InputStream is = null;
            BufferedReader reader = null;

            List<Profession> listProfession = Profession.listAll(Profession.class);
            Map<String, Profession> mapProfession = new HashMap<>();
            for (Profession profession : listProfession) {
                mapProfession.put(profession.getName(), profession);
            }

            List<SavoirFaire> listSavoirFaire = SavoirFaire.listAll(SavoirFaire.class);
            Map<String, SavoirFaire> mapSavoirFaire = new HashMap<>();
            for (SavoirFaire savoirFaire : listSavoirFaire) {
                mapSavoirFaire.put(savoirFaire.getName(), savoirFaire);
            }

            List<ImportContact> listImportContact = ImportContact.find(ImportContact.class,"import_completed = ?","0");

            for (ImportContact current : listImportContact) {
                try {
                    is = getAssets().open(current.getPath());
                    reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    String line = null;


                    int readerSize = 5000;
                    int readerCount = 0;
                    int compteur = 0;
                    publishProgress(compteur);
                    while ((line = reader.readLine()) != null) {
                        readerCount ++;
                        compteur = readerCount*100/readerSize;
                        publishProgress(compteur);
                        final String SEPARATEUR = "\\|";
                        String lineSplitted[] = line.split(SEPARATEUR);

                        if (lineSplitted[1].equals("Identifiant PP")) {
                            continue;
                        }

                        Contact contact = new Contact();
                        contact.setIdPP(lineSplitted[2]);
                        contact.setCodeCivilite(lineSplitted[3]);
                        contact.setNom(lineSplitted[7].toUpperCase());
                        if (lineSplitted[8].length()>1) {
                            String prenom = lineSplitted[8].substring(0,1).toUpperCase()+lineSplitted[8].substring(1,lineSplitted[8].length()-1).toLowerCase();
                        }
                        contact.setPrenom(lineSplitted[8]);
                        contact.setProfession(mapProfession.get(lineSplitted[10]));

                        if (lineSplitted[16].equals("Qualifié en Médecine Générale") || lineSplitted[16].equals("Spécialiste en Médecine Générale")) {
                            contact.setSavoirFaire(mapSavoirFaire.get("Médecine Générale"));
                        } else {
                            contact.setSavoirFaire(mapSavoirFaire.get(lineSplitted[16]));
                        }
                        if (lineSplitted.length>24) {
                            contact.setRaisonSocial(lineSplitted[24]);
                        }
                        if (lineSplitted.length>26) {
                            contact.setComplement(lineSplitted[26]);
                            if (contact.getComplement().equalsIgnoreCase(contact.getRaisonSocial())) {
                                contact.setComplement(null);
                            }
                        }
                        String adresse = "";
                        if ((lineSplitted.length>28) && (!lineSplitted[28].isEmpty())){
                            adresse = lineSplitted[28]+" ";
                        }

                        if ((lineSplitted.length>31) && (!lineSplitted[31].isEmpty())){
                            adresse += lineSplitted[31]+" ";
                        }
                        if ((lineSplitted.length>32) && (!lineSplitted[32].isEmpty())){
                            adresse += lineSplitted[32];
                        }
                        contact.setAdresse(adresse.toUpperCase());

                        if (lineSplitted.length>34 && !lineSplitted[34].isEmpty())  {

                            contact.setCp(lineSplitted[34].substring(0,5));
                            contact.setVille(lineSplitted[34].substring(6));

                        } else {
                            contact.setCp("");
                        }
                        if (lineSplitted.length>40 && !lineSplitted[40].isEmpty())  {
                            lineSplitted[40] = lineSplitted[40].replace(" ", "");
                            lineSplitted[40] = lineSplitted[40].replace(".", "");
                            if (lineSplitted[40].length() == 9) {
                                contact.setTelephone("0" + lineSplitted[40]);
                            } else if (lineSplitted[40].length() == 10) {
                                contact.setTelephone(lineSplitted[40]);
                            }
                        }
                        if (lineSplitted.length>42 && !lineSplitted[42].isEmpty())  {
                            lineSplitted[42] = lineSplitted[42].replace(" ", "");
                            lineSplitted[42] = lineSplitted[42].replace(".", "");
                            if (lineSplitted[42].length() == 9) {
                                contact.setFax("0" + lineSplitted[42]);
                            } else if (lineSplitted[42].length() == 10) {
                                contact.setFax(lineSplitted[42]);
                            }
                        }
                        if (lineSplitted.length>43 && !lineSplitted[43].isEmpty())  {
                            contact.setEmail(lineSplitted[43]);
                        }

                        List<ContactLight> listContactLight = ContactLight.find(ContactLight.class, "id_pp = ?",lineSplitted[2]);
                        if (listContactLight.size()>0) {

                            boolean bool = false;
                            for (ContactLight contactLight : listContactLight){
                                if (comparer(contactLight, contact)){
                                    Log.i("existant","medecin deja cree: "+lineSplitted[2]);
                                    bool = true;
                                    continue;
                                }
                            }
                            if (bool) {
                                continue;
                            }
                        }
                        if (contact.getAdresse() != null && contact.getCp() != null && contact.getVille() != null) {
                            contact.enregisterCoordonnees(context);
                        }
                        Log.i("enregistre","medecin new: "+lineSplitted[2]);
                        contact.save();
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ignored) {
                        }
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ignored) {
                        }
                    }
                }
                current.setImportCompleted(true);
                current.save();
                publishProgress(100);
                //a voir si ça passe
                //Toast.makeText(MainActivity.this, "Import de " + current.getPath() + " fini", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "IMPORT TOTAL FINI", Toast.LENGTH_LONG).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }


    public void remplirDefaultBD() {


      /*  Long count = Duree.count(Duree.class);
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

    public void remplirExempleBD() {

        Long count = Utilisateur.count(Utilisateur.class);
        if (count ==0) {
            Utilisateur user = new Utilisateur("Bob",new Date(),"desc Bob");
            List<Departement> listDep = Departement.findWithQuery(Departement.class, "Select * from Departement where numero like '06'");
            Departement dep = listDep.get(0);

            user.setDepartement(dep);
            user.save();
            user = new Utilisateur("John",new Date(),"desc John");
            user.setDepartement(dep);
           // user.setActif(true);
            user.save();
            user = new Utilisateur("Bill",new Date(),"desc Bill");
            user.setDepartement(dep);
            user.save();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            CharSequence name = "channelName";
            //String description = getString(R.string.channel_description);
            String description = "channelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifTest", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean comparer(ContactLight medecinLight, Contact medecin){
        boolean bool = true;
        if (medecinLight.getIdPP() != null && medecin.getIdPP() != null) {
            bool = bool && medecinLight.getIdPP().equalsIgnoreCase(medecin.getIdPP());
        }
        if (medecinLight.getAdresse() != null && medecin.getAdresse() != null) {
            bool = bool && medecinLight.getAdresse().equalsIgnoreCase(medecin.getAdresse());
        }
        if (medecinLight.getCp() != null && medecin.getCp() != null) {
            bool = bool && medecinLight.getCp().equalsIgnoreCase(medecin.getCp());
        }
        if (medecinLight.getVille() != null && medecin.getVille() != null) {
            bool = bool && medecinLight.getVille().equalsIgnoreCase(medecin.getVille());
        }
        return bool;
    }
}
