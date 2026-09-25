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
import org.example.hms2026.model.Patient;

import java.io.IOException;

public class PatientViewController {

    @FXML private TableView<Patient> tblPatients;
    @FXML private TableColumn<Patient, String> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colEmail;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, String> colDiagnosis;

    @FXML
    public void initialize() {
        // Map Column ជាមួយ Model Property
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colName != null) colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (colEmail != null) colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        if (colPhone != null) colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        if (colDiagnosis != null) colDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));

        // Load ទិន្នន័យពី DataStore ចូល TableView ដោយស្វ័យប្រវត្តិ
        if (tblPatients != null && DataStore.getPatients() != null) {
            tblPatients.setItems(DataStore.getPatients());
        }
    }

    @FXML
    private void handleAddPatient() throws IOException {
        openPatientModal(null);
    }

    @FXML
    private void handleEditPatient() throws IOException {
        Patient selected = tblPatients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a patient to edit!");
            return;
        }
        openPatientModal(selected);
    }

    @FXML
    private void handleDeletePatient() {
        Patient selected = tblPatients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a patient to delete!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete patient: " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                DataStore.getPatients().remove(selected);
            }
        });
    }

    private void openPatientModal(Patient patient) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/patient-form.fxml"));
        Parent parent = loader.load();

        PatientFormController controller = loader.getController();
        if (controller != null) {
            controller.setPatientForEdit(patient);
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(patient == null ? "Add Patient" : "Edit Patient");
        stage.setScene(new Scene(parent));
        stage.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}