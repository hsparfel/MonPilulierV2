package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;
@Table
public class Cabinet extends SugarRecord implements Serializable, Comparable<Cabinet>{

private Long id;
private String name;
private String detail;
private Date creationDate;
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
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", creationDate=" + creationDate +
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
