package com.pouillos.monpilulier.activities;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.orm.SugarRecord;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.add.AddAnalyseActivity;
import com.pouillos.monpilulier.activities.add.AddExamenActivity;
import com.pouillos.monpilulier.activities.add.AddOrdonnanceActivity;
import com.pouillos.monpilulier.activities.add.AddProfilActivity;
import com.pouillos.monpilulier.activities.add.AddUserActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherAnalyseActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherContactActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherEtablissementActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherExamenActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherGraphiqueActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherPhotoActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherProfilActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvAnalyseActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvContactActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherRdvExamenActivity;
import com.pouillos.monpilulier.activities.photo.MakePhotoActivity;
import com.pouillos.monpilulier.activities.recherche.ChercherContactActivity;
import com.pouillos.monpilulier.activities.recherche.ChercherEtablissementActivity;
import com.pouillos.monpilulier.activities.tools.ImportContactActivity;
import com.pouillos.monpilulier.activities.tools.ImportEtablissementActivity;
import com.pouillos.monpilulier.activities.tools.ImportMedicamentActivity;
import com.pouillos.monpilulier.activities.tools.PriseNotificationBroadcastReceiver;
import com.pouillos.monpilulier.activities.tools.RdvAnalyseNotificationBroadcastReceiver;
import com.pouillos.monpilulier.activities.tools.RdvContactNotificationBroadcastReceiver;
import com.pouillos.monpilulier.activities.tools.RdvExamenNotificationBroadcastReceiver;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.AlarmRdv;
import com.pouillos.monpilulier.entities.AssociationAlarmRdv;
import com.pouillos.monpilulier.entities.Prise;
import com.pouillos.monpilulier.entities.Rdv;
import com.pouillos.monpilulier.entities.RdvAnalyse;
import com.pouillos.monpilulier.entities.RdvContact;
import com.pouillos.monpilulier.entities.RdvExamen;
import com.pouillos.monpilulier.entities.Utilisateur;
import com.pouillos.monpilulier.enumeration.Echeance;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icepick.Icepick;
import icepick.State;

