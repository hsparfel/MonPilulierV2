package com.pouillos.monpilulier.enumeration;

public enum TypePhoto {
    //Objets directement construits
    Analyse("Resultat Analyse"),
    Examen("Examen");

    private String name = "";

    //Constructeur
    TypePhoto(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }

}
