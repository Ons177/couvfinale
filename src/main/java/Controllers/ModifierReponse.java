package Controllers;

import entities.Reclamation;
import entities.feedbackreponserec;
import entities.Reponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.*;
import services.ServiceFeedbackRepRec;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ModifierReponse {

    @FXML
    private Button AnnulerModifReponse;

    @FXML
    private TextArea Contenu;

    @FXML
    private Label DateRecDetail;

    @FXML
    private DatePicker DateReponse;

    @FXML
    private Label DescRecDetail;

    @FXML
    private Button ModifierReponse;

    @FXML
    private Label NomUser;

    @FXML
    private Label PrenomUser;

    @FXML
    private Label PrioriteRecDetail;

    @FXML
    private Label StatutRecDetail;

    @FXML
    private Label TypeRecDetail;
    private Reponse currentReponse;
    private Reclamation currentReclamation;

    public void setReclamationAndReponse(Reclamation rec, Reponse rep) {
        this.currentReclamation = rec;
        this.currentReponse = rep;
        TypeRecDetail.setText(rec.getTypeReclamation());
        DescRecDetail.setText(rec.getDescription());
        DateRecDetail.setText(rec.getDateCreation());
        PrioriteRecDetail.setText(rec.getPriorite());
        StatutRecDetail.setText(rec.getStatut());
        Contenu.setText(rep.getContenu());
        //DateReponse.setValue(LocalDate.parse(rec.getDateCreation()));
    }

    @FXML
    void OnAnnulerModifReponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReponses.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AnnulerModifReponse.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnModifierReponse(ActionEvent event) {
        if (currentReponse != null) {
            try {
                // First delete existing feedback for this response
                ServiceFeedbackRepRec feedbackService = new ServiceFeedbackRepRec();
                feedbackService.supprimerReactionByReponseId(currentReponse.getIdReponse());

                // Now modify the response content and date
                currentReponse.setContenu(Contenu.getText());
                currentReponse.setDateReponse(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                try {
                    String contenu = Contenu.getText();
                    if (FrenchProfanityAPI.containsProfanity(contenu)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Avertissement");
                        alert.setHeaderText("Contenu inapproprié détecté");
                        alert.setContentText("Votre réponse contient des mots inappropriés. Veuillez les modifier.");
                        alert.showAndWait();
                        return;  // Exit the method if profanity is found
                    }
                ServiceReponse service = new ServiceReponse();
                service.modifier(currentReponse);

                // Success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Réponse modifiée");
                alert.setContentText("Votre réponse a été modifiée avec succès.");
                alert.showAndWait();

                // Return to AfficherMesReponses
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReponses.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ModifierReponse.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Mes Réponses");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de la modification");
                alert.setContentText("Une erreur est survenue lors de la modification de la réponse: " + e.getMessage());
                alert.showAndWait();
            }
        } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

}}
