package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.enumeration.Duree;
import com.pouillos.monpilulier.enumeration.Frequence;

import java.io.Serializable;
import java.util.Date;

public class Ordonnance extends SugarRecord implements Serializable, Comparable<Ordonnance>  {

    Utilisateur utilisateur;
    Contact contact;
    RdvContact rdvContact;
    Date date;
    boolean validated = false;

    public Ordonnance() {
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public RdvContact getRdvContact() {
        return rdvContact;
    }

    public void setRdvContact(RdvContact rdvContact) {
        this.rdvContact = rdvContact;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    @Override
    public int compareTo(Ordonnance o) {
        return this.getId().compareTo(o.getId());
    }


}
