package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.activities.utils.DateUtils;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class Prise extends SugarRecord implements Serializable, Comparable<Ordonnance> , AfficherDetail {

    private String name;
    private String detail;
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
    public String afficherTitre() {
        String reponse = prescription.getMedicament().getName()+" - "+ DateUtils.ecrireDateHeure(date);
        return reponse;
    }

    @Override
    public String afficherDetail() {
        return null;
    }

    @Override
    public int compareTo(Ordonnance o) {
        return this.getId().compareTo(o.getId());
    }
}
