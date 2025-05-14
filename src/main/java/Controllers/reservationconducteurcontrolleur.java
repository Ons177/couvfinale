package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import services.resannoservice;
import entities.Resanonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.SQLException;

public class reservationconducteurcontrolleur {

    @FXML
    private Label titreAnnoncecr;

    @FXML
    private Label dateReservationcr;

    @FXML
    private Label dateDebutcr;

    @FXML
    private Label dateFincr;

    @FXML
    private Label statutcr;

    private entities.Resanonce reservation;
    private resannoservice reservationService = new resannoservice();

    public void setData(entities.Resanonce reservation) {
        this.reservation = reservation;
        titreAnnoncecr.setText("Annonce réservée : " + reservation.getAnonce().getTitre());
        dateReservationcr.setText("Date réservation : " + reservation.getDateReservationa().toString());
        dateDebutcr.setText("Date début : " + reservation.getDateDebuta().toString());
        dateFincr.setText("Date fin : " + reservation.getDateFina().toString());
        statutcr.setText("Statut : " + reservation.getStatutano());
    }

    @FXML
    void accepterReservation(ActionEvent event) {
        if (reservation != null) {
            try {
                reservation.setStatuta(Resanonce.StatutReservation.confirmée);
                reservationService.update(reservation);
                statutcr.setText("Statut : Confirmée");
                showAlert("Réservation acceptée", "La réservation a été confirmée.");


            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void refuserReservation(ActionEvent event) throws SQLException {
        if (reservation != null) {
            boolean confirmed = showConfirmation("Refuser cette réservation ?", "Cette action est irréversible.");
            if (confirmed) {
                try {
                    reservation.setStatuta(Resanonce.StatutReservation.refusée);
                    reservationService.update(reservation);

                    showAlert("Réservation refusée", "La réservation a été refusée.");
                }
                catch(SQLException e){
                    System.out.println(e.getMessage());
                }

            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    public void voirDetails(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uneAnonce.fxml"));
            Parent root = loader.load();

            uneanonceaffichagecontrolleur controller = loader.getController();
            controller.setAnonce(reservation.getAnonce()); // tu crées cette méthode juste après

            dateDebutcr.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

