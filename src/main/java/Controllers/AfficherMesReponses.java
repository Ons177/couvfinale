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

public class AfficherMesReponses {
    @FXML
    private FlowPane cardsContainer;
    @FXML
    public void initialize() {
        ServiceReclamation service = new ServiceReclamation();
        try {
            List<Reclamation> all = service.recuperer();
            for (Reclamation rec : all) {
                if ("traité".equals(rec.getStatut())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ReclamationReponduCard.fxml"));
                    AnchorPane card = loader.load();
                    ReclamationReponduCardController cardController = loader.getController();
                    cardController.setData(rec);
                    cardController.voirReponseBtn.setVisible(true);
                    //card.setOnMouseClicked(e -> openDetail(rec));
                    cardController.voirReponseBtn.setOnAction(e -> openDetail(rec));
                    cardsContainer.getChildren().add(card);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button AnnulerAffichRep;

    @FXML
    void OnAnnulerAffichRep(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/AccueilReponseAdmin.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) AnnulerAffichRep.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.setTitle("Accueil Réponses");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AccueilReponseAdmin.fxml: " + e.getMessage());
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

              FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/DetailReclamationVoirRepondusAdmin.fxml"));
              Parent root = loader.load();
              DetailReclamationVoirRepondusAdmin controller = loader.getController();
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
