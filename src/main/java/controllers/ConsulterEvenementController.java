package controllers;

import entities.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class ConsulterEvenementController {

    @FXML private Label titleLabel;
    @FXML private Label eventTitle;
    @FXML private Label eventDate;
    @FXML private Label eventLocation;
    @FXML private Label eventDescription;

    private Evenement selectedEvent;

    public void setSelectedEvent(Evenement event) {
        this.selectedEvent = event;
        displayEventDetails();
        System.out.println(event.getDateDebut());
        System.out.println(event.getDescription());

    }

    private void displayEventDetails() {
        if (selectedEvent == null) {
            return;
        }

        eventTitle.setText(selectedEvent.getTitre());
        eventDate.setText("Date: " + selectedEvent.getDateDebut().toString());
        eventLocation.setText("Lieu: " + selectedEvent.getLieu());
        eventDescription.setText("Description: " + selectedEvent.getDescription());
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Accueil.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
