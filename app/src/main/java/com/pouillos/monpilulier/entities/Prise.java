package com.pouillos.monpilulier.entities;

import java.util.Date;

public class Prise {

    private int id;
    private String name;
    private String detail;
    private Date creationDate;
    private OrdoPrescription prescription;
    private Date date;
    private boolean effectue;

    public Prise() {
    }

    public Prise(String name, String detail, OrdoPrescription prescription, Date date, boolean effectue) {
        this.name = name;
        this.detail = detail;
        this.prescription = prescription;
        this.date = date;
        this.effectue = effectue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public OrdoPrescription getPrescription() {
        return prescription;
    }

    public void setPrescription(OrdoPrescription prescription) {
        this.prescription = prescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isEffectue() {
        return effectue;
    }

    public void setEffectue(boolean effectue) {
        this.effectue = effectue;
    }

    @Override
    public String toString() {
        return "Prise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", creationDate=" + creationDate +
                ", prescription=" + prescription +
                ", date=" + date +
                ", effectue=" + effectue +
                '}';
    }
}
