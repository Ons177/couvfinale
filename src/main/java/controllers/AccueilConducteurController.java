package controllers;

import entities.Evenement;
import entities.Utilisateur;
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

public class AccueilConducteurController {

    @FXML private FlowPane eventsList;
    private Evenement selectedEvent;

    @FXML
    public void initialize() {
        loadEvents();
    }

    private void loadEvents() {
        try {
            Utilisateur user = UserSession.getCurrentUser(); // Utilisateur connecté
            ServiceEvenement service = new ServiceEvenement();
            List<Evenement> events = service.getByCreateur(user.getId_utilisateur()); // Seulement ses événements
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
                dateLabel.getStyleClass().add("event-date");

                Label lieuLabel = new Label("Lieu: " + event.getLieu());
                lieuLabel.getStyleClass().add("event-lieu");

                HBox buttonsBox = new HBox(10);
                buttonsBox.setAlignment(Pos.CENTER);

                Button afficherButton = new Button("Afficher");
                afficherButton.getStyleClass().add("afficher-button");
                afficherButton.setOnAction(e -> afficherEvenementDepuisCarte(event));

                buttonsBox.getChildren().addAll(afficherButton);

                eventCard.getChildren().addAll(
                        titleLabel,
                        eventImage,
                        dateLabel,
                        lieuLabel,
                        buttonsBox
                );

                eventsList.getChildren().add(eventCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les événements !");
        }
    }

    private void afficherEvenementDepuisCarte(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherEvenement.fxml"));
            Parent root = loader.load();

            AfficherEvenementController controller = loader.getController();
            controller.setSelectedEvent(event);

            Scene scene = new Scene(root);
            Stage stage = (Stage) eventsList.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page Afficher");
        }
    }

    private void viewEventDetails(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConsulterEvenement.fxml"));
            Parent root = loader.load();

            ConsulterEvenementController controller = loader.getController();
            if (controller != null) {
                controller.setSelectedEvent(event);
            } else {
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

    @FXML
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
    }

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
    private void allerConsulterReservation(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ConsulterReservation.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page des réservations");
        }
    }

    @FXML
    private void allerAjouterEvenement(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AjouterEvenement.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page d'ajout");
        }
    }
    public void allermenuconducteur(ActionEvent actionEvent) { try {
        // Load the AcceuilReclamation FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MenuConducteur.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);

        // Get the current stage
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        // Set the new scene
        stage.setScene(scene);
        stage.setTitle("Menuconducteur");
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
