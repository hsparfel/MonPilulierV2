package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.enumeration.QuantiteDose;

import java.io.Serializable;
import java.util.Date;

public class Prise extends SugarRecord implements Serializable, Comparable<Prise>  {

    Prescription prescription;
    QuantiteDose qteDose;
    Dose dose;
    Date date;



    public Prise() {
    }






    @Override
    public int compareTo(Prise o) {
        return this.getId().compareTo(o.getId());
    }


}
