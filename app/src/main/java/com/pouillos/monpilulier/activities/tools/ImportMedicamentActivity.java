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
import com.pouillos.monpilulier.entities.FormePharmaceutique;
import com.pouillos.monpilulier.entities.Medicament;
import com.pouillos.monpilulier.entities.ImportMedicament;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.SavoirFaire;
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

public class ImportMedicamentActivity extends NavDrawerActivity implements Serializable, BasicUtils {

    @State
    ImportMedicament importMedicament;

    private List<ImportMedicament> listImportMedicamentBD;

    @BindView(R.id.nbMedicamentImported)
    TextView nbMedicamentImported;

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
        setContentView(R.layout.activity_import_medicament);

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        ImportMedicamentActivity.AsyncTaskRunnerBDImportMedicament runnerBDImportMedicament = new ImportMedicamentActivity.AsyncTaskRunnerBDImportMedicament();
        runnerBDImportMedicament.execute();

        setTitle("Import Medicament");

        afficherNbMedicamentEnregistre();
    }

    public void afficherNbMedicamentEnregistre() {
        Long nb = Medicament.count(Medicament.class);
        int total = 13023;
        Long pourcentage = nb*100/total;
        nbMedicamentImported.setText(" nb de medicaments importes:" + nb.toString()+"/"+total+" ("+pourcentage+"%)");
    }

    public class AsyncTaskRunnerBDImportMedicament extends AsyncTask<Void, Integer, Void> {
        protected Void doInBackground(Void...voids) {
            publishProgress(0);
            publishProgress(10);
            listImportMedicamentBD = ImportMedicament.listAll(ImportMedicament.class);
            Collections.sort(listImportMedicamentBD);
            publishProgress(100);
            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ImportMedicamentActivity.this, "recup des list d'import terminee", Toast.LENGTH_LONG).show();
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabImport)
    public void fabImportClick() {
        progressBar.setVisibility(View.VISIBLE);
        ImportMedicamentActivity.AsyncTaskRunnerMedicament runnerMedicament = new ImportMedicamentActivity.AsyncTaskRunnerMedicament(this);
        runnerMedicament.execute();

    }

    private class AsyncTaskRunnerMedicament extends AsyncTask<Void, Integer, Void> {

        private Context context;
        public AsyncTaskRunnerMedicament(Context context) {
            this.context=context;
        }

        protected Void doInBackground(Void...voids) {

            InputStream is = null;
            BufferedReader reader = null;

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

            List<ImportMedicament> listImportMedicament = ImportMedicament.find(ImportMedicament.class,"import_completed = ?","0");
            //List<ImportMedicament> listImportMedicament = ImportMedicament.listAll(ImportMedicament.class);
//TODO A REVOIR
            int nbImportEffectue =0;
            int nbImportIgnore = 0;
            int readerCount=0;
            int nbLigneLue=0;
            //ImportMedicament current = listImportMedicament.get(0);
            for (ImportMedicament current : listImportMedicament) {
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
                    reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                    String line = null;

                    int readerSize = 15090;
                    readerCount = 0;
                    int compteur = 0;
                    publishProgress(compteur);



                    while ((line = reader.readLine()) != null) {
                        //todo revoir cette condition

                            if (readerCount<nbLigneLue) {
                                readerCount ++;
                                continue;
                            }
                        readerCount ++;
                        compteur = readerCount*100/readerSize;
                        //compteur ++;
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
            afficherNbMedicamentEnregistre();
            Toast.makeText(ImportMedicamentActivity.this, "IMPORT TOTAL FINI", Toast.LENGTH_LONG).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... integer) {
            progressBar.setProgress(integer[0],true);
        }
    }

    @OnClick(R.id.fabSupprDoublon)
    public void fabSupprDoublonClick() {

        Toast.makeText(ImportMedicamentActivity.this, "raz import medicament & medicament", Toast.LENGTH_LONG).show();
       //mis en commentaire pr eviter raz non voulue
        // SugarRecord.executeQuery("DELETE FROM IMPORT_MEDICAMENT");
       // SugarRecord.executeQuery("DELETE FROM MEDICAMENT");
       // SugarRecord.executeQuery("update import_medicament set import_completed =0 where id = 48");
        SugarRecord.executeQuery("update import_medicament set import_completed =0 where id = 71");
    }




}
