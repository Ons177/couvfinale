package Controllers;

import entities.Reclamation;
import entities.Reponse;
import entities.feedbackreponserec;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.ServiceFeedbackRepRec;
import services.ServiceReclamation;
import services.ServiceReponse;

import java.io.IOException;
import java.sql.SQLException;

public class DetailReclamationVoirRepondusAdmin {
    @FXML
    private Label NomUser;

    @FXML
    private Label PrenomUser;

    @FXML
    private Button AnnulerDetailReclamationVoirRepondusAdmin;

    @FXML
    private Label ContenuRep;

    @FXML
    private Label DateRecDetail;

    @FXML
    private Label DateRep;

    @FXML
    private Label DescRecDetail;

    @FXML
    private Button ModifierReponse;

    @FXML
    private Label PrioriteRecDetail;

    @FXML
    private Label StatutRecDetail;

    @FXML
    private Button SupprimerReponse;
    private Reponse currentReponse;

    @FXML
    private Label TypeRecDetail;
    private Reclamation currentReclamation;
    @FXML
    private Label labelReaction;

    @FXML
    private Label labelDateReaction;

    @FXML
    void OnAnnulerDetailReclamationVoirRepondusAdmin(ActionEvent event) {
        try {
            // Load the AcceuilReclamation FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReponses.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) AnnulerDetailReclamationVoirRepondusAdmin.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.setTitle("Mes Réponses");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AfficherMesReponses.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void OnModifierReponse(ActionEvent event) {
            try {
                // Charger le fichier FXML de ModifierReponse
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ModifierReponse.fxml"));
                Parent root = loader.load();
                ModifierReponse controller = loader.getController();
                controller.setReclamationAndReponse(currentReclamation,currentReponse);


                // Obtenir la scène actuelle et changer pour la nouvelle scène
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Réponse");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de la page ModifierReponse.fxml");
            }
        }

        @FXML
        void OnSupprimerReponse (ActionEvent event){
            if (currentReponse == null || currentReclamation == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Action impossible");
                alert.setContentText("Aucune réponse ou réclamation sélectionnée.");
                alert.showAndWait();
                return;
            }

            ServiceReponse serviceReponse = new ServiceReponse();
            ServiceReclamation serviceReclamation = new ServiceReclamation();

            try {
                // Delete the response
                serviceReponse.supprimer(currentReponse);

                // Update the reclamation status to "en attente"
                currentReclamation.setStatut("en attente");
                serviceReclamation.modifier(currentReclamation);

                // Confirmation alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("La réponse a été supprimée et le statut de la réclamation est mis à jour.");
                alert.showAndWait();
                // After deletion, return to the list page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReponses.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) SupprimerReponse.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Mes Réponses");
                stage.show();

                // Redirect or update the UI if needed
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Problème lors de la suppression");
                alert.setContentText("Une erreur est survenue: " + e.getMessage());
                alert.showAndWait();
            }
        }


    public void setReclamationAndReponse(Reclamation rec, Reponse rep) {
        this.currentReclamation = rec;
        this.currentReponse = rep;

        TypeRecDetail.setText(rec.getTypeReclamation());
        DescRecDetail.setText(rec.getDescription());
        DateRecDetail.setText(rec.getDateCreation());
        PrioriteRecDetail.setText(rec.getPriorite());
        StatutRecDetail.setText(rec.getStatut());

        if (rep != null) {
            ContenuRep.setText(rep.getContenu());
            DateRep.setText(rep.getDateReponse().toString());

            // Load reaction (feedback)
            ServiceFeedbackRepRec serviceFeedback = new ServiceFeedbackRepRec();
            try {
                feedbackreponserec feedback = serviceFeedback.getFeedbackByReponseId(rep.getIdReponse());
                if (feedback != null) {
                    labelReaction.setText(feedback.getReaction());
                    labelDateReaction.setText(feedback.getDateFeedbackrep());
                } else {
                    labelReaction.setText("Aucune réaction");
                    labelDateReaction.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                labelReaction.setText("Erreur lors du chargement");
                labelDateReaction.setText("");
            }

        } else {
            ContenuRep.setText("Aucune réponse");
            DateRep.setText("");
            labelReaction.setText("Aucune réaction");
            labelDateReaction.setText("");
        }
    }


}
