package com.pouillos.monpilulier.entities;

import java.util.Date;

public class OrdoExamen {

    private Examen examen;
    private String detail;
    private Ordonnance ordonnance;
    private Cabinet cabinet;
    private Date date;

    public OrdoExamen() {
    }

    public OrdoExamen(Examen examen, String detail, Ordonnance ordonnance, Cabinet cabinet, Date date) {
        this.examen = examen;
        this.detail = detail;
        this.ordonnance = ordonnance;
        this.cabinet = cabinet;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "OrdoExamen{" +
                ", examen=" + examen +
                ", detail='" + detail + '\'' +
                ", ordonnance=" + ordonnance +
                ", cabinet=" + cabinet +
                ", date=" + date +
                '}';
    }
}
