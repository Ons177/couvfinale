package controllers;

import entities.Utilisateur;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.MyDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class ModifyProfileController {

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker naissancePicker;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField roleField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField telField;

    private Utilisateur currentUser;
    private boolean isAdmin;

    // Setter appelé pour passer l'utilisateur courant
    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;

        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        telField.setText(user.getTelephone());
        naissancePicker.setValue(user.getDate_naissance().toLocalDate());
        roleField.setText(user.getRole());
    }

    @FXML
    private void onSaveButtonClick(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String telephone = telField.getText();
        String role = roleField.getText();

        // Récupérer la date depuis le DatePicker
        LocalDate naissanceLocalDate = naissancePicker.getValue();
        if (naissanceLocalDate == null) {
            showError("Veuillez sélectionner une date de naissance.");
            return;
        }

        // ✅ Convertir la date LocalDate → java.sql.Date
        Date naissanceDate = Date.valueOf(naissanceLocalDate);

        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || role.isEmpty()) {
            showError("Tous les champs doivent être remplis.");
            return;
        }

        // Exécuter la mise à jour dans un thread
        new Thread(() -> {
            // ✅ Appel corrigé avec la date en type Date
            boolean success = updateUserInfo(email, nom, prenom, telephone, naissanceDate, role);
            Platform.runLater(() -> {
                if (success) {
                    // Mise à jour en mémoire
                    currentUser.setNom(nom);
                    currentUser.setPrenom(prenom);
                    currentUser.setEmail(email);
                    currentUser.setTelephone(telephone);
                    currentUser.setDate_naissance(naissanceDate);
                    currentUser.setRole(role);

                    showSuccess("Les informations ont été mises à jour avec succès !");

                    // Après la mise à jour, rediriger en fonction du rôle
                    try {
                        FXMLLoader loader;
                        Parent root;

                        if (UserSession.isAdmin()) {
                            // Si l'utilisateur est un admin, revenir à AfficherUtilisateurs
                            loader = new FXMLLoader(getClass().getResource("/Views/AfficherUtilisateurs.fxml"));
                            root = loader.load();

                            // Optionnel: Actualiser la liste des utilisateurs dans AfficherUtilisateursController
                            AfficherUtilisateursController controller = loader.getController();
                            controller.refreshUserList();  // Actualiser la liste des utilisateurs si nécessaire
                        } else {
                            // Si l'utilisateur est normal, revenir à UserProfile
                            loader = new FXMLLoader(getClass().getResource("/Views/UserProfile.fxml"));
                            root = loader.load();
                            UserProfileController controller = loader.getController();
                            controller.setUser(currentUser);  // Passer l'utilisateur courant
                        }

                        // Remplacer la scène actuelle par la nouvelle scène
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        showError("Erreur lors du chargement de la page de profil.");
                    }
                } else {
                    showError("Échec de la mise à jour des informations.");
                }
            });
        }).start();
    }



    private boolean updateUserInfo(String email, String nom, String prenom, String telephone, Date naissance, String role) {
        String query = "UPDATE utilisateur SET nom = ?, prenom = ?, telephone = ?, date_naissance = ?, role = ? WHERE email = ?";

        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, telephone);
            statement.setDate(4, naissance);
            statement.setString(5, role);
            statement.setString(6, email);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            return false;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/UserProfile.fxml"));
            Parent root = loader.load();

            // Transmettre l'utilisateur au contrôleur de UserProfile
            UserProfileController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement du profil.");
        }
    }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        System.out.println("isAdmin: " + isAdmin);  // Vérifie que cette valeur est bien passée
    }


    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader;
            Parent root;

            // Vérifier si l'utilisateur est un admin à l'aide de UserSession
            if (UserSession.isAdmin()) {
                // Si l'utilisateur est un admin, revenir à AfficherUtilisateurs
                loader = new FXMLLoader(getClass().getResource("/Views/AfficherUtilisateurs.fxml"));
                root = loader.load();

                // Optionnel: Actualiser la liste des utilisateurs dans AfficherUtilisateursController
                AfficherUtilisateursController controller = loader.getController();
                controller.refreshUserList();  // Actualiser la liste des utilisateurs si nécessaire
            } else {
                // Si l'utilisateur n'est pas un admin, revenir à UserProfile
                loader = new FXMLLoader(getClass().getResource("/Views/UserProfile.fxml"));
                root = loader.load();
                UserProfileController controller = loader.getController();
                controller.setUser(currentUser);  // Passer l'utilisateur courant
            }

            // Remplacer la scène actuelle par la nouvelle scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors de la redirection.");
        }
    }



}


