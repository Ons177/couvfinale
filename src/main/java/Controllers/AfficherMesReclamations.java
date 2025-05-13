package Controllers;

import entities.Reclamation;
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
import java.util.List;

public class AfficherMesReclamations {

    @FXML
    private Button AnnulerAffichRec;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    public void initialize() {
        ServiceReclamation service = new ServiceReclamation();
        try {
            List<Reclamation> all = service.recuperer();
            for (Reclamation rec : all) {
                if ("en attente".equals(rec.getStatut())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ReclamationCard.fxml"));
                    AnchorPane card = loader.load();
                    ReclamationCardController cardController = loader.getController();
                    cardController.setData(rec);
                    cardController.voirReponseBtn.setVisible(false);
                    card.setOnMouseClicked(e -> openDetail(rec));
                    cardsContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnAnnulerAffichRec(ActionEvent event) {
        try {
            // Load the AcceuilReclamation FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AcceuilReclamation.fxml"));
            Parent root = loader.load();
            
            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);
            
            // Get the current stage
            Stage stage = (Stage) AnnulerAffichRec.getScene().getWindow();
            
            // Set the new scene
            stage.setScene(scene);
            stage.setTitle("Accueil Réclamations");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AcceuilReclamation.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openDetail(entities.Reclamation rec) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/DetailReclamation.fxml"));
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
