package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;


public class OrdoAnalyse extends SugarRecord implements Serializable, Comparable<OrdoAnalyse>, AfficherDetail {

    private Analyse analyse;
    private String detail;
    private Ordonnance ordonnance;
    private Cabinet cabinet;

    public OrdoAnalyse() {
    }

    public OrdoAnalyse(Analyse analyse, String detail, Ordonnance ordonnance, Cabinet cabinet) {
        this.analyse = analyse;
        this.detail = detail;
        this.ordonnance = ordonnance;
        this.cabinet = cabinet;
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


    @Override
    public int compareTo(OrdoAnalyse o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String afficherTitre() {
        return analyse.getName();
    }

    @Override
    public String afficherDetail() {
        return detail;
    }
}
