package controllers;

import entities.Utilisateur;
import javafx.animation.FadeTransition;

import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.UserService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherUtilisateursController implements Initializable {

    @FXML
    private FlowPane flowPaneUtilisateurs;

    private UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Utilisateur> utilisateurs = userService.getAllUsers();
        if (utilisateurs != null && !utilisateurs.isEmpty()) {
            for (Utilisateur u : utilisateurs) {
                VBox userCard = createUserCard(u);
                flowPaneUtilisateurs.getChildren().add(userCard);
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), userCard);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
        } else {
            showAlert("Information", "Aucun utilisateur trouvé.");
        }
    }

    private VBox createUserCard(Utilisateur user) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #A8D8FF; -fx-background-color: #E0F7FF; -fx-border-radius: 5;");
        card.setPrefSize(150, 150);
        card.getStyleClass().add("card");

        Label emojiLabel = new Label("👤");
        emojiLabel.setStyle("-fx-font-size: 48px;");

        Label nameLabel = new Label((user.getNom() != null ? user.getNom() : "Nom inconnu") + " " +
                (user.getPrenom() != null ? user.getPrenom() : "Prénom inconnu"));

        // === BOUTON MODIFIER ===
        Button modifierButton = new Button("Modifier");
        modifierButton.setStyle("-fx-background-color: #6CBF84; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        modifierButton.setOnMouseEntered(e -> modifierButton.setStyle("-fx-background-color: #4CAF70; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;"));
        modifierButton.setOnMouseExited(e -> modifierButton.setStyle("-fx-background-color: #6CBF84; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;"));

        modifierButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyProfile.fxml"));
                Parent root = loader.load();
                ModifyProfileController controller = loader.getController();
                controller.setCurrentUser(user);
                controller.setIsAdmin(true);
                Stage stage = (Stage) modifierButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // === BOUTON SUPPRIMER ===
        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #7BB9FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #4D8EFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #7BB9FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;"));

        deleteButton.setOnAction(event -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
            confirmationAlert.setContentText("Cette action est irréversible.");

            if (confirmationAlert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
                boolean success = userService.supprimer(user);
                if (success) {
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), card);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> flowPaneUtilisateurs.getChildren().remove(card));
                    fadeOut.play();
                    showAlert("Succès", "L'utilisateur a été supprimé.");
                } else {
                    showAlert("Erreur", "Une erreur est survenue lors de la suppression.");
                }
            }
        });

        // === CLIC SUR CARTE POUR VOIR LES INFOS ===
        card.setOnMouseClicked(event -> {
            StringBuilder info = new StringBuilder();
            info.append("Nom : ").append(user.getNom() != null ? user.getNom() : "inconnu").append("\n");
            info.append("Prénom : ").append(user.getPrenom() != null ? user.getPrenom() : "inconnu").append("\n");
            info.append("Email : ").append(user.getEmail() != null ? user.getEmail() : "inconnu").append("\n");
            info.append("Rôle : ").append(user.getRole() != null ? user.getRole() : "inconnu").append("\n");
            info.append("Tel : ").append(user.getTelephone() != null ? user.getTelephone() : "inconnu").append("\n");

            // Calcul de l'âge si date de naissance disponible
            if (user.getDate_naissance() != null) {
                int age = Period.between(user.getDate_naissance().toLocalDate(), LocalDate.now()).getYears();
                info.append("Âge : ").append(age).append(" ans\n");
            } else {
                info.append("Âge : inconnu\n");
            }

            // Si l'utilisateur est conducteur, on ajoute CIN et numéro de permis
            if ("conducteur".equalsIgnoreCase(user.getRole())) {
                info.append("CIN : ").append(user.getCin() != null ? user.getCin() : "inconnu").append("\n");
                info.append("Numéro permis : ").append(user.getPermis() != null ? user.getPermis() : "inconnu").append("\n");
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/UserInfoDialog.fxml"));
                Parent root = loader.load();

                UserInfoDialogController controller = loader.getController();
                controller.setUser(user);

                Stage dialogStage = new Stage();
                controller.setDialogStage(dialogStage);

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.setTitle("Détails de l'utilisateur");
                dialogStage.setResizable(false);
                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        card.getChildren().addAll(emojiLabel, nameLabel, modifierButton, deleteButton);
        return card;
    }

    public void refreshUserList() {
        flowPaneUtilisateurs.getChildren().clear();
        List<Utilisateur> utilisateurs = userService.getAllUsers();
        for (Utilisateur user : utilisateurs) {
            flowPaneUtilisateurs.getChildren().add(createUserCard(user));
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}