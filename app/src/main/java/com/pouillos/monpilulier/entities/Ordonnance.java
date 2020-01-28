package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class Ordonnance extends SugarRecord implements Serializable, Comparable<Ordonnance> , AfficherDetail {

    private String detail;
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


    @Override
    public int compareTo(Ordonnance o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String afficherTitre() {
        String reponse = medecin.getName()+" - "+ DateUtils.ecrireDate(date);
        return reponse;
    }

    @Override
    public String afficherDetail() {
        return null;
    }
}
