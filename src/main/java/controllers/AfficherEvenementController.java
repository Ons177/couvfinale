package controllers;

import entities.Evenement;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import services.ServiceEvenement;

public class AfficherEvenementController {

    @FXML private VBox eventDetails;
    @FXML private Label titleLabel;

    private Evenement selectedEvent;

    public void setSelectedEvent(Evenement event) {
        this.selectedEvent = event;
        afficherDetails();
    }

    private void afficherDetails() {
        if (selectedEvent == null) return;

        eventDetails.getChildren().clear();

        Label titre = new Label("Titre : " + selectedEvent.getTitre());
        Label description = new Label("Description : " + selectedEvent.getDescription());
        Label lieu = new Label("Lieu : " + selectedEvent.getLieu());
        Label dateDebut = new Label("Début : " + selectedEvent.getDateDebut().toString());
        Label dateFin = new Label("Fin : " + selectedEvent.getDateFin().toString());
        Label type = new Label("Type : " + selectedEvent.getTypeEvenement());

        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        description.setWrapText(true);

        eventDetails.getChildren().addAll(titre, description, lieu, dateDebut, dateFin, type);
    }

    @FXML
    private void retourAccueil() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AccueilConducteur.fxml"));
            Stage stage = (Stage) eventDetails.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierEvenement() {
        if (selectedEvent == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifierEvenement.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et lui passer l'événement
            ModifierEvenementController controller = loader.getController();
            controller.setEventToModify(selectedEvent);

            // Afficher la scène
            Stage stage = (Stage) eventDetails.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void supprimerEvenement() {
        if (selectedEvent == null) return;

        // Boîte de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Suppression de l'événement");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

        // Attente de la réponse de l'utilisateur
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ServiceEvenement service = new ServiceEvenement();
                    service.supprimer(selectedEvent.getIdEvenement());

                    // Alerte succès
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès");
                    alert.setContentText("Événement supprimé avec succès !");
                    alert.showAndWait();

                    // Retour à l'accueil
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/AccueilConducteur.fxml"));
                    Stage stage = (Stage) eventDetails.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur");
                    error.setContentText("Erreur lors de la suppression : " + e.getMessage());
                    error.showAndWait();
                }
            }
        });
    }

}

