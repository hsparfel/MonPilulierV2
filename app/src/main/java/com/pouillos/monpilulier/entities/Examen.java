package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class Examen extends SugarRecord implements Serializable, Comparable<Examen> {

private String name;

    public Examen() {
    }

    public Examen(String name) {
        this.name = name;
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Examen o) {
        return this.name.compareTo(o.name);
    }


}
