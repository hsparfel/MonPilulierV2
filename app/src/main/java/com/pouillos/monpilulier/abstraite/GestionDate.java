package com.pouillos.monpilulier.abstraite;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface GestionDate {

    public default String EcrireDate(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }




}
