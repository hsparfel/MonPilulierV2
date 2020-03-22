package com.pouillos.monpilulier.activities.utils;

import com.pouillos.monpilulier.entities.Duree;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static String ecrireDate(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }

    public static Date calculerDateFin(Date dateDebut, int nbDuree, Duree duree){
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(dateDebut);
        switch (duree.getName()) {
            case "an":
                gregorianCalendar.add(GregorianCalendar.YEAR,nbDuree);
                break;
            case "mois":
                gregorianCalendar.add(GregorianCalendar.MONTH,nbDuree);
                break;
            case "semaine":
                gregorianCalendar.add(GregorianCalendar.WEEK_OF_MONTH,nbDuree);
                break;
            case "jour":
                gregorianCalendar.add(GregorianCalendar.DAY_OF_WEEK,nbDuree);
                break;
        }
        Date dateFin = gregorianCalendar.getTime();
        return dateFin;
    }
}
