package controllers;

import entities.Reservation;
import entities.Trajet;
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
import services.ServiceReservation;
import services.ServiceTrajet;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherReservationController {

    @FXML
    private FlowPane reservationContainer;

    @FXML
    private VBox emptyState;

    private ServiceReservation serviceReservation;
    private ServiceTrajet serviceTrajet;
    private Trajet selectedTrajet;

    @FXML
    private void initialize() {
        serviceReservation = new ServiceReservation();
        serviceTrajet = new ServiceTrajet();
        reservationContainer.setHgap(24);
        reservationContainer.setVgap(24);
        loadReservations();
    }

    public void setTrajet(Trajet trajet) {
        this.selectedTrajet = trajet;
        loadReservations();
    }

    private void loadReservations() {
        reservationContainer.getChildren().clear();
        try {
            List<Reservation> reservations = selectedTrajet != null
                    ? serviceReservation.recupererParTrajet(selectedTrajet)
                    : serviceReservation.recuperer();
            System.out.println("Loaded " + reservations.size() + " reservations");

            if (reservations.isEmpty()) {
                emptyState.setVisible(true);
                emptyState.setManaged(true);
                reservationContainer.setVisible(false);
                reservationContainer.setManaged(false);
            } else {
                emptyState.setVisible(false);
                emptyState.setManaged(false);
                reservationContainer.setVisible(true);
                reservationContainer.setManaged(true);

                for (Reservation reservation : reservations) {
                    VBox card = createReservationCard(reservation);
                    FlowPane.setMargin(card, new Insets(10));
                    reservationContainer.getChildren().add(card);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les réservations: " + e.getMessage());
        }
    }

    private VBox createReservationCard(Reservation reservation) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evenement-item");
        card.setUserData(reservation);

        // Header
        HBox header = new HBox(10);
        header.getStyleClass().add("card-header");

        Label titleLabel = new Label(reservation.getNomPassager() + " " + reservation.getPrenomPassager());
        titleLabel.getStyleClass().add("event-title");
        header.getChildren().add(titleLabel);

        // Info Container
        VBox infoContainer = new VBox(8);
        infoContainer.getStyleClass().add("info-container");

        // Email
        HBox emailBox = new HBox(5);
        Label emailLabel = new Label("Email:");
        emailLabel.getStyleClass().add("info-label");
        Label emailValue = new Label(reservation.getEmail());
        emailValue.getStyleClass().add("info-value");
        emailBox.getChildren().addAll(emailLabel, emailValue);

        // Téléphone
        HBox telephoneBox = new HBox(5);
        Label telephoneLabel = new Label("Téléphone:");
        telephoneLabel.getStyleClass().add("info-label");
        Label telephoneValue = new Label(reservation.getTelephone() != null ? reservation.getTelephone() : "N/A");
        telephoneValue.getStyleClass().add("info-value");
        telephoneBox.getChildren().addAll(telephoneLabel, telephoneValue);

        // Nombre de places
        HBox placesBox = new HBox(5);
        Label placesLabel = new Label("Places:");
        placesLabel.getStyleClass().add("info-label");
        Label placesValue = new Label(String.valueOf(reservation.getNombrePlaces()));
        placesValue.getStyleClass().add("info-value");
        placesBox.getChildren().addAll(placesLabel, placesValue);

        // Trajet
        HBox trajetBox = new HBox(5);
        Label trajetLabel = new Label("Trajet:");
        trajetLabel.getStyleClass().add("info-label");
        Label trajetValue = new Label(reservation.getTrajet().getVilleDepart() + " → " + reservation.getTrajet().getVilleArrivee());
        trajetValue.getStyleClass().add("info-value");
        trajetBox.getChildren().addAll(trajetLabel, trajetValue);

        infoContainer.getChildren().addAll(emailBox, telephoneBox, placesBox, trajetBox);

        // Actions
        HBox actions = new HBox(10); // Fixed typo: removed invalid "PTPTPTPTPT"
        actions.getStyleClass().add("actions-container");

        Button editButton = new Button("Modifier");
        editButton.getStyleClass().addAll("action-button", "edit-button");
        editButton.setOnAction(e -> editReservation(reservation));

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().addAll("action-button", "delete-button");
        deleteButton.setOnAction(e -> deleteReservation(reservation));

        actions.getChildren().addAll(editButton, deleteButton);

        card.getChildren().addAll(header, infoContainer, actions);
        return card;
    }

    private void editReservation(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Parent root = loader.load();

            ModifierReservationController controller = loader.getController();
            controller.setReservationData(reservation);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réservation");
            stage.showAndWait();

            loadReservations(); // Refresh after edit
        } catch (IOException e) {
            System.out.println("Error opening edit form: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le formulaire de modification: " + e.getMessage());
        }
    }

    private void deleteReservation(Reservation reservation) {
        try {
            serviceReservation.supprimer(reservation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation supprimée avec succès !");
            loadReservations(); // Refresh after deletion
        } catch (SQLException e) { // Fixed syntax: corrected "lets)" to proper catch block
            System.out.println("Error deleting reservation: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la réservation: " + e.getMessage());
        }
    }



    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/accueiltrajets.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) reservationContainer.getScene().getWindow();
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