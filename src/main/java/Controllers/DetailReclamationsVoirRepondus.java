package Controllers;

import entities.Reclamation;
import entities.Reponse;
import entities.feedbackreponserec;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import services.ServiceFeedbackRepRec;

import java.io.IOException;

public class DetailReclamationsVoirRepondus {

    @FXML
    private Button AnnulerDetailRecVoirRep;

    @FXML
    private Label ContenuRep1;

    @FXML
    private Label DateRecDetail;

    @FXML
    private Label DateRep1;

    @FXML
    private Label DescRecDetail;

    @FXML
    private Label PrioriteRecDetail;

    @FXML
    private Label StatutRecDetail;

    @FXML
    private Label TypeRecDetail;

    @FXML
    private Button BtnReagir;

    private Reclamation currentReclamation;
    private Reponse currentReponse; // ✅ New field to store current Reponse

    @FXML
    void onReactToResponse(ActionEvent event) {
        if (currentReponse != null) {
            showReactionDialog(currentReponse.getIdReponse()); // ✅ Use correct id_reponse
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Aucune réponse disponible pour réagir.");
            alert.showAndWait();
        }
    }

    private void showReactionDialog(int idReponse) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Réagissez à la réponse");

        Label label = new Label("Votre réaction :");

        ToggleGroup group = new ToggleGroup();

        RadioButton helpful = new RadioButton("👍 Utile");
        helpful.setUserData("helpful");
        helpful.setToggleGroup(group);

        RadioButton notHelpful = new RadioButton("👎 Pas utile");
        notHelpful.setUserData("not_helpful");
        notHelpful.setToggleGroup(group);

        RadioButton neutral = new RadioButton("😐 Neutre");
        neutral.setUserData("neutral");
        neutral.setToggleGroup(group);
        neutral.setSelected(true);

        VBox content = new VBox(10, label, helpful, notHelpful, neutral);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK && group.getSelectedToggle() != null) {
                String selectedReaction = group.getSelectedToggle().getUserData().toString();
                String currentDate = java.time.LocalDate.now().toString();

                feedbackreponserec feedback = new feedbackreponserec(
                        0, // ID auto-incremented
                        idReponse,
                        selectedReaction,
                        currentDate
                );

                try {
                    new ServiceFeedbackRepRec().ajouterReaction(feedback);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Merci pour votre réaction !");
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage());
                    error.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    public void setReclamationAndReponse(Reclamation rec, Reponse rep) {
        this.currentReclamation = rec;
        this.currentReponse = rep; // ✅ Store the Reponse object

        TypeRecDetail.setText(rec.getTypeReclamation());
        DescRecDetail.setText(rec.getDescription());
        DateRecDetail.setText(rec.getDateCreation());
        PrioriteRecDetail.setText(rec.getPriorite());
        StatutRecDetail.setText(rec.getStatut());

        if (rep != null) {
            ContenuRep1.setText(rep.getContenu());
            DateRep1.setText(rep.getDateReponse());
        } else {
            ContenuRep1.setText("Aucune réponse");
            DateRep1.setText("");
        }
    }

    @FXML
    void OnAnnulerDetailRecVoirRep(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AcceuilReclamation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AnnulerDetailRecVoirRep.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Accueil Réclamations");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AcceuilReclamation.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
