package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class OrdoExamen extends SugarRecord implements Serializable, Comparable<OrdoExamen>, AfficherDetail {

    private Examen examen;
    private String detail;
    private Ordonnance ordonnance;
    private Cabinet cabinet;

    public OrdoExamen() {
    }

    public OrdoExamen(Examen examen, String detail, Ordonnance ordonnance, Cabinet cabinet) {
        this.examen = examen;
        this.detail = detail;
        this.ordonnance = ordonnance;
        this.cabinet = cabinet;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
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
    public int compareTo(OrdoExamen o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String afficherTitre() {
        return examen.getName();
    }

    @Override
    public String afficherDetail() {
        return detail;
    }
}
