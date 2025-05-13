package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuAdminController {

    @FXML
    private Button monProfilButton;

    @FXML
    private void initialize() {
        monProfilButton.setOnAction(e -> openAdminDashboard());
    }

    private void openAdminDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/AdminDashboard.fxml"));
            Stage stage = (Stage) monProfilButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void seDeconnecter(ActionEvent event) {
        try {
            // Charger la vue Login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour le login
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Appliquer la nouvelle scène à la fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

