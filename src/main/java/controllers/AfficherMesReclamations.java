package controllers;

import entities.Reclamation;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.ServiceReclamation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherMesReclamations {

    @FXML
    private Button AnnulerAffichRec;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    public void initialize() {
        Utilisateur currentUser = UserSession.getCurrentUser(); // 🔥 Get user directly

        if (currentUser == null) {
            System.err.println("User is not set! Make sure to set the user object before using it.");
            return;
        }

        ServiceReclamation service = new ServiceReclamation();
        try {
            List<Reclamation> all = service.recuperer();
            for (Reclamation rec : all) {
                if (rec.getIdUtilisateur() == currentUser.getId_utilisateur()
                        && "en attente".equals(rec.getStatut())) {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReclamationCard.fxml"));
                    AnchorPane card = loader.load();
                    ReclamationCardController cardController = loader.getController();
                    cardController.setData(rec);
                    cardController.voirReponseBtn.setVisible(false);
                    card.setOnMouseClicked(e -> openDetail(rec));
                    cardsContainer.getChildren().add(card);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnAnnulerAffichRec(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AcceuilReclamation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AnnulerAffichRec.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Accueil Réclamations");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AcceuilReclamation.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openDetail(Reclamation rec) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DetailReclamation.fxml"));
            Parent root = loader.load();
            DetailReclamation controller = loader.getController();
            controller.setReclamation(rec);
            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Détail Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
