package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

@Table (name = "Etablissement")
public class EtablissementVille extends SugarRecord implements Serializable, Comparable<EtablissementVille> {

    private String ville;

    public EtablissementVille() {
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public int compareTo(EtablissementVille o) {
        return this.ville.compareTo(o.ville);
    }


}
