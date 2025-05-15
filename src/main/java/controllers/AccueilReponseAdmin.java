package controllers;

import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import services.ServiceReclamation;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AccueilReponseAdmin {

    @FXML
    private Button AffichRepAccueil;

    @FXML
    private FlowPane cardsContainer;

    @FXML
    private Button RetourRepMenu;

    @FXML
    private ComboBox<String> filtrerComboBox;

    private List<Reclamation> allReclamations;

    @FXML
    void OnAffichRepAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherMesReponses.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AffichRepAccueil.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réponses");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AfficherMesReponses.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


   @FXML
   public void initialize() {
       ServiceReclamation service = new ServiceReclamation();
       ObservableList<String> filterOptions = FXCollections.observableArrayList("Service", "Prix", "Trajet", "Autre");
       filtrerComboBox.setItems(filterOptions);

       try {
           allReclamations = service.recuperer();
           allReclamations.sort(Comparator.comparingInt(r -> getPriorityValue(r.getPriorite())));
           displayReclamations(allReclamations);

           filtrerComboBox.setOnAction(event -> {
               String selectedType = filtrerComboBox.getValue();
               if (selectedType != null) {
                   filterReclamations(allReclamations, selectedType);
               }
           });
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    private int getPriorityValue(String priority) {
        switch (priority.toLowerCase()) {
            case "haute": return 0;
            case "moyenne": return 1;
            case "basse": return 2;
            default: return 3; // unknown priorities go last
        }
    }

    private void filterReclamations(List<Reclamation> reclamations, String type) {
        List<Reclamation> filteredReclamations = reclamations.stream()
                .filter(r -> r.getTypeReclamation().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        displayReclamations(filteredReclamations);
    }

    private void displayReclamations(List<Reclamation> reclamations) {
        cardsContainer.getChildren().clear();
        for (Reclamation rec : reclamations) {
            if ("en attente".equals(rec.getStatut())) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReclamationAdminCard.fxml"));
                    AnchorPane card = loader.load();
                    ReclamationAdminCardController cardController = loader.getController();
                    cardController.setData(rec);
                    cardController.voirReclamationBtn.setOnAction(e -> openDetail(rec));
                    cardsContainer.getChildren().add(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openDetail(Reclamation rec) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DetailReclamationsAdmin.fxml"));
            Parent root = loader.load();
            DetailReclamationsAdmin controller = loader.getController();
            controller.setReclamation(rec);
            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Détail Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void RetourRepMenu(ActionEvent event) {
        try {
            // Load the AcceuilReclamation FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MenuAdmin.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) RetourRepMenu.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.setTitle("Menu Réponses");
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de navigation");
            alert.setContentText("Une erreur est survenue lors de la navigation: " + e.getMessage());
            alert.showAndWait();
        }
    }

}
