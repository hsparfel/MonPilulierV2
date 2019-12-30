package com.pouillos.monpilulier.entities;

import java.util.Date;

public class Profil {

    private int id;
    private String detail;
    private Date creationDate;
    private Utilisateur utilisateur;
    private float poids;
    private int taille;
    private float imc;
    private Date date;

    public Profil() {
    }

    public Profil(String detail, Utilisateur utilisateur, float poids, int taille, float imc, Date date) {
        this.detail = detail;
        this.utilisateur = utilisateur;
        this.poids = poids;
        this.taille = taille;
        this.imc = imc;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public float getImc() {
        return imc;
    }

    public void setImc(float imc) {
        this.imc = imc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Profil{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                ", creationDate=" + creationDate +
                ", utilisateur=" + utilisateur +
                ", poids=" + poids +
                ", taille=" + taille +
                ", imc=" + imc +
                ", date='" + date + '\'' +
                '}';
    }
}
