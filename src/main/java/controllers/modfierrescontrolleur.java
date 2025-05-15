package controllers;

import entities.Resanonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import services.resannoservice;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class modfierrescontrolleur {
    private Resanonce reservation;

    public void setReservation(Resanonce r) {
        this.reservation = r;
        preRemplirChamps();
    }
    private void preRemplirChamps() {
        if (reservation != null) {
            ddresm.setValue(reservation.getDateDebuta().toLocalDate());
            dfresm.setValue(reservation.getDateFina().toLocalDate());
        }
    }


    @FXML
    private DatePicker ddresm;

    @FXML
    private DatePicker dfresm;

    @FXML
    void modifierres(ActionEvent event) {
        resannoservice service = new resannoservice();
        if (reservation == null) {
            showAlert("Erreur : aucune réservation à modifier.");
            return;
        }

        if (ddresm.getValue() == null || dfresm.getValue() == null) {
            showAlert("Veuillez sélectionner une date de début et une date de fin.");
            return;
        }

        LocalDate debut = ddresm.getValue();
        LocalDate fin = dfresm.getValue();

        if (fin.isBefore(debut)) {
            showAlert("La date de fin doit être après la date de début.");
            return;
        }

        // Mise à jour de l'objet existant
        reservation.setDateDebuta(Date.valueOf(debut));
        reservation.setDateFina(Date.valueOf(fin));

        try {
            service.modifier(reservation);
            showAlert("Réservation modifiée avec succès !");


        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de la modification de la réservation.");
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Réservation");
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void retourresm(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AnoncesAccueil.fxml"));
            ddresm.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}

