package com.pouillos.monpilulier.activities.recherche;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.orm.SugarRecord;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.AccueilActivity;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherContactActivity;
import com.pouillos.monpilulier.activities.afficher.AfficherEtablissementActivity;
import com.pouillos.monpilulier.entities.AssociationUtilisateurContact;
import com.pouillos.monpilulier.entities.AssociationUtilisateurEtablissement;
import com.pouillos.monpilulier.entities.Contact;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.entities.Etablissement;
import com.pouillos.monpilulier.entities.EtablissementVille;
import com.pouillos.monpilulier.entities.Prescription;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.Region;
import com.pouillos.monpilulier.entities.SavoirFaire;
import com.pouillos.monpilulier.entities.TypeEtablissement;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterEtablissement;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterPrescription;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRdvAnalyse;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRdvContact;
import com.pouillos.monpilulier.recycler.adapter.RecyclerAdapterRdvExamen;
import com.pouillos.monpilulier.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import icepick.Icepick;
import icepick.State;

public class ChercherEtablissementActivity extends NavDrawerActivity implements RecyclerAdapterEtablissement.Listener {

    @State
    boolean booleanVille;
    @State
    boolean booleanCp;
    @State
    boolean booleanProximite;
    @State
    boolean booleanPharmacie;
    @State
    boolean booleanAutreType;
    @State
    String[] listAutocompletionVille;

    private List<String> listVilleBD;
    private List<TypeEtablissement> listTypeEtablissementBD;
    private RecyclerAdapterEtablissement adapterEtablissement;
    List<Etablissement> listEtablissement;
    @State
    Etablissement etablissementSelected;

    @BindView(R.id.textZip)
    TextInputEditText textZip;
    @BindView(R.id.layoutZip)
    TextInputLayout layoutZip;

    @BindView(R.id.list_etablissement)
    RecyclerView listeEtablissement;

    @BindView(R.id.chipVille)
    Chip chipVille;
    @BindView(R.id.chipCp)
    Chip chipCp;

    @BindView(R.id.chipPharmacie)
    Chip chipPharmacie;
    @BindView(R.id.chipAutreType)
    Chip chipAutreType;
    @BindView(R.id.chipGroupRechercheType)
    ChipGroup chipGroupRechercheType;

    @BindView(R.id.fabChercher)
    FloatingActionButton fabChercher;
    @BindView(R.id.fabRaz)
    FloatingActionButton fabRaz;

    @BindView(R.id.selectionVille)
    AutoCompleteTextView selectionVille;
    @BindView(R.id.listVille)
    TextInputLayout listVille;
    @BindView(R.id.selectionAutreType)
    AutoCompleteTextView selectionAutreType;
    @BindView(R.id.listAutreType)
    TextInputLayout listAutreType;
    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    private LocationManager objgps;
    private LocationListener objlistener;
    double actualLatitude;
    double actualLongitude;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_chercher_etablissement);
