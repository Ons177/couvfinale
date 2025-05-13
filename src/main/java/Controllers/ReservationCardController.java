package Controllers;

import entities.Reservation;
import entities.Trajet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import services.ServiceReservation;
import services.ServiceTrajet;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;

public class ReservationCardController extends ListCell<Reservation> {
    @FXML
    private VBox cardContainer;
    @FXML
    private Label nomLabel;
    @FXML
    private Label prenomLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label telephoneLabel;
    @FXML
    private Label placesLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label trajetLabel;
    @FXML
    private HBox buttonContainer;
    @FXML
    private Button modifierButton;
    @FXML
    private Button supprimerButton;

    private FXMLLoader loader;
    private ServiceReservation serviceReservation;
    private ServiceTrajet serviceTrajet;

    public ReservationCardController() {
        serviceReservation = new ServiceReservation();
        serviceTrajet = new ServiceTrajet();
    }

    @Override
    protected void updateItem(Reservation reservation, boolean empty) {
        super.updateItem(reservation, empty);

        if (empty || reservation == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/ReservationCard.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            nomLabel.setText("Nom: " + reservation.getNomPassager());
            prenomLabel.setText("Prénom: " + reservation.getPrenomPassager());
            emailLabel.setText("Email: " + reservation.getEmail());
            telephoneLabel.setText("Téléphone: " + reservation.getTelephone());
            placesLabel.setText("Nombre de places: " + reservation.getNombrePlaces());
            
            Trajet trajet = reservation.getTrajet();
            if (trajet != null) {
                trajetLabel.setText("Trajet: " + trajet.getVilleDepart() + " -> " + trajet.getVilleArrivee());
            }

            modifierButton.setOnAction(event -> modifierReservation(reservation));
            supprimerButton.setOnAction(event -> supprimerReservation(reservation));

            setText(null);
            setGraphic(cardContainer);
        }
    }

    private void modifierReservation(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur et injection de la réservation à modifier
            ModifierReservationController controller = loader.getController();
            controller.setReservationData(reservation); // <--- ceci est compatible avec ta méthode

            // Création et affichage d'une nouvelle fenêtre (stage)
            Stage stage = new Stage();
            stage.setTitle("Modifier Réservation");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void supprimerReservation(Reservation reservation) {
        try {
            // Récupérer le trajet associé
            Trajet trajet = reservation.getTrajet();
            if (trajet != null) {
                // Restaurer les places disponibles
                trajet.setNbrPlaces(trajet.getNbrPlaces() + reservation.getNombrePlaces());
                serviceTrajet.modifier(trajet);
            }

            serviceReservation.supprimer(reservation);
            showAlert("Succès", "La réservation a été supprimée avec succès", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Erreur", "Une erreur est survenue lors de la suppression de la réservation", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 