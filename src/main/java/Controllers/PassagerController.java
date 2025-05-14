package controllers;

import entities.Utilisateur;
import controllers.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class PassagerController {

    @FXML
    private Button btnMonProfil;

    @FXML
    private void initialize() {
        // Récupérer l'utilisateur connecté depuis UserSession
        Utilisateur utilisateur = UserSession.getCurrentUser();
        if (utilisateur != null) {
            System.out.println("Bienvenue passager : " + utilisateur.getNom());
        }
    }

    @FXML
    void ouvrirProfil(ActionEvent event) {
        try {
            // Chargement de la vue UserProfile
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/UserProfile.fxml"));
            Parent root = loader.load();

            // Passer l'utilisateur connecté au UserProfileController
            UserProfileController controller = loader.getController();
            controller.setUser(UserSession.getCurrentUser());

            // Créer une nouvelle scène pour afficher le profil
            Scene scene = new Scene(root);

            // Création et affichage de la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Profil Passager");
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

    @FXML
    public void acceuilClient(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Accueil.fxml"));
            Parent root = loader.load();

            // Obtenir la scène depuis l'événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Accueil Client");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
