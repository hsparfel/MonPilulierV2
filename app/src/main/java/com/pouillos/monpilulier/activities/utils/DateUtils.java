package com.pouillos.monpilulier.activities.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String ecrireDate(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }

}
