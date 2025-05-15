
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class ReservationEvenementController {

    @FXML private VBox eventDetails;
    @FXML private TextField nbPlacesField;
    @FXML private Label prixLabel;
    @FXML private Label meteoLabel;

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
        String input = nbPlacesField.getText().trim();
        if (input.isEmpty()) {
            prixLabel.setText("0.0 TND");
            return;
        }
        try {
            int nbPlaces = Integer.parseInt(input);
            if (nbPlaces < 0) {
                prixLabel.setText("0.0 TND");
                return;
            }
            double prixTotal = nbPlaces * PRIX_PAR_PLACE;
            // Use Locale.US to ensure period as decimal separator
            prixLabel.setText(String.format(Locale.US, "%.2f TND", prixTotal));
        } catch (NumberFormatException e) {
            prixLabel.setText("0.0 TND");
        }
    }

    @FXML
    private void confirmerReservation(ActionEvent event) {
        try {
            if (selectedEvent == null) {
                showAlert("Erreur", "Aucun événement sélectionné.", Alert.AlertType.ERROR);
                return;
            }

            // Validate number of places
            String nbPlacesText = nbPlacesField.getText().trim();
            System.out.println("Saisie brute (nbPlaces): [" + nbPlacesText + "]");
            if (nbPlacesText.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer le nombre de places.", Alert.AlertType.ERROR);
                return;
            }

            int nbPlaces;
            try {
                nbPlaces = Integer.parseInt(nbPlacesText);
                System.out.println("Nombre de places converti: " + nbPlaces);
            } catch (NumberFormatException e) {
                System.out.println("Erreur de conversion pour nbPlaces: [" + nbPlacesText + "]");
                showAlert("Erreur", "Veuillez entrer un nombre entier valide (ex. 1, 2, 3).", Alert.AlertType.ERROR);
                return;
            }

            if (nbPlaces <= 0) {
                showAlert("Erreur", "Le nombre de places doit être supérieur à 0.", Alert.AlertType.ERROR);
                return;
            }

            // Validate price
            String prixText = prixLabel.getText().replace(" TND", "").trim();
            System.out.println("Saisie brute (prix): [" + prixText + "]");
            if (prixText.isEmpty()) {
                showAlert("Erreur", "Le prix est invalide ou non défini.", Alert.AlertType.ERROR);
                return;
            }

            double prix;
            try {
                // First try parsing with default locale (expects period)
                prix = Double.parseDouble(prixText);
                System.out.println("Prix converti: " + prix);
            } catch (NumberFormatException e) {
                System.out.println("Erreur de conversion standard pour prix: [" + prixText + "]");
                try {
                    // Fallback: Parse with locale that uses comma (e.g., fr_FR)
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("fr", "FR"));
                    symbols.setDecimalSeparator(',');
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
                    Number parsedNumber = decimalFormat.parse(prixText);
                    prix = parsedNumber.doubleValue();
                    System.out.println("Prix converti avec DecimalFormat: " + prix);
                } catch (Exception ex) {
                    System.out.println("Erreur de conversion finale pour prix: [" + prixText + "]");
                    showAlert("Erreur", "Le prix doit être un nombre valide (ex. 50.00 ou 50,00).", Alert.AlertType.ERROR);
                    return;
                }
            }

            if (prix < 0) {
                showAlert("Erreur", "Le prix ne peut pas être négatif.", Alert.AlertType.ERROR);
                return;
            }

            // Proceed with reservation
            Utilisateur user = UserSession.getCurrentUser();
            ReservationEvenement reservation = new ReservationEvenement();
            reservation.setIdEvenement(selectedEvent.getIdEvenement());
            reservation.setIdUtilisateur(user.getId_utilisateur());
            reservation.setNbPlaces(nbPlaces);
            reservation.setDateReservation(LocalDate.now());
            reservation.setStatut_reservation("en attente");
            reservation.setPrix(prix);

            ServiceReservationEvenement service = new ServiceReservationEvenement();
            service.reserver(reservation);

            showAlert("Succès", "Réservation effectuée avec succès !", Alert.AlertType.INFORMATION);
            retourAccueil(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue est survenue : " + e.getMessage(), Alert.AlertType.ERROR);
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
