package org.example.hms2026.controller_admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Appointment;

import java.io.IOException;

public class AppointmentViewController {

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private TableColumn<Appointment, String> colAppointmentId;
    @FXML private TableColumn<Appointment, String> colPatientName;
    @FXML private TableColumn<Appointment, String> colDoctorName;
    @FXML private TableColumn<Appointment, String> colAppointmentDate;
    @FXML private TableColumn<Appointment, String> colStatus;

    @FXML
    public void initialize() {
        if (colAppointmentId != null) colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colPatientName != null) colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        if (colDoctorName != null) colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        if (colAppointmentDate != null) colAppointmentDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tblAppointments != null && DataStore.getAppointments() != null) {
            tblAppointments.setItems(DataStore.getAppointments());
        }
    }

    @FXML
    private void handleAddAppointment() {
        try {
            openAppointmentModal(null);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open Add Appointment form: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditAppointment() {
        Appointment selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an appointment to edit!");
            return;
        }
        try {
            openAppointmentModal(selected);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open Edit Appointment form: " + e.getMessage());
        }
    }

    private void openAppointmentModal(Appointment appointment) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/appointment-form.fxml"));
        Parent parent = loader.load();

        AppointmentFormController controller = loader.getController();
        if (controller != null) {
            controller.setAppointmentForEdit(appointment);
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(appointment == null ? "Add Appointment" : "Edit Appointment");
        stage.setScene(new Scene(parent));
        stage.showAndWait();

        if (tblAppointments != null) {
            tblAppointments.refresh();
        }
    }

    @FXML
    private void handleDeleteAppointment() {
        Appointment selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an appointment to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete appointment ID: " + selected.getId() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                DataStore.getAppointments().remove(selected);
                if (tblAppointments != null) {
                    tblAppointments.refresh();
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment deleted successfully!");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}