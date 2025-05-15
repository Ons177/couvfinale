package controllers;

import entities.Resanonce;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import services.resannoservice;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
public class voirmesreservationscontrolleur implements Initializable {
    private Utilisateur currentUser;
    @FXML
    private ScrollPane scrollid;

    @FXML
    private VBox vboxresid;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = UserSession.getCurrentUser();
        afficherReservations();
    }

    // Méthode pour récupérer et afficher les réservations
    private void afficherReservations() {
        resannoservice service = new resannoservice();
        List<Resanonce> reservations;
        try {
            if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                // L'admin voit toutes les réservations
                reservations = service.recuperer();
            } else {
                reservations = service.getReservationsPourUtilisateur(currentUser.getId_utilisateur());
            }        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
       }
        try {

            for (Resanonce reservation : reservations) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservationitem.fxml"));
                Parent root = loader.load();

                ResanonceItemController controller = loader.getController();
                controller.setData(reservation);
                vboxresid.getChildren().add(root);


            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void retouraccresaff(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AnoncesAccueil.fxml"));
            vboxresid.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}

