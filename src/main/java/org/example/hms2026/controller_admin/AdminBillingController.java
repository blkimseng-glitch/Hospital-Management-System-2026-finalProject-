package org.example.hms2026.controller_admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Billing;

import java.io.IOException;

public class AdminBillingController {

    @FXML private TableView<Billing> tblAdminBilling;
    @FXML private TableColumn<Billing, String> colBillId;
    @FXML private TableColumn<Billing, String> colPatientName;
    @FXML private TableColumn<Billing, String> colDoctorName;
    @FXML private TableColumn<Billing, String> colDescription;
    @FXML private TableColumn<Billing, Double> colAmount;
    @FXML private TableColumn<Billing, String> colStatus;
    @FXML private TextField txtSearch;

    @FXML
    public void initialize() {
        if (colBillId != null) colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        if (colPatientName != null) colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        if (colDoctorName != null) colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        if (colDescription != null) colDescription.setCellValueFactory(new PropertyValueFactory<>("diseaseDetails"));

        if (colAmount != null) {
            colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            colAmount.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Double amount, boolean empty) {
                    super.updateItem(amount, empty);
                    if (empty || amount == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", amount));
                    }
                }
            });
        }
        if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tblAdminBilling != null && DataStore.getBillings() != null) {
            tblAdminBilling.setItems(DataStore.getBillings());
        }
    }

    @FXML
    private void handleCreateInvoice(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/billing-form.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create Patient Invoice & Receipt");
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            if (tblAdminBilling != null) {
                tblAdminBilling.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open billing form: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteInvoice(ActionEvent event) {
        if (tblAdminBilling == null) return;
        Billing selected = tblAdminBilling.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DataStore.getBillings().remove(selected);
            tblAdminBilling.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Invoice deleted successfully!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an invoice to delete!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}