package controllers;

import entities.Trajet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceTrajet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherTrajetController {

    @FXML
    private FlowPane trajetContainer;

    @FXML
    private VBox emptyState;

    private ServiceTrajet serviceTrajet;

    @FXML
    private void initialize() {
        serviceTrajet = new ServiceTrajet();
        loadTrajets();
    }

    private void loadTrajets() {
        trajetContainer.getChildren().clear();
        try {
            List<Trajet> trajets = serviceTrajet.recuperer();
            System.out.println("Loaded " + trajets.size() + " trajets");

            if (trajets.isEmpty()) {
                emptyState.setVisible(true);
                emptyState.setManaged(true);
                trajetContainer.setVisible(false);
                trajetContainer.setManaged(false);
            } else {
                emptyState.setVisible(false);
                emptyState.setManaged(false);
                trajetContainer.setVisible(true);
                trajetContainer.setManaged(true);

                for (Trajet trajet : trajets) {
                    VBox card = createTrajetCard(trajet);
                    trajetContainer.getChildren().add(card);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading trajets: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les trajets: " + e.getMessage());
        }
    }

    private VBox createTrajetCard(Trajet trajet) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evenement-item");
        card.setUserData(trajet);

        // Style de la carte : fond blanc, coins arrondis, bordure noire, ombre
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 20;" +
                        "-fx-border-color: black;" +       // bordure noire
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0.3, 0, 2);"
        );

        // Header
        HBox header = new HBox(10);
        header.getStyleClass().add("card-header");

        Label titleLabel = new Label(trajet.getVilleDepart() + " → " + trajet.getVilleArrivee());
        titleLabel.getStyleClass().add("event-title");
        titleLabel.setStyle("-fx-text-fill: black;");  // texte noir
        header.getChildren().add(titleLabel);

        // Info Container
        VBox infoContainer = new VBox(8);
        infoContainer.getStyleClass().add("info-container");

        // Date
        HBox dateBox = new HBox(5);
        Label dateLabel = new Label("Date:");
        dateLabel.getStyleClass().add("info-label");
        dateLabel.setStyle("-fx-text-fill: black;");
        Label dateValue = new Label(trajet.getDateDepart().toString());
        dateValue.getStyleClass().add("info-value");
        dateValue.setStyle("-fx-text-fill: black;");
        dateBox.getChildren().addAll(dateLabel, dateValue);

        // Heure
        HBox heureBox = new HBox(5);
        Label heureLabel = new Label("Heure:");
        heureLabel.getStyleClass().add("info-label");
        heureLabel.setStyle("-fx-text-fill: black;");
        Label heureValue = new Label(trajet.getHeureDepart() != null ? trajet.getHeureDepart().toString() : "N/A");
        heureValue.getStyleClass().add("info-value");
        heureValue.setStyle("-fx-text-fill: black;");
        heureBox.getChildren().addAll(heureLabel, heureValue);

        // Prix
        HBox prixBox = new HBox(5);
        Label prixLabel = new Label("Prix:");
        prixLabel.getStyleClass().add("info-label");
        prixLabel.setStyle("-fx-text-fill: black;");
        Label prixValue = new Label(String.format("%.2f TND", trajet.getPrix()));
        prixValue.getStyleClass().add("info-value");
        prixValue.setStyle("-fx-text-fill: black;");
        prixBox.getChildren().addAll(prixLabel, prixValue);

        // Places
        HBox placesBox = new HBox(5);
        Label placesLabel = new Label("Places:");
        placesLabel.getStyleClass().add("info-label");
        placesLabel.setStyle("-fx-text-fill: black;");
        Label placesValue = new Label(String.valueOf(trajet.getNbrPlaces()));
        placesValue.getStyleClass().add("info-value");
        placesValue.setStyle("-fx-text-fill: black;");
        placesBox.getChildren().addAll(placesLabel, placesValue);

        // Bagage
        HBox bagageBox = new HBox(5);
        Label bagageLabel = new Label("Bagage:");
        bagageLabel.getStyleClass().add("info-label");
        bagageLabel.setStyle("-fx-text-fill: black;");
        Label bagageValue = new Label(trajet.getBagage() != null ? trajet.getBagage() : "N/A");
        bagageValue.getStyleClass().add("info-value");
        bagageValue.setStyle("-fx-text-fill: black;");
        bagageBox.getChildren().addAll(bagageLabel, bagageValue);

        infoContainer.getChildren().addAll(dateBox, heureBox, prixBox, placesBox, bagageBox);

        // Actions
        HBox actions = new HBox(10);
        actions.getStyleClass().add("actions-container");

        Button editButton = new Button("Modifier");
        editButton.getStyleClass().addAll("action-button", "edit-button");
        editButton.setOnAction(e -> editTrajet(trajet));

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().addAll("action-button", "delete-button");
        deleteButton.setOnAction(e -> deleteTrajet(trajet));

        actions.getChildren().addAll(editButton, deleteButton);

        card.getChildren().addAll(header, infoContainer, actions);
        return card;
    }

    private void editTrajet(Trajet trajet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTrajet.fxml"));
            Parent root = loader.load();

            ModifierTrajetController controller = loader.getController();
            controller.setTrajetData(trajet);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Trajet");
            stage.showAndWait();

            loadTrajets(); // Refresh after edit
        } catch (IOException e) {
            System.out.println("Error opening edit form: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification: " + e.getMessage());
        }
    }

    private void deleteTrajet(Trajet trajet) {
        try {
            serviceTrajet.supprimer(trajet);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Trajet supprimé avec succès !");
            loadTrajets(); // Refresh after deletion
        } catch (SQLException e) {
            System.out.println("Error deleting trajet: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le trajet: " + e.getMessage());
        }
    }

    @FXML
    private void ajouterTrajet(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTrajet.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) trajetContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Trajet");
        } catch (IOException e) {
            System.out.println("Error opening add form: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire d'ajout: " + e.getMessage());
        }
    }

    @FXML
    private void afficherReservations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) trajetContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Réservations");
        } catch (IOException e) {
            System.out.println("Error opening reservations: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des réservations: " + e.getMessage());
        }
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MenuConducteur.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) trajetContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
        } catch (IOException e) {
            System.out.println("Error returning to home: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à l'accueil: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}