package controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entities.Anonce;
import entities.Utilisateur;
import entities.Vehicule;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;
import services.VehicleService;
import services.anonceservice;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ajouteranoncecontrolleur {
    private File imageFile;
    private double latitude;
    private double longitude;
    private Utilisateur currentUser = UserSession.getCurrentUser();


    @FXML
    private ComboBox<String> comboVehicule;
    @FXML
     private TextField idadressa;

     @FXML
     private DatePicker iddatea;

     @FXML
     private TextArea iddesa;


     @FXML
     private TextField idprixja;

     @FXML
     private TextField idta;

     @FXML
     private ImageView imageViewAn;
    @FXML
    private WebView webviewid;
    private VehicleService vehicleService = new VehicleService();


    public void initialize() {
        WebEngine engine = webviewid.getEngine();
        URL url = getClass().getResource("/map.html");
        engine.load(url.toExternalForm());

        // Attendre que la page soit entièrement chargée
        engine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaApp", this);
                System.out.println("Pont Java/JS établi !");
            }
        });
        List<Vehicule> vehicules = vehicleService.getVehiculesByIdUtilisateur(currentUser.getId_utilisateur());

        // Créer une liste de matricules
        List<String> matricules = new ArrayList<>();
        for (Vehicule vehicule : vehicules) {
            matricules.add(vehicule.getMatricule());
        }

        // Ajouter les matricules au ComboBox
        comboVehicule.setItems(FXCollections.observableArrayList(matricules));
    }
    public void onLocationSelected(double lat, double lng) {
        System.out.println("Latitude : " + lat + ", Longitude : " + lng);
        this.latitude = lat;
        this.longitude = lng;
    }


    private final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dbpsiatzl",
            "api_key", "619151831588127",
            "api_secret", "SK4cGx6bHN1yQKKmxHHr7T5livo"
    ));

     @FXML
     void TelechargerImageAnonce(ActionEvent event) {
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Choisir une image");
         fileChooser.getExtensionFilters().addAll(
                 new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
         );

         File selectedFile = fileChooser.showOpenDialog(null);

         if (selectedFile != null) {
             imageFile = selectedFile;
             Image image = new Image(selectedFile.toURI().toString());
             imageViewAn.setImage(image);
         }

     }

     @FXML
     void ajouteranonce(ActionEvent event) {
         if (!validerSaisie()) {
             return;
         }
         String imageUrl = "";
         anonceservice anonceservice = new anonceservice();
         String selectedMatricule = comboVehicule.getSelectionModel().getSelectedItem();
         Vehicule vehicule = vehicleService.getVehiculeByMatricule(selectedMatricule);


         if (imageFile != null) {
             try {
                 Map uploadResult = cloudinary.uploader().upload(imageFile, ObjectUtils.emptyMap());
                 imageUrl = (String) uploadResult.get("secure_url");
             } catch (Exception e) {
                 e.printStackTrace();
                 return;
             }
         }
         Anonce anonce = new Anonce(idta.getText(),iddesa.getText(),java.sql.Date.valueOf(iddatea.getValue()),Double.parseDouble(idprixja.getText()),vehicule.getId_vehicule(),currentUser.getId_utilisateur()
                 ,imageUrl,idadressa.getText(),latitude,longitude);
         try {
             anonceservice.ajouter(anonce);
             Alert alert = new Alert(Alert.AlertType.INFORMATION);
             alert.setTitle("Ajouter anonce");
             alert.setContentText("anonce ajouté avec succés");
             alert.showAndWait();
         }
         catch (SQLException e){
             System.out.println(e.getMessage());
         }

     }

    private boolean validerSaisie() {
        // Vérifier que tous les champs obligatoires sont remplis
        if (idta.getText().isEmpty()) {
            showAlert("Erreur", "Le titre ne peut pas être vide.");
            return false;
        }

        if (idadressa.getText().isEmpty()) {
            showAlert("Erreur", "L'adresse ne peut pas être vide.");
            return false;
        }

        if (idprixja.getText().isEmpty()) {
            showAlert("Erreur", "Le prix ne peut pas être vide.");
            return false;
        }

        if (!isValidDouble(idprixja.getText())) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
            return false;
        }
        LocalDate dateDisponibilite = iddatea.getValue();
        if (dateDisponibilite.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date de disponibilité ne peut pas être avant la date actuelle.");
            return false;
        }
        if (iddatea.getValue() == null) {
            showAlert("Erreur", "La date ne peut pas être vide.");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void setAdresse(String adresse) {
        Platform.runLater(() -> idadressa.setText(adresse));
    }




}


