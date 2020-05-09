package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.activities.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class RdvAnalyse extends SugarRecord implements Serializable, Comparable<RdvAnalyse> {

    private String note;
    private Utilisateur utilisateur;
    private Analyse analyse;
    private Date date;

    public RdvAnalyse() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(RdvAnalyse o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return DateUtils.ecrireDateHeure(date)+ " - " + analyse.getName();
    }
}
