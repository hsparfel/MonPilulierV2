package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class Analyse extends SugarRecord implements Serializable, Comparable<Analyse> {

private String name;

    public Analyse() {
    }

    public Analyse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Analyse o) {
        return this.name.compareTo(o.name);
    }

}