// 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        //progressBar.setVisibility(View.VISIBLE);

        ChercherEtablissementActivity.AsyncTaskRunnerBDTypeEtablissement runnerBDTypeEtablissement = new ChercherEtablissementActivity.AsyncTaskRunnerBDTypeEtablissement();
        runnerBDTypeEtablissement.execute();

        fabChercher.hide();
        fabRaz.hide();
        hideAll();
        setTitle("Chercher Etablissement");


    }
    final Looper looper = null;
    final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("Location Changes", location.toString());
            actualLatitude=location.getLatitude();
            actualLongitude=location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Status Changed", String.valueOf(status));
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Provider Enabled", provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Provider Disabled", provider);
        }
    };


    public void localiserTelephone() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ) {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            locationManager.requestSingleUpdate(criteria, locationListener, looper);

            Location localisation = locationManager.getLastKnownLocation(provider.getName());
            if (localisation != null) {
                actualLatitude = localisation.getLatitude();
                actualLongitude = localisation.getLongitude();
            }

        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        }

    public static double calculerDistance(double latDepart, double longDepart, double latArrivee, double longArrivee) {
        double distance = 0;
//todo revoir le calcul car à priori faux
        if ((latDepart != latArrivee) || (longDepart != longArrivee)) {

            double radlat1 = Math.PI * latDepart / 180;
            double radlat2 = Math.PI * latArrivee / 180;
            double theta = longDepart - longArrivee;
            double radtheta = Math.PI * theta / 180;

            distance = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
            if (distance > 1) {
                distance = 1;
            }
            distance = Math.acos(distance);

            distance = 6371*distance;
        }
        return distance;
    }

    public class AsyncTaskRunnerEtablissement extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            publishProgress(5);
            listEtablissement = new ArrayList<>();
            String requete = "";
            requete +="SELECT * FROM ETABLISSEMENT ";
            if (booleanVille) {
                requete += "WHERE VILLE = \""+selectionVille.getText().toString()+"\"";
            } else if (booleanCp) {
                requete += "WHERE CP = \""+textZip.getText().toString()+"\"";
            }
            if (booleanPharmacie) {
                listTypeEtablissementBD = TypeEtablissement.find(TypeEtablissement.class,"name = ?","Pharmacie d'Officine");

              //  requete += " AND TYPE_ETABLISSEMENT = "+listTypeEtablissementBD.get(0).getId().toString();
            } else if (booleanAutreType) {
                listTypeEtablissementBD = TypeEtablissement.find(TypeEtablissement.class,"name = ?",selectionAutreType.getText().toString());

               // requete += " AND TYPE_ETABLISSEMENT = \""+selectionAutreType.getText()+"\"";
            }
            requete += " AND TYPE_ETABLISSEMENT = "+listTypeEtablissementBD.get(0).getId().toString();
            listEtablissement = Etablissement.findWithQuery(Etablissement.class, requete);
            for (Etablissement currentEtablissemnt : listEtablissement) {
                currentEtablissemnt.setDistance(calculerDistance(actualLatitude,actualLongitude,currentEtablissemnt.getLatitude(),currentEtablissemnt.getLongitude()));
            }
            Collections.sort(listEtablissement);
            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            //publishProgress(0);
            if (listEtablissement.size() == 0) {
                Toast.makeText(ChercherEtablissementActivity.this, R.string.text_no_matching, Toast.LENGTH_LONG).show();
                //textRechercheIntervenant.setVisibility(View.GONE);
                //layoutContact.setVisibility(View.GONE);

            } else {
                configureRecyclerView();
                configureOnClickRecyclerView();
                listeEtablissement.setVisibility(View.VISIBLE);
            }



        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(listeEtablissement, R.layout.recycler_list_etablissement)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                    }
                });
    }

    public void configureRecyclerView() {
        adapterEtablissement = new RecyclerAdapterEtablissement(listEtablissement, this);
        //adapterEtablissement = new RecyclerAdapterEtablissement(listEtablissement,actualLatitude,actualLongitude, this);
        listeEtablissement.setAdapter(adapterEtablissement);
        listeEtablissement.setLayoutManager(new LinearLayoutManager(this));

    }

    private void hideAll() {
        listVille.setVisibility(View.GONE);
        listAutreType.setVisibility(View.GONE);
        layoutZip.setVisibility(View.GONE);
        chipGroupRechercheType.setVisibility(View.GONE);
        listeEtablissement.setVisibility(View.GONE);
        booleanVille = false;
        booleanAutreType = false;
        booleanCp = false;
        booleanPharmacie = false;
        booleanProximite = false;
    }

    @Override
    public void onClickEtablissementButton(int position) {
         Etablissement etablissement = listEtablissement.get(position);
      //  List<Etablissement> listeEtablissement = Etablissement.find(Etablissement.class,"NUMERO_FINESS_ET = ?",etablissement.getNumeroFinessET());
      //  Etablissement etablissementSelected = listeEtablissement.get(0);
        AssociationUtilisateurEtablissement assocUE = new AssociationUtilisateurEtablissement(activeUser,etablissement);
      //  assocUE.setUtilisateur(activeUser);
      //  assocUE.setEtablissement(etablissementSelected);
        assocUE.setId(assocUE.save());
        Toast.makeText(ChercherEtablissementActivity.this, "association: "+activeUser+" & "+etablissement, Toast.LENGTH_LONG).show();

        //creer la classe
        ouvrirActiviteSuivante(this, AfficherEtablissementActivity.class,"etablissementId",etablissement.getId(),false);
    }

    public class AsyncTaskRunnerEtablissementVille extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            publishProgress(5);
            listVilleBD = new ArrayList<>();
            List<EtablissementVille> listEtablissementVille = EtablissementVille.listAll(EtablissementVille.class);
            for (EtablissementVille currentEtablissementVille : listEtablissementVille) {
                if (!listVilleBD.contains(currentEtablissementVille.getVille())) {
                    listVilleBD.add(currentEtablissementVille.getVille());
                }
            }
            Collections.sort(listVilleBD);
            publishProgress(25);
            List<String> listVilleString = new ArrayList<>();
            listAutocompletionVille = new String[listVilleBD.size()];
            for (String currentVille : listVilleBD) {
                listVilleString.add(currentVille);
            }
            listVilleString.toArray(listAutocompletionVille);

            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            //publishProgress(0);
            if (listVilleBD.size() == 0) {
                Toast.makeText(ChercherEtablissementActivity.this, R.string.text_no_matching, Toast.LENGTH_LONG).show();
                //textRechercheIntervenant.setVisibility(View.GONE);
                //layoutContact.setVisibility(View.GONE);

            } else {
                //layoutContact.setVisibility(View.VISIBLE);
                listVille.setVisibility(View.VISIBLE);
                selectionVille.requestFocus();
            }

            ArrayAdapter<String> adapter  = new ArrayAdapter<String>(ChercherEtablissementActivity.this, R.layout.list_item_two_lines, R.id.item, listAutocompletionVille);
            selectionVille.setAdapter(adapter);
            selectionVille.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                        long arg3) {
                    Toast.makeText(ChercherEtablissementActivity.this, "Selected ville: \t" + arg0.getAdapter().getItem(arg2), Toast.LENGTH_SHORT).show();
                    chipGroupRechercheType.setVisibility(View.VISIBLE);
                }
            });
            selectionVille.setThreshold(1);

            //layoutContact.setVisibility(View.VISIBLE);


            //Masquer & raz tout le reste



        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    public class AsyncTaskRunnerBDTypeEtablissement extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            publishProgress(5);
            //listTypeEtablissementBD = new ArrayList<>();
            listTypeEtablissementBD = TypeEtablissement.listAll(TypeEtablissement.class);

            Collections.sort(listTypeEtablissementBD);
            publishProgress(25);

            publishProgress(100);
            return null;
        }

        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            buildDropdownMenu(listTypeEtablissementBD, ChercherEtablissementActivity.this,selectionAutreType);
            localiserTelephone();
            //publishProgress(0);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.chipVille)
    public void chipVilleClick() {
        booleanVille=!booleanVille;
        if (booleanVille) {
            progressBar.setVisibility(View.VISIBLE);
            ChercherEtablissementActivity.AsyncTaskRunnerEtablissementVille runnerEtablissementVille = new ChercherEtablissementActivity.AsyncTaskRunnerEtablissementVille();
            runnerEtablissementVille.execute();
            chipVille.setEnabled(false);
            chipCp.setEnabled(true);
            booleanCp=false;
            chipPharmacie.setEnabled(true);
            chipPharmacie.setChecked(false);
            booleanPharmacie=false;
            chipAutreType.setEnabled(true);
            chipAutreType.setChecked(false);
            booleanAutreType=false;
            chipGroupRechercheType.setVisibility(View.GONE);
            layoutZip.setVisibility(View.GONE);
            textZip.setText("");
            listAutreType.setVisibility(View.GONE);
            selectionAutreType.setText("",false);
            listeEtablissement.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.chipCp)
    public void chipCpClick() {
        booleanCp=!booleanCp;
        if (booleanCp) {
            layoutZip.setVisibility(View.VISIBLE);
            textZip.requestFocus();
            chipVille.setEnabled(true);
            chipCp.setEnabled(false);
            booleanVille=false;
            chipPharmacie.setEnabled(true);
            chipPharmacie.setChecked(false);
            booleanPharmacie=false;
            chipAutreType.setEnabled(true);
            chipAutreType.setChecked(false);
            booleanAutreType=false;
            chipGroupRechercheType.setVisibility(View.GONE);
            listVille.setVisibility(View.GONE);
            selectionVille.setText("",false);
            listAutreType.setVisibility(View.GONE);
            selectionAutreType.setText("",false);
            listeEtablissement.setVisibility(View.GONE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.chipPharmacie)
    public void chipPharmacieClick() {
        booleanPharmacie=!booleanPharmacie;
        if (booleanPharmacie) {
            progressBar.setVisibility(View.VISIBLE);
            ChercherEtablissementActivity.AsyncTaskRunnerEtablissement runnerEtablissement = new ChercherEtablissementActivity.AsyncTaskRunnerEtablissement();
            runnerEtablissement.execute();
            chipPharmacie.setEnabled(false);

            chipAutreType.setEnabled(true);
            chipAutreType.setEnabled(true);
            chipAutreType.setChecked(false);
            booleanAutreType=false;
            listAutreType.setVisibility(View.GONE);
            selectionAutreType.setText("",false);
            listeEtablissement.setVisibility(View.GONE);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @OnClick(R.id.chipAutreType)
    public void chipAutreTypeClick() {
        booleanAutreType=!booleanAutreType;
        if (booleanAutreType) {
            listAutreType.setVisibility(View.VISIBLE);
            selectionAutreType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                        long arg3) {
                    Toast.makeText(ChercherEtablissementActivity.this, "Selected type: \t" + arg0.getAdapter().getItem(arg2), Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.VISIBLE);
                    ChercherEtablissementActivity.AsyncTaskRunnerEtablissement runnerEtablissement = new ChercherEtablissementActivity.AsyncTaskRunnerEtablissement();
                    runnerEtablissement.execute();
                }
            });
            chipPharmacie.setEnabled(true);
            chipPharmacie.setChecked(false);
            booleanPharmacie=false;
            chipAutreType.setEnabled(false);
            booleanPharmacie=false;
            listVille.setVisibility(View.GONE);
            selectionVille.setText("",false);
            listeEtablissement.setVisibility(View.GONE);
        }
    }



    @OnTextChanged(R.id.textZip)
    public void textZipChange() {
        if (textZip.getText().toString().length() == 5) {
            chipGroupRechercheType.setVisibility(View.VISIBLE);
        }
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
}
