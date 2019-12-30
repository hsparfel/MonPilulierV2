package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.abstraite.GestionDate;

import java.io.Serializable;
import java.util.Date;
@Table
public class Ordonnance extends SugarRecord implements Serializable, Comparable<Ordonnance>, GestionDate {

    private Long id;
    private String detail;
    private Date creationDate;
    private Utilisateur utilisateur;
    private Medecin medecin;
    private Date date;

    public Ordonnance() {
    }

    public Ordonnance(String detail, Utilisateur utilisateur, Medecin medecin, Date date) {
        this.detail = detail;
        this.utilisateur = utilisateur;
        this.medecin = medecin;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "Ordonnance{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                ", creationDate=" + creationDate +
                ", utilisateur=" + utilisateur +
                ", medecin=" + medecin +
                ", date=" + date +
                '}';
    }

    public String getName() {
        return "" + medecin.getName() +
                ", " + date;
    }

    @Override
    public int compareTo(Ordonnance o) {
        return this.id.compareTo(o.getId());
    }
}
