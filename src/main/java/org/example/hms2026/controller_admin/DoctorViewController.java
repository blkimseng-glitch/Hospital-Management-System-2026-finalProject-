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
import org.example.hms2026.model.Doctor;

import java.io.IOException;

public class DoctorViewController {

    @FXML private TableView<Doctor> tblDoctors;
    @FXML private TableColumn<Doctor, String> colDoctorId;
    @FXML private TableColumn<Doctor, String> colDoctorName;
    @FXML private TableColumn<Doctor, String> colDoctorSpecialization;
    @FXML private TableColumn<Doctor, String> colDoctorPhone;
    @FXML private TableColumn<Doctor, String> colDoctorEmail;

    @FXML
    public void initialize() {
        if (colDoctorId != null) colDoctorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colDoctorName != null) colDoctorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (colDoctorSpecialization != null) colDoctorSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        if (colDoctorPhone != null) colDoctorPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        if (colDoctorEmail != null) colDoctorEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        if (tblDoctors != null && DataStore.getDoctors() != null) {
            tblDoctors.setItems(DataStore.getDoctors());
        }
    }

    @FXML
    private void handleAddDoctor() {
        try {
            openDoctorModal(null);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open Add Doctor form: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditDoctor() {
        Doctor selected = tblDoctors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a doctor to edit!");
            return;
        }
        try {
            openDoctorModal(selected);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open Edit Doctor form: " + e.getMessage());
        }
    }

    private void openDoctorModal(Doctor doctor) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/doctor-form.fxml"));
        Parent parent = loader.load();

        DoctorFormController controller = loader.getController();
        if (controller != null) {
            controller.setDoctorForEdit(doctor);
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(doctor == null ? "Add Doctor" : "Edit Doctor");
        stage.setScene(new Scene(parent));
        stage.showAndWait();

        if (tblDoctors != null) {
            tblDoctors.refresh();
        }
    }

    @FXML
    private void handleDeleteDoctor() {
        Doctor selected = tblDoctors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a doctor to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete doctor: " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                DataStore.getDoctors().remove(selected);
                if (tblDoctors != null) {
                    tblDoctors.refresh();
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor deleted successfully!");
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