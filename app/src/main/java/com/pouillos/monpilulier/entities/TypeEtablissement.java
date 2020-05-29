package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class TypeEtablissement extends SugarRecord implements Serializable, Comparable<TypeEtablissement> {

    @NotNull
    private String name;

    public TypeEtablissement() {
    }

    public TypeEtablissement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(TypeEtablissement o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
