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
    private Button RetourRecMenu;

    private Utilisateur user;

    @FXML
    public void initialize() {
        // Get the current user from session
        user = UserSession.getCurrentUser();

        // Check if user is null, meaning user is not logged in
        if (user == null) {
            System.err.println("User session is null! User might not be logged in properly.");
            showAlert("Erreur", "L'utilisateur n'est pas connecté.");
            return;
        }

        ServiceReclamation service = new ServiceReclamation();
        try {
            // Fetch only reclamations for the logged-in user
            List<Reclamation> all = service.recuperer(user.getId_utilisateur());

            for (Reclamation rec : all) {
                if ("traité".equalsIgnoreCase(rec.getStatut())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReclamationCard1.fxml"));
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
            showAlert("Erreur", "Échec du chargement des réclamations.");
        }
    }

    @FXML
    void OnAfficherMesRecAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AfficherMesReclamations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AfficherMesRecAccueil.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Mes Réclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de chargement de la vue : AfficherMesReclamations.");
        }
    }

    @FXML
    void OnAjouterRecAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjouterReclamation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) AjouterRecAccueil.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ajouter une réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de chargement de la vue : AjouterReclamation.");
        }
    }

    private void openDetail(Reclamation rec) {
        try {
            ServiceReponse serviceReponse = new ServiceReponse();
            Reponse reponse = null;

            for (Reponse rep : serviceReponse.recuperer()) {
                if (rep.getIdReclamation() == rec.getIdReclamation()) {
                    reponse = rep;
                    break;
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DetailReclamationsVoirRepondus.fxml"));
            Parent root = loader.load();
            DetailReclamationsVoirRepondus controller = loader.getController();
            controller.setReclamationAndReponse(rec, reponse);

            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Détail Réponse");
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'afficher le détail de la réponse.");
        }
    }

    @FXML
    void RetourRecMenu(ActionEvent event) {
        try {
            if (user == null) {
                throw new IllegalStateException("Utilisateur non connecté.");
            }

            String role = user.getRole().toLowerCase();
            String fxmlPath;

            if ("passager".equals(role)) {
                fxmlPath = "/Views/MenuPassager.fxml";
            } else if ("conducteur".equals(role)) {
                fxmlPath = "/Views/MenuConducteur.fxml";
            } else {
                throw new IllegalStateException("Type de rôle inconnu : " + role);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) RetourRecMenu.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Menu Client");
            stage.show();

        } catch (IOException | IllegalStateException e) {
            showAlert("Erreur", "Erreur de navigation : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
