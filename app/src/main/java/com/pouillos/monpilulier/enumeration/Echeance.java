package com.pouillos.monpilulier.enumeration;

public enum Echeance {
    //Objets directement construits
    OneHourAfter("dans une heure"),
    OneDayAfter("demain");

    private String name = "";

    //Constructeur
    Echeance(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }

}
