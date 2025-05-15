package controllers;

import entities.Anonce;
import entities.Resanonce;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import services.resannoservice;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ajouterreservationcontrolleur {

    @FXML
    private DatePicker datedebr;

    @FXML
    private DatePicker datefinr;

    @FXML
    private Label prixresa;

    private Anonce anonce;
    Utilisateur currentuser = UserSession.getCurrentUser();



    public void setAnonce(Anonce a) {
        this.anonce = a;
    }

    @FXML
    void confirmerres(ActionEvent event) {
        resannoservice service = new resannoservice();

        if (anonce == null) {
            System.out.println("Erreur : aucune annonce liée à la réservation.");
            return;
        }

        if (datedebr.getValue() == null || datefinr.getValue() == null) {
            showAlert("Veuillez sélectionner une date de début et une date de fin.");
            return;
        }

        LocalDate debut = datedebr.getValue();
        LocalDate fin = datefinr.getValue();

        if (fin.isBefore(debut)) {
            showAlert("La date de fin doit être après la date de début.");
            return;
        }
        if (debut.isBefore(anonce.getDateDisponibilite().toLocalDate())) {
            showAlert("La date de début doit être postérieure ou égale à la date de disponibilité du véhicule ("
                    + anonce.getDateDisponibilite().toLocalDate() + ").");
            return;
        }


        int utilisateurId = currentuser.getId_utilisateur();

        Resanonce res = new Resanonce();
        res.setAnonceId(anonce);
        res.setUtilisateurId(utilisateurId);
        res.setDateDebuta(Date.valueOf(debut));
        res.setDateFina(Date.valueOf(fin));
        res.setStatuta(Resanonce.StatutReservation.en_attente);// Enum

        try {
            if (!service.isDateRangeAvailable(anonce.getIdAnonce(), debut, fin)) {
                showAlert("Ce véhicule est déjà réservé pour la période sélectionnée.");
                return;
            }
            service.ajouter(res);
            showAlert("Réservation enregistrée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ajout de la réservation.");
        }


    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Réservation");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void retouraccra(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AnoncesAccueil.fxml"));
            datedebr.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }




}

