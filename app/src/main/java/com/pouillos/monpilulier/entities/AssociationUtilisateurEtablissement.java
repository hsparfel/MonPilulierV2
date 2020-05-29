package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;


public class AssociationUtilisateurEtablissement extends SugarRecord implements Serializable, Comparable<AssociationUtilisateurEtablissement>{

    private Utilisateur utilisateur;
    private Etablissement etablissement;

    public AssociationUtilisateurEtablissement() {
    }

    public AssociationUtilisateurEtablissement(Utilisateur utilisateur, Etablissement etablissement) {
        this.utilisateur = utilisateur;
        this.etablissement = etablissement;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        etablissement = etablissement;
    }

    @Override
    public int compareTo(AssociationUtilisateurEtablissement o) {
        return this.getId().compareTo(o.getId());
    }

    public boolean isExistante() {
        boolean bool=false;
        List<AssociationUtilisateurEtablissement> list = AssociationUtilisateurEtablissement.find(AssociationUtilisateurEtablissement.class,"utilisateur = ? and etablissement = ?", this.utilisateur.getId().toString(), this.etablissement.getId().toString());
        if (list.size() > 0) {
            bool = true;
        }
        return bool;
    }
}
