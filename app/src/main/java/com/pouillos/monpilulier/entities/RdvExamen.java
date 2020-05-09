package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.activities.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class RdvExamen extends SugarRecord implements Serializable, Comparable<RdvExamen> {

    private String note;
    private Utilisateur utilisateur;
    private Examen examen;
    private Date date;

    public RdvExamen() {
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
    public int compareTo(RdvExamen o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return DateUtils.ecrireDateHeure(date)+ " - " + examen.getName();
    }
}
