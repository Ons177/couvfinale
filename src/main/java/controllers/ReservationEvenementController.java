package controllers;

import entities.Evenement;
import entities.ReservationEvenement;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import services.MeteoService;
import services.ServiceReservationEvenement;

import java.time.LocalDate;

public class ReservationEvenementController {

    @FXML private VBox eventDetails;
    @FXML private TextField nbPlacesField;
    @FXML private Label prixLabel;
    @FXML private Label meteoLabel; // 👈 Nouveau label météo

    private Evenement selectedEvent;
    private static final double PRIX_PAR_PLACE = 10.0;

    @FXML
    public void initialize() {
        nbPlacesField.textProperty().addListener((obs, oldValue, newValue) -> updatePrix());
    }

    public void setSelectedEvent(Evenement event) {
        this.selectedEvent = event;
        displayEventDetails();
    }

    private void displayEventDetails() {
        if (selectedEvent == null || eventDetails == null) return;

        VBox detailsBox = new VBox(10);
        detailsBox.getStyleClass().add("event-card");
        detailsBox.setPadding(new Insets(15));

        Label titleLabel = new Label(selectedEvent.getTitre());
        titleLabel.getStyleClass().add("event-title");

        Label dateLabel = new Label("Date: " + selectedEvent.getDateDebut().toString());
        dateLabel.getStyleClass().add("event-date");

        Label lieuLabel = new Label("Lieu: " + selectedEvent.getLieu());
        lieuLabel.getStyleClass().add("event-lieu");

        detailsBox.getChildren().addAll(titleLabel, dateLabel, lieuLabel);
        eventDetails.getChildren().setAll(detailsBox);
    }

    private void updatePrix() {
        try {
            int nbPlaces = Integer.parseInt(nbPlacesField.getText());
            if (nbPlaces < 0) throw new NumberFormatException();
            double prixTotal = nbPlaces * PRIX_PAR_PLACE;
            prixLabel.setText(String.format("%.2f €", prixTotal));
        } catch (NumberFormatException e) {
            prixLabel.setText("0.0 €");
        }
    }

    @FXML
    private void confirmerReservation(ActionEvent event) {
        try {
            if (selectedEvent == null) {
                showAlert("Erreur", "Aucun événement sélectionné.", Alert.AlertType.ERROR);
                return;
            }

            String nbPlacesText = nbPlacesField.getText();
            if (nbPlacesText == null || nbPlacesText.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer le nombre de places.", Alert.AlertType.ERROR);
                return;
            }

            int nbPlaces = Integer.parseInt(nbPlacesText);
            if (nbPlaces <= 0) {
                showAlert("Erreur", "Le nombre de places doit être supérieur à 0.", Alert.AlertType.ERROR);
                return;
            }
            Utilisateur user = UserSession.getCurrentUser();
            double prix = Double.parseDouble(prixLabel.getText().replace(" €", ""));

            ReservationEvenement reservation = new ReservationEvenement();
            reservation.setIdEvenement(selectedEvent.getIdEvenement());
            reservation.setIdUtilisateur(user.getId_utilisateur()); // Remplacez par l'utilisateur connecté
            reservation.setNbPlaces(nbPlaces);
            reservation.setDateReservation(LocalDate.now());
            reservation.setStatut("en attente");
            reservation.setPrix(prix); // Assurez-vous que ce champ existe dans votre classe

            ServiceReservationEvenement service = new ServiceReservationEvenement();
            service.reserver(reservation);

            showAlert("Succès", "Réservation effectuée avec succès !", Alert.AlertType.INFORMATION);
            retourAccueil(event);

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le nombre de places doit être un entier.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Accueil.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page d'accueil", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void afficherMeteo(ActionEvent event) {
        if (selectedEvent == null) {
            showAlert("Erreur", "Aucun événement sélectionné.", Alert.AlertType.ERROR);
            return;
        }

        String ville = selectedEvent.getLieu();
        String resultat = MeteoService.getTemperatureEtDescription(ville);
        meteoLabel.setText(resultat);
    }

    private void showAlert(String titre, String contenu, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
