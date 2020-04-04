package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class Region extends SugarRecord implements Serializable, Comparable<com.pouillos.monpilulier.entities.Region> {

    @NotNull
    private String nom;

    public Region() {
    }

    public Region(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

        @Override
    public int compareTo(com.pouillos.monpilulier.entities.Region o) {
        return this.nom.compareTo(o.nom);
    }

}
