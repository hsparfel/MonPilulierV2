package com.pouillos.monpilulier.activities.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtils {


    public static String ecrireDateLettre(Date date) {
        Map<Integer, String> mapJours = new HashMap<>();
        mapJours.put(1,"Dimanche");
        mapJours.put(2,"Lundi");
        mapJours.put(3,"Mardi");
        mapJours.put(4,"Mercredi");
        mapJours.put(5,"Jeudi");
        mapJours.put(6,"Vendredi");
        mapJours.put(7,"Samedi");

        Map<Integer, String> mapMois = new HashMap<>();
        mapMois.put(0,"Janvier");
        mapMois.put(1,"Fevrier");
        mapMois.put(2,"Mars");
        mapMois.put(3,"Avril");
        mapMois.put(4,"Mai");
        mapMois.put(5,"Juin");
        mapMois.put(6,"Juillet");
        mapMois.put(7,"Aout");
        mapMois.put(8,"Septembre");
        mapMois.put(9,"Octobre");
        mapMois.put(10,"Novembre");
        mapMois.put(11,"Decembre");

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int month = c.get(Calendar.MONTH);


        String dateMaj = "";
        dateMaj += mapJours.get(dayOfWeek)+" ";
        dateMaj += dayOfWeek+" ";
        dateMaj += mapMois.get(month)+" ";
        dateMaj += date.getYear()+1900;

        return dateMaj;
    }

    public static String recupJour(Date date) {
        String pattern = "dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }

    public static String recupMois(Date date) {
        String pattern = "MM";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }

    public static String recupAnnee(Date date) {
        String pattern = "yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }



    public static String ecrireDate(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }

    public static String ecrireAnnee(Date date) {
        String pattern = "yyyy";
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

    public static Date ajouterSeconde(Date date, int nbSeconde) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND,nbSeconde);
        Date dateCalculee = calendar.getTime();
        return dateCalculee;
    }

    public static Date ajouterMinute(Date date, int nbMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,nbMinute);
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

    public static Date ajouterJour(Date date, int nbJours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK,nbJours);
        Date dateCalculee = calendar.getTime();
        return dateCalculee;
    }


    public static Date ajouterJourArrondi(Date date, int nbJours, int heure) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_WEEK,nbJours);
        calendar.set(Calendar.HOUR_OF_DAY, heure);
        calendar.set(Calendar.MINUTE,0);
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
