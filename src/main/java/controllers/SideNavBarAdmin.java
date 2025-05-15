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

public class SideNavBarAdmin {


    @FXML
    private HBox compteBtn;

    @FXML
    private Pane content_area;

    @FXML
    public void initialize() {
        // Charger la page ListPosts.fxml par défaut au démarrage
        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/fxml/Admin/ListPostsAdmin.fxml"));
            content_area.getChildren().clear();
            content_area.getChildren().add(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Configurer l'événement de clic sur le bouton compte
        compteBtn.setOnMouseClicked(event -> {
            try {
                Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/fxml/Admin/ListPostsAdmin.fxml"));
                content_area.getChildren().clear();
                content_area.getChildren().add(newLoadedPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    public void retourmenuadmin(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de l'interface admin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MenuAdmin.fxml"));
            Parent root = loader.load();

            // Obtenir la scène depuis l’événement
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Accueil Admin");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
