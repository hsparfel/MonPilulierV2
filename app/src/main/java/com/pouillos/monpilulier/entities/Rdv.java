package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class Rdv extends SugarRecord implements Serializable, Comparable<Rdv>, AfficherDetail {

    private String detail;
    private Utilisateur utilisateur;
    private Medecin medecin;
    private Analyse analyse;
    private Examen examen;
    private Cabinet cabinet;
    private Date date;

    public Rdv() {
    }

    public Rdv(String detail, Utilisateur utilisateur, Medecin medecin, Analyse analyse, Examen examen, Cabinet cabinet, Date date) {
        this.detail = detail;
        this.utilisateur = utilisateur;
        this.medecin = medecin;
        this.analyse = analyse;
        this.examen = examen;
        this.cabinet = cabinet;
        this.date = date;
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

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Rdv o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String afficherTitre() {
        String reponse = DateUtils.ecrireDate(date);
        if(medecin!=null) {
            reponse += " - "+medecin.getName();
        }
        if(cabinet!=null) {
            reponse += " - "+cabinet.getName();
        }
        if(analyse!=null) {
            reponse += " - "+analyse.getName();
        }
        if(examen!=null) {
            reponse += " - "+examen.getName();
        }






        return reponse;
    }

    @Override
    public String afficherDetail() {
        return null;
    }
}
