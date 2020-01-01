package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

@Table
public class Association extends SugarRecord implements Serializable, Comparable<Association>{

    private Utilisateur utilisateur;
    private Medecin medecin;

    public Association() {
    }

    public Association(Utilisateur utilisateur, Medecin medecin) {
        this.utilisateur = utilisateur;
        this.medecin = medecin;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    @Override
    public int compareTo(Association o) {
        return this.getId().compareTo(o.getId());
    }
}
