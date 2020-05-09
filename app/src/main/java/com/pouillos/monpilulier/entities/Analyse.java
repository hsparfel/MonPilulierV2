package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return name;
    }
}
