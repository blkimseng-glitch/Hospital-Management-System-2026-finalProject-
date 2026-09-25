package org.example.hms2026.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneRouter {

    public static void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneRouter.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error Switch Scene: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void loadSubView(StackPane contentPane, String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(SceneRouter.class.getResource(fxmlPath));
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("❌ Error Loading Sub-View: " + fxmlPath);
            e.printStackTrace();
        }
    }
}