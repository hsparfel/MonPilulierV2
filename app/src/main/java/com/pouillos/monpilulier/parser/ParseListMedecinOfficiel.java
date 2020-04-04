package com.pouillos.monpilulier.parser;

import android.content.res.AssetManager;

import com.facebook.stetho.common.StringUtil;
import com.pouillos.monpilulier.entities.FormePharmaceutique;
import com.pouillos.monpilulier.entities.MedecinOfficiel;
import com.pouillos.monpilulier.entities.MedecinOfficiel;
import com.pouillos.monpilulier.entities.Profession;
import com.pouillos.monpilulier.entities.SavoirFaire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseListMedecinOfficiel {

    public static void readFile(AssetManager mgr, String path) {
        String contents = "";
        InputStream is = null;
        BufferedReader reader = null;

        try {
            is = mgr.open(path);
            //reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
            //reader = new BufferedReader(new InputStreamReader(is,"windows-1252"));
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            //contents = reader.readLine();
            String line = null;

            List<MedecinOfficiel> listMedecinOfficiel = MedecinOfficiel.listAll(MedecinOfficiel.class);
            Map<String, MedecinOfficiel> mapMedecinOfficiel = new HashMap<>();
            for (MedecinOfficiel medecinOfficiel : listMedecinOfficiel) {
                mapMedecinOfficiel.put(medecinOfficiel.getIdPP(), medecinOfficiel);
            }

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

            while ((line = reader.readLine()) != null) {

                final String SEPARATEUR = "\\|";
                String lineSplitted[] = line.split(SEPARATEUR);

                if (lineSplitted[1].equals("Identifiant PP")) {
                    continue;
                }
                MedecinOfficiel medecinOfficiel = new MedecinOfficiel();
                
                //verif si existant
                MedecinOfficiel verifMedecinOfficiel = null;
                verifMedecinOfficiel = mapMedecinOfficiel.get(lineSplitted[2]);
                if (verifMedecinOfficiel != null){
                    continue;
                }

                /*List<MedecinOfficiel> listMedecinOfficiel = MedecinOfficiel.find(MedecinOfficiel.class,"id_pp = ?", lineSplitted[2]);
                if (listMedecinOfficiel.size()>0) {
                    continue;
                    //medecinOfficiel = listMedecinOfficiel.get(0);
                }*/

                medecinOfficiel.setIdPP(lineSplitted[2]);
                medecinOfficiel.setCodeCivilite(lineSplitted[3]);
                medecinOfficiel.setNom(lineSplitted[7].toUpperCase());
                if (lineSplitted[8].length()>1) {
                    String prenom = lineSplitted[8].substring(0,1).toUpperCase()+lineSplitted[8].substring(1,lineSplitted[8].length()-1).toLowerCase();
                }
                medecinOfficiel.setPrenom(lineSplitted[8]);
                medecinOfficiel.setProfession(mapProfession.get(lineSplitted[10]));

                if (lineSplitted[16].equals("Qualifié en Médecine Générale") || lineSplitted[16].equals("Spécialiste en Médecine Générale")) {
                    medecinOfficiel.setSavoirFaire(mapSavoirFaire.get("Médecine Générale"));
                } else {
                    medecinOfficiel.setSavoirFaire(mapSavoirFaire.get(lineSplitted[16]));
                }
                if (lineSplitted.length>24) {
                    medecinOfficiel.setRaisonSocial(lineSplitted[24]);
                }
                if (lineSplitted.length>26) {
                    medecinOfficiel.setComplement(lineSplitted[26]);
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
                medecinOfficiel.setAdresse(adresse.toUpperCase());
                /*if (lineSplitted.length>35) {
                    if (lineSplitted[35].length() == 4) {
                        medecinOfficiel.setCp("0" + lineSplitted[35]);
                    } else {
                        medecinOfficiel.setCp(lineSplitted[35]);
                    }
                }
                if (lineSplitted.length>37) {
                    medecinOfficiel.setVille(lineSplitted[37].toUpperCase());
                }*/
                if (lineSplitted.length>35) {
                    medecinOfficiel.setCp(lineSplitted[34].substring(0,5));
                    medecinOfficiel.setVille(lineSplitted[34].substring(6));
                }
                if (lineSplitted.length>40) {
                    lineSplitted[40] = lineSplitted[40].replace(" ", "");
                    lineSplitted[40] = lineSplitted[40].replace(".", "");
                    if (lineSplitted[40].length() == 9) {
                        medecinOfficiel.setTelephone("0" + lineSplitted[40]);
                    } else if (lineSplitted[40].length() == 10) {
                        medecinOfficiel.setTelephone(lineSplitted[40]);
                    }
                }
                if (lineSplitted.length>42) {
                    lineSplitted[42] = lineSplitted[42].replace(" ", "");
                    lineSplitted[42] = lineSplitted[42].replace(".", "");
                    if (lineSplitted[42].length() == 9) {
                        medecinOfficiel.setFax("0" + lineSplitted[42]);
                    } else if (lineSplitted[42].length() == 10) {
                        medecinOfficiel.setFax(lineSplitted[42]);
                    }
                }
                if (lineSplitted.length>43) {
                    medecinOfficiel.setEmail(lineSplitted[43]);
                }
                medecinOfficiel.save();
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
