package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class ImportMedicament extends SugarRecord implements Serializable, Comparable<ImportMedicament> {

    @NotNull
    private String path;
    @NotNull
    private boolean importCompleted;
    private String dateDebut;
    private String dateFin;
    private int nbLigneLue;
    private int nbImportEffectue;
    private int nbImportIgnore;




    public ImportMedicament(String path, boolean importCompleted) {
        this.path = path;
        this.importCompleted = importCompleted;
    }

    public ImportMedicament() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isImportCompleted() {
        return importCompleted;
    }

    public void setImportCompleted(boolean importCompleted) {
        this.importCompleted = importCompleted;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbImportEffectue() {
        return nbImportEffectue;
    }

    public void setNbImportEffectue(int nbImportEffectue) {
        this.nbImportEffectue = nbImportEffectue;
    }

    public int getNbImportIgnore() {
        return nbImportIgnore;
    }

    public void setNbImportIgnore(int nbImportIgnore) {
        this.nbImportIgnore = nbImportIgnore;
    }

    public int getNbLigneLue() {
        return nbLigneLue;
    }

    public void setNbLigneLue(int nbLigneLue) {
        this.nbLigneLue = nbLigneLue;
    }

    @Override
    public int compareTo(ImportMedicament o) {
        return this.getId().compareTo(o.getId());
    }
}
