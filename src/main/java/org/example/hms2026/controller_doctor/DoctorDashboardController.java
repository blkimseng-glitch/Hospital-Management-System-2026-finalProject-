package org.example.hms2026.controller_doctor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Appointment;
import org.example.hms2026.model.Diagnosis;
import org.example.hms2026.model.Doctor;
import org.example.hms2026.model.Medicine;
import org.example.hms2026.util.SceneRouter;
import org.example.hms2026.util.UserSession;

import java.time.LocalDate;

public class DoctorDashboardController {

    @FXML private Label lblSidebarDocName;
    @FXML private Label lblSidebarDocSpec;

    @FXML private Label lblProfileName;
    @FXML private Label lblProfileSpecialization;
    @FXML private Label lblProfilePhone;
    @FXML private Label lblProfileEmail;

    @FXML private Label lblPatientsCount;
    @FXML private Label lblAppointmentsCount;

    @FXML private Button btnNavDashboard;
    @FXML private Button btnNavDiagnosis;
    @FXML private Button btnNavAppointment;
    @FXML private Button btnNavProfile;
    @FXML private Button btnNavStock;

    @FXML private VBox viewDashboard;
    @FXML private VBox viewDiagnosis;
    @FXML private VBox viewAppointment;
    @FXML private VBox viewProfile;
    @FXML private VBox viewMedicinesStock;

    @FXML private TextField txtSearchKeyword;
    @FXML private ComboBox<String> cmbSortOption;
    @FXML private TableView<Appointment> tblSearchAppointments;
    @FXML private TableColumn<Appointment, String> colSearchId;
    @FXML private TableColumn<Appointment, String> colSearchPatient;
    @FXML private TableColumn<Appointment, String> colSearchDate;
    @FXML private TableColumn<Appointment, String> colSearchStatus;

    @FXML private TextField txtDiagPatientName;
    @FXML private TextField txtDiagNotes;
    @FXML private TableView<Diagnosis> tblDiagnosisHistory;
    @FXML private TableColumn<Diagnosis, String> colDiagPatient;
    @FXML private TableColumn<Diagnosis, String> colDiagNotes;
    @FXML private TableColumn<Diagnosis, String> colDiagDate;

    @FXML private TableView<Appointment> tblDoctorAppointments;
    @FXML private TableColumn<Appointment, String> colDocApptId;
    @FXML private TableColumn<Appointment, String> colDocApptPatient;
    @FXML private TableColumn<Appointment, String> colDocApptDate;
    @FXML private TableColumn<Appointment, String> colDocApptStatus;

    @FXML private TextField txtSearchMedicine;
    @FXML private ComboBox<String> cmbSortMedicine;
    @FXML private TableView<Medicine> tblMedicines;
    @FXML private TableColumn<Medicine, String> colMedId;
    @FXML private TableColumn<Medicine, String> colMedName;
    @FXML private TableColumn<Medicine, Integer> colMedStock;
    @FXML private TableColumn<Medicine, Double> colMedPrice;

    private ObservableList<Diagnosis> diagnosisList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Doctor currentDoctor = UserSession.getInstance().getCurrentDoctor();
        if (currentDoctor != null) {
            if (lblSidebarDocName != null) {
                lblSidebarDocName.setText(currentDoctor.getName());
            }
            if (lblSidebarDocSpec != null) {
                lblSidebarDocSpec.setText(currentDoctor.getSpecialization());
            }

            if (lblProfileName != null) {
                lblProfileName.setText(currentDoctor.getName());
            }
            if (lblProfileSpecialization != null) {
                lblProfileSpecialization.setText(currentDoctor.getSpecialization());
            }
            if (lblProfilePhone != null) {
                lblProfilePhone.setText(currentDoctor.getPhone());
            }
            if (lblProfileEmail != null) {
                lblProfileEmail.setText(currentDoctor.getEmail());
            }
        }

        updateCounters();

