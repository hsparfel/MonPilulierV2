package com.pouillos.monpilulier.enumeration;

public enum Graphique {
    //Objets directement construits
    Weight("Poids","kg"),
    Size("Taille","cm"),
    Imc("Imc","");

    private String name = "";
    private String unite = "";

    //Constructeur
    Graphique(String name, String unite){
        this.name = name;
        this.unite = unite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String toString(){
        return name;
    }

}
