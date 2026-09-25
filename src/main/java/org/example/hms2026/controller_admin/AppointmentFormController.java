package org.example.hms2026.controller_admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Appointment;

public class AppointmentFormController {
    @FXML private TextField txtId;
    @FXML private TextField txtPatientName;
    @FXML private TextField txtDoctorName;
    @FXML private TextField txtDateTime;
    @FXML private TextField txtStatus;

    private Appointment currentAppointment;
    private boolean isEditMode = false;

    public void setAppointmentForEdit(Appointment appointment) {
        if (appointment != null) {
            this.currentAppointment = appointment;
            this.isEditMode = true;

            txtId.setText(appointment.getId());
            txtPatientName.setText(appointment.getPatientName());
            txtDoctorName.setText(appointment.getDoctorName());
            txtDateTime.setText(appointment.getDateTime());
            txtStatus.setText(appointment.getStatus());

            txtId.setEditable(false);
        }
    }

    @FXML
    private void handleSave() {
        String id = txtId.getText().trim();
        String patientName = txtPatientName.getText().trim();
        String doctorName = txtDoctorName.getText().trim();
        String dateTime = txtDateTime.getText().trim();
        String status = txtStatus.getText().trim();

        if (id.isEmpty() || patientName.isEmpty() || doctorName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all required fields!");
            alert.showAndWait();
            return;
        }

        if (isEditMode) {
            currentAppointment.setId(id);
            currentAppointment.setPatientName(patientName);
            currentAppointment.setDoctorName(doctorName);
            currentAppointment.setDateTime(dateTime);
            currentAppointment.setStatus(status);
        } else {
            Appointment newAppointment = new Appointment(id, patientName, doctorName, dateTime, status);
            DataStore.getAppointments().add(newAppointment);
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