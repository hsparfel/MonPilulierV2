package com.pouillos.monpilulier.entities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.orm.SugarRecord;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class Contact extends SugarRecord implements Serializable, Comparable<Contact> {

    private String idPP;
    private String codeCivilite;
    private String nom;
    private String prenom;
    private Profession profession;
    private SavoirFaire savoirFaire;
    private String raisonSocial;
    private String complement;
    private String adresse;
    private String cp;
    private String ville;
    private String telephone;
    private String fax;
    private String email;
    private Departement departement;
    private Region region;
    private double latitude;
    private double longitude;

    public Contact() {
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

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public SavoirFaire getSavoirFaire() {
        return savoirFaire;
    }

    public void setSavoirFaire(SavoirFaire savoirFaire) {
        this.savoirFaire = savoirFaire;
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
        if (!cp.equalsIgnoreCase("")) {
            this.departement = Departement.find(Departement.class,"numero = ?",cp.substring(0,2)).get(0);
        } else {
            this.departement = Departement.find(Departement.class,"numero = ?","XX").get(0);
        }
        this.region = this.departement.getRegion();
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

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        String affichage = "";
        if (codeCivilite != null) {
            affichage += codeCivilite + " ";
        }
        if (nom != null) {
            affichage += nom + " ";
        }
        if (prenom != null) {
            affichage += prenom;
        }
        if (savoirFaire != null) {
            affichage += " - " + savoirFaire;
        } else {
            if (profession != null) {
                affichage +=  " - " + profession;
            }
        }

        return affichage;
    }

    @Override
    public int compareTo(Contact o) {
        return this.nom.compareTo(o.nom);
    }

    public void enregisterCoordonnees(Context context) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(adresse+", "+cp+" "+ville+", FRANCE",1);
            if (address.size()>0) {
                Address location = address.get(0);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
