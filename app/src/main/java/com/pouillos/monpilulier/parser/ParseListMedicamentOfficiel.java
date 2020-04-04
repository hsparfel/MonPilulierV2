package com.pouillos.monpilulier.parser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.pouillos.monpilulier.activities.MainActivity;
import com.pouillos.monpilulier.entities.FormePharmaceutique;
import com.pouillos.monpilulier.entities.MedecinOfficiel;
import com.pouillos.monpilulier.entities.MedicamentOfficiel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseListMedicamentOfficiel {

    public static void readFile(AssetManager mgr, String path) {
        String contents = "";
        InputStream is = null;
        BufferedReader reader = null;


        try {
            is = mgr.open(path);
            reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
            //reader = new BufferedReader(new InputStreamReader(is,"windows-1252"));
            //contents = reader.readLine();
            String line = null;

            List<MedicamentOfficiel> listMedicamentOfficiel = MedicamentOfficiel.listAll(MedicamentOfficiel.class);
            Map<Long, MedicamentOfficiel> mapMedicamentOfficiel = new HashMap<>();
            for (MedicamentOfficiel medicamentOfficiel : listMedicamentOfficiel) {
                mapMedicamentOfficiel.put(medicamentOfficiel.getCodeCIS(), medicamentOfficiel);
            }

            List<FormePharmaceutique> listFormePharamaceutique = FormePharmaceutique.listAll(FormePharmaceutique.class);
            Map<String, FormePharmaceutique> mapFormePharamaceutique = new HashMap<>();
            for (FormePharmaceutique formePharmaceutique : listFormePharamaceutique) {
                mapFormePharamaceutique.put(formePharmaceutique.getName(), formePharmaceutique);
            }

            while ((line = reader.readLine()) != null) {

                final String SEPARATEUR = "\t";
                String lineSplitted[] = line.split(SEPARATEUR);

                MedicamentOfficiel medicamentOfficiel = new MedicamentOfficiel();
                //verif si commercialise
                if (!lineSplitted[6].equals("Commercialisée") ) {
                    continue;
                }

                //verif si existant
                MedicamentOfficiel verifMedicamentOfficiel = null;
                verifMedicamentOfficiel = mapMedicamentOfficiel.get(Long.parseLong(lineSplitted[0]));
                if (verifMedicamentOfficiel != null){
                    continue;
                }

                medicamentOfficiel.setCodeCIS(Long.parseLong(lineSplitted[0]));

                int positionVirgule = lineSplitted[1].indexOf(",");
                if (positionVirgule >=0){
                    medicamentOfficiel.setDenomination(lineSplitted[1].substring(0,positionVirgule));
                } else {
                    medicamentOfficiel.setDenomination(lineSplitted[1]);
                }
                if (lineSplitted[2].substring(0,1).equals(" ")) {
                    lineSplitted[2] = lineSplitted[2].substring(1,lineSplitted[2].length());
                }

                medicamentOfficiel.setFormePharmaceutique(mapFormePharamaceutique.get(lineSplitted[2]));

                int positionPointVirgule = lineSplitted[10].indexOf(";");
                if (positionPointVirgule >=0){
                    medicamentOfficiel.setTitulaire(lineSplitted[10].substring(0,positionPointVirgule));
                } else {
                    medicamentOfficiel.setTitulaire(lineSplitted[10]);
                }
                medicamentOfficiel.save();
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
    }
}
