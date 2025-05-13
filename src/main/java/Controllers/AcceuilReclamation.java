package Controllers;

import entities.Reclamation;
import entities.Reponse;
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
import services.ServiceReponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AcceuilReclamation {

    @FXML
    private Button AfficherMesRecAccueil;

    @FXML
    private Button AjouterRecAccueil;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    public void initialize() {
        ServiceReclamation service = new ServiceReclamation();
        try {
            List<Reclamation> all = service.recuperer();
            for (Reclamation rec : all) {
                if ("traité".equals(rec.getStatut())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ReclamationCard1.fxml"));
                    AnchorPane card = loader.load();
                    ReclamationCardController cardController = loader.getController();
                    cardController.setData(rec);
                    cardController.voirReponseBtn.setVisible(true);
                    cardController.voirReponseBtn.setOnAction(e -> openDetail(rec));
                    cardsContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void OnAfficherMesRecAccueil(ActionEvent event) {
        try {
            // Load the AfficherMesReclamations FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AfficherMesReclamations.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) AfficherMesRecAccueil.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.setTitle("Mes Réclamations");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AfficherMesReclamations.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void OnAjouterRecAccueil(ActionEvent event) {
        try {
            // Load the AjouterReclamation FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AjouterReclamation.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) AjouterRecAccueil.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.setTitle("Ajouter une réclamation");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AjouterReclamation.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

     private void openDetail (entities.Reclamation rec){
            try {
                // Fetch the response for this reclamation
                ServiceReponse serviceReponse = new ServiceReponse();
                Reponse reponse = null;
                for (Reponse rep : serviceReponse.recuperer()) {
                    if (rep.getIdReclamation() == rec.getIdReclamation()) {
                        reponse = rep;
                        break;
                    }
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/DetailReclamationsVoirRepondus.fxml"));
                Parent root = loader.load();
                DetailReclamationsVoirRepondus controller = loader.getController();
                controller.setReclamationAndReponse(rec, reponse); // <-- new method
                Stage stage = (Stage) cardsContainer.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Détail Réponse");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
}