public class NavDrawerActivity extends AppCompatActivity implements BasicUtils, NavigationView.OnNavigationItemSelectedListener {
    //FOR DESIGN

    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @State
    protected Utilisateur activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //a redefinit à chq fois
        super.onCreate(savedInstanceState);

        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() != 0) {
            activeUser = listUserActif.get(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        //getMenuInflater().inflate(R.menu.menu_add_item_to_db, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_home:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AccueilActivity.class,true);
                break;
            case R.id.activity_main_drawer_profile:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AddProfilActivity.class,true);
                break;
            case R.id.activity_main_drawer_evolution:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherGraphiqueActivity.class,true);
                //ouvrirActiviteSuivante(NavDrawerActivity.this, BarToLineChartActivity.class,true);
                break;
            case R.id.activity_main_drawer_account:
               // ouvrirActiviteSuivante(NavDrawerActivity.this, AddUserActivity.class, getResources().getString(R.string.id_user), activeUser.getId());
                ouvrirActiviteSuivante(NavDrawerActivity.this, AddUserActivity.class,"userId",activeUser.getId(),true);

                break;
            case R.id.activity_main_drawer_change_account:
               // ouvrirActiviteSuivante(NavDrawerActivity.this, AuthentificationActivity.class, getResources().getString(R.string.id_user), activeUser.getId());
                ouvrirActiviteSuivante(NavDrawerActivity.this, AuthentificationActivity.class,true);
                break;
            case R.id.activity_main_drawer_ordonnances:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AddOrdonnanceActivity.class,true);

                break;
            /*case R.id.activity_main_drawer_treatments:
                Toast.makeText(this, "à implementer 2", Toast.LENGTH_LONG).show();
                break;*/

            case R.id.activity_main_drawer_contact_appointments:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherRdvContactActivity.class,true);
                break;
            case R.id.activity_main_drawer_analysis_appointments:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherRdvAnalyseActivity.class,true);
                break;
            case R.id.activity_main_drawer_exam_appointments:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherRdvExamenActivity.class,true);
                break;
            case R.id.activity_main_drawer_contacts:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherContactActivity.class,true);
                break;
            case R.id.activity_main_drawer_etablissement:
                ouvrirActiviteSuivante(NavDrawerActivity.this, AfficherEtablissementActivity.class,true);
                break;
            case R.id.activity_main_drawer_oldAccueil:
                ouvrirActiviteSuivante(NavDrawerActivity.this, MainActivity.class,true);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myProfilActivity = new Intent(NavDrawerActivity.this, ChercherContactActivity.class);
        startActivity(myProfilActivity);
        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            /*case R.id.menu_activity_main_params:
                Toast.makeText(this, "Il n'y a rien à paramétrer ici, passez votre chemin...", Toast.LENGTH_LONG).show();
                return true;*/
            case R.id.menu_activity_main_search:
                //Toast.makeText(this, "Recherche indisponible, demandez plutôt l'avis de Google, c'est mieux et plus rapide.", Toast.LENGTH_LONG).show();
                myProfilActivity = new Intent(NavDrawerActivity.this, ChercherContactActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.addAnalyse:
                myProfilActivity = new Intent(NavDrawerActivity.this, AddAnalyseActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.addExamen:
                myProfilActivity = new Intent(NavDrawerActivity.this, AddExamenActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.listAllAnalyse:
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherAnalyseActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.listAllExamen:
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherExamenActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.listMyProfil:
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherProfilActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.takePicture:
                myProfilActivity = new Intent(NavDrawerActivity.this, MakePhotoActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.pickPicture:
                myProfilActivity = new Intent(NavDrawerActivity.this, AfficherPhotoActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.importContact:
                myProfilActivity = new Intent(NavDrawerActivity.this, ImportContactActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.importEtablissement:
                myProfilActivity = new Intent(NavDrawerActivity.this, ImportEtablissementActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.importMedicament:
                myProfilActivity = new Intent(NavDrawerActivity.this, ImportMedicamentActivity.class);
                startActivity(myProfilActivity);
                return true;
            case R.id.rchEtablissement:
                myProfilActivity = new Intent(NavDrawerActivity.this, ChercherEtablissementActivity.class);
                startActivity(myProfilActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    public void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

    }

    // 2 - Configure Drawer Layout
    public void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    public void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void ouvrirActiviteSuivante(Context context, Class classe, boolean bool) {
        Intent intent = new Intent(context, classe);
        startActivity(intent);
        if (bool) {
            finish();
        }
    }
    public void revenirActivitePrecedente(String sharedName, String dataName, Long itemId) {
        SharedPreferences preferences=getSharedPreferences(sharedName,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putLong(dataName,itemId);
        editor.commit();
        finish();
    }


    public void ouvrirActiviteSuivante(Context context, Class classe, String nomExtra, Long objetIdExtra, boolean bool) {
        Intent intent = new Intent(context, classe);
        intent.putExtra(nomExtra, objetIdExtra);
        startActivity(intent);
        if (bool) {
            finish();
        }
    }

    public void ouvrirActiviteSuivante(Context context, Class classe, String nomExtra, String objetExtra, String nomExtra2, Long objetIdExtra2, boolean bool) {
        Intent intent = new Intent(context, classe);
        intent.putExtra(nomExtra, objetExtra);
        intent.putExtra(nomExtra2, objetIdExtra2);
        startActivity(intent);
        if (bool) {
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected Date ActualiserDate(Date date, String time){
        Date dateActualisee = date;
        int nbHour = Integer.parseInt(time.substring(0,2));
        int nbMinute = Integer.parseInt(time.substring(3));
        dateActualisee = DateUtils.ajouterHeure(dateActualisee,nbHour);
        dateActualisee = DateUtils.ajouterMinute(dateActualisee,nbMinute);
        return dateActualisee;
    }

    protected Utilisateur findActiveUser() {
        Utilisateur user = null;
        List<Utilisateur> listUserActif = Utilisateur.find(Utilisateur.class, "actif = ?", "1");
        if (listUserActif.size() !=0){
            user = listUserActif.get(0);
        }
       return user;
    }

    protected <T> void buildDropdownMenu(List<T> listObj, Context context, AutoCompleteTextView textView) {
        List<String> listString = new ArrayList<>();
        String[] listDeroulante;
        listDeroulante = new String[listObj.size()];
        for (T object : listObj) {
            listString.add(object.toString());
        }
        listString.toArray(listDeroulante);
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.list_item, listDeroulante);
        textView.setAdapter(adapter);
    }

    @Override
    public Executor getMainExecutor() {
        return super.getMainExecutor();
    }



    protected <T extends SugarRecord> void deleteItem(Context context, T item, Class classe, boolean toConfirm) {
        if (toConfirm) {
            new MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.dialog_delete_title)
                    .setMessage(R.string.dialog_delete_message)
                    .setNegativeButton(R.string.dialog_delete_negative, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, R.string.dialog_delete_negative_toast, Toast.LENGTH_LONG).show();

                        }
                    })
                    .setPositiveButton(R.string.dialog_delete_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(context, R.string.dialog_delete_positive_toast, Toast.LENGTH_LONG).show();
                            item.delete();
                            supprimerNotification(item, context);

                            ouvrirActiviteSuivante(context, classe,true);

                        }
                    })
                    .show();
        } else {
            item.delete();
            supprimerNotification(item, context);
            ouvrirActiviteSuivante(context, classe,true);
        }



    }

    protected boolean isChecked(ChipGroup chipGroup) {
        boolean bool;
        if (chipGroup.getCheckedChipId() != -1) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }


    protected boolean isFilled(TextInputEditText textInputEditText){
        boolean bool;
        if (textInputEditText.length()>0) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    protected boolean isFilled(Object object){
        boolean bool;
        if (object!=null) {
            bool = true;
        } else {
            bool = false;
        }
        return bool;
    }

    protected boolean isValidTel(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && textView.getText().length() <10) {
            //textView.requestFocus();
            //textView.setError("Saisie Non Valide  (10 chiffres)");
            return false;
        } else {
            return true;
        }
    }

    protected boolean isValidZip(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && textView.getText().length() <5) {
            //textView.requestFocus();
            //textView.setError("Saisie Non Valide  (5 chiffres)");
            return false;
        } else {
            return true;
        }
    }

    protected boolean isEmailAdress(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();
    }
    protected boolean isValidEmail(TextView textView) {
        if (!TextUtils.isEmpty(textView.getText()) && !isEmailAdress(textView.getText().toString())) {
            //textView.requestFocus();
            //textView.setError("Saisie Non Valide (email)");
            return false;
        } else {
            return true;
        }
    }

    protected static float floatArrondi(float number, int decimalPlace) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


    protected<T> void activerNotification(T object, Context context) {
        if (object instanceof RdvContact) {
           startAlert((RdvContact) object, Echeance.OneHourAfter.toString(), context);
           startAlert((RdvContact) object, Echeance.OneDayAfter.toString(), context);
        } else if (object instanceof RdvAnalyse) {
            startAlert((RdvAnalyse) object, Echeance.OneHourAfter.toString(), context);
            startAlert((RdvAnalyse) object, Echeance.OneDayAfter.toString(), context);
        } else if (object instanceof RdvExamen) {
            startAlert((RdvExamen) object, Echeance.OneHourAfter.toString(), context);
            startAlert((RdvExamen) object, Echeance.OneDayAfter.toString(), context);
        } else if (object instanceof Prise) {
            startAlert((Prise) object, context);
        }

    }
    /*protected<T> void activerNotification(Class classe, Date dateRdv, T object, Context context) {
        if (classe == RdvContactNotificationBroadcastReceiver.class) {
            startAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Contact) object).toStringShort(), Echeance.OneHourAfter.toString(),context);
            startAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Contact) object).toStringShort(), Echeance.OneDayAfter.toString(),context);
        } else if (classe == RdvAnalyseNotificationBroadcastReceiver.class) {
            startAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Analyse) object).toString(), Echeance.OneHourAfter.toString(),context);
            startAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Analyse) object).toString(), Echeance.OneDayAfter.toString(),context);
        } else if (classe == RdvExamenNotificationBroadcastReceiver.class) {
            startAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Examen) object).toString(), Echeance.OneHourAfter.toString(),context);
            startAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Examen) object).toString(), Echeance.OneDayAfter.toString(),context);
        }

    }*/

    protected<T> void supprimerNotification(T object, Context context) {
        if (object instanceof RdvContact) {
            List<AssociationAlarmRdv> listAssociationAlarmRdv = AssociationAlarmRdv.find(AssociationAlarmRdv.class,"rdv_contact = ?", ((RdvContact) object).getId().toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            for (AssociationAlarmRdv current : listAssociationAlarmRdv) {
                alarmRdv = AlarmRdv.findById(AlarmRdv.class,current.getAlarmRdv().getId());
                if (alarmRdv.getEcheance().equalsIgnoreCase(Echeance.OneHourAfter.toString())) {
                    cancelAlert((RdvContact) object, Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());
                } else if (alarmRdv.getEcheance().equalsIgnoreCase(Echeance.OneDayAfter.toString())) {
                    cancelAlert((RdvContact) object, Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
                }
            }
        } else if (object instanceof RdvAnalyse) {
            List<AssociationAlarmRdv> listAssociationAlarmRdv = AssociationAlarmRdv.find(AssociationAlarmRdv.class,"rdv_analyse = ?", ((RdvAnalyse) object).getId().toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            for (AssociationAlarmRdv current : listAssociationAlarmRdv) {
                alarmRdv = AlarmRdv.findById(AlarmRdv.class,current.getAlarmRdv().getId());
                if (alarmRdv.getEcheance().equalsIgnoreCase(Echeance.OneHourAfter.toString())) {
                    cancelAlert((RdvAnalyse) object, Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());
                } else if (alarmRdv.getEcheance().equalsIgnoreCase(Echeance.OneDayAfter.toString())) {
                    cancelAlert((RdvAnalyse) object, Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
                }
            }
        } else if (object instanceof RdvExamen) {
            List<AssociationAlarmRdv> listAssociationAlarmRdv = AssociationAlarmRdv.find(AssociationAlarmRdv.class,"rdv_examen = ?", ((RdvExamen) object).getId().toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            for (AssociationAlarmRdv current : listAssociationAlarmRdv) {
                alarmRdv = AlarmRdv.findById(AlarmRdv.class,current.getAlarmRdv().getId());
                if (alarmRdv.getEcheance().equalsIgnoreCase(Echeance.OneHourAfter.toString())) {
                    cancelAlert((RdvExamen) object, Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());
                } else if (alarmRdv.getEcheance().equalsIgnoreCase(Echeance.OneDayAfter.toString())) {
                    cancelAlert((RdvExamen) object, Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
                }
            }
        }
    }

    /*protected<T> void supprimerNotification(Class classe, Date dateRdv, T object, Context context) {
        if (classe == RdvContactNotificationBroadcastReceiver.class) {
            List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterHeure(dateRdv,-1).toString(),((Contact) object).toStringShort(),Echeance.OneHourAfter.toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Contact) object).toStringShort(), Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());

            listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterJourArrondi(dateRdv,-1,7).toString(),((Contact) object).toStringShort(),Echeance.OneDayAfter.toString());
            alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Contact) object).toStringShort(), Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
        } else if (classe == RdvAnalyseNotificationBroadcastReceiver.class) {
            List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterHeure(dateRdv,-1).toString(),((Analyse) object).toString(),Echeance.OneHourAfter.toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Analyse) object).toString(), Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());

            listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterJourArrondi(dateRdv,-1,7).toString(),((Analyse) object).toString(),Echeance.OneDayAfter.toString());
            alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Analyse) object).toString(), Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
        } else if (classe == RdvExamenNotificationBroadcastReceiver.class) {
            List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterHeure(dateRdv,-1).toString(),((Examen) object).toString(),Echeance.OneHourAfter.toString());
            AlarmRdv alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterHeure(dateRdv,-1), ((Examen) object).toString(), Echeance.OneHourAfter.toString(),context, alarmRdv.getRequestCode());

            listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"date_string = ? and detail = ? and echeance = ?",DateUtils.ajouterJourArrondi(dateRdv,-1,7).toString(),((Examen) object).toString(),Echeance.OneDayAfter.toString());
            alarmRdv = new AlarmRdv();
            if (listAlarmRdv.size()>0) {
                alarmRdv = listAlarmRdv.get(0);
            }
            cancelAlert(classe, DateUtils.ajouterJourArrondi(dateRdv,-1,7), ((Examen) object).toString(), Echeance.OneDayAfter.toString(),context, alarmRdv.getRequestCode());
        }
    }*/

    protected<T> void startAlert(T object, Context context) {
        //Class classe = object.getClass();
        Intent intent = null;

        Prise prise = (Prise) object;

            intent = new Intent(this, PriseNotificationBroadcastReceiver.class);
            intent.putExtra("detail",prise.getMedicament().getDenominationShort()+ " - "+prise.getQteDose()+ " "+prise.getDose().getName());

        //intent.putExtra("echeance",echeance);
        Date dateJour = new Date();
        Long dateJourLong = dateJour.getTime();
        int requestCode =dateJourLong.intValue();
        Log.i("requestCode",""+requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Date dateAlerte = prise.getDate();



        alarmManager.set(AlarmManager.RTC_WAKEUP, dateAlerte.getTime(), pendingIntent);
        AlarmRdv alarmRdv = new AlarmRdv();
        alarmRdv.setClasse(object.getClass().getName());
        alarmRdv.setDate(dateAlerte);
        alarmRdv.setDateString(dateAlerte.toString());
        alarmRdv.setDetail(intent.getStringExtra("detail"));
        //alarmRdv.setEcheance(echeance);
        alarmRdv.setRequestCode(requestCode);
        alarmRdv.setId(alarmRdv.save());


        Toast.makeText(this, "Alarm set : " + prise.getDate().toString(), Toast.LENGTH_LONG).show();
    }

    protected<T> void startAlert(T object, String echeance, Context context) {
        //Class classe = object.getClass();
        Intent intent = null;

            Rdv rdv = (Rdv) object;
            if (object instanceof RdvContact) {
                intent = new Intent(this, RdvContactNotificationBroadcastReceiver.class);
                intent.putExtra("detail",((RdvContact) object).getContact().toStringShort());
            } else if (object instanceof RdvAnalyse) {
                intent = new Intent(this, RdvAnalyseNotificationBroadcastReceiver.class);
                intent.putExtra("detail",((RdvAnalyse) object).getAnalyse().getName());
            } else if (object instanceof RdvExamen) {
                intent = new Intent(this, RdvExamenNotificationBroadcastReceiver.class);
                intent.putExtra("detail",((RdvExamen) object).getExamen().getName());
            }
            intent.putExtra("echeance",echeance);
            Date dateJour = new Date();
            Long dateJourLong = dateJour.getTime();
            int requestCode =dateJourLong.intValue();
            Log.i("requestCode",""+requestCode);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Date dateAlerte = new Date();
            if (echeance.equalsIgnoreCase(Echeance.OneHourAfter.toString())){
                dateAlerte = DateUtils.ajouterHeure(rdv.getDate(),-1);
            } else if (echeance.equalsIgnoreCase(Echeance.OneDayAfter.toString())){
                dateAlerte = DateUtils.ajouterJourArrondi(rdv.getDate(),-1,7);
            }


            alarmManager.set(AlarmManager.RTC_WAKEUP, dateAlerte.getTime(), pendingIntent);
            AlarmRdv alarmRdv = new AlarmRdv();
            alarmRdv.setClasse(object.getClass().getName());
            alarmRdv.setDate(dateAlerte);
            alarmRdv.setDateString(dateAlerte.toString());
            alarmRdv.setDetail(intent.getStringExtra("detail"));
            alarmRdv.setEcheance(echeance);
            alarmRdv.setRequestCode(requestCode);
            alarmRdv.setId(alarmRdv.save());

            AssociationAlarmRdv associationAlarmRdv = new AssociationAlarmRdv();
            associationAlarmRdv.setAlarmRdv(alarmRdv);

            if (object instanceof RdvContact) {
                associationAlarmRdv.setRdvContact((RdvContact) object);
            } else if (object instanceof RdvAnalyse) {
                associationAlarmRdv.setRdvAnalyse((RdvAnalyse) object);
            } else if (object instanceof RdvExamen) {
                associationAlarmRdv.setRdvExamen((RdvExamen) object);
            }
            associationAlarmRdv.setId(associationAlarmRdv.save());
            Toast.makeText(this, "Alarm set : " + rdv.getDate().toString(), Toast.LENGTH_LONG).show();


    }

    /*protected void startAlert(Class classe, Date dateRdv, String detail, String echeance, Context context) {
        Intent intent = new Intent(this, classe);
        intent.putExtra("detail",detail);
        intent.putExtra("echeance",echeance);
        Date dateJour = new Date();
        Long dateJourLong = dateJour.getTime();
        int requestCode =dateJourLong.intValue();
        Log.i("requestCode",""+requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, dateRdv.getTime(), pendingIntent);
        AlarmRdv alarmRdv = new AlarmRdv();
        alarmRdv.setClasse(classe.getName());
        alarmRdv.setDate(dateRdv);
        alarmRdv.setDateString(dateRdv.toString());
        alarmRdv.setDetail(detail);
        alarmRdv.setEcheance(echeance);
        alarmRdv.setRequestCode(requestCode);
        alarmRdv.setId(alarmRdv.save());

        AssociationAlarmRdv associationAlarmRdv = new AssociationAlarmRdv();
        associationAlarmRdv.setAlarmRdv(alarmRdv);

        if (classe == RdvContactNotificationBroadcastReceiver.class) {
            // associationAlarmRdv.setRdvContact();
        } else if (classe == RdvAnalyseNotificationBroadcastReceiver.class) {

        } else if (classe == RdvExamenNotificationBroadcastReceiver.class) {

        }



        Toast.makeText(this, "Alarm set : " + dateRdv.toString(), Toast.LENGTH_LONG).show();
    }*/

    protected<T> void cancelAlert(T object, String echeance, Context context,int requestCode) {
        Intent intent = null;
        Rdv rdv = (Rdv) object;
        if (object instanceof RdvContact) {
            intent = new Intent(this, RdvContactNotificationBroadcastReceiver.class);
            intent.putExtra("detail",((RdvContact) object).getContact().toStringShort());
        } else if (object instanceof RdvAnalyse) {
            intent = new Intent(this, RdvAnalyseNotificationBroadcastReceiver.class);
            intent.putExtra("detail",((RdvAnalyse) object).getAnalyse().getName());
        } else if (object instanceof RdvExamen) {
            intent = new Intent(this, RdvExamenNotificationBroadcastReceiver.class);
            intent.putExtra("detail",((RdvExamen) object).getExamen().getName());
        }
        intent.putExtra("echeance",echeance);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"request_code = ?", ""+requestCode);
        AlarmRdv alarmRdv = new AlarmRdv();
        if (listAlarmRdv.size()>0) {
            alarmRdv = listAlarmRdv.get(0);
            alarmRdv.delete();
        }

        List<AssociationAlarmRdv> listAssociationAlarmRdv = AssociationAlarmRdv.find(AssociationAlarmRdv.class,"alarm_rdv = ?", alarmRdv.getId().toString());
        if (listAssociationAlarmRdv.size()>0) {
            AssociationAlarmRdv associationAlarmRdv = listAssociationAlarmRdv.get(0);
            associationAlarmRdv.delete();
        }
        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_LONG).show();
    }

    /*private void cancelAlert(Class classe, Date dateRdv, String detail, String echeance, Context context,int requestCode) {
        Intent intent = new Intent(this, classe);
        intent.putExtra("detail",detail);
        intent.putExtra("echeance",echeance);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        List<AlarmRdv> listAlarmRdv = AlarmRdv.find(AlarmRdv.class,"request_code = ?", ""+requestCode);
        if (listAlarmRdv.size()>0) {
            AlarmRdv alarmRdv = listAlarmRdv.get(0);
            alarmRdv.delete();
        }
        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_LONG).show();
    }*/

}
