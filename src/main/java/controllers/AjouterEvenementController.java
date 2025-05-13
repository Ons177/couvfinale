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

public class AjouterEvenementController {

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
    private TextField heureField; // format attendu : HH:mm

    @FXML
    private TextField idCreateurField;

    @FXML
    private TextField typeEvenementField;

    @FXML
    private TextField nbPlacesField;


    private Evenement eventToModify;

    public void setEventToModify(Evenement event) {
        this.eventToModify = event;

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
    private void ajouterEvenement(ActionEvent event) {
        try {
            // Récupération et conversion des données
            LocalDate dateDebut = dateDebutPicker.getValue();
            String heureStr = heureField.getText(); // ex: "14:30"
            LocalTime heure = LocalTime.parse(heureStr);
            LocalDateTime dateHeure = LocalDateTime.of(dateDebut, heure);

            ServiceEvenement service = new ServiceEvenement();

            if (eventToModify != null) {
                // Update existing event
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
            } else {
                // Create new event
                Evenement e = new Evenement(
                        0,
                        titreField.getText(),
                        descriptionArea.getText(),
                        lieuField.getText(),
                        dateDebut,
                        dateFinPicker.getValue(),
                        dateHeure,
                        Integer.parseInt(idCreateurField.getText()),
                        typeEvenementField.getText()
                );

                service.ajouter(e);
                showAlert("Succès", "Événement ajouté avec succès !", Alert.AlertType.INFORMATION);
            }

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
            showAlert("Erreur", "Erreur lors de l'opération : " + ex.getMessage(), Alert.AlertType.ERROR);
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
    private void alleraccueil(ActionEvent event) {
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
