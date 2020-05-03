package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;


public class Medicament extends SugarRecord implements Serializable, Comparable<Medicament> {

private Long codeCIS;
private String denomination;
private FormePharmaceutique formePharmaceutique;
private String titulaire;

    public Medicament() {
    }

    public Long getCodeCIS() {
        return codeCIS;
    }

    public void setCodeCIS(Long codeCIS) {
        this.codeCIS = codeCIS;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public FormePharmaceutique getFormePharmaceutique() {
        return formePharmaceutique;
    }

    public void setFormePharmaceutique(FormePharmaceutique formePharmaceutique) {
        this.formePharmaceutique = formePharmaceutique;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }

    @Override
    public int compareTo(Medicament o) {
        return this.denomination.compareTo(o.denomination);
    }

}
