package org.example.hms2026.controller_patient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Appointment;
import org.example.hms2026.model.Billing;
import org.example.hms2026.model.Patient;
import org.example.hms2026.util.SceneRouter;
import org.example.hms2026.util.UserSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientDashboardController {

    // Sidebar Profile Labels (សម្រាប់បង្ហាញឈ្មោះ និង ID ពិតរបស់អ្នកជំងឺដែល Login)
    @FXML private Label lblPatientName;
    @FXML private Label lblMemberId;

    // Sidebar Views Containers
    @FXML private VBox viewAppointments;
    @FXML private VBox viewHistory;
    @FXML private VBox viewBilling;

    // Sidebar Navigation Buttons
    @FXML private Button btnNavAppointments;
    @FXML private Button btnNavHistory;
    @FXML private Button btnNavBilling;

    // Appointments Table
    @FXML private TableView<Appointment> tblMyAppointments;
    @FXML private TableColumn<Appointment, String> colApptId;
    @FXML private TableColumn<Appointment, String> colApptDoctor;
    @FXML private TableColumn<Appointment, String> colApptDate;
    @FXML private TableColumn<Appointment, String> colApptStatus;

    // Medical History Table
    @FXML private TableView<Billing> tblMedicalHistory;
    @FXML private TableColumn<Billing, String> colHistBillId;
    @FXML private TableColumn<Billing, String> colHistDoctor;
    @FXML private TableColumn<Billing, String> colHistDiagnosis;
    @FXML private TableColumn<Billing, String> colHistMedicine;
    @FXML private TableColumn<Billing, String> colHistDetails;
    @FXML private TableColumn<Billing, String> colHistStatus;

    // Billing Table
    @FXML private TableView<Billing> tblMyBilling;
    @FXML private TableColumn<Billing, String> colBillId;
    @FXML private TableColumn<Billing, String> colBillDoctor;
    @FXML private TableColumn<Billing, String> colBillDetails;
    @FXML private TableColumn<Billing, Double> colBillAmount;
    @FXML private TableColumn<Billing, String> colBillStatus;

    // Receipt Summary Labels
    @FXML private Label lblReceiptDate;
    @FXML private Label lblSubtotal;
    @FXML private Label lblBalanceDue;

    @FXML
    public void initialize() {
        // 🔄 ទាញយក និងបង្ហាញព័ត៌មាន Profile របស់អ្នកជំងឺពី UserSession
        Patient currentPatient = UserSession.getInstance().getCurrentPatient();
        if (currentPatient != null) {
            if (lblPatientName != null) {
                lblPatientName.setText(currentPatient.getName()); // បង្ហាញឈ្មោះពិត ឧ. Dara Sok
            }
            if (lblMemberId != null) {
                lblMemberId.setText("Member ID: " + currentPatient.getId()); // បង្ហាញ ID ពិត ឧ. P001
            }
        }

        if (tblMyAppointments != null) tblMyAppointments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if (tblMedicalHistory != null) tblMedicalHistory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if (tblMyBilling != null) tblMyBilling.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // បង្ហាញកាលបរិច្ឆេទបច្ចុប្បន្នលើវិក្កយបត្រ
        if (lblReceiptDate != null) {
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            lblReceiptDate.setText("Date: " + currentDate);
        }

        // Mapping Appointments Table
        if (colApptId != null) colApptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colApptDoctor != null) colApptDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        if (colApptDate != null) colApptDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        if (colApptStatus != null) colApptStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tblMyAppointments != null && DataStore.getAppointments() != null) {
            tblMyAppointments.setItems(DataStore.getAppointments());
        }

        // Mapping Medical History Table
        if (colHistBillId != null) colHistBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        if (colHistDoctor != null) colHistDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        if (colHistDiagnosis != null) colHistDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        if (colHistMedicine != null) colHistMedicine.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        if (colHistDetails != null) colHistDetails.setCellValueFactory(new PropertyValueFactory<>("diseaseDetails"));
        if (colHistStatus != null) colHistStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tblMedicalHistory != null && DataStore.getBillings() != null) {
            tblMedicalHistory.setItems(DataStore.getBillings());
        }

        // Mapping Billing Table Columns & CellFactory for Amount
        if (colBillId != null) colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        if (colBillDoctor != null) colBillDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        if (colBillDetails != null) colBillDetails.setCellValueFactory(new PropertyValueFactory<>("diseaseDetails"));

        if (colBillAmount != null) {
            colBillAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            colBillAmount.setCellFactory(column -> new TableCell<Billing, Double>() {
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

        if (colBillStatus != null) colBillStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load Billing Data and Selection Listener for Subtotal & Balance Due
        if (tblMyBilling != null && DataStore.getBillings() != null) {
            tblMyBilling.setItems(DataStore.getBillings());

            tblMyBilling.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    if (newVal != null) {
                        double amount = newVal.getTotalAmount();
                        if (lblSubtotal != null) lblSubtotal.setText(String.format("$%.2f", amount));
                        if (lblBalanceDue != null) lblBalanceDue.setText(String.format("$ %.2f", amount));
                    } else {
                        if (lblSubtotal != null) lblSubtotal.setText("$0.00");
                        if (lblBalanceDue != null) lblBalanceDue.setText("$ 0.00");
                    }
                } catch (Exception e) {
                    System.err.println("Error updating billing numbers: " + e.getMessage());
                }
            });
        }
    }

    // 🔄 មុខងារប្ដូរផ្ទាំង View និងរក្សាទំហំរចនាសម្ព័ន្ធ Sidebar ឱ្យនៅថេរ
    @FXML
    private void handleSwitchView(ActionEvent event) {
        if (viewAppointments != null) viewAppointments.setVisible(false);
        if (viewHistory != null) viewHistory.setVisible(false);
        if (viewBilling != null) viewBilling.setVisible(false);

        String inactiveStyle = "-fx-alignment: CENTER_LEFT; -fx-padding: 12px 14px; -fx-background-radius: 8px; -fx-font-size: 13px; -fx-background-color: transparent; -fx-text-fill: #E0E7FF; -fx-cursor: hand;";
        if (btnNavAppointments != null) btnNavAppointments.setStyle(inactiveStyle);
        if (btnNavHistory != null) btnNavHistory.setStyle(inactiveStyle);
        if (btnNavBilling != null) btnNavBilling.setStyle(inactiveStyle);

        String activeStyle = "-fx-alignment: CENTER_LEFT; -fx-padding: 12px 14px; -fx-background-radius: 8px; -fx-font-size: 13px; -fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";

        if (event.getSource() instanceof Button clickedButton) {
            clickedButton.setStyle(activeStyle);

            if (clickedButton == btnNavAppointments && viewAppointments != null) {
                viewAppointments.setVisible(true);
            } else if (clickedButton == btnNavHistory && viewHistory != null) {
                viewHistory.setVisible(true);
            } else if (clickedButton == btnNavBilling && viewBilling != null) {
                viewBilling.setVisible(true);
            }
        }
    }

    // 💳 មុខងារបង្ហាញ KHQR Dialog សម្រាប់ទូទាត់ប្រាក់ដូចដើម
    @FXML
    private void handlePayBill(ActionEvent event) {
        var selectedBill = tblMyBilling.getSelectionModel().getSelectedItem();

        if (selectedBill == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("សូមជ្រើសរើសវិក្កយបត្រ (Invoice) ណាមួយដែលចង់បង់ប្រាក់!");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> qrDialog = new Dialog<>();
        qrDialog.setTitle("Scan KHQR to Pay");
        qrDialog.setHeaderText("វិក្កយបត្រ ID: " + selectedBill.getBillId() + " | ទឹកប្រាក់សរុប: $" + String.format("%.2f", selectedBill.getTotalAmount()));

        VBox dialogContent = new VBox(15);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(20));
        dialogContent.setStyle("-fx-background-color: white;");

        Label qrBox = new Label("[ KHQR / ABA PAYWAY QR CODE ]");
        qrBox.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1E3A8A; -fx-padding: 40; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-color: #F8FAFC;");

        Label instructions = new Label("សូមប្រើប្រាស់កម្មវិធី ABA Mobile, ACLEDA ឬ Bakong ដើម្បីស្កេនទូទាត់ប្រាក់។\nចំនួនទឹកប្រាក់ត្រូវបង់: $" + String.format("%.2f", selectedBill.getTotalAmount()));
        instructions.setStyle("-fx-text-alignment: center; -fx-text-fill: #64748B;");

        dialogContent.getChildren().addAll(qrBox, instructions);
        qrDialog.getDialogPane().setContent(dialogContent);
        qrDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        qrDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                selectedBill.setStatus("Paid");
                tblMyBilling.refresh();
                if (tblMedicalHistory != null) tblMedicalHistory.refresh();

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("ការទូទាត់ប្រាក់បានជោគជ័យអរគុណ!");
                success.showAndWait();
            }
        });
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SceneRouter.switchScene(event, "/org/example/hms2026/view/auth/login-view.fxml");
    }
}