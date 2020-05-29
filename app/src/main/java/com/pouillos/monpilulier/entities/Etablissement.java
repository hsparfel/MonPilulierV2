package com.pouillos.monpilulier.entities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class Etablissement extends SugarRecord implements Serializable, Comparable<Etablissement> {

    private String numeroFinessET;
    private String raisonSocial;
    private String adresse;
    private String cp;
    private String ville;
    private String telephone;
    private String fax;
    private Departement departement;
    private Region region;
    private double latitude;
    private double longitude;
    private TypeEtablissement typeEtablissement;
    @Ignore
    private double distance;

    public Etablissement() {
    }

    public String getNumeroFinessET() {
        return numeroFinessET;
    }

    public void setNumeroFinessET(String numeroFinessET) {
        this.numeroFinessET = numeroFinessET;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
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

    public TypeEtablissement getTypeEtablissement() {
        return typeEtablissement;
    }

    public void setTypeEtablissement(TypeEtablissement typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return raisonSocial;
    }

    /*@Override
    public int compareTo(Etablissement o) {
        return this.raisonSocial.compareTo(o.raisonSocial);
    }*/

    @Override
    public int compareTo(Etablissement o) {
        return Double.compare(this.distance,o.distance);
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
