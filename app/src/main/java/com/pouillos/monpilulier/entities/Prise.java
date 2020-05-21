package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.enumeration.QuantiteDose;

import java.io.Serializable;
import java.util.Date;

public class Prise extends SugarRecord implements Serializable, Comparable<Prise>  {

    Ordonnance ordonnance;
    Date date;
    boolean effectue;
    Medicament medicament;
    Dose dose;
    Double qteDose;

    public Prise() {
    }

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isEffectue() {
        return effectue;
    }

    public void setEffectue(boolean effectue) {
        this.effectue = effectue;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public Dose getDose() {
        return dose;
    }

    public void setDose(Dose dose) {
        this.dose = dose;
    }

    public Double getQteDose() {
        return qteDose;
    }

    public void setQteDose(Double qteDose) {
        this.qteDose = qteDose;
    }

    @Override
    public int compareTo(Prise o) {
        return this.getId().compareTo(o.getId());
    }


}
