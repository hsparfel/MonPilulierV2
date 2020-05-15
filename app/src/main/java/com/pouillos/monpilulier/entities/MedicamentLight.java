package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

@Table(name = "Medicament")
public class MedicamentLight extends SugarRecord implements Serializable, Comparable<MedicamentLight> {

private Long codeCIS;
private String denomination;

    public MedicamentLight() {
    }

    public Long getCodeCIS() {
        return codeCIS;
    }

    public void setCodeCIS(Long codeCIS) {
        this.codeCIS = codeCIS;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    @Override
    public int compareTo(MedicamentLight o) {
        return this.denomination.compareTo(o.denomination);
    }

    @Override
    public String toString() {
        return denomination;
    }
}
