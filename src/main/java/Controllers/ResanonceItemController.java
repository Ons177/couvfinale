package Controllers;

import entities.Resanonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import services.resannoservice;

import java.io.IOException;
import java.sql.SQLException;

public class ResanonceItemController {

    @FXML
    private Label ddresitem;

    @FXML
    private Label dfresitem;

    @FXML
    private Label dritemres;

    @FXML
    private Label stresitem;

    @FXML
    private Label titresitem;
    private Resanonce reservation;
    public void setData(Resanonce reservation) {
        this.reservation = reservation;
        titresitem.setText("Annonce réservée : " + reservation.getAnonce().getTitre());
        dritemres.setText("Date réservation : " + reservation.getDateReservationa().toString());
        ddresitem.setText("Date début : " + reservation.getDateDebuta().toString());
        dfresitem.setText("Date fin : " + reservation.getDateFina().toString());
        stresitem.setText("Statut : " + reservation.getStatutano());
    }

    @FXML
    void modifierresc(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierres.fxml"));
            Parent root = loader.load();
            modfierrescontrolleur controller = loader.getController();
            controller.setReservation(reservation);
            ddresitem.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void supprimerresc(ActionEvent event) {
        try {
            resannoservice service = new resannoservice();
            service.delete(reservation);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("supprimer reservation");
            alert.setContentText("reservation supprimée avec succés");
            alert.showAndWait();

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }


    }

    @FXML
    void voirdetailsanoncesp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uneAnonce.fxml"));
            Parent root = loader.load();

            uneanonceaffichagecontrolleur controller = loader.getController();
            controller.setAnonce(reservation.getAnonce()); // tu crées cette méthode juste après

            ddresitem.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
