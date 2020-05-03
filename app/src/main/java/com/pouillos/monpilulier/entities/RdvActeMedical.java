package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class RdvActeMedical extends SugarRecord implements Serializable, Comparable<RdvActeMedical> {

    private String note;
    private Utilisateur utilisateur;
    private Analyse analyse;
    private Examen examen;
    private Date date;

    public RdvActeMedical() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Analyse getAnalyse() {
        return analyse;
    }

    public void setAnalyse(Analyse analyse) {
        this.analyse = analyse;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(RdvActeMedical o) {
        return this.getId().compareTo(o.getId());
    }


}
