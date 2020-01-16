package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.io.Serializable;


public class Association extends SugarRecord implements Serializable, Comparable<Association>{

    private Long utilisateur;
    private Long medecin;

    public Association() {
    }

    public Association(Long utilisateur, Long medecin) {
        this.utilisateur = utilisateur;
        this.medecin = medecin;
    }

    public Long getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Long utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Long getMedecin() {
        return medecin;
    }

    public void setMedecin(Long medecin) {
        this.medecin = medecin;
    }

    @Override
    public int compareTo(Association o) {
        return this.getId().compareTo(o.getId());
    }
}
