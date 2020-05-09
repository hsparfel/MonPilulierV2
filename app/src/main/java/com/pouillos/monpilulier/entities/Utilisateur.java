package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;


public class Utilisateur extends SugarRecord implements Serializable, Comparable<Utilisateur> {

private String name;
private Date dateDeNaissance;
private String sexe;
private Departement departement;
private boolean actif;


    public Utilisateur() {
    }

    public Utilisateur(String name, Date dateDeNaissance, String sexe) {
        this.name = name;
        this.dateDeNaissance = dateDeNaissance;
        this.sexe = sexe;
    }

    public Utilisateur(String name, Date dateDeNaissance, String sexe, Departement departement) {
        this.name = name;
        this.dateDeNaissance = dateDeNaissance;
        this.sexe = sexe;
        this.departement = departement;
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

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Utilisateur o) {
        return this.name.compareTo(o.name);
    }

    /*public Utilisateur findActifUser() {
        Utilisateur utilisateur = new Utilisateur();
        try {
            utilisateur = (Utilisateur) find(Utilisateur.class, "actif = ?", "1").get(0);
        } catch (Exception e) {
        }
        return utilisateur;
    }*/

    /*@Override
    public String afficherTitre() {
        return name;
    }

    @Override
    public String afficherDetail() {
        return null;
    }*/
}
