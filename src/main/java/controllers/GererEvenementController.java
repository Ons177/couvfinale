package controllers;

import entities.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;

public class GererEvenementController {

    @FXML private Label titreLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label lieuLabel;
    @FXML private Label dateLabel;
    @FXML private Label heureLabel;

    private Evenement evenement;

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
        titreLabel.setText(evenement.getTitre());
        descriptionLabel.setText(evenement.getDescription());
        lieuLabel.setText("Lieu : " + evenement.getLieu());
        dateLabel.setText("Du " + evenement.getDateDebut() + " au " + evenement.getDateFin());
        heureLabel.setText("Heure : " + evenement.getHeure());
    }

    @FXML
    private void accepterEvenement() {
        try {
            ServiceEvenement service = new ServiceEvenement();
            service.accepterEvenement(evenement.getIdEvenement());
            System.out.println("Événement accepté : " + evenement.getTitre());
            retournerAccueil();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refuserEvenement() {
        try {
            ServiceEvenement service = new ServiceEvenement();
            service.refuserEvenement(evenement.getIdEvenement());
            System.out.println("Événement refusé : " + evenement.getTitre());
            retournerAccueil();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retournerAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titreLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
