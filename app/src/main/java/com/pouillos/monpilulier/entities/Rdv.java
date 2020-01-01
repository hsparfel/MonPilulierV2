package com.pouillos.monpilulier.entities;

import java.util.Date;

public class Rdv {

    private String detail;
    private Utilisateur utilisateur;
    private Medecin medecin;
    private Date date;
    private Cabinet cabinet;

    public Rdv() {
    }

    public Rdv(String detail, Utilisateur utilisateur, Medecin medecin, Date date, Cabinet cabinet) {
        this.detail = detail;
        this.utilisateur = utilisateur;
        this.medecin = medecin;
        this.date = date;
        this.cabinet = cabinet;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    @Override
    public String toString() {
        return "Rdv{" +
                ", detail='" + detail + '\'' +
                ", utilisateur=" + utilisateur +
                ", medecin=" + medecin +
                ", date='" + date + '\'' +
                ", cabinet=" + cabinet +
                '}';
    }
}
