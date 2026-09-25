package org.example.hms2026.controller_admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Patient;

public class PatientFormController {

    @FXML private Label lblFormTitle;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAge;
    @FXML private ComboBox<String> cmbGender;
    @FXML private ComboBox<String> cmbBloodGroup;
    @FXML private TextField txtDiagnosis;

    private Patient editingPatient = null;

    @FXML
    public void initialize() {
        cmbGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        cmbBloodGroup.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"));
    }

    public void setPatientForEdit(Patient patient) {
        this.editingPatient = patient;
        if (patient != null) {
            lblFormTitle.setText("Edit Patient Record");
            txtId.setText(patient.getId());
            txtId.setDisable(true);
            txtName.setText(patient.getName());
            txtEmail.setText(patient.getEmail());
            txtPhone.setText(patient.getPhone());
            txtAge.setText(String.valueOf(patient.getAge()));
            cmbGender.setValue(patient.getGender());
            cmbBloodGroup.setValue(patient.getBloodGroup());
            txtDiagnosis.setText(patient.getDiagnosis());
        }
    }

    @FXML
    private void handleSave() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String gender = cmbGender.getValue() != null ? cmbGender.getValue() : "N/A";
        String bloodGroup = cmbBloodGroup.getValue() != null ? cmbBloodGroup.getValue() : "N/A";
        String diagnosis = txtDiagnosis.getText().trim();

        if (id.isEmpty() || name.isEmpty()) return;

        int age = 0;
        try {
            age = Integer.parseInt(txtAge.getText().trim());
        } catch (NumberFormatException e) {
            age = 0;
        }

        if (editingPatient == null) {
            // Create New Patient - បញ្ជូន 8 Parameters ឱ្យត្រូវតាម Patient.java Model
            Patient newPatient = new Patient(id, name, email, phone, age, gender, bloodGroup, diagnosis);
            DataStore.getPatients().add(newPatient);
        } else {
            // Update Patient
            int index = DataStore.getPatients().indexOf(editingPatient);
            if (index >= 0) {
                DataStore.getPatients().set(index, new Patient(id, name, email, phone, age, gender, bloodGroup, diagnosis));
            }
        }
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }
}