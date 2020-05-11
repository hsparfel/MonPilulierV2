package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.activities.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class RdvAnalyse extends Rdv {

    private Analyse analyse;

    public RdvAnalyse() {
    }

    public Analyse getAnalyse() {
        return analyse;
    }

    public void setAnalyse(Analyse analyse) {
        this.analyse = analyse;
    }

    @Override
    public String toString() {
        return DateUtils.ecrireDateHeure(date)+ " - " + analyse.getName();
    }
}
