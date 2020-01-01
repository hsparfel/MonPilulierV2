package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;
@Table
public class Medecin extends SugarRecord implements Serializable, Comparable<Medecin>{

    private String name;
    private String detail;
    private Specialite specialite;
    private String telephone;
    private String email;

    public Medecin() {
    }

    public Medecin(String name, String detail, Specialite specialite, String telephone, String email) {
        this.name = name;
        this.detail = detail;
        this.specialite = specialite;
        this.telephone = telephone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Medecin{" +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", specialite=" + specialite +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int compareTo(Medecin o) {
        return this.name.compareTo(o.name);
    }
}
