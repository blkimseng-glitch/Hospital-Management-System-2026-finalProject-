package org.example.hms2026.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hms2026.model.Appointment;
import org.example.hms2026.model.Doctor;
import org.example.hms2026.model.Patient;
import org.example.hms2026.model.Medicine;
import org.example.hms2026.model.Billing;
import org.example.hms2026.model.User;

public class DataStore {


    private static final ObservableList<User> users = FXCollections.observableArrayList(
            new User("admin", "admin123", "ADMIN"),

            new User("dr.vicheka", "doc123", "DOCTOR"),
            new User("dr.piseth", "doc123", "DOCTOR"),

            new User("dara.sok", "pat123", "PATIENT"),
            new User("chan.thy", "pat123", "PATIENT")
    );

    private static final ObservableList<Patient> patients = FXCollections.observableArrayList(
            new Patient("P001", "Dara Sok", "dara@email.com", "012345678", 25, "Male", "O", "Fever"),
            new Patient("P002", "Chan Thy", "thy@email.com", "098765432", 30, "Female", "A", "Headache")
    );

    private static final ObservableList<Doctor> doctors = FXCollections.observableArrayList(
            new Doctor("D001", "Dr. Vicheka", "Cardiology", "011223344", "vicheka@hospital.com"),
            new Doctor("D002", "Dr. Piseth", "Neurology", "099887766", "piseth@hospital.com")
    );

    private static final ObservableList<Appointment> appointments = FXCollections.observableArrayList(
            new Appointment("A001", "Dara Sok", "Dr. Vicheka", "2026-06-10 10:00 AM", "Confirmed"),
            new Appointment("A002", "Chan Thy", "Dr. Piseth", "2026-06-11 02:30 PM", "Pending")
    );

    private static final ObservableList<Medicine> medicines = FXCollections.observableArrayList(
            new Medicine("M001", "Paracetamol 500mg", "Painkiller", 150, 0.50),
            new Medicine("M002", "Amoxicillin 250mg", "Antibiotic", 80, 1.20),
            new Medicine("M003", "Ibuprofen 400mg", "Anti-inflammatory", 200, 0.80),
            new Medicine("M004", "Omeprazole 20mg", "Antacid", 60, 1.50)
    );

    private static final ObservableList<Billing> billings = FXCollections.observableArrayList(
            new Billing(
                    "B001",
                    "John Doe",
                    "Dr. Vicheka",
                    "Common Cold",
                    "Paracetamol (2x)",
                    "Rest for 3 days",
                    2.20,
                    "Unpaid"
            )
    );

    // Getters ទាំងអស់
    public static ObservableList<User> getUsers() { return users; }
    public static ObservableList<Billing> getBillings() { return billings; }
    public static ObservableList<Patient> getPatients() { return patients; }
    public static ObservableList<Doctor> getDoctors() { return doctors; }
    public static ObservableList<Appointment> getAppointments() { return appointments; }
    public static ObservableList<Medicine> getMedicines() { return medicines; }
}