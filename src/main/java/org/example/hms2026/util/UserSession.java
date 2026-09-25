package org.example.hms2026.util;

import org.example.hms2026.model.Doctor;
import org.example.hms2026.model.Patient;

public class UserSession {
    private static UserSession instance;
    private String username;
    private String role;
    private Patient currentPatient;
    private Doctor currentDoctor; // បន្ថែម Doctor សម្រាប់เก็บ Session វេជ្ជបណ្ឌិត

    private UserSession(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public static UserSession getInstance(String username, String role) {
        if (instance == null) {
            instance = new UserSession(username, role);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void clearSession() {
        instance = null;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }

    public Patient getCurrentPatient() { return currentPatient; }
    public void setCurrentPatient(Patient currentPatient) { this.currentPatient = currentPatient; }

    public Doctor getCurrentDoctor() { return currentDoctor; }
    public void setCurrentDoctor(Doctor currentDoctor) { this.currentDoctor = currentDoctor; }
}