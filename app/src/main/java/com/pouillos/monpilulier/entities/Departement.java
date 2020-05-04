package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class Departement extends SugarRecord implements Serializable, Comparable<Departement> {

    @NotNull
    private String numero;
    @NotNull
    private String nom;
    @NotNull
    private Region Region;

    public Departement() {
    }

    public Departement(String numero, String nom, Region region) {
        this.numero = numero;
        this.nom = nom;
        Region = region;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Region getRegion() {
        return Region;
    }

    public void setRegion(Region region) {
        Region = region;
    }

    @Override
    public String toString() {
        return numero+" - "+nom;
    }

    @Override
    public int compareTo(Departement o) {
        return this.numero.compareTo(o.numero);
    }

}
