package controllers;

import entities.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceEvenement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccueilAdminController {

    @FXML
    private FlowPane flowPaneEvents;

    @FXML
    public void initialize() {
        ServiceEvenement service = new ServiceEvenement();
        try {
            List<Evenement> events = service.getEnAttente(); // ✅ seulement ceux en attente

            for (Evenement e : events) {
                VBox card = creerCarteEvenement(e); // ✅ nom cohérent
                flowPaneEvents.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void afficherEvenements() {
        ServiceEvenement service = new ServiceEvenement();

        try {
            List<Evenement> evenements = service.getAll();

            for (Evenement event : evenements) {
                VBox card = creerCarteEvenement(event);
                flowPaneEvents.getChildren().add(card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox creerCarteEvenement(Evenement event) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: #cccccc;");
        box.setPrefWidth(320);

        // Image de l'événement
        ImageView eventImage = new ImageView(new Image(getClass().getResource("/images/evenement.png").toExternalForm()));
        eventImage.setFitWidth(320);
        eventImage.setFitHeight(200);
        eventImage.setPreserveRatio(true);

        Label titre = new Label(event.getTitre());
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label date = new Label("Du " + event.getDateDebut() + " au " + event.getDateFin());
        Label lieu = new Label("Lieu : " + event.getLieu());

        Button btnGerer = new Button("Gérer");
        btnGerer.setOnAction(e -> ouvrirInterfaceGestion(event));

        box.getChildren().addAll(eventImage, titre, date, lieu, btnGerer);
        return box;
    }


    private void ouvrirInterfaceGestion(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GererEvenement.fxml"));
            Parent root = loader.load();

            // Passer l'événement au contrôleur
            GererEvenementController controller = loader.getController();
            controller.setEvenement(event);

            // Remplacer la scène actuelle
            Stage stage = (Stage) flowPaneEvents.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
