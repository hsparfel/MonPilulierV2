package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;


public class AssociationOfficielle extends SugarRecord implements Serializable, Comparable<AssociationOfficielle>{

    private Utilisateur utilisateur;
    private MedecinOfficiel medecinOfficiel;

    public AssociationOfficielle() {
    }

    public AssociationOfficielle(Utilisateur utilisateur, MedecinOfficiel medecinOfficiel) {
        this.utilisateur = utilisateur;
        this.medecinOfficiel = medecinOfficiel;
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
        medecinOfficiel = medecinOfficiel;
    }

    @Override
    public int compareTo(AssociationOfficielle o) {
        return this.getId().compareTo(o.getId());
    }

    public boolean isExistante() {
        boolean bool=false;
        List<AssociationOfficielle> list = AssociationOfficielle.find(AssociationOfficielle.class,"utilisateur = ? and medecin_officiel = ?", this.utilisateur.getId().toString(), this.medecinOfficiel.getId().toString());
        if (list.size() > 0) {
            bool = true;
        }
        return bool;
    }
}
