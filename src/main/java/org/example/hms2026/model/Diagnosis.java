package org.example.hms2026.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Diagnosis {
    private final StringProperty patientName;
    private final StringProperty notes;
    private final StringProperty date;

    public Diagnosis(String patientName, String notes, String date) {
        this.patientName = new SimpleStringProperty(patientName);
        this.notes = new SimpleStringProperty(notes);
        this.date = new SimpleStringProperty(date);
    }

    public String getPatientName() { return patientName.get(); }
    public StringProperty patientNameProperty() { return patientName; }
    public void setPatientName(String patientName) { this.patientName.set(patientName); }

    public String getNotes() { return notes.get(); }
    public StringProperty notesProperty() { return notes; }
    public void setNotes(String notes) { this.notes.set(notes); }

    public String getDate() { return date.get(); }
    public StringProperty dateProperty() { return date; }
    public void setDate(String date) { this.date.set(date); }
}