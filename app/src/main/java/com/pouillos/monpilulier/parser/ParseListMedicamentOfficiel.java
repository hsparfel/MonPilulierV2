package com.pouillos.monpilulier.parser;

import android.content.res.AssetManager;

import com.pouillos.monpilulier.entities.FormePharmaceutique;
import com.pouillos.monpilulier.entities.Medicament;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseListMedicamentOfficiel {

//save the context recievied via constructor in a local variable



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

            while ((line = reader.readLine()) != null) {

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
    }
}
