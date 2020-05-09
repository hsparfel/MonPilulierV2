package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;


public class AssociationUtilisateurContact extends SugarRecord implements Serializable, Comparable<AssociationUtilisateurContact>{

    private Utilisateur utilisateur;
    private Contact contact;

    public AssociationUtilisateurContact() {
    }

    public AssociationUtilisateurContact(Utilisateur utilisateur, Contact contact) {
        this.utilisateur = utilisateur;
        this.contact = contact;
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
        contact = contact;
    }

    @Override
    public int compareTo(AssociationUtilisateurContact o) {
        return this.getId().compareTo(o.getId());
    }

    public boolean isExistante() {
        boolean bool=false;
        List<AssociationUtilisateurContact> list = AssociationUtilisateurContact.find(AssociationUtilisateurContact.class,"utilisateur = ? and contact = ?", this.utilisateur.getId().toString(), this.contact.getId().toString());
        if (list.size() > 0) {
            bool = true;
        }
        return bool;
    }
}
