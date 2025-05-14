package Controllers;

import entities.Anonce;
import entities.Vehicule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import services.VehicleService;

import java.io.IOException;

public class uneanonceaffichagecontrolleur {

    @FXML
    private Label labelcouleura;

    @FXML
    private Label labelmodelea;

    @FXML
    private Label marquelabela;

    @FXML
    private Label nbreplacelabela;



    @FXML
    private Label addaff;

    @FXML
    private Label dateaff;

    @FXML
    private Label desaff;

    @FXML
    private Label idtitreanonceaff;

    @FXML
    private ImageView imgvaff;

    @FXML
    private Label prixaff;

    private Anonce anonce;
    private WebEngine webEngine;
    VehicleService vehicleService = new VehicleService();
    private Vehicule vehicule;

    public void setAnonce(Anonce anonce) {
        this.anonce = anonce;
        this.vehicule=vehicleService.getVehiculeByIdVehicule(anonce.getVehiculeId());
        System.out.println("ID véhicule de l'annonce : " + anonce.getVehiculeId());
        afficherDetails();
    }

    // Remplir les détails de l'annonce dans les éléments FXML
    private void afficherDetails() {
        idtitreanonceaff.setText(anonce.getTitre());
        dateaff.setText("Disponible à partir du : " + anonce.getDateDisponibilite().toString());
        prixaff.setText("Prix : " + anonce.getPrixParJour() + " TND");
        addaff.setText("Lieu : " + anonce.getAdresse());
        desaff.setText("Description : " + anonce.getDescription());
        labelmodelea.setText("Modele :"+ vehicule.getModele());
        labelcouleura.setText("Couleur :"+ vehicule.getCouleur());
        marquelabela.setText("Marque :"+ vehicule.getMarque());
        nbreplacelabela.setText("nombre de places :" + vehicule.getNbre_places());


        // Charger l'image si elle existe
        if (anonce.getPhotoVehicule() != null && !anonce.getPhotoVehicule().isEmpty()) {
            try {
                Image image = new Image(anonce.getPhotoVehicule(), true);
                imgvaff.setImage(image);
            } catch (Exception e) {
                System.out.println("Erreur chargement image : " + e.getMessage());
            }
        }

        // Initialisation du WebEngine pour charger la carte dans le WebView
    }




    @FXML
    void retouracc(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AnoncesAccueil.fxml"));
            ((javafx.scene.Node) event.getSource()).getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
