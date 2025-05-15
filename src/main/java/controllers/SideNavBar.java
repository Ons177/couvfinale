package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SideNavBar {

    @FXML
    private HBox compteBtn;

    @FXML
    private Pane content_area;

    @FXML
    public void initialize() {
        // Charger la page ListPosts.fxml par défaut au démarrage
        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/fxml/ListPosts.fxml"));
            content_area.getChildren().clear();
            content_area.getChildren().add(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configurer l'événement de clic sur le bouton compte
        compteBtn.setOnMouseClicked(event -> {
            try {
                Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/fxml/ListPosts.fxml"));
                content_area.getChildren().clear();
                content_area.getChildren().add(newLoadedPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void retourmenu(ActionEvent event) {
        try {
            // Supposons que tu as une classe UserSession ou autre
            String role = UserSession.getCurrentUser().getRole(); // Exemple : "conducteur", "passager"

            String fxmlPath = null;

            switch (role.toLowerCase()) {

                case "conducteur":
                    fxmlPath = "/Views/MenuConducteur.fxml";
                    break;
                case "passager":
                    fxmlPath = "/Views/MenuPassager.fxml";
                    break;
                default:
                    System.out.println("Rôle inconnu : " + role);
                    return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Accueil - " + role);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}