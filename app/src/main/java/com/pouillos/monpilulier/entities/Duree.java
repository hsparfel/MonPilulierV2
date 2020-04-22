package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class Duree extends SugarRecord implements Serializable, Comparable<Duree>  {

private String name;

    public Duree() {
    }

    public Duree(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public int compareTo(Duree o) {
        return this.getId().compareTo(o.getId());
    }


}
