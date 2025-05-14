package Controllers;

import entities.Reclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import services.FrenchProfanityAPI;
import services.ServiceReclamation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ModifierReclamation {

    @FXML
    private Button AnnulerModifReclamation;

    @FXML
    private DatePicker DateCreation;

    @FXML
    private TextArea DescriptionReclamation;

    @FXML
    private Button ModifReclamation1;

    @FXML
    private ComboBox<String> PrioriteReclamation;

    @FXML
    private ComboBox<String> TypeReclamation;

    private Reclamation currentReclamation;

    public void setReclamation(Reclamation rec) {
        this.currentReclamation = rec;
        if (TypeReclamation.getItems().isEmpty()) {
            TypeReclamation.getItems().addAll("Trajet", "Service", "Prix", "Autre");
        }
        if (PrioriteReclamation.getItems().isEmpty()) {
            PrioriteReclamation.getItems().addAll("Basse", "Moyenne", "Haute");
        }
        TypeReclamation.setValue(rec.getTypeReclamation());
        DescriptionReclamation.setText(rec.getDescription());
        //DateCreation.setValue(LocalDate.parse(rec.getDateCreation()));
        PrioriteReclamation.setValue(rec.getPriorite());
    }

    @FXML
    void OnAnnulerModifReclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReclamations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AnnulerModifReclamation.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnModifReclamation1(ActionEvent event) {
        if (currentReclamation != null) {
            try {
                currentReclamation.setTypeReclamation(TypeReclamation.getValue());
                currentReclamation.setDescription(DescriptionReclamation.getText());
                currentReclamation.setDateCreation(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                currentReclamation.setPriorite(PrioriteReclamation.getValue());
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
                ServiceReclamation service = new ServiceReclamation();
                service.modifier(currentReclamation);
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Réclamation modifiée");
                alert.setContentText("Votre réclamation a été modifiée avec succès.");
                alert.showAndWait();
                // Return to AfficherMesReclamations
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReclamations.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ModifReclamation1.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Mes Réclamations");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de la modification");
                alert.setContentText("Une erreur est survenue lors de la modification de la réclamation: " + e.getMessage());
                alert.showAndWait();
            }
        } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }}}
