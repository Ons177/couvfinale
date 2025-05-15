package controllers;

import entities.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import services.ServiceEvenement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AcceuilController {

    @FXML private FlowPane eventsList;
    private Evenement selectedEvent;
    @FXML private TextField searchField;
    @FXML
    private Button MenuPassager;


    @FXML
    public void initialize() {
        loadEvents("");
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            loadEvents(newText); // Recharge avec filtre
        });
    }

    private void loadEvents(String filter) {
        try {
            ServiceEvenement service = new ServiceEvenement();
            List<Evenement> events = service.getAll(); // Récupère tous

            // Si un filtre est appliqué, on le garde
            if (filter != null && !filter.isEmpty()) {
                events.removeIf(event -> !event.getTitre().toLowerCase().contains(filter.toLowerCase()));
            }

            eventsList.getChildren().clear();

            for (Evenement event : events) {
                VBox eventCard = new VBox(10);
                eventCard.getStyleClass().add("event-card");
                eventCard.setPrefWidth(320);
                eventCard.setPadding(new Insets(15));

                Label titleLabel = new Label(event.getTitre());
                titleLabel.getStyleClass().add("event-title");
                titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

                ImageView eventImage = new ImageView(new Image(getClass().getResource("/images/evenement.png").toExternalForm()));
                eventImage.setFitWidth(320);
                eventImage.setFitHeight(240);
                eventImage.setPreserveRatio(true);

                Label dateLabel = new Label("Disponible à " + event.getDateDebut().toString());
                Label lieuLabel = new Label("Lieu: " + event.getLieu());

                HBox buttonsBox = new HBox(10);
                buttonsBox.setAlignment(Pos.CENTER);

                Button consulterButton = new Button("Consulter");
                consulterButton.setOnAction(e -> viewEventDetails(event));

                Button reserverButton = new Button("Reserver");
                reserverButton.setOnAction(e -> reserveEvent(event));

                buttonsBox.getChildren().addAll(consulterButton, reserverButton);

                eventCard.getChildren().addAll(titleLabel, eventImage, dateLabel, lieuLabel, buttonsBox);
                eventsList.getChildren().add(eventCard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les événements !");
        }
    }

    private void viewEventDetails(Evenement event) {
        try {
            System.out.println("Chargement de l'événement : " + event.getTitre());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsulterEvenement.fxml"));
            Parent root = loader.load();

            ConsulterEvenementController controller = loader.getController();
            if (controller != null) {
                controller.setSelectedEvent(event);
            } else {
                System.out.println("Contrôleur null !");
                showAlert("Erreur", "Erreur lors du chargement du contrôleur");
                return;
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) eventsList.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page de détails");
        }
    }


    /*@FXML
    private void afficherEvenement(ActionEvent event) {
        if (selectedEvent == null) {
            showAlert("Erreur", "Veuillez sélectionner un événement d'abord !");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherEvenement.fxml"));
            Parent root = loader.load();

            AfficherEvenementController controller = loader.getController();
            controller.setSelectedEvent(selectedEvent);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page Afficher");
        }
    }*/

    private void reserveEvent(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReservationEvenement.fxml"));
            Parent root = loader.load();
            
            ReservationEvenementController controller = loader.getController();
            controller.setSelectedEvent(event);
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) eventsList.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page de réservation");
        }
    }

    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(contenu);
        alert.showAndWait();
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

    public void MenuPassager(ActionEvent actionEvent) { try {
        // Load the AcceuilReclamation FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MenuPassager.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);

        // Get the current stage
        Stage stage = (Stage) MenuPassager.getScene().getWindow();

        // Set the new scene
        stage.setScene(scene);
        stage.setTitle("MenuPassager");
        stage.show();
    } catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur de navigation");
        alert.setContentText("Une erreur est survenue lors de la navigation: " + e.getMessage());
        alert.showAndWait();
    }
    }


}


