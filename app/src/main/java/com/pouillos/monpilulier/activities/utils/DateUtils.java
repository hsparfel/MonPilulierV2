package com.pouillos.monpilulier.activities.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String ecrireDate(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }

    public static String ecrireHeure(Date date) {
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }

    public static String ecrireDateHeure(Date date) {
        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }

    public static Date ajouterJour(Date date, int nbJours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK,nbJours);
        Date dateCalculee = calendar.getTime();
        return dateCalculee;
    }

    public static Date ajouterHeure(Date date, int nbHeures) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,nbHeures);
        Date dateCalculee = calendar.getTime();
        return dateCalculee;
    }

    public static Date ajouterSemaine(Date date, int nbSemaines) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_MONTH,nbSemaines);
        Date dateCalculee = calendar.getTime();
        return dateCalculee;
    }

    public static Date ajouterMois(Date date, int nbMois) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,nbMois);
        Date dateCalculee = calendar.getTime();
        return dateCalculee;
    }

    public static Date ajouterAnnee(Date date, int nbAnnees) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,nbAnnees);
        Date dateCalculee = calendar.getTime();
        return dateCalculee;
    }
}
