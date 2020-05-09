package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return name;
    }
}
