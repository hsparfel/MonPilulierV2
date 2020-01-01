package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;
@Table
public class Dose extends SugarRecord implements Serializable, Comparable<Dose>{

private String name;
private String detail;

    public Dose() {
    }

    public Dose(String name, String detail) {
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
    public String toString() {
        return "Dose{" +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    @Override
    public int compareTo(Dose o) {
        return this.name.compareTo(o.name);
    }
}
