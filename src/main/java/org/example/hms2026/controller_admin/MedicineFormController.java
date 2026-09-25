package org.example.hms2026.controller_admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Medicine;

public class MedicineFormController {

    @FXML private TextField txtMedicineId;
    @FXML private TextField txtMedicineName;
    @FXML private TextField txtCategory;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtUnitPrice;

    private Medicine editingMedicine;

    public void setMedicineForEdit(Medicine medicine) {
        this.editingMedicine = medicine;
        if (medicine != null) {
            txtMedicineId.setText(medicine.getId());
            txtMedicineId.setEditable(false);
            txtMedicineName.setText(medicine.getName());
            txtCategory.setText(medicine.getCategory());
            txtQuantity.setText(String.valueOf(medicine.getStock()));   // ប្រើ getStock()
            txtUnitPrice.setText(String.valueOf(medicine.getPrice())); // ប្រើ getPrice()
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            String id = txtMedicineId.getText();
            String name = txtMedicineName.getText();
            String category = txtCategory.getText();
            int qty = Integer.parseInt(txtQuantity.getText());
            double price = Double.parseDouble(txtUnitPrice.getText());

            if (id.isEmpty() || name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all required fields!");
                return;
            }

            if (editingMedicine == null) {
                // បន្ថែមថ្មី (Add) ដោយផ្អែកលើ Constructor នៃ Medicine class របស់អ្នក
                Medicine newMed = new Medicine(id, name, category, qty, price);
                DataStore.getMedicines().add(newMed);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine added successfully!");
            } else {
                // កែប្រែទិន្នន័យចាស់ (Edit)
                editingMedicine.setName(name);
                editingMedicine.setCategory(category);
                editingMedicine.setStock(qty);   // ប្រើ setStock()
                editingMedicine.setPrice(price); // ប្រើ setPrice()
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine updated successfully!");
            }

            Stage stage = (Stage) txtMedicineId.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be an integer and Unit Price must be a valid number!");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) txtMedicineId.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}