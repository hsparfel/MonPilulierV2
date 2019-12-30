package com.pouillos.monpilulier.entities;

public class PatientMedecin {
    private Utilisateur patient;
    private Medecin medecin;

    public PatientMedecin(Utilisateur patient, Medecin medecin) {
        this.patient = patient;
        this.medecin = medecin;
    }

    public Utilisateur getPatient() {
        return patient;
    }

    public void setPatient(Utilisateur patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    @Override
    public String toString() {
        return "PatientMedecin{" +
                "patient=" + patient +
                ", medecin=" + medecin +
                '}';
    }
}
