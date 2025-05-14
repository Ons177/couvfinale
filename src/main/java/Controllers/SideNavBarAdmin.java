package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/FXML/Admin/ListPostsAdmin.fxml"));
            content_area.getChildren().clear();
            content_area.getChildren().add(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Configurer l'événement de clic sur le bouton compte
        compteBtn.setOnMouseClicked(event -> {
            try {
                Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/FXML/Admin/ListPostsAdmin.fxml"));
                content_area.getChildren().clear();
                content_area.getChildren().add(newLoadedPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
