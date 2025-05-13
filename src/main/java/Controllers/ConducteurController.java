package Controllers;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ConducteurController {

    @FXML
    private Button btnMonProfil;

    @FXML
    private void initialize() {
        // Vous récupérez l'utilisateur connecté via UserSession
        Utilisateur utilisateur = UserSession.getCurrentUser();
        if (utilisateur != null) {
            System.out.println("Bienvenue conducteur : " + utilisateur.getNom());
        }
    }

    @FXML
    private void ouvrirProfil(ActionEvent event) {
        try {
            // Chargement de la vue UserProfile
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/UserProfile.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur de UserProfile
            UserProfileController controller = loader.getController();

            // Passer l'utilisateur connecté au UserProfileController
            controller.setUser(UserSession.getCurrentUser());

            // Affichage du profil utilisateur après le menu dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Profil Conducteur");

            // Création de la scène avec le contenu chargé
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
