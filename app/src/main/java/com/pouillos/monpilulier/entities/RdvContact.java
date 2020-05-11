package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.activities.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class RdvContact extends Rdv {

    private Contact contact;

    public RdvContact() {
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return DateUtils.ecrireDateHeure(date) + " - " + contact.getNom();
    }
}
