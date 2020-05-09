package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;


public class AssociationFormeDose extends SugarRecord implements Serializable, Comparable<AssociationFormeDose>{

    private FormePharmaceutique formePharmaceutique;
    private Dose dose;

    public AssociationFormeDose() {
    }

    public AssociationFormeDose(FormePharmaceutique formePharmaceutique, Dose dose) {
        this.formePharmaceutique = formePharmaceutique;
        this.dose = dose;
    }

    public AssociationFormeDose(String formePharmaceutique, String dose) {
        this.formePharmaceutique = FormePharmaceutique.find(FormePharmaceutique.class, "name = ?",formePharmaceutique).get(0);
        this.dose = Dose.find(Dose.class, "name = ?",dose).get(0);
    }

    public FormePharmaceutique getFormePharmaceutique() {
        return formePharmaceutique;
    }

    public void setFormePharmaceutique(FormePharmaceutique formePharmaceutique) {
        this.formePharmaceutique = formePharmaceutique;
    }

    public Dose getDose() {
        return dose;
    }

    public void setDose(Dose dose) {
        this.dose = dose;
    }

    @Override
    public int compareTo(AssociationFormeDose o) {
        return this.getId().compareTo(o.getId());
    }

    public boolean isExistante() {
        boolean bool=false;
        List<AssociationFormeDose> list = AssociationFormeDose.find(AssociationFormeDose.class,"formePharmaceutique = ? and dose = ?", this.formePharmaceutique.getId().toString(), this.dose.getId().toString());
        if (list.size() > 0) {
            bool = true;
        }
        return bool;
    }
}
