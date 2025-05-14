package tests;

import Controllers.UserSession;
import entities.Utilisateur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Charger l'utilisateur et le mettre dans la session
        UserService userService = new UserService();
        System.out.println("Tentative de chargement de l'utilisateur avec l'ID 52...");
        Utilisateur utilisateur = userService.getUserById(53);

        if (utilisateur != null) {
            System.out.println("Utilisateur trouvé : " + utilisateur);
            UserSession.setCurrentUser(utilisateur);
        } else {
            System.err.println("Utilisateur non trouvé pour l'ID 52");
            // Afficher une alerte à l'utilisateur
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Utilisateur non trouvé");
            alert.setContentText("L'utilisateur avec l'ID 53 n'a pas été trouvé dans la base de données.");
            alert.showAndWait();
            return;
        }

        // Charger le bon FXML pour AnonceAcceuilcontrolleur
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AnoncesAccueil.fxml"));

        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Accueil Annonces");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}