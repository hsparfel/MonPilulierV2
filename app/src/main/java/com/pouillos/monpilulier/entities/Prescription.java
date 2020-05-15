package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.enumeration.Duree;
import com.pouillos.monpilulier.enumeration.Frequence;

import java.io.Serializable;

public class Prescription extends SugarRecord implements Serializable, Comparable<Prescription>  {

    Ordonnance ordonnance;
    Medicament medicament;
    Duree duree;
    Frequence frequence;
    boolean whenNeeded;

    public Prescription() {
    }

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public Duree getDuree() {
        return duree;
    }

    public void setDuree(Duree duree) {
        this.duree = duree;
    }

    public Frequence getFrequence() {
        return frequence;
    }

    public void setFrequence(Frequence frequence) {
        this.frequence = frequence;
    }

    public boolean isWhenNeeded() {
        return whenNeeded;
    }

    public void setWhenNeeded(boolean whenNeeded) {
        this.whenNeeded = whenNeeded;
    }

    @Override
    public int compareTo(Prescription o) {
        return this.getId().compareTo(o.getId());
    }


}
