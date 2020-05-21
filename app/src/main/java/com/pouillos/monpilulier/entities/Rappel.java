package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class Rappel extends SugarRecord implements Serializable, Comparable<Rappel> {

    private Prescription prescription;
    private Double quantiteDose;

    private Dose dose;

    private String heure;

    public Rappel() {
    }

    public Double getQuantiteDose() {
        return quantiteDose;
    }

    public void setQuantiteDose(Double quantiteDose) {
        this.quantiteDose = quantiteDose;
    }

    public Dose getDose() {
        return dose;
    }

    public void setDose(Dose dose) {
        this.dose = dose;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    @Override
    public int compareTo(Rappel o) {
        return this.getId().compareTo(o.getId());
    }

}
