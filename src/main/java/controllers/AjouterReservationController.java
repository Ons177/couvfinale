package controllers;

import controllers.UserSession;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import entities.Reservation;
import entities.Trajet;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import services.ServiceReservation;
import services.ServiceTrajet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AjouterReservationController {

    @FXML
    private Label trajetLabel;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField nombrePlacesField;
    @FXML
    private Button reserverButton;
    @FXML
    private Label errorLabel;
    private accueiltrajetcontrolleur parentController;

    private Trajet trajet;
    private ServiceReservation serviceReservation;
    private ServiceTrajet serviceTrajet;


    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
        trajetLabel.setText(trajet.getVilleDepart() + " → " + trajet.getVilleArrivee());
    }



    public void setParentController(accueiltrajetcontrolleur parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void initialize() {
        serviceReservation = new ServiceReservation();
        serviceTrajet = new ServiceTrajet();

        // Fetch user and populate fields
        populateUserFields();
    }

    private void populateUserFields() {
        Utilisateur user = UserSession.getCurrentUser();
        if (user != null) {
            nomField.setText(user.getNom() != null ? user.getNom() : "");
            prenomField.setText(user.getPrenom() != null ? user.getPrenom() : "");
            emailField.setText(user.getEmail() != null ? user.getEmail() : "");
            telephoneField.setText(user.getTelephone() != null ? user.getTelephone() : "");
        } else {
            System.err.println("Utilisateur non connecté.");
            showError("Utilisateur non connecté. Veuillez remplir les champs manuellement.");
        }
    }



    @FXML
    private void ajouterReservation() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String telephone = telephoneField.getText().trim();
        String nombrePlacesText = nombrePlacesField.getText().trim();
        boolean hasError = false;

        clearErrors();

        // Input validation (in French)
        if (nom.isEmpty()) {
            showError("Le nom est requis");
            nomField.getStyleClass().add("error");
            hasError = true;
        }
        if (prenom.isEmpty()) {
            showError("Le prénom est requis");
            prenomField.getStyleClass().add("error");
            hasError = true;
        }
        if (email.isEmpty()) {
            showError("L'email est requis");
            emailField.getStyleClass().add("error");
            hasError = true;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Format d'email invalide");
            emailField.getStyleClass().add("error");
            hasError = true;
        }
        if (telephone.isEmpty()) {
            showError("Le téléphone est requis");
            telephoneField.getStyleClass().add("error");
            hasError = true;
        } else if (!telephone.matches("^[0-9]{8}$")) {
            showError("Le téléphone doit contenir exactement 8 chiffres");
            telephoneField.getStyleClass().add("error");
            hasError = true;
        }
        int nombrePlaces = 0;
        try {
            nombrePlaces = Integer.parseInt(nombrePlacesText);
            if (nombrePlaces <= 0) {
                showError("Le nombre de places doit être supérieur à 0");
                nombrePlacesField.getStyleClass().add("error");
                hasError = true;
            } else if (nombrePlaces > trajet.getNbrPlaces()) {
                showError("Le nombre de places dépasse les places disponibles (" + trajet.getNbrPlaces() + ")");
                nombrePlacesField.getStyleClass().add("error");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            showError("Le nombre de places doit être un nombre valide");
            nombrePlacesField.getStyleClass().add("error");
            hasError = true;
        }

        if (hasError) return;

        // Calculate prixTotal
        float prixTotal = trajet.getPrix() * nombrePlaces;

        // Create reservation
        Utilisateur currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            showError("Utilisateur non connecté. Impossible de faire la réservation.");
            return;
        }
        Reservation reservation = new Reservation(nom, prenom, email, telephone, nombrePlaces, trajet, currentUser, prixTotal);


        try {
            // Add reservation
            serviceReservation.ajouter(reservation);

            // Update Trajet's available places
            int updatedPlaces = trajet.getNbrPlaces() - nombrePlaces;
            trajet.setNbrPlaces(updatedPlaces);
            serviceTrajet.modifier(trajet);

            // Generate QR Code
            String qrContent = "Réservation\n" +
                    "Passager: " + nom + " " + prenom + "\n" +
                    "Trajet: " + trajet.getVilleDepart() + " → " + trajet.getVilleArrivee() + "\n" +
                    "Prix Total: " + prixTotal + " TND";
            Image qrImage = generateQRCode(qrContent, 200, 200);

            // Show notification with success message
            Notifications notification = Notifications.create()
                    .title("Réservation Ajoutée")
                    .text("Votre réservation a été ajoutée avec succès. Total: " + prixTotal + " TND")
                    .position(Pos.TOP_RIGHT);
            notification.show();

            // Display QR Code in a new window
            displayQRCode(qrImage);

            // Clear fields
            clearFields();

            // Refresh the accueiltrajetcontrolleur to reflect updated places
            if (parentController != null) {
                parentController.refreshTrajets();
            }

            // Close the current reservation window
            Stage stage = (Stage) reserverButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showError("Erreur lors de l'ajout de la réservation: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void clearErrors() {
        errorLabel.setVisible(false);
        nomField.getStyleClass().remove("error");
        prenomField.getStyleClass().remove("error");
        emailField.getStyleClass().remove("error");
        telephoneField.getStyleClass().remove("error");
        nombrePlacesField.getStyleClass().remove("error");
    }

    private void clearFields() {
        nomField.setText("");
        prenomField.setText("");
        emailField.setText("");
        telephoneField.setText("");
        nombrePlacesField.setText("");
    }

    private Image generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return new Image(new java.io.ByteArrayInputStream(pngData));
    }

    private void displayQRCode(Image qrImage) {
        Stage qrStage = new Stage();
        qrStage.setTitle("Code QR de la Réservation");

        ImageView imageView = new ImageView(qrImage);
        VBox qrBox = new VBox(10, imageView);
        qrBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(qrBox, 250, 250);
        qrStage.setScene(scene);
        qrStage.show();
    }

    private void refreshAccueilTrajet() {
        try {
            // Get the current stage of the accueiltrajet (assuming it was the parent)
            Stage currentStage = (Stage) reserverButton.getScene().getWindow();
            if (currentStage != null) {
                Scene currentScene = currentStage.getScene();
                Parent root = FXMLLoader.load(getClass().getResource("/accueiltrajet.fxml"));
                currentScene.setRoot(root);
            }
        } catch (IOException e) {
            showError("Erreur lors de la mise à jour de l'accueil: " + e.getMessage());
        }
    }
}