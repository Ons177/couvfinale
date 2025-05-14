package Controllers;

import entities.Anonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import services.anonceservice;

import java.io.IOException;
import java.sql.SQLException;

public class useranonceitemcontrolleur {

    private Anonce anonce;

    @FXML
    private ImageView imageanonceuserid;

    @FXML
    private Label adresseuseran;

    @FXML
    private Label dateuseranon;

    @FXML
    private Label prixuseran;

    @FXML
    private Label titreanonceuserid;

    public void setAnonce(Anonce anonce) {
        this.anonce = anonce;
        // ici tu affiches les infos dans les labels
        titreanonceuserid.setText(anonce.getTitre());
        dateuseranon.setText("Disponible le :" + anonce.getDateDisponibilite().toString());
        adresseuseran.setText("Lieu:" + anonce.getAdresse());
        prixuseran.setText(String.format("Prix : %.2f TND", anonce.getPrixParJour()));
        if (anonce.getPhotoVehicule() != null && !anonce.getPhotoVehicule().isEmpty()) {
            imageanonceuserid.setImage(new Image(anonce.getPhotoVehicule(), true));
        }
    }


    @FXML
    void consulteranonce(ActionEvent event) {
        if (anonce != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uneAnonce.fxml"));
                Parent root = loader.load();

                uneanonceaffichagecontrolleur controller = loader.getController();
                controller.setAnonce(anonce); // tu crées cette méthode juste après

                imageanonceuserid.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur : l'annonce est null, impossible de consulter.");
        }

    }

    @FXML
    void modifieranonce(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierAnonce.fxml"));
            Parent root = loader.load();

            modifierAnoncecontrolleur controller = loader.getController();
            controller.setAnonce(anonce); // envoie l'annonce à modifier

            imageanonceuserid.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void supprimeranonce(ActionEvent event) {
        anonceservice service = new anonceservice();
        try {
            service.delete(anonce);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("supprimer anonce");
            alert.setContentText("anonce supprimée avec succés");
            alert.showAndWait();
            ((FlowPane) imageanonceuserid.getParent().getParent()).getChildren().remove(imageanonceuserid.getParent());

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }

    }

}


