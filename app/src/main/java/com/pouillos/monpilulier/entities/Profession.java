package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class Profession extends SugarRecord implements Serializable, Comparable<Profession> {

    @NotNull
    private String name;

    public Profession() {
    }

    public Profession(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Profession o) {
        return this.name.compareTo(o.name);
    }

}
