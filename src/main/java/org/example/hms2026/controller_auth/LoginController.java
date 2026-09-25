package org.example.hms2026.controller_auth;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.hms2026.database.DataStore;
import org.example.hms2026.model.Doctor;
import org.example.hms2026.model.Patient;
import org.example.hms2026.model.User;
import org.example.hms2026.model.enums.UserRole;
import org.example.hms2026.util.SceneRouter;
import org.example.hms2026.util.UserSession;

public class LoginController {

    @FXML private ComboBox<UserRole> cmbRole;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @FXML
    public void initialize() {
        // ដាក់ List Enum Role ចូលក្នុង ComboBox
        cmbRole.setItems(FXCollections.observableArrayList(UserRole.values()));
        cmbRole.setValue(UserRole.ADMIN); // Default Role

        if (lblError != null) {
            lblError.setVisible(false);
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        UserRole selectedRole = cmbRole.getValue();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Please fill in all fields!");
            lblError.setVisible(true);
            return;
        }


        boolean isValidUser = false;
        for (User u : DataStore.getUsers()) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)
                    && u.getRole().equalsIgnoreCase(selectedRole.name())) {
                isValidUser = true;
                break;
            }
        }

        if (isValidUser) {
            lblError.setVisible(false);


            UserSession.getInstance(username, selectedRole.name());


            if (selectedRole == UserRole.PATIENT) {
                for (Patient p : DataStore.getPatients()) {
                    String formattedName = p.getName().toLowerCase().replace(" ", ".");
                    if (formattedName.equals(username.toLowerCase())) {
                        UserSession.getInstance().setCurrentPatient(p);
                        break;
                    }
                }
            }


            if (selectedRole == UserRole.DOCTOR) {
                for (Doctor d : DataStore.getDoctors()) {

                    String docUsername = "dr." + d.getName().toLowerCase().replace(" ", "").replace("dr.", "");
                    if (docUsername.equals(username.toLowerCase()) || d.getEmail().toLowerCase().contains(username.toLowerCase())) {
                        UserSession.getInstance().setCurrentDoctor(d);
                        break;
                    }
                }
            }

            // 3. Switch Page ទៅតាម Role
            if (selectedRole == UserRole.ADMIN) {
                SceneRouter.switchScene(event, "/org/example/hms2026/view/admin/admin-layout.fxml");
            } else if (selectedRole == UserRole.DOCTOR) {
                SceneRouter.switchScene(event, "/org/example/hms2026/view/doctor/doctor-layout.fxml");
            } else if (selectedRole == UserRole.PATIENT) {
                SceneRouter.switchScene(event, "/org/example/hms2026/view/patient/patient-dashboard.fxml");
            }
        } else {
            lblError.setText("Invalid Username, Password, or Role!");
            lblError.setVisible(true);
        }
    }
}