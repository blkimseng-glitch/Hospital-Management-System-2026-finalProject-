package org.example.hms2026.controller_admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Doctor;

public class DoctorFormController {
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtSpecialization;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;

    private Doctor currentDoctor;
    private boolean isEditMode = false;

    public void setDoctorForEdit(Doctor doctor) {
        if (doctor != null) {
            this.currentDoctor = doctor;
            this.isEditMode = true;

            txtId.setText(doctor.getId());
            txtName.setText(doctor.getName());
            txtSpecialization.setText(doctor.getSpecialization());
            txtPhone.setText(doctor.getPhone());
            txtEmail.setText(doctor.getEmail());

            txtId.setEditable(false);
        }
    }

    @FXML
    private void handleSave() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String specialization = txtSpecialization.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        if (id.isEmpty() || name.isEmpty() || specialization.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields!");
            alert.showAndWait();
            return;
        }

        if (isEditMode) {
            currentDoctor.setId(id);
            currentDoctor.setName(name);
            currentDoctor.setSpecialization(specialization);
            currentDoctor.setPhone(phone);
            currentDoctor.setEmail(email);
        } else {
            Doctor newDoctor = new Doctor(id, name, specialization, phone, email);
            DataStore.getDoctors().add(newDoctor);
        }

        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }
}