package controllers;

import entities.Evenement;
import services.ServiceEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ModifierEvenementController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField lieuField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField heureField;

    @FXML
    private TextField idCreateurField;

    @FXML
    private TextField typeEvenementField;

    private Evenement eventToModify;

    public void setEventToModify(Evenement event) {
        this.eventToModify = event;
        // Pre-fill all fields with the event's current data
        titreField.setText(event.getTitre());
        descriptionArea.setText(event.getDescription());
        lieuField.setText(event.getLieu());
        dateDebutPicker.setValue(event.getDateDebut());
        dateFinPicker.setValue(event.getDateFin());
        heureField.setText(event.getHeure().toLocalTime().toString());
        idCreateurField.setText(String.valueOf(event.getIdCreateur()));
        typeEvenementField.setText(event.getTypeEvenement());
    }

    @FXML
    public void initialize() {
        Scene scene = titreField.getScene();
        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        }
    }

    @FXML
    private void modifierEvenement(ActionEvent event) {
        try {
            // Récupération et conversion des données
            LocalDate dateDebut = dateDebutPicker.getValue();
            String heureStr = heureField.getText();
            LocalTime heure = LocalTime.parse(heureStr);
            LocalDateTime dateHeure = LocalDateTime.of(dateDebut, heure);

            ServiceEvenement service = new ServiceEvenement();

            // Update the event with new values
            eventToModify.setTitre(titreField.getText());
            eventToModify.setDescription(descriptionArea.getText());
            eventToModify.setLieu(lieuField.getText());
            eventToModify.setDateDebut(dateDebut);
            eventToModify.setDateFin(dateFinPicker.getValue());
            eventToModify.setHeure(dateHeure);
            eventToModify.setIdCreateur(Integer.parseInt(idCreateurField.getText()));
            eventToModify.setTypeEvenement(typeEvenementField.getText());

            service.modifier(eventToModify);
            showAlert("Succès", "Événement modifié avec succès !", Alert.AlertType.INFORMATION);

            // Return to Acceuil with refreshed list
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilConducteur.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Erreur", "Erreur lors de la modification : " + ex.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du rechargement de la page", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String titre, String contenu, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilConducteur.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page d'accueil.", Alert.AlertType.ERROR);
        }
    }
}
