package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;

public class Dose extends SugarRecord implements Serializable, Comparable<Dose>  {

private String name;

    public Dose() {
    }

    public Dose(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public int compareTo(Dose o) {
        return this.getId().compareTo(o.getId());
    }


}
