package com.pouillos.monpilulier.enumeration;

public enum Frequence {
    //Objets directement construits
    WhenNeeded("En cas de besoin"),
    EveryDay("Chaque jour, X fois par jour"),
    EveryDayByHour("Chaque jour, toutes les X heures"),
    EveryXDays("Tous les X jours"),
    ChosenDays("Certains jours de la semaine"),
    Cycle("X jours de prise, Y jours sans prise");

    private String name = "";

    //Constructeur
    Frequence(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }

}
