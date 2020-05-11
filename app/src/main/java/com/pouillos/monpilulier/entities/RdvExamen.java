package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.activities.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class RdvExamen extends Rdv {

    private Examen examen;

    public RdvExamen() {
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    @Override
    public String toString() {
        return DateUtils.ecrireDateHeure(date)+ " - " + examen.getName();
    }
}
