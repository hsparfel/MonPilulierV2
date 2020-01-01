package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.abstraite.GestionDate;

import java.io.Serializable;
import java.util.Date;

@Table
public class Utilisateur extends SugarRecord implements Serializable, Comparable<Utilisateur>, GestionDate {

private String name;
private Date dateDeNaissance;
private String sexe;
private boolean actif;


    public Utilisateur() {
    }

    public Utilisateur(String name, Date dateDeNaissance, String sexe) {
        this.name = name;
        this.sexe = sexe;
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                ", name='" + name + '\'' +
                ", dateDeNaissance=" + dateDeNaissance +
                ", sexe='" + sexe + '\'' +
                ", actif=" + actif +
                '}';
    }

    @Override
    public int compareTo(Utilisateur o) {
        return this.name.compareTo(o.name);
    }

    public Utilisateur findActifUser() {
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = (Utilisateur) find(Utilisateur.class, "actif = ?", "1").get(0);
        } catch (Exception e) {
        }
        return utilisateur;
    }
}
