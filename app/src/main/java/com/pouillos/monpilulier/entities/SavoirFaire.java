package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class SavoirFaire extends SugarRecord implements Serializable, Comparable<SavoirFaire> {

    @NotNull
    private String name;

    public SavoirFaire() {
    }

    public SavoirFaire(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(SavoirFaire o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
