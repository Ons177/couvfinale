package controllers;

import entities.Reclamation;
import entities.Reponse;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import services.FrenchProfanityAPI;
import services.ServiceReponse;
import services.UserService;

import java.io.IOException;
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

    @FXML
    private Label labelReaction;

    @FXML
    private Label labelDateReaction;

    private Reponse currentReponse;
    private Reclamation currentReclamation;
    private Utilisateur user;

    public void setReclamationAndReponse(Reclamation rec, Reponse rep) {
        this.currentReclamation = rec;
        this.currentReponse = rep;
        UserService utilisateurService = new UserService();
        Utilisateur utilisateur = utilisateurService.getById(rec.getIdUtilisateur());

        TypeRecDetail.setText(rec.getTypeReclamation());
        DescRecDetail.setText(rec.getDescription());
        DateRecDetail.setText(rec.getDateCreation());
        PrioriteRecDetail.setText(rec.getPriorite());
        StatutRecDetail.setText(rec.getStatut());

        Contenu.setText(rep.getContenu());
        PrenomUser.setText(utilisateur.getPrenom());
        NomUser.setText(utilisateur.getNom());

      /* if (rep.getReaction() != null) {
            // Display the current reaction and date if they exist
            labelReaction.setText(rep.getReaction());
            labelDateReaction.setText(rep.getDateFeedbackrep());
        }*/
    }

    @FXML
    void OnAnnulerModifReponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherMesReponses.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AnnulerModifReponse.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réponses");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnModifierReponse(ActionEvent event) {
        if (currentReponse != null) {
            if (Contenu.getText().isEmpty() /*|| DateReponse.getValue() == null*/ ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Champs manquants");
                alert.setContentText("Veuillez remplir tous les champs obligatoires.");
                alert.showAndWait();
                return;
            }
            try {
                String contenu = Contenu.getText();
                // String reaction = labelReaction.getText(); // You can use the reaction here, if needed, otherwise leave it empty

                // Check for profanity in the content
                if (FrenchProfanityAPI.containsProfanity(contenu)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText("Contenu inapproprié détecté");
                    alert.setContentText("Votre réponse contient des mots inappropriés. Veuillez les modifier.");
                    alert.showAndWait();
                    return;
                }

                // Update the response content and reset the reaction
                currentReponse.setContenu(contenu);
                currentReponse.setDateReponse(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                currentReponse.setReaction(null); // Remove the reaction when modifying the response
                currentReponse.setDateFeedbackrep(null); // Clear the feedback date

                // Call the service to update the response
                ServiceReponse service = new ServiceReponse();
                service.modifier(currentReponse);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Réponse modifiée");
                alert.setContentText("Votre réponse a été modifiée avec succès.");
                alert.showAndWait();

                // Navigate back to the responses list
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherMesReponses.fxml"));
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
                alert.setContentText("Une erreur est survenue lors de la modification de la réponse : " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}