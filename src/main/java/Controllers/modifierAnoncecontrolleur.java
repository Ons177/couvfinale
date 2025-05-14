package Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entities.Anonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;
import services.anonceservice;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;

public class modifierAnoncecontrolleur {
    private File imageFile;
    @FXML
    private WebView webviewidmod;

    private double latitude;
    private double longitude;


    @FXML
    private TextField addmod;

    @FXML
    private DatePicker datemod;

    @FXML
    private TextArea desmod;

    @FXML
    private ImageView imagemod;

    @FXML
    private TextField prixmod;

    @FXML
    private TextField titmod;
    private Anonce anonce;
    private ImageView imageViewAn;
    private final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dbpsiatzl",
            "api_key", "619151831588127",
            "api_secret", "SK4cGx6bHN1yQKKmxHHr7T5livo"
    ));

    private boolean validerSaisie() {
        if (titmod.getText().isEmpty()) {
            showAlert("Erreur", "Le titre ne peut pas être vide.");
            return false;
        }

        if (addmod.getText().isEmpty()) {
            showAlert("Erreur", "L'adresse ne peut pas être vide.");
            return false;
        }

        if (prixmod.getText().isEmpty()) {
            showAlert("Erreur", "Le prix ne peut pas être vide.");
            return false;
        }

        if (!isValidDouble(prixmod.getText())) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
            return false;
        }
        LocalDate dateDisponibilite = datemod.getValue();
        if (dateDisponibilite.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date de disponibilité ne peut pas être avant la date actuelle.");
            return false;
        }
        if (datemod.getValue() == null) {
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

    public void initialize() {
        WebEngine engine = webviewidmod.getEngine();
        URL url = getClass().getResource("/map.html");
        engine.load(url.toExternalForm());

        engine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("javaApp", this);
            }
        });
    }
    public void onLocationSelected(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }
    private void initialiserMap() {
        WebEngine engine = webviewidmod.getEngine();
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                String script = String.format("setMarker(%f, %f);", latitude, longitude);
                engine.executeScript(script);
            }
        });
    }



    public void setAnonce(Anonce a) {
        this.anonce = a;
        preRemplirChamps();
        this.latitude = a.getLatitude();
        this.longitude = a.getLongitude();
        initialiserMap(); // 🔥 charge la carte après avoir mis les données

    }
    private void preRemplirChamps() {
        if (anonce != null) {
            titmod.setText(anonce.getTitre());
            addmod.setText(anonce.getAdresse());
            prixmod.setText(String.valueOf(anonce.getPrixParJour()));
            desmod.setText(anonce.getDescription());
            datemod.setValue(anonce.getDateDisponibilite().toLocalDate());
            if (anonce.getPhotoVehicule() != null && !anonce.getPhotoVehicule().isEmpty()) {
                imagemod.setImage(new javafx.scene.image.Image(anonce.getPhotoVehicule(), true));
            }
        }
    }


    @FXML
    void accueilmod(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AnoncesAccueil.fxml"));
            addmod.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void choisirimagemod(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(imagemod.getScene().getWindow());

        if (selectedFile != null) {
            imageFile = selectedFile;
            Image image = new Image(selectedFile.toURI().toString());
            imagemod.setImage(image);
        }

    }

    @FXML
    void modifieran(ActionEvent event) {
        if (!validerSaisie()) {
            return; // Si une saisie est invalide, on arrête la méthode ici
        }
        try {
            if (imageFile != null) {
                Map uploadResult = cloudinary.uploader().upload(imageFile, ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");
                anonce.setPhotoVehicule(imageUrl);
            }

            anonce.setTitre(titmod.getText());
            anonce.setAdresse(addmod.getText());
            anonce.setDescription(desmod.getText());
            anonce.setPrixParJour(Double.parseDouble(prixmod.getText()));
            anonce.setDateDisponibilite(java.sql.Date.valueOf(datemod.getValue()));
            anonce.setLatitude(latitude);
            anonce.setLongitude(longitude);


            anonceservice service = new anonceservice();
            service.update(anonce);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modifier anonce");
            alert.setContentText("anonce modifié avec succés");
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AnonceAccueil.fxml"));
            Parent root = loader.load();
            titmod.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
