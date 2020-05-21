package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;


public class Medicament extends SugarRecord implements Serializable, Comparable<Medicament> {

private Long codeCIS;
private String denomination;
private String denominationShort;
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
        if (denomination.length()>20) { //nb il s'agit de l'affichage pour les notifs 20 c'est ok peut-etre que je peux monter jusqu'à 30 à voir
            this.denominationShort = denomination.substring(0,21)+"...";
        } else {
            this.denominationShort = denomination;
        }

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

    public String getDenominationShort() {
        return denominationShort;
    }

    public void setDenominationShort(String denominationShort) {
        this.denominationShort = denominationShort;
    }

    @Override
    public int compareTo(Medicament o) {
        return this.denomination.compareTo(o.denomination);
    }

    @Override
    public String toString() {
        return denomination;
    }
}
