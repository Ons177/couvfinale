package Controllers;

import entities.Reclamation;
import entities.Reponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import services.ServiceReclamation;
import services.ServiceReponse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    @FXML
    private Label TypeRecDetail;

    @FXML
    private Label labelReaction;

    @FXML
    private Label labelDateReaction;

    private Reclamation currentReclamation;
    private Reponse currentReponse;

    @FXML
    void OnAnnulerDetailReclamationVoirRepondusAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReponses.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AnnulerDetailReclamationVoirRepondusAdmin.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réponses");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de AfficherMesReponses.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void OnModifierReponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ModifierReponse.fxml"));
            Parent root = loader.load();
            currentReponse.setReaction(null); // remove reaction
            currentReponse.setDateFeedbackrep(null); // remove feedback date


            ModifierReponse controller = loader.getController();
            controller.setReclamationAndReponse(currentReclamation, currentReponse);

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
    void OnSupprimerReponse(ActionEvent event) {
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
            serviceReponse.supprimer(currentReponse);

            currentReclamation.setStatut("en attente");
            serviceReclamation.modifier(currentReclamation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("La réponse a été supprimée et le statut de la réclamation est mis à jour.");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReponses.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) SupprimerReponse.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réponses");
            stage.show();

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
            DateRep.setText(rep.getDateReponse());

            if (rep.getReaction() != null && !rep.getReaction().isEmpty()) {
                labelReaction.setText(rep.getReaction());
                labelDateReaction.setText(rep.getDateFeedbackrep());
            } else {
                labelReaction.setText("Aucune réaction");
                labelDateReaction.setText("");
            }
        } else {
            ContenuRep.setText("Aucune réponse");
            DateRep.setText("");
            labelReaction.setText("Aucune réaction");
            labelDateReaction.setText("");
        }
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        HBox.setHgrow(DescRecDetail, Priority.ALWAYS);
        DescRecDetail.setMaxWidth(Double.MAX_VALUE);
    }
}
