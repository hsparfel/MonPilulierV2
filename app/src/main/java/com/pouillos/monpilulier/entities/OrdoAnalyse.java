package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.abstraite.GestionDate;

import java.io.Serializable;
import java.util.Date;

@Table
public class OrdoAnalyse extends SugarRecord implements Serializable, Comparable<OrdoAnalyse>, GestionDate {

    private Analyse analyse;
    private String detail;
    private Ordonnance ordonnance;
    private Cabinet cabinet;
    private Date date;

    public OrdoAnalyse() {
    }

    public OrdoAnalyse(Analyse analyse, String detail, Ordonnance ordonnance, Cabinet cabinet, Date date) {
        this.analyse = analyse;
        this.detail = detail;
        this.ordonnance = ordonnance;
        this.cabinet = cabinet;
        this.date = date;
    }

    public Analyse getAnalyse() {
        return analyse;
    }

    public void setAnalyse(Analyse analyse) {
        this.analyse = analyse;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "OrdoAnalyse{" +
                ", analyse=" + analyse +
                ", detail='" + detail + '\'' +
                ", ordonnance=" + ordonnance +
                ", cabinet=" + cabinet +
                ", date=" + date +
                '}';
    }

    public String getName() {
        return  "analyse=" + analyse.getName() +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(OrdoAnalyse o) {
        return this.getId().compareTo(o.getId());
    }
}
