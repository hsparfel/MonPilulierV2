package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.io.Serializable;

public class FormePharmaceutique extends SugarRecord implements Serializable, Comparable<FormePharmaceutique> {

    @NotNull
    private String name;

    public FormePharmaceutique() {
    }

    public FormePharmaceutique(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(FormePharmaceutique o) {
        return this.name.compareTo(o.name);
    }

}