        if (colSearchId != null) colSearchId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colSearchPatient != null) colSearchPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        if (colSearchDate != null) colSearchDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        if (colSearchStatus != null) colSearchStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tblSearchAppointments != null && DataStore.getAppointments() != null) {
            FilteredList<Appointment> filteredAppts = new FilteredList<>(DataStore.getAppointments(), b -> true);
            if (txtSearchKeyword != null) {
                txtSearchKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredAppts.setPredicate(appt -> {
                        if (newValue == null || newValue.isEmpty()) return true;
                        String lowerFilter = newValue.toLowerCase();
                        if (appt.getPatientName().toLowerCase().contains(lowerFilter)) return true;
                        if (appt.getId().toLowerCase().contains(lowerFilter)) return true;
                        return false;
                    });
                });
            }
            SortedList<Appointment> sortedAppts = new SortedList<>(filteredAppts);
            tblSearchAppointments.setItems(sortedAppts);

            if (cmbSortOption != null) {
                cmbSortOption.getItems().addAll("Status", "Date (Newest)", "Date (Oldest)");
                cmbSortOption.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == null) return;
                    if (newValue.equals("Status")) {
                        sortedAppts.setComparator((a1, a2) -> a1.getStatus().compareToIgnoreCase(a2.getStatus()));
                    } else if (newValue.equals("Date (Newest)")) {
                        sortedAppts.setComparator((a1, a2) -> a2.getDateTime().compareTo(a1.getDateTime()));
                    } else if (newValue.equals("Date (Oldest)")) {
                        sortedAppts.setComparator((a1, a2) -> a1.getDateTime().compareTo(a2.getDateTime()));
                    }
                });
            }
        }

        if (colDocApptId != null) colDocApptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colDocApptPatient != null) colDocApptPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        if (colDocApptDate != null) colDocApptDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        if (colDocApptStatus != null) colDocApptStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tblDoctorAppointments != null && DataStore.getAppointments() != null) {
            tblDoctorAppointments.setItems(DataStore.getAppointments());
        }

        if (colMedId != null) colMedId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colMedName != null) colMedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (colMedStock != null) colMedStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        if (colMedPrice != null) colMedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        if (tblMedicines != null && DataStore.getMedicines() != null) {
            FilteredList<Medicine> filteredMedicines = new FilteredList<>(DataStore.getMedicines(), b -> true);

            if (txtSearchMedicine != null) {
                txtSearchMedicine.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredMedicines.setPredicate(medicine -> {
                        if (newValue == null || newValue.isEmpty()) return true;
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (medicine.getId().toLowerCase().contains(lowerCaseFilter)) return true;
                        if (medicine.getName().toLowerCase().contains(lowerCaseFilter)) return true;
                        return false;
                    });
                });
            }

            SortedList<Medicine> sortedMedicines = new SortedList<>(filteredMedicines);
            tblMedicines.setItems(sortedMedicines);

            if (cmbSortMedicine != null) {
                cmbSortMedicine.getItems().addAll("Name (A-Z)", "Stock (Low to High)", "Stock (High to Low)", "Price ($)");

                cmbSortMedicine.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == null) return;
                    if (newValue.equals("Name (A-Z)")) {
                        sortedMedicines.setComparator((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
                    } else if (newValue.equals("Stock (Low to High)")) {
                        sortedMedicines.setComparator((m1, m2) -> Integer.compare(m1.getStock(), m2.getStock()));
                    } else if (newValue.equals("Stock (High to Low)")) {
                        sortedMedicines.setComparator((m1, m2) -> Integer.compare(m2.getStock(), m1.getStock()));
                    } else if (newValue.equals("Price ($)")) {
                        sortedMedicines.setComparator((m1, m2) -> Double.compare(m1.getPrice(), m2.getPrice()));
                    }
                });
            }
        }

        if (colDiagPatient != null) colDiagPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        if (colDiagNotes != null) colDiagNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        if (colDiagDate != null) colDiagDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        if (tblDiagnosisHistory != null) {
            tblDiagnosisHistory.setItems(diagnosisList);
        }
    }

    private void updateCounters() {
        if (lblPatientsCount != null && DataStore.getPatients() != null) {
            lblPatientsCount.setText(String.valueOf(DataStore.getPatients().size()));
        }
        if (lblAppointmentsCount != null && DataStore.getAppointments() != null) {
            lblAppointmentsCount.setText(String.valueOf(DataStore.getAppointments().size()));
        }
    }

    @FXML
    private void handleSwitchView(ActionEvent event) {
        Button sourceBtn = (Button) event.getSource();

        if (viewDashboard != null) { viewDashboard.setVisible(false); viewDashboard.setManaged(false); }
        if (viewDiagnosis != null) { viewDiagnosis.setVisible(false); viewDiagnosis.setManaged(false); }
        if (viewAppointment != null) { viewAppointment.setVisible(false); viewAppointment.setManaged(false); }
        if (viewProfile != null) { viewProfile.setVisible(false); viewProfile.setManaged(false); }
        if (viewMedicinesStock != null) { viewMedicinesStock.setVisible(false); viewMedicinesStock.setManaged(false); }

        Button[] navButtons = {btnNavDashboard, btnNavDiagnosis, btnNavAppointment, btnNavProfile, btnNavStock};
        for (Button btn : navButtons) {
            if (btn != null) {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #E0E7FF; -fx-background-radius: 6;");
            }
        }

        if (sourceBtn == btnNavDashboard) {
            if (viewDashboard != null) { viewDashboard.setVisible(true); viewDashboard.setManaged(true); }
        } else if (sourceBtn == btnNavDiagnosis) {
            if (viewDiagnosis != null) { viewDiagnosis.setVisible(true); viewDiagnosis.setManaged(true); }
        } else if (sourceBtn == btnNavAppointment) {
            if (viewAppointment != null) { viewAppointment.setVisible(true); viewAppointment.setManaged(true); }
        } else if (sourceBtn == btnNavProfile) {
            if (viewProfile != null) { viewProfile.setVisible(true); viewProfile.setManaged(true); }
        } else if (sourceBtn == btnNavStock) {
            if (viewMedicinesStock != null) { viewMedicinesStock.setVisible(true); viewMedicinesStock.setManaged(true); }
        }

        if (sourceBtn != null) {
            sourceBtn.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6;");
        }
    }

    @FXML
    private void handleSaveDiagnosis(ActionEvent event) {
        String patientName = txtDiagPatientName != null ? txtDiagPatientName.getText() : "";
        String notes = txtDiagNotes != null ? txtDiagNotes.getText() : "";
        if (patientName.isEmpty() || notes.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter patient name and diagnosis notes!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Diagnosis diag = new Diagnosis(patientName, notes, LocalDate.now().toString());
        diagnosisList.add(diag);

        if (txtDiagPatientName != null) txtDiagPatientName.clear();
        if (txtDiagNotes != null) txtDiagNotes.clear();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SceneRouter.switchScene(event, "/org/example/hms2026/view/auth/login-view.fxml");
    }
}