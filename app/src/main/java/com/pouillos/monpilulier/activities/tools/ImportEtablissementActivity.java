package com.pouillos.monpilulier.activities.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orm.SugarRecord;
import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.NavDrawerActivity;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.entities.Etablissement;
import com.pouillos.monpilulier.entities.ImportEtablissement;
import com.pouillos.monpilulier.entities.TypeEtablissement;
import com.pouillos.monpilulier.entities.Departement;
import com.pouillos.monpilulier.interfaces.BasicUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class ImportEtablissementActivity extends NavDrawerActivity implements Serializable, BasicUtils {

    @State
    ImportEtablissement importEtablissement;

    private List<ImportEtablissement> listImportEtablissementBD;

    @BindView(R.id.nbEtablissementImported)
    TextView nbEtablissementImported;

    @BindView(R.id.fabImport)
    FloatingActionButton fabImport;
    @BindView(R.id.fabSupprDoublon)
    FloatingActionButton fabSupprDoublon;

    @BindView(R.id.my_progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_import_etablissement);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        ImportEtablissementActivity.AsyncTaskRunnerBDImportEtablissement runnerBDImportEtablissement = new ImportEtablissementActivity.AsyncTaskRunnerBDImportEtablissement();
        runnerBDImportEtablissement.execute();

        setTitle("Import Etablissement");

        afficherNbEtablissementEnregistre();
    }

    public void afficherNbEtablissementEnregistre() {
        Long nb = Etablissement.count(Etablissement.class);
        int total = 95186;
        Long pourcentage = nb*100/total;

        nbEtablissementImported.setText(" nb de etablissements importes:" + nb.toString()+"/"+total+" ("+pourcentage+"%)");
    }

    public class AsyncTaskRunnerBDImportEtablissement extends AsyncTask<Void, Integer, Void> {
        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            publishProgress(10);
            listImportEtablissementBD = ImportEtablissement.listAll(ImportEtablissement.class);
            Collections.sort(listImportEtablissementBD);
            publishProgress(100);
            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ImportEtablissementActivity.this, "recup des list d'import terminee", Toast.LENGTH_LONG).show();
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabImport)
    public void fabImportClick() {
        progressBar.setVisibility(View.VISIBLE);
        ImportEtablissementActivity.AsyncTaskRunnerEtablissement runnerEtablissement = new ImportEtablissementActivity.AsyncTaskRunnerEtablissement(this);
        runnerEtablissement.execute();

    }

    private class AsyncTaskRunnerEtablissement extends AsyncTask<Void, Integer, Void> {

        private Context context;
        public AsyncTaskRunnerEtablissement(Context context) {
            this.context=context;
        }

        protected Void doInBackground(Void...voids) {

            InputStream is = null;
            BufferedReader reader = null;

            List<TypeEtablissement> listTypeEtablissement = TypeEtablissement.listAll(TypeEtablissement.class);
            Map<String, TypeEtablissement> mapTypeEtablissement = new HashMap<>();
            for (TypeEtablissement typeEtablissement : listTypeEtablissement) {
                mapTypeEtablissement.put(typeEtablissement.getName(), typeEtablissement);
            }
            List<Departement> listDepartement = Departement.listAll(Departement.class);
            Map<String, Departement> mapDepartement = new HashMap<>();
            for (Departement departement : listDepartement) {
                mapDepartement.put(departement.getNumero(), departement);
            }

            List<ImportEtablissement> listImportEtablissement = ImportEtablissement.find(ImportEtablissement.class,"import_completed = ?","0");
            //List<ImportEtablissement> listImportEtablissement = ImportEtablissement.listAll(ImportEtablissement.class);

            int nbImportEffectue =0;
            int nbImportIgnore = 0;
            int readerCount=0;
            int nbLigneLue=0;
            //ImportEtablissement current = listImportEtablissement.get(0);
            for (ImportEtablissement current : listImportEtablissement) {
                nbImportEffectue =0;
                nbImportIgnore = 0;
                //readerCount=0;
                nbLigneLue=0;
                if (current.getDateDebut() == null) {
                    current.setDateDebut(DateUtils.ecrireDateHeure(new Date()));
                }
                if (current.getNbLigneLue() != 0) {
                    nbLigneLue = current.getNbLigneLue();
                }
                if (current.getNbImportEffectue() != 0) {
                    nbImportEffectue = current.getNbImportEffectue();
                }
                if (current.getNbImportIgnore() != 0) {
                    nbImportIgnore = current.getNbImportIgnore();
                }

                //current.setDateFin("");
                //current.setNbImportEffectue(nbImportEffectue);
                ///current.setNbImportIgnore(nbImportIgnore);
                // nbImportEffectue = 0;
                // nbImportIgnore = 0;

                try {
                    is = getAssets().open(current.getPath());
                    reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    String line = null;

                    int readerSize = 95186;
                    readerCount = 0;
                    int compteur = 0;
                    publishProgress(compteur);
                    while ((line = reader.readLine()) != null) {

                            if (readerCount<nbLigneLue) {
                                readerCount ++;
                                continue;
                            }
                        readerCount ++;
                        compteur = readerCount*100/readerSize;
                        //compteur ++;
                        publishProgress(compteur);
                        final String SEPARATEUR = "\\;";
                        String lineSplitted[] = line.split(SEPARATEUR);

                        Etablissement etablissement = new Etablissement();
                        etablissement.setNumeroFinessET(lineSplitted[0]);
                        etablissement.setRaisonSocial(lineSplitted[1]);
                        etablissement.setAdresse(lineSplitted[2]);
                        if (lineSplitted[4].length() == 4) {
                            etablissement.setCp("0"+lineSplitted[4]);
                        } else {
                            etablissement.setCp(lineSplitted[4]);
                        }
                     /*   if (lineSplitted[5].length()>6 && lineSplitted[5].substring(lineSplitted[5].length()-5).equalsIgnoreCase("CEDEX")){
                            etablissement.setVille(lineSplitted[5].substring(0,lineSplitted[5].length()-6));
                        } else {
                            etablissement.setVille(lineSplitted[5]);
                        }*/
                     //todo  revoir avec contains pour tous les cedex
                        if (lineSplitted[5].contains("CEDEX")) {
                            int index = lineSplitted[5].indexOf("CEDEX");
                            etablissement.setVille(lineSplitted[5].substring(0,index-1));
                        } else {
                            etablissement.setVille(lineSplitted[5]);
                        }


                        if (lineSplitted[5].length()>6 && lineSplitted[5].substring(lineSplitted[5].length()-5).equalsIgnoreCase("CEDEX")){
                            etablissement.setVille(lineSplitted[5].substring(0,lineSplitted[5].length()-6));
                        } else {
                            etablissement.setVille(lineSplitted[5]);
                        }
////////////////////////
                        if (lineSplitted[6].length() == 9) {
                            etablissement.setTelephone("0"+lineSplitted[6]);
                        }
                        if (lineSplitted[7].length() == 9) {
                            etablissement.setFax("0"+lineSplitted[7]);
                        }

                        etablissement.setTypeEtablissement(mapTypeEtablissement.get(lineSplitted[8]));

                        if (lineSplitted[3].equalsIgnoreCase("2A") || lineSplitted[3].equalsIgnoreCase("2B")) {
                            etablissement.setDepartement(mapDepartement.get("20"));
                        } else if (lineSplitted[3].equalsIgnoreCase("9A") || lineSplitted[3].equalsIgnoreCase("9B") || lineSplitted[3].equalsIgnoreCase("9C") || lineSplitted[3].equalsIgnoreCase("9D") || lineSplitted[3].equalsIgnoreCase("9E") || lineSplitted[3].equalsIgnoreCase("9F")) {
                            etablissement.setDepartement(mapDepartement.get("97"));
                        } else if (lineSplitted[3].length()==1) {
                            etablissement.setDepartement(mapDepartement.get("0"+lineSplitted[3]));
                        } else {
                            etablissement.setDepartement(mapDepartement.get(lineSplitted[3]));
                        }
                        etablissement.setRegion(etablissement.getDepartement().getRegion());

                        if (etablissement.getAdresse() != null && etablissement.getCp() != null && etablissement.getVille() != null) {
                            etablissement.enregisterCoordonnees(context);
                        }
                        //Log.i("enregistre","medecin new: "+lineSplitted[2]);
                        etablissement.save();
                        nbLigneLue++;
                        nbImportEffectue++;
                        current.setNbLigneLue(nbLigneLue);
                        current.setNbImportEffectue(nbImportEffectue);
                        current.save();
                    }
                } catch (final Exception e) {
                    nbImportIgnore++;
                    current.setNbImportIgnore(nbImportIgnore);
                    current.save();
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
                current.setDateFin(DateUtils.ecrireDateHeure(new Date()));
                current.setNbImportIgnore(nbImportIgnore);
                current.setNbImportEffectue(nbImportEffectue);
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
            afficherNbEtablissementEnregistre();
            Toast.makeText(ImportEtablissementActivity.this, "IMPORT TOTAL FINI", Toast.LENGTH_LONG).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabSupprDoublon)
    public void fabSupprDoublonClick() {

        Toast.makeText(ImportEtablissementActivity.this, "raz import etablissement & etablissement", Toast.LENGTH_LONG).show();
        //SugarRecord.executeQuery("DELETE FROM IMPORT_ETABLISSEMENT");
        //SugarRecord.executeQuery("DELETE FROM ETABLISSEMENT");
        SugarRecord.executeQuery("update import_etablissement set import_completed =0 where id = 9");

    }


}
