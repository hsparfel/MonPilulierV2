package com.pouillos.monpilulier.enumeration;

public enum Duree {
    //Objets directement construits
    NoEnding("Pas de date de fin"),
    UntilDate("Jusqu'à une date"),
    DuringDays("Pendant X jours");

    private String name = "";

    //Constructeur
    Duree(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }

}
