package Controllers;

import entities.Reclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceReclamation;
import services.FrenchProfanityAPI;  // Import the Profanity API class

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AjouterReclamation {

    @FXML
    private Button AjoutReclamation;

    @FXML
    private Button AnnulerAjoutReclamation;

    @FXML
    private DatePicker DateCreation;

    @FXML
    private TextArea DescriptionReclamation;

    @FXML
    private ComboBox<String> PrioriteReclamation;

    @FXML
    private ComboBox<String> TypeReclamation;

    private ServiceReclamation serviceReclamation;

    @FXML
    public void initialize() {
        // Initialize the service
        serviceReclamation = new ServiceReclamation();

        // Initialize TypeReclamation ComboBox
        TypeReclamation.getItems().addAll(
                "Trajet",
                "Service",
                "Prix",
                "Autre"
        );

        // Initialize PrioriteReclamation ComboBox
        PrioriteReclamation.getItems().addAll(
                "Basse",
                "Moyenne",
                "Haute"
        );

        // Set default date to today
        //DateCreation.setValue(LocalDate.now());
    }

    @FXML
    void OnAjouterReclamation(ActionEvent event) {
        // Validate inputs
        if (TypeReclamation.getValue() == null || DescriptionReclamation.getText().isEmpty() ||
                PrioriteReclamation.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        try {
            // Step 1: Check for profanity
            String description = DescriptionReclamation.getText();
            if (FrenchProfanityAPI.containsProfanity(description)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText("Contenu inapproprié détecté");
                alert.setContentText("Votre réclamation contient des mots inappropriés. Veuillez les modifier.");
                alert.showAndWait();
                return;  // Exit the method if profanity is found
            }

            // Step 2: Create new reclamation if no profanity
            Reclamation reclamation = new Reclamation(
                    1, // TODO: Replace with actual user ID
                    TypeReclamation.getValue(),
                    description,
                    LocalDate.now().format(DateTimeFormatter.ISO_DATE), // current date
                    "en attente",
                    PrioriteReclamation.getValue()
            );

            // Add to database
            serviceReclamation.ajouter(reclamation);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Réclamation ajoutée");
            alert.setContentText("Votre réclamation a été ajoutée avec succès.");
            alert.showAndWait();

            // Return to AfficherMesReclamations
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReclamations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AjoutReclamation.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réclamations");
            stage.show();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ajout");
            alert.setContentText("Une erreur est survenue lors de l'ajout de la réclamation: " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de navigation");
            alert.setContentText("Une erreur est survenue lors de la navigation: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void OnAnnulerAjoutReclamation(ActionEvent event) {
        try {
            // Load the AcceuilReclamation FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AcceuilReclamation.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) AnnulerAjoutReclamation.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.setTitle("Accueil Réclamations");
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de navigation");
            alert.setContentText("Une erreur est survenue lors de la navigation: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
