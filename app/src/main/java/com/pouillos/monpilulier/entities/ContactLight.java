package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
@Table (name = "Contact")
public class ContactLight extends SugarRecord implements Serializable, Comparable<ContactLight> {

    private String idPP;
    private String codeCivilite;
    private String nom;
    private String prenom;
    //private Profession profession;
    //private SavoirFaire savoirFaire;
    private String raisonSocial;
    private String complement;
    private String adresse;
    private String cp;
    private String ville;
    private String telephone;
    private String fax;
    private String email;
    //private Departement departement;
    //private Region region;

    public ContactLight() {
    }

    public String getIdPP() {
        return idPP;
    }

    public void setIdPP(String idPP) {
        this.idPP = idPP;
    }

    public String getCodeCivilite() {
        return codeCivilite;
    }

    public void setCodeCivilite(String codeCivilite) {
        this.codeCivilite = codeCivilite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(ContactLight o) {
        return this.nom.compareTo(o.nom);
    }


}
