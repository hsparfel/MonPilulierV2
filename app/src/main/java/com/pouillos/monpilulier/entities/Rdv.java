package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.activities.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class Rdv extends SugarRecord implements Serializable, Comparable<Rdv> {

    private String note;
    private Utilisateur utilisateur;
    private Contact contact;
    private Date date;

    public Rdv() {
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

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Rdv o) {
        return this.getDate().compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return DateUtils.ecrireDateHeure(date) + " - " + contact.getNom();
    }
}
