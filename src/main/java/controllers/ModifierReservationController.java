package controllers;

import entities.Reservation;
import entities.Trajet;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceReservation;
import services.ServiceTrajet;

import java.sql.SQLException;
import java.util.List;

public class ModifierReservationController {

    @FXML
    private TextField nomPassagerField;

    @FXML
    private TextField prenomPassagerField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField nombrePlacesField;

    @FXML
    private ComboBox<Trajet> trajetComboBox;


    private ServiceReservation serviceReservation;
    private ServiceTrajet serviceTrajet;
    private Reservation reservation;

    @FXML
    private void initialize() {
        serviceReservation = new ServiceReservation();
        serviceTrajet = new ServiceTrajet();
        loadTrajets();

        // Add listeners to update summary label dynamically
        nomPassagerField.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        prenomPassagerField.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        telephoneField.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        nombrePlacesField.textProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        trajetComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
    }

    public void setReservationData(Reservation reservation) {
        this.reservation = reservation;
        nomPassagerField.setText(reservation.getNomPassager());
        prenomPassagerField.setText(reservation.getPrenomPassager());
        emailField.setText(reservation.getEmail());
        telephoneField.setText(reservation.getTelephone());
        nombrePlacesField.setText(String.valueOf(reservation.getNombrePlaces()));
        trajetComboBox.setValue(reservation.getTrajet());
        updateSummary();
    }

    private void loadTrajets() {
        try {
            List<Trajet> trajets = serviceTrajet.recuperer();
            trajetComboBox.setItems(FXCollections.observableArrayList(trajets));
            trajetComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Trajet trajet, boolean empty) {
                    super.updateItem(trajet, empty);
                    if (empty || trajet == null) {
                        setText(null);
                    } else {
                        setText(trajet.getVilleDepart() + " → " + trajet.getVilleArrivee());
                    }
                }
            });
            trajetComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Trajet trajet, boolean empty) {
                    super.updateItem(trajet, empty);
                    if (empty || trajet == null) {
                        setText(null);
                    } else {
                        setText(trajet.getVilleDepart() + " → " + trajet.getVilleArrivee());
                    }
                }
            });
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les trajets: " + e.getMessage());
        }
    }

    @FXML
    private void modifierReservation() {
        try {
            // Validation
            if (nomPassagerField.getText().isEmpty() || prenomPassagerField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || nombrePlacesField.getText().isEmpty() ||
                    trajetComboBox.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            reservation.setNomPassager(nomPassagerField.getText());
            reservation.setPrenomPassager(prenomPassagerField.getText());
            reservation.setEmail(emailField.getText());
            reservation.setTelephone(telephoneField.getText());
            reservation.setNombrePlaces(Integer.parseInt(nombrePlacesField.getText()));
            reservation.setTrajet(trajetComboBox.getValue());

            serviceReservation.modifier(reservation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation modifiée avec succès !");

            // Close window
            Stage stage = (Stage) nomPassagerField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nombre de places doit être un nombre valide.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de modifier la réservation: " + e.getMessage());
        }
    }

    @FXML
    private void annuler() {
        Stage stage = (Stage) nomPassagerField.getScene().getWindow();
        stage.close();
    }

    private void updateSummary() {
        StringBuilder summary = new StringBuilder("Résumé de la réservation modifiée:\n");

        summary.append("Nom: ").append(nomPassagerField.getText().isEmpty() ? "Non spécifié" : nomPassagerField.getText()).append("\n");
        summary.append("Prénom: ").append(prenomPassagerField.getText().isEmpty() ? "Non spécifié" : prenomPassagerField.getText()).append("\n");
        summary.append("Email: ").append(emailField.getText().isEmpty() ? "Non spécifié" : emailField.getText()).append("\n");
        summary.append("Téléphone: ").append(telephoneField.getText().isEmpty() ? "Non spécifié" : telephoneField.getText()).append("\n");
        summary.append("Nombre de places: ").append(nombrePlacesField.getText().isEmpty() ? "Non spécifié" : nombrePlacesField.getText()).append("\n");
        summary.append("Trajet: ").append(trajetComboBox.getValue() == null ? "Non spécifié" :
                trajetComboBox.getValue().getVilleDepart() + " → " + trajetComboBox.getValue().getVilleArrivee());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}