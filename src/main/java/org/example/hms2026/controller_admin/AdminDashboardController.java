package org.example.hms2026.controller_admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Appointment;
import org.example.hms2026.model.Billing;
import org.example.hms2026.model.Doctor;
import org.example.hms2026.model.Medicine;
import org.example.hms2026.model.Patient;
import org.example.hms2026.util.SceneRouter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminDashboardController {

    @FXML private BorderPane mainBorderPane;

    // Sidebar Buttons
    @FXML private Button btnDashboard;
    @FXML private Button btnDoctors;
    @FXML private Button btnPatients;
    @FXML private Button btnAppointments;
    @FXML private Button btnBilling;
    @FXML private Button btnMedicine;

    // Stack Views ក្នុង StackPane តែមួយ
    @FXML private VBox dashboardView;
    @FXML private VBox doctorsView;
    @FXML private VBox patientsView;
    @FXML private VBox appointmentsView;
    @FXML private VBox billingView;
    @FXML private VBox medicineStockView;

    @FXML private Label lblTotalPatients;
    @FXML private Label lblTotalDoctors;
    @FXML private Label lblTotalAppointments;

    // Table Views
    @FXML private TableView<Patient> tblPatients;
    @FXML private TableView<Doctor> tblDoctors;
    @FXML private TableView<Appointment> tblAppointments;
    @FXML private TableView<Billing> tblAdminBilling;
    @FXML private TableView<Medicine> tblMedicineStock;
    @FXML private TableView<Appointment> tblRecentAppointments;

    // BarChart
    @FXML private BarChart<String, Number> statsBarChart;

    // Patient Columns
    @FXML private TableColumn<Patient, String> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colEmail;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, String> colDiagnosis;

    // Doctor Columns
    @FXML private TableColumn<Doctor, String> colDoctorId;
    @FXML private TableColumn<Doctor, String> colDoctorName;
    @FXML private TableColumn<Doctor, String> colDoctorSpecialization;
    @FXML private TableColumn<Doctor, String> colDoctorPhone;
    @FXML private TableColumn<Doctor, String> colDoctorEmail;

    // Appointment Columns
    @FXML private TableColumn<Appointment, String> colAppointmentId;
    @FXML private TableColumn<Appointment, String> colPatientName;
    @FXML private TableColumn<Appointment, String> colAppointmentDate;
    @FXML private TableColumn<Appointment, String> colStatus;

    // Recent Appointments Columns
    @FXML private TableColumn<Appointment, String> colRecentId;
    @FXML private TableColumn<Appointment, String> colRecentPatient;
    @FXML private TableColumn<Appointment, String> colRecentDoctor;
    @FXML private TableColumn<Appointment, String> colRecentDateTime;
    @FXML private TableColumn<Appointment, String> colRecentStatus;

    // Billing Columns
    @FXML private TableColumn<Billing, String> colBillId;
    @FXML private TableColumn<Billing, String> colBillPatient;
    @FXML private TableColumn<Billing, String> colBillDoctor;
    @FXML private TableColumn<Billing, String> colBillDesc;
    @FXML private TableColumn<Billing, Double> colBillAmount;
    @FXML private TableColumn<Billing, String> colBillStatus;

    // Medicine Columns
    @FXML private TableColumn<Medicine, String> colMedId;
    @FXML private TableColumn<Medicine, String> colMedName;
    @FXML private TableColumn<Medicine, String> colMedCategory;
    @FXML private TableColumn<Medicine, Integer> colMedQuantity;
    @FXML private TableColumn<Medicine, Double> colMedPrice;

    @FXML
    public void initialize() {
        updateCounters();

        // 1. Patient Mapping
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colName != null) colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (colEmail != null) colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        if (colPhone != null) colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        if (colDiagnosis != null) colDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));

        if (tblPatients != null && DataStore.getPatients() != null) {
            tblPatients.setItems(DataStore.getPatients());
            DataStore.getPatients().addListener((javafx.collections.ListChangeListener<Patient>) c -> updateCounters());
        }

        // 2. Doctor Mapping
        if (colDoctorId != null) colDoctorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colDoctorName != null) colDoctorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (colDoctorSpecialization != null) colDoctorSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        if (colDoctorPhone != null) colDoctorPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        if (colDoctorEmail != null) colDoctorEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        if (tblDoctors != null && DataStore.getDoctors() != null) {
            tblDoctors.setItems(DataStore.getDoctors());
            DataStore.getDoctors().addListener((javafx.collections.ListChangeListener<Doctor>) c -> updateCounters());
        }

        // 3. Appointment Mapping
        if (colAppointmentId != null) colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colPatientName != null) colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        if (colAppointmentDate != null) colAppointmentDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tblAppointments != null && DataStore.getAppointments() != null) {
            tblAppointments.setItems(DataStore.getAppointments());
            DataStore.getAppointments().addListener((javafx.collections.ListChangeListener<Appointment>) c -> updateCounters());
        }

        // 4. Admin Billing Mapping
        if (colBillId != null) colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        if (colBillPatient != null) colBillPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        if (colBillDoctor != null) colBillDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        if (colBillDesc != null) colBillDesc.setCellValueFactory(new PropertyValueFactory<>("diseaseDetails"));

        if (colBillAmount != null) {
            colBillAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
            colBillAmount.setCellFactory(col -> new TableCell<>() {
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

        if (tblAdminBilling != null && DataStore.getBillings() != null) {
            tblAdminBilling.setItems(DataStore.getBillings());
        }

        // 5. Medicine Stock Mapping
        if (colMedId != null) colMedId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colMedName != null) colMedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (colMedCategory != null) colMedCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        if (colMedQuantity != null) colMedQuantity.setCellValueFactory(new PropertyValueFactory<>("stock"));
        if (colMedPrice != null) {
            colMedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colMedPrice.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty || price == null) {
                        setText(null);
                    } else {
                        setText(String.format("$%.2f", price));
                    }
                }
            });
        }

        if (tblMedicineStock != null && DataStore.getMedicines() != null) {
            tblMedicineStock.setItems(DataStore.getMedicines());
        }

        // 6. Recent Appointments Mapping
        if (colRecentId != null) colRecentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colRecentPatient != null) colRecentPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        if (colRecentDoctor != null) colRecentDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        if (colRecentDateTime != null) colRecentDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        if (colRecentStatus != null) colRecentStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tblRecentAppointments != null && DataStore.getAppointments() != null) {
            tblRecentAppointments.setItems(DataStore.getAppointments());
        }

        // BarChart Data Initialization
        if (statsBarChart != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Hospital Metrics");
            series.getData().add(new XYChart.Data<>("Patients", DataStore.getPatients().size()));
            series.getData().add(new XYChart.Data<>("Doctors", DataStore.getDoctors().size()));
            series.getData().add(new XYChart.Data<>("Appointments", DataStore.getAppointments().size()));

            statsBarChart.getData().clear();
            statsBarChart.getData().add(series);
        }

        // បើកបង្ហាញ Dashboard ជាកាតព្វកិច្ចពេលចាប់ផ្ដើមដំបូង
        switchView(dashboardView);
    }

    private void updateCounters() {
        if (lblTotalPatients != null && DataStore.getPatients() != null) {
            lblTotalPatients.setText(String.valueOf(DataStore.getPatients().size()));
        }
        if (lblTotalDoctors != null && DataStore.getDoctors() != null) {
            lblTotalDoctors.setText(String.valueOf(DataStore.getDoctors().size()));
        }
        if (lblTotalAppointments != null && DataStore.getAppointments() != null) {
            lblTotalAppointments.setText(String.valueOf(DataStore.getAppointments().size()));
        }
    }

    // ==========================================
    // 🧭 NAVIGATION METHODS (StackPane Switcher)
    // ==========================================

    @FXML
    private void handleGoToDashboard(ActionEvent event) {
        setActiveButton(btnDashboard);
        switchView(dashboardView);
    }

    @FXML
    private void handleGoToDoctors(ActionEvent event) {
        setActiveButton(btnDoctors);
        switchView(doctorsView);
    }

    @FXML
    private void handleGoToPatients(ActionEvent event) {
        setActiveButton(btnPatients);
        switchView(patientsView);
    }

    @FXML
    private void handleGoToAppointments(ActionEvent event) {
        setActiveButton(btnAppointments);
        switchView(appointmentsView);
    }

    @FXML
    private void handleGoToBilling(ActionEvent event) {
        setActiveButton(btnBilling);
        switchView(billingView);
    }

    @FXML
    private void handleGoToMedicine(ActionEvent event) {
        setActiveButton(btnMedicine);
        switchView(medicineStockView);
    }

    private void switchView(VBox targetView) {
        if (dashboardView != null) { dashboardView.setVisible(false); dashboardView.setManaged(false); }
        if (doctorsView != null) { doctorsView.setVisible(false); doctorsView.setManaged(false); }
        if (patientsView != null) { patientsView.setVisible(false); patientsView.setManaged(false); }
        if (appointmentsView != null) { appointmentsView.setVisible(false); appointmentsView.setManaged(false); }
        if (billingView != null) { billingView.setVisible(false); billingView.setManaged(false); }
        if (medicineStockView != null) { medicineStockView.setVisible(false); medicineStockView.setManaged(false); }

        if (targetView != null) {
            targetView.setVisible(true);
            targetView.setManaged(true);
        }
    }

    private void setActiveButton(Button activeBtn) {
        Button[] buttons = {btnDashboard, btnDoctors, btnPatients, btnAppointments, btnBilling, btnMedicine};
        for (Button btn : buttons) {
            if (btn != null) btn.getStyleClass().remove("nav-button-active");
        }
        if (activeBtn != null && !activeBtn.getStyleClass().contains("nav-button-active")) {
            activeBtn.getStyleClass().add("nav-button-active");
        }
    }

    // Patient CRUD
    @FXML private void handleAddPatient() throws IOException { openPatientModal(null); }
    @FXML private void handleEditPatient() throws IOException {
        Patient selected = tblPatients.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(Alert.AlertType.WARNING, "Warning", "Please select a patient to edit!"); return; }
        openPatientModal(selected);
    }
    @FXML private void handleDeletePatient() {
        Patient selected = tblPatients.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(Alert.AlertType.WARNING, "Warning", "Please select a patient to delete!"); return; }
        DataStore.getPatients().remove(selected);
    }
    private void openPatientModal(Patient patient) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/patient-form.fxml"));
        Parent parent = loader.load();
        PatientFormController controller = loader.getController();
        if (controller != null) controller.setPatientForEdit(patient);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(patient == null ? "Add Patient" : "Edit Patient");
        stage.setScene(new Scene(parent));
        stage.showAndWait();
    }

    // Doctor CRUD (Updated with Form Modal)
    @FXML private void handleAddDoctor() {
        try {
            openDoctorModal(null);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open doctor form: " + e.getMessage());
        }
    }

    @FXML private void handleEditDoctor() {
        if (tblDoctors == null) return;
        Doctor selected = tblDoctors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a doctor to edit!");
            return;
        }
        try {
            openDoctorModal(selected);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open doctor form: " + e.getMessage());
        }
    }

    @FXML private void handleDeleteDoctor() {
        if (tblDoctors == null) return;
        Doctor selected = tblDoctors.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a doctor to delete!");
            return;
        }
        DataStore.getDoctors().remove(selected);
        if (tblDoctors != null) tblDoctors.refresh();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor deleted successfully!");
    }

    private void openDoctorModal(Doctor doctor) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/doctor-form.fxml"));
        Parent parent = loader.load();
        DoctorFormController controller = loader.getController();
        if (controller != null && doctor != null) {
            controller.setDoctorForEdit(doctor);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(doctor == null ? "Add Doctor" : "Edit Doctor");
        stage.setScene(new Scene(parent));
        stage.showAndWait();
        if (tblDoctors != null) tblDoctors.refresh();
    }

    // Appointment CRUD (Updated with Form Modal)
    @FXML private void handleAddAppointment() {
        try {
            openAppointmentModal(null);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open appointment form: " + e.getMessage());
        }
    }

    @FXML private void handleEditAppointment() {
        if (tblAppointments == null) return;
        Appointment selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an appointment to edit!");
            return;
        }
        try {
            openAppointmentModal(selected);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open appointment form: " + e.getMessage());
        }
    }

    @FXML private void handleDeleteAppointment() {
        if (tblAppointments == null) return;
        Appointment selected = tblAppointments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an appointment to delete!");
            return;
        }
        DataStore.getAppointments().remove(selected);
        if (tblAppointments != null) tblAppointments.refresh();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment deleted successfully!");
    }

    private void openAppointmentModal(Appointment appointment) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/appointment-form.fxml"));
        Parent parent = loader.load();
        AppointmentFormController controller = loader.getController();
        if (controller != null && appointment != null) {
            controller.setAppointmentForEdit(appointment);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(appointment == null ? "Add Appointment" : "Edit Appointment");
        stage.setScene(new Scene(parent));
        stage.showAndWait();
        if (tblAppointments != null) tblAppointments.refresh();
    }

    // Billing Actions
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

    @FXML private void handleDeleteInvoice(ActionEvent event) {
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

    // Medicine Actions
    @FXML private void handleAddMedicine(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/medicine-form.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Medicine Stock");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            if (tblMedicineStock != null) tblMedicineStock.refresh();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open medicine form: " + e.getMessage());
        }
    }

    @FXML private void handleEditMedicine(ActionEvent event) {
        if (tblMedicineStock == null) return;
        Medicine selected = tblMedicineStock.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a medicine item to edit!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/hms2026/view/admin/medicine-form.fxml"));
            Parent parent = loader.load();
            MedicineFormController controller = loader.getController();
            if (controller != null) controller.setMedicineForEdit(selected);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Medicine Stock");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            if (tblMedicineStock != null) tblMedicineStock.refresh();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open medicine form: " + e.getMessage());
        }
    }

    @FXML private void handleDeleteMedicine(ActionEvent event) {
        if (tblMedicineStock == null) return;
        Medicine selected = tblMedicineStock.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DataStore.getMedicines().remove(selected);
            tblMedicineStock.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine deleted successfully!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a medicine item to delete!");
        }
    }

    @FXML private void handleLogout(ActionEvent event) {
        SceneRouter.switchScene(event, "/org/example/hms2026/view/auth/login-view.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}