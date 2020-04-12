package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class RdvOfficiel extends SugarRecord implements Serializable, Comparable<RdvOfficiel> {

    private String note;
    private Utilisateur utilisateur;
    private MedecinOfficiel medecinOfficiel;
    private Date date;

    public RdvOfficiel() {
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

    public MedecinOfficiel getMedecinOfficiel() {
        return medecinOfficiel;
    }

    public void setMedecinOfficiel(MedecinOfficiel medecinOfficiel) {
        this.medecinOfficiel = medecinOfficiel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(RdvOfficiel o) {
        return this.getId().compareTo(o.getId());
    }


}
