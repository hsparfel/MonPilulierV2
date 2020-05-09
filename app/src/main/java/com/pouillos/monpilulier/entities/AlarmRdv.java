package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class AlarmRdv extends SugarRecord implements Serializable, Comparable<AlarmRdv> {

    private String classe;
    private Date date;
    private String dateString;
    private String detail;
    private String echeance;
    private int requestCode;

    public AlarmRdv() {
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getEcheance() {
        return echeance;
    }

    public void setEcheance(String echeance) {
        this.echeance = echeance;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "classe='" + classe + '\'' +
                ", date=" + date +
                ", dateString='" + dateString + '\'' +
                ", detail='" + detail + '\'' +
                ", echeance='" + echeance + '\'' +
                ", requestCode='" + requestCode + '\'' +
                '}';
    }


    @Override
    public int compareTo(AlarmRdv o) {
        return this.getId().compareTo(o.getId());
    }
}
