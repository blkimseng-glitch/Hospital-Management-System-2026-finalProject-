package org.example.hms2026.controller_admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Billing;
import org.example.hms2026.model.Doctor;
import org.example.hms2026.model.Patient;

import java.time.LocalDate;

public class BillingFormController {

    @FXML private Label lblReceiptNo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Patient> cmbPatients;
    @FXML private ComboBox<Doctor> cmbDoctors;
    @FXML private TextField txtPatientContact;
    @FXML private TextField txtPatientEmail;
    @FXML private TextField txtDoctorLicense;
    @FXML private TextField txtDiagnosis;
    @FXML private TextField txtDescription;
    @FXML private TextField txtAmount;
    @FXML private Label lblSubtotal;
    @FXML private Label lblBalanceDue;

    @FXML
    public void initialize() {
        if (datePicker != null) datePicker.setValue(LocalDate.now());

        // Load Patients & Doctors into ComboBoxes
        if (cmbPatients != null && DataStore.getPatients() != null) {
            cmbPatients.setItems(DataStore.getPatients());
            cmbPatients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    if (txtPatientContact != null) txtPatientContact.setText(newVal.getPhone());
                    if (txtPatientEmail != null) txtPatientEmail.setText(newVal.getEmail());
                }
            });
        }

        if (cmbDoctors != null && DataStore.getDoctors() != null) {
            cmbDoctors.setItems(DataStore.getDoctors());
            cmbDoctors.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    if (txtDoctorLicense != null) txtDoctorLicense.setText("LIC-" + newVal.getId());
                }
            });
        }

        // Live calculation listener for amount
        if (txtAmount != null) {
            txtAmount.textProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    double amt = newVal.isEmpty() ? 0.0 : Double.parseDouble(newVal);
                    if (lblSubtotal != null) lblSubtotal.setText(String.format("$%.2f", amt));
                    if (lblBalanceDue != null) lblBalanceDue.setText(String.format("$%.2f", amt));
                } catch (NumberFormatException e) {
                    // Ignore invalid number typing temporarily
                }
            });
        }
    }

    @FXML
    private void handleSaveInvoice(ActionEvent event) {
        Patient selectedPatient = cmbPatients.getSelectionModel().getSelectedItem();
        Doctor selectedDoctor = cmbDoctors.getSelectionModel().getSelectedItem();
        String description = txtDescription.getText();
        String amountStr = txtAmount.getText();

        if (selectedPatient == null || selectedDoctor == null || amountStr.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "សូមបំពេញព័ត៌មានអ្នកជំងឺ វេជ្ជបណ្ឌិត និងទឹកប្រាក់ឱ្យបានត្រឹមត្រូវ!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            String billId = "B" + String.format("%03d", DataStore.getBillings().size() + 1);
            String patientName = selectedPatient.getName();
            String doctorName = selectedDoctor.getName();
            String diagnosis = txtDiagnosis.getText().isEmpty() ? description : txtDiagnosis.getText();

            // បង្កើត Object Billing ថ្មី និងបន្ថែមចូល DataStore
            Billing newBilling = new Billing(billId, patientName, doctorName, diagnosis, description, diagnosis, amount, "Unpaid");
            DataStore.getBillings().add(newBilling);

            Alert success = new Alert(Alert.AlertType.INFORMATION, "វិក្កយបត្រត្រូវបានបង្កើត និងរក្សាទុកដោយជោគជ័យ!", ButtonType.OK);
            success.showAndWait();

            // បិទ Window នេះ
            Stage stage = (Stage) txtAmount.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "ចំនួនទឹកប្រាក់ (Amount) ត្រូវតែជាតួលេខត្រឹមត្រូវ!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) txtAmount.getScene().getWindow();
        stage.close();
    }
}