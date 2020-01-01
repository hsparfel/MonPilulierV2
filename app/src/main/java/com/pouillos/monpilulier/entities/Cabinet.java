package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;
@Table
public class Cabinet extends SugarRecord implements Serializable, Comparable<Cabinet>{


private String name;
private String detail;
private String adresse;
private String cp;
private String ville;

    public Cabinet() {
    }

    public Cabinet(String name, String detail, String adresse, String cp, String ville) {
        this.name = name;
        this.detail = detail;
        this.adresse = adresse;
        this.cp = cp;
        this.ville = ville;
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

    @Override
    public String toString() {
        return "Cabinet{" +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", adresse='" + adresse + '\'' +
                ", cp='" + cp + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }

    @Override
    public int compareTo(Cabinet o) {
        return this.name.compareTo(o.name);
    }
}
