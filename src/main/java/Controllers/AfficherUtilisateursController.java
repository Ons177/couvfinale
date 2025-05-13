package Controllers;

import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
import java.util.List;
import java.util.ResourceBundle;

public class AfficherUtilisateursController implements Initializable {

    @FXML
    private FlowPane flowPaneUtilisateurs;

    private UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Utilisateur> utilisateurs = userService.getAllUsers(); // méthode à implémenter
        if (utilisateurs != null && !utilisateurs.isEmpty()) {
            for (Utilisateur u : utilisateurs) {
                VBox userCard = createUserCard(u);
                flowPaneUtilisateurs.getChildren().add(userCard);
                // Ajouter une animation de fade-in pour chaque carte utilisateur
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
        VBox card = new VBox(10); // Espacement entre les éléments
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #A8D8FF; -fx-background-color: #E0F7FF; -fx-border-radius: 5;");
        card.setPrefSize(150, 150);
        card.getStyleClass().add("card");

        // Emoji représentant une personne inconnue
        Label emojiLabel = new Label("👤");
        emojiLabel.setStyle("-fx-font-size: 48px;");

        // Label pour afficher le nom et prénom de l'utilisateur
        Label nameLabel = new Label((user.getNom() != null ? user.getNom() : "Nom inconnu") + " " + (user.getPrenom() != null ? user.getPrenom() : "Prénom inconnu"));
        nameLabel.getStyleClass().add("label");

        // === BOUTON MODIFIER ===
        Button modifierButton = new Button("Modifier");
        modifierButton.setStyle("-fx-background-color: #6CBF84; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        modifierButton.setOnMouseEntered(event -> {
            modifierButton.setStyle("-fx-background-color: #4CAF70; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        });
        modifierButton.setOnMouseExited(event -> {
            modifierButton.setStyle("-fx-background-color: #6CBF84; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        });
        // Code dans AfficherUtilisateursController
        modifierButton.setOnAction(event -> {
            try {
                // Charger la scène ModifyProfile
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyProfile.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de ModifyProfile
                ModifyProfileController controller = loader.getController();
                controller.setCurrentUser(user); // Passer l'utilisateur courant
                controller.setIsAdmin(true); // Passer 'isAdmin' à true si l'on est admin

                // Remplacer la scène actuelle
                Stage stage = (Stage) modifierButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });





            /* {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyProfile.fxml"));
                AnchorPane modifierPane = loader.load();

                ModifyProfileController controller = loader.getController();
                controller.setCurrentUser(user);

                Stage stage = new Stage();
                stage.setTitle("Modifier Utilisateur");
                stage.setScene(new Scene(modifierPane));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger la fenêtre de modification de l'utilisateur.");
            }*/


        // === BOUTON SUPPRIMER ===
        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #7BB9FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setStyle("-fx-background-color: #4D8EFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        });
        deleteButton.setOnMouseExited(event -> {
            deleteButton.setStyle("-fx-background-color: #7BB9FF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 12;");
        });
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

        // Action sur clic de la carte
        card.setOnMouseClicked(event -> {
            showAlert("Informations de l'utilisateur",
                    "Nom: " + (user.getNom() != null ? user.getNom() : "inconnu") + "\n" +
                            "Prénom: " + (user.getPrenom() != null ? user.getPrenom() : "inconnu") + "\n" +
                            "Email: " + (user.getEmail() != null ? user.getEmail() : "inconnu") + "\n" +
                            "Rôle: " + (user.getRole() != null ? user.getRole() : "inconnu"));
        });

        // Ajouter les éléments à la carte dans l’ordre
        card.getChildren().addAll(emojiLabel, nameLabel, modifierButton, deleteButton);

        return card;
    }
    public void refreshUserList() {
        flowPaneUtilisateurs.getChildren().clear();
        List<Utilisateur> utilisateurs = userService.getAllUsers(); // Récupérer la liste des utilisateurs
        for (Utilisateur user : utilisateurs) {
            flowPaneUtilisateurs.getChildren().add(createUserCard(user));
        }
    }
    // Méthode pour afficher une alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
