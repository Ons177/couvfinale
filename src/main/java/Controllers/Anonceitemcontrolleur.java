package Controllers;

import entities.Anonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Anonceitemcontrolleur {

    @FXML
    private Label datelabel;

    @FXML
    private Label emplacementlabel;

    @FXML
    private ImageView imageAnonce;

    @FXML
    private Label prixlabel;

    @FXML
    private Label titrelabel;
    private Anonce anonce;

    // Méthode d'initialisation de l'interface
    public void initialize(Anonce anonce) {
        this.anonce = anonce;
        titrelabel.setText(anonce.getTitre());

        if (anonce.getDateDisponibilite() != null) {
            datelabel.setText("Disponible le:" + anonce.getDateDisponibilite().toString());
        } else {
            datelabel.setText("Date non spécifiée");
        }

        emplacementlabel.setText("Lieu:" + anonce.getAdresse());

        prixlabel.setText(String.format("Prix : %.2f TND", anonce.getPrixParJour()));

        if (anonce.getPhotoVehicule() != null && !anonce.getPhotoVehicule().isEmpty()) {
            try {
                Image image = new Image(anonce.getPhotoVehicule(), true);
                imageAnonce.setImage(image);
            } catch (Exception e) {
                System.out.println("Erreur chargement image : " + e.getMessage());
            }
        }
    }





 @FXML
    void Consulterit(ActionEvent event) {
        if (anonce != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uneAnonce.fxml"));
                Parent root = loader.load();

                uneanonceaffichagecontrolleur controller = loader.getController();
                controller.setAnonce(anonce); // tu crées cette méthode juste après

                titrelabel.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur : l'annonce est null, impossible de consulter.");
        }

    }
    @FXML
    void resererveranonce(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterreservation.fxml"));
            Parent root = loader.load();
            ajouterreservationcontrolleur controller = loader.getController();

            controller.setAnonce(this.anonce);

            titrelabel.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}

