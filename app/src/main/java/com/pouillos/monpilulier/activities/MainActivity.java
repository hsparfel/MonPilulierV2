package com.pouillos.monpilulier.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.facebook.stetho.Stetho;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.listallx.ListAllProfilActivity;
import com.pouillos.monpilulier.activities.listallx.ListAllUserActivity;
import com.pouillos.monpilulier.activities.listmyx.ListMyProfilActivity;
import com.pouillos.monpilulier.activities.newx.NewUserActivity;
import com.pouillos.monpilulier.activities.recherche.ChercherMedecinOfficielActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Analyse;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Duree;
import com.pouillos.monpilulier.entities.Examen;
import com.pouillos.monpilulier.entities.FormePharmaceutique;
import com.pouillos.monpilulier.entities.MedecinOfficiel;
import com.pouillos.monpilulier.entities.MedicamentOfficiel;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.Region;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.parser.ParseListMedecinOfficiel;
import com.pouillos.monpilulier.parser.ParseListMedicamentOfficiel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

    private NotificationCompat.Builder notBuilder;
    private static final int MY_NOTIFICATION_ID = 12345;
    private static final int MY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        TextView textUser = (TextView) findViewById(R.id.textUser);
        Button buttonListAllUser = (Button) findViewById(R.id.buttonListAllUser);
        Button buttonNewUser = (Button) findViewById(R.id.buttonNewUser);

        Button buttonNewOrdonnance = (Button) findViewById(R.id.buttonNewOrdonnance);
        Button buttonListAllOrdonnance = (Button) findViewById(R.id.buttonListAllOrdonnance);
        Button buttonListAllOrdoAnalyse = (Button) findViewById(R.id.buttonListAllOrdoAnalyse);
        Button buttonListAllOrdoExamen = (Button) findViewById(R.id.buttonListAllOrdoExamen);

        Button buttonRAZ = (Button) findViewById(R.id.buttonRAZ);
        Button buttonNewRdv = (Button) findViewById(R.id.buttonNewRdv);
        Button buttonListAllRdv = (Button) findViewById(R.id.buttonListAllRdv);
        Button buttonListMyRdv = (Button) findViewById(R.id.buttonListMyRdv);
        Button buttonProfil = (Button) findViewById(R.id.buttonProfil);
        Button buttonListAllProfil = (Button) findViewById(R.id.buttonListAllProfil);
        Button buttonListMyProfil = (Button) findViewById(R.id.buttonListMyProfil);
        Button buttonListMyPrise = (Button) findViewById(R.id.buttonListMyPrise);
        Button buttonCreerNotification = (Button) findViewById(R.id.buttonCreerNotification);
        Button buttonNewMedicamentOfficiel = (Button) findViewById(R.id.buttonMajMedicamentOfficiel);
        Button buttonNewMedecinOfficiel = (Button) findViewById(R.id.buttonMajMedecinOfficiel);
        Button buttonInfoDb = (Button) findViewById(R.id.buttonInfoDb);
        Button buttonChercherMedecinOfficiel = (Button) findViewById(R.id.buttonChercherMedecinOfficiel);

        //remplir BD avec valeur par defaut
        remplirDefaultBD();

        remplirFormePharmaceutiqueBD();
        remplirProfessionBD();
        remplirSavoirFaireBD();
        remplirRegionBD();
        remplirDepartementBD();
        remplirExempleBD();

        //afficher le user actif
        Utilisateur utilisateur = (new Utilisateur()).findActifUser();
        if (utilisateur.getId() == null) {
            textUser.setText("user inconnu");
        } else {
            textUser.setText(utilisateur.getName());
        }

        //notification
        //creation du channel
        createNotificationChannel();


        this.notBuilder = new NotificationCompat.Builder(this, "notifTest");
        // The message will automatically be canceled when the user clicks on Panel
        this.notBuilder.setAutoCancel(true);

        buttonCreerNotification.setOnClickListener(v -> {

            // --------------------------
            // Prepare a notification
            // --------------------------

            this.notBuilder.setSmallIcon(R.mipmap.ic_launcher);
            this.notBuilder.setTicker("This is a ticker");
            this.notBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Set the time that the event occurred.
            // Notifications in the panel are sorted by this time.

            Date dateJour = new Date();

            this.notBuilder.setWhen(new DateUtils().ajouterHeure(dateJour, 1).getTime() + 10*1000);

            this.notBuilder.setWhen(System.currentTimeMillis()+ 300* 1000);
            this.notBuilder.setShowWhen(false);
            this.notBuilder.setContentTitle("This is title");
            this.notBuilder.setContentText("This is content text ....");

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
        });

        buttonChercherMedecinOfficiel.setOnClickListener(v -> {
            Intent myProfilActivity = new Intent(MainActivity.this, ChercherMedecinOfficielActivity.class);
            //myProfilActivity.putExtra("activitySource", MainActivity.class);
            startActivity(myProfilActivity);
        });

        buttonNewMedicamentOfficiel.setOnClickListener(v -> {
            remplirMedicamentOfficielBD();
        });

        buttonNewMedecinOfficiel.setOnClickListener(v -> {
            remplirMedecinOfficielBD();
        });

        buttonInfoDb.setOnClickListener(v -> {
           // List<MedicamentOfficiel> listMedicamentOfficiel = MedicamentOfficiel.listAll(MedicamentOfficiel.class);
            //List<MedecinOfficiel> listMedecinOfficiel = MedecinOfficiel.listAll(MedecinOfficiel.class);
            long size1 = MedicamentOfficiel.count(MedicamentOfficiel.class);
            long size2 = MedecinOfficiel.count(MedecinOfficiel.class);
            //ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "","nb medicament: "+size1+" - nb medecin: "+size2, false);
            Toast toast = Toast.makeText(MainActivity.this, "nb medicament: "+size1+" - nb medecin: "+size2, Toast.LENGTH_LONG);
            toast.show();
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















    }

    public void remplirDepartementBD() {
        //remplir Departement
        List<Departement> listDepartement = Departement.listAll(Departement.class);
        if (listDepartement.size()==0) {
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
        }
        //verif a suppr
        listDepartement = Departement.listAll(Departement.class);
        int listSize = listDepartement.size();
    }

    public void remplirRegionBD() {
        //remplir Region
        List<Region> listRegion = Region.listAll(Region.class);
        if (listRegion.size()==0) {
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
        }
        //verif a suppr
        listRegion = Region.listAll(Region.class);
        int listSize = listRegion.size();
    }

    public void remplirSavoirFaireBD() {
        //remplir SavoirFaire
        List<SavoirFaire> listSavoirFaire = SavoirFaire.listAll(SavoirFaire.class);
        if (listSavoirFaire.size()==0) {
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
        //verif a suppr
        listSavoirFaire = SavoirFaire.listAll(SavoirFaire.class);
        int listSize = listSavoirFaire.size();
    }
    public void remplirProfessionBD() {
        //remplir FormeProfession
        List<Profession> listProfession = Profession.listAll(Profession.class);
        if (listProfession.size()==0) {
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
        //verif a suppr
        listProfession = Profession.listAll(Profession.class);
        int listSize = listProfession.size();
    }

    public void remplirFormePharmaceutiqueBD() {

        //remplir FormePharmaceutique
        List<FormePharmaceutique> listFormePharmaceutique = FormePharmaceutique.listAll(FormePharmaceutique.class);
        if (listFormePharmaceutique.size()==0) {
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
        //verif a suppr
        listFormePharmaceutique = FormePharmaceutique.listAll(FormePharmaceutique.class);
        int listSize = listFormePharmaceutique.size();
    }

    public void remplirMedicamentOfficielBD() {
        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", true);
        //MedicamentOfficiel.deleteAll(MedicamentOfficiel.class);
        ParseListMedicamentOfficiel parser = new ParseListMedicamentOfficiel();
        parser.readFile(getAssets(),"CIS_bdpm.txt");

        Toast toast = Toast.makeText(MainActivity.this, "Import fini", Toast.LENGTH_LONG);
        toast.show();
    }

    public void remplirMedecinOfficielBD() {
        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", true);
        //MedecinOfficiel.deleteAll(MedecinOfficiel.class);
        ParseListMedecinOfficiel parser = new ParseListMedecinOfficiel();
        parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_test.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_0.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_1.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_2.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_3.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_4.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_5.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_6.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_7.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_8.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_9.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_10.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_11.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_12.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_13.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_14.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_15.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_16.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_17.txt");
        //parser.readFile(getAssets(),"PS_LibreAcces_Personne_activite_18.txt");


        Toast toast = Toast.makeText(MainActivity.this, "Import fini", Toast.LENGTH_LONG);
        toast.show();
    }

    public void remplirDefaultBD() {

        List<Duree> listDuree = Duree.listAll(Duree.class);
        if (listDuree.size()==0) {
            new Duree("jour").save();
            new Duree("semaine").save();
            new Duree("mois").save();
            new Duree("an").save();
        }

        List<Analyse> listAnalyse = Analyse.listAll(Analyse.class);
        if (listAnalyse.size()==0) {
            new Analyse("sang").save();
            new Analyse("urine").save();
            new Analyse("selle").save();
            new Analyse("adn").save();
            new Analyse("autre").save();
        }
    }

    public void remplirExempleBD() {

        List<Utilisateur> listUtilisateur = Utilisateur.listAll(Utilisateur.class);
        if (listUtilisateur.size()==0) {
            Utilisateur user = new Utilisateur("Bob",new Date(),"desc Bob");
            List<Departement> listDep = Departement.findWithQuery(Departement.class, "Select * from Departement where numero like '06'");
            Departement dep = listDep.get(0);

            user.setDepartement(dep);
            user.save();
            user = new Utilisateur("John",new Date(),"desc John");
            user.setDepartement(dep);
            user.save();
            user = new Utilisateur("Bill",new Date(),"desc Bill");
            user.setDepartement(dep);
            user.save();
        }

        List<Examen> listExamen = Examen.listAll(Examen.class);
        if (listExamen.size()==0) {
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
}
