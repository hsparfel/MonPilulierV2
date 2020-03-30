package com.pouillos.monpilulier.entities;

import com.orm.SugarRecord;
import com.pouillos.monpilulier.interfaces.AfficherDetail;

import java.io.Serializable;
import java.util.Date;

public class OrdoPrescription extends SugarRecord implements Serializable, Comparable<OrdoPrescription>, AfficherDetail {

    private String detail;
    private Ordonnance ordonnance;
    private Medicament medicament;
    private float nbDose;
    private Dose dose;
    private int nbFrequence;
    private Duree frequence;
    private int nbDuree;
    private Duree duree;
    private Date dateDebut;
    private Date dateFin;
    private boolean matin;
    private boolean midi;
    private boolean soir;
    private boolean repasAvant;
    private boolean repasPendant;
    private boolean repasApres;

    public OrdoPrescription() {
    }

    public OrdoPrescription(String detail, Ordonnance ordonnance, Medicament medicament, float nbDose, Dose dose, int nbFrequence, Duree frequence, int nbDuree, Duree duree, Date dateDebut, Date dateFin, boolean matin, boolean midi, boolean soir, boolean repasAvant, boolean repasPendant, boolean repasApres) {
        this.detail = detail;
        this.ordonnance = ordonnance;
        this.medicament = medicament;
        this.nbDose = nbDose;
        this.dose = dose;
        this.nbFrequence = nbFrequence;
        this.frequence = frequence;
        this.nbDuree = nbDuree;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.matin = matin;
        this.midi = midi;
        this.soir = soir;
        this.repasAvant = repasAvant;
        this.repasPendant = repasPendant;
        this.repasApres = repasApres;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public float getNbDose() {
        return nbDose;
    }

    public void setNbDose(float nbDose) {
        this.nbDose = nbDose;
    }

    public Dose getDose() {
        return dose;
    }

    public void setDose(Dose dose) {
        this.dose = dose;
    }

    public int getNbFrequence() {
        return nbFrequence;
    }

    public void setNbFrequence(int nbFrequence) {
        this.nbFrequence = nbFrequence;
    }

    public Duree getFrequence() {
        return frequence;
    }

    public void setFrequence(Duree frequence) {
        this.frequence = frequence;
    }

    public int getNbDuree() {
        return nbDuree;
    }

    public void setNbDuree(int nbDuree) {
        this.nbDuree = nbDuree;
    }

    public Duree getDuree() {
        return duree;
    }

    public void setDuree(Duree duree) {
        this.duree = duree;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isMatin() {
        return matin;
    }

    public void setMatin(boolean matin) {
        this.matin = matin;
    }

    public boolean isMidi() {
        return midi;
    }

    public void setMidi(boolean midi) {
        this.midi = midi;
    }

    public boolean isSoir() {
        return soir;
    }

    public void setSoir(boolean soir) {
        this.soir = soir;
    }

    public boolean isRepasAvant() {
        return repasAvant;
    }

    public void setRepasAvant(boolean repasAvant) {
        this.repasAvant = repasAvant;
    }

    public boolean isRepasPendant() {
        return repasPendant;
    }

    public void setRepasPendant(boolean repasPendant) {
        this.repasPendant = repasPendant;
    }

    public boolean isRepasApres() {
        return repasApres;
    }

    public void setRepasApres(boolean repasApres) {
        this.repasApres = repasApres;
    }

    @Override
    public int compareTo(OrdoPrescription o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String afficherTitre() {
        return medicament.getName();
    }

    @Override
    public String afficherDetail() {
        return detail;
    }
}
