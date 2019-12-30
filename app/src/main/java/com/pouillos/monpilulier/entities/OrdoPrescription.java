package com.pouillos.monpilulier.entities;

import java.util.Date;

public class OrdoPrescription {

    private int id;
    private String detail;
    private Date creationDate;
    private Ordonnance ordonnance;
    private Date date;
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


    public OrdoPrescription() {
    }

    public OrdoPrescription(String detail, Ordonnance ordonnance, Date date, Medicament medicament, float nbDose, Dose dose, int nbFrequence, Duree frequence, int nbDuree, Duree duree, Date dateDebut, Date dateFin, boolean matin, boolean midi, boolean soir) {
        this.detail = detail;
        this.ordonnance = ordonnance;
        this.date = date;
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
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public boolean getMatin() {
        return matin;
    }

    public void setMatin(boolean matin) {
        this.matin = matin;
    }

    public boolean getMidi() {
        return midi;
    }

    public void setMidi(boolean midi) {
        this.midi = midi;
    }

    public boolean getSoir() {
        return soir;
    }

    public void setSoir(boolean soir) {
        this.soir = soir;
    }

    @Override
    public String toString() {
        return "OrdoPrescription{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                ", creationDate=" + creationDate +
                ", ordonnance=" + ordonnance +
                ", date=" + date +
                ", medicament=" + medicament +
                ", nbDose=" + nbDose +
                ", dose=" + dose +
                ", nbFrequence=" + nbFrequence +
                ", frequence=" + frequence +
                ", nbDuree=" + nbDuree +
                ", duree=" + duree +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", matin=" + matin +
                ", midi=" + midi +
                ", soir=" + soir +
                '}';
    }
}
