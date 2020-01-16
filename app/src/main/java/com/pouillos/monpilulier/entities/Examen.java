package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class Examen extends SugarRecord implements Serializable, Comparable<Examen>, AfficherDetail {

private String name;
private String detail;

    public Examen() {
    }

    public Examen(String name, String detail) {
        this.name = name;
        this.detail = detail;
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



    @Override
    public int compareTo(Examen o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String afficherTitre() {
        return name;
    }

    @Override
    public String afficherDetail() {
        return detail;
    }
}
