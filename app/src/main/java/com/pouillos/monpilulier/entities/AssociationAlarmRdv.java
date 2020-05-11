package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;

import java.io.Serializable;


public class AssociationAlarmRdv extends SugarRecord implements Serializable, Comparable<AssociationAlarmRdv>{

    private AlarmRdv alarmRdv;
    private RdvContact rdvContact;
    private RdvAnalyse rdvAnalyse;
    private RdvExamen rdvExamen;

    public AssociationAlarmRdv() {
    }

    public AlarmRdv getAlarmRdv() {
        return alarmRdv;
    }

    public void setAlarmRdv(AlarmRdv alarmRdv) {
        this.alarmRdv = alarmRdv;
    }

    public RdvContact getRdvContact() {
        return rdvContact;
    }

    public void setRdvContact(RdvContact rdvContact) {
        this.rdvContact = rdvContact;
    }

    public RdvAnalyse getRdvAnalyse() {
        return rdvAnalyse;
    }

    public void setRdvAnalyse(RdvAnalyse rdvAnalyse) {
        this.rdvAnalyse = rdvAnalyse;
    }

    public RdvExamen getRdvExamen() {
        return rdvExamen;
    }

    public void setRdvExamen(RdvExamen rdvExamen) {
        this.rdvExamen = rdvExamen;
    }

    @Override
    public int compareTo(AssociationAlarmRdv o) {
        return this.getId().compareTo(o.getId());
    }


}
