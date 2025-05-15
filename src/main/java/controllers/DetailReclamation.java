package controllers;

import entities.Reclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.ServiceReclamation;

import java.io.IOException;

public class DetailReclamation {

    @FXML
    private Button AnnulerDetailReclamation;

    @FXML
    private Label DateRecDetail;

    @FXML
    private Label DescRecDetail;

    @FXML
    private Button ModifierReclamation;

    @FXML
    private Label PrioriteRecDetail;

    @FXML
    private Label StatutRecDetail;

    @FXML
    private Button SupprimerReclamation;

    @FXML
    private Label TypeRecDetail;

    @FXML
    private Button AfficherMesRecAccueil;

    @FXML
    private FlowPane reclamationsContainer;

    private Reclamation currentReclamation;

    @FXML
    void OnModifierReclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifierReclamation.fxml"));
            Parent root = loader.load();
            ModifierReclamation controller = loader.getController();
            controller.setReclamation(currentReclamation);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Modifier Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnAnnulerDetailReclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherMesReclamations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AnnulerDetailReclamation.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnSupprimerReclamation(ActionEvent event) {
        if (currentReclamation == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Action impossible");
            alert.setContentText("Aucune réclamation sélectionnée.");
            alert.showAndWait();
            return;
        }

        ServiceReclamation serviceReclamation = new ServiceReclamation();

        try {
            // Delete the reclamation
            serviceReclamation.supprimer(currentReclamation);

            // Confirmation alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("La réclamation a été supprimée.");
            alert.showAndWait();

            // After deletion, return to the list page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherMesReclamations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) SupprimerReclamation.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réclamations");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            // Error handling alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème lors de la suppression");
            alert.setContentText("Une erreur est survenue: " + e.getMessage());
            alert.showAndWait();
        }


    }

    @FXML
    void OnAfficherMesRecAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherMesReclamations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AfficherMesRecAccueil.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReclamation(Reclamation rec) {
        this.currentReclamation = rec;

        TypeRecDetail.setText(rec.getTypeReclamation());
        DescRecDetail.setText(rec.getDescription());
        DateRecDetail.setText(rec.getDateCreation());
        PrioriteRecDetail.setText(rec.getPriorite());
        StatutRecDetail.setText(rec.getStatut());
    }

}

