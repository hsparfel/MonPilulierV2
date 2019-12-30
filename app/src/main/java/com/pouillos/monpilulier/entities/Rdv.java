package com.pouillos.monpilulier.entities;

import java.util.Date;

public class Rdv {

private int id;
private String detail;
private Date creationDate;
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
                "id=" + id +
                ", detail='" + detail + '\'' +
                ", creationDate=" + creationDate +
                ", utilisateur=" + utilisateur +
                ", medecin=" + medecin +
                ", date='" + date + '\'' +
                ", cabinet=" + cabinet +
                '}';
    }
}
