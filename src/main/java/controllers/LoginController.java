package controllers;

import entities.Utilisateur;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.UserService;
import entities.MailService;
import javafx.event.ActionEvent;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private final Map<String, LocalDateTime> lockTimes = new HashMap<>();
    private final Map<String, Integer> failedAttempts = new HashMap<>();

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private VBox loginForm;
    @FXML private VBox resetPasswordForm;
    @FXML private TextField resetEmailField;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        resetPasswordForm.setVisible(false);
    }

    @FXML
    public void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showTemporaryError("Veuillez remplir tous les champs.");
            return;
        }

        // Vérification blocage
        if (lockTimes.containsKey(email)) {
            LocalDateTime lockTime = lockTimes.get(email);
            long minutesPassed = java.time.Duration.between(lockTime, LocalDateTime.now()).toMinutes();

            if (minutesPassed < 2) {
                showTemporaryError("Compte bloqué pour 2 minutes.");

                showAlert("Compte bloqué", "Votre compte est temporairement bloqué. Réessayez dans quelques instants.");
                return;
            } else {
                lockTimes.remove(email);
                failedAttempts.put(email, 0);
            }
        }

        Utilisateur user = userService.login(email, password);

        if (user == null) {
            int attempts = failedAttempts.getOrDefault(email, 0) + 1;
            failedAttempts.put(email, attempts);

            if (attempts >= 3) {
                lockTimes.put(email, LocalDateTime.now());
                showAlert("Compte bloqué", "Vous avez dépassé les 3 tentatives. Votre compte est bloqué pour 2 minutes.");
                showTemporaryError("Compte bloqué pour 2 minutes.");
            } else {
                showTemporaryError("Identifiants incorrects. Tentative " + attempts + "/3.");
            }
            return;
        }

        // Authentification réussie
        failedAttempts.remove(email);
        lockTimes.remove(email);

        if ("CONDUCTEUR".equalsIgnoreCase(user.getRole()) && !user.isApproved()) {
            showTemporaryError("Votre compte conducteur n'est pas encore approuvé.");
            return;
        }

        UserSession.setCurrentUser(user);

        switch (user.getRole().toUpperCase()) {
            case "ADMIN":
                loadAdminDashboard();
                break;
            case "CONDUCTEUR":
            case "PASSAGER":
                loadUserMenu(user);
                break;
            default:
                showTemporaryError("Rôle utilisateur non reconnu.");
        }
    }

    private void loadUserMenu(Utilisateur user) {
        String fxmlPath;
        switch (user.getRole().toUpperCase()) {
            case "CONDUCTEUR":
                fxmlPath = "/Views/MenuConducteur.fxml";
                break;
            case "PASSAGER":
                fxmlPath = "/Views/MenuPassager.fxml";
                break;
            default:
                showTemporaryError("Rôle utilisateur non reconnu.");
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du chargement de l'interface.");
            e.printStackTrace();
        }
    }

    private void loadAdminDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/MenuAdmin.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger le menu admin.");
            e.printStackTrace();
        }
    }

    private void showTemporaryError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> errorLabel.setVisible(false));
        pause.play();
    }

    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Signup.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Inscription - SmartRide");
            stage.setMaximized(true);
            stage.show();

            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'écran d'inscription.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        loginForm.setVisible(false);
        resetPasswordForm.setVisible(true);
    }

    public void handleSendResetLink() {
        String email = resetEmailField.getText().trim();

        if (email.isEmpty()) {
            showAlert("Attention", "Veuillez entrer votre adresse e-mail.");
            return;
        }

        Utilisateur user = userService.getUtilisateurByEmail(email);
        if (user == null) {
            showAlert("Erreur", "Aucun compte trouvé avec cet e-mail.");
            return;
        }

        String password = user.getMdp();
        String subject = "🔒 Récupération de mot de passe - Smart Ride";
        String logoUrl = "https://i.imgur.com/Qb6xSRv.png";
        String htmlMessage = "<html><body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;'>"
                + "<div style='max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 0 10px rgba(0,0,0,0.1);'>"
                + "<div style='background-color: #007BFF; padding: 20px; text-align: center;'>"
                + "<img src='" + logoUrl + "' alt='Smart Ride' style='width: 120px;'/></div>"
                + "<div style='padding: 30px;'>"
                + "<h2 style='color: #333;'>Bonjour " + user.getPrenom() + ",</h2>"
                + "<p style='font-size: 16px;'>Vous avez demandé à récupérer votre mot de passe. Voici vos informations :</p>"
                + "<div style='background-color: #f0f0f0; padding: 20px; border-radius: 6px;'>"
                + "<p><strong>Email :</strong> " + user.getEmail() + "</p>"
                + "<p><strong>Mot de passe :</strong> " + password + "</p>"
                + "</div>"
                + "<p style='margin-top: 20px; font-size: 14px; color: #555;'>Pour votre sécurité, pensez à le modifier rapidement.</p>"
                + "<p style='margin-top: 30px;'>🚗 L’équipe Smart Ride</p>"
                + "</div></div></body></html>";

        final String fromEmail = "smartrides11@gmail.com";
        final String appPassword = "akpc enam yncv sjjt"; // à stocker ailleurs en prod

        try {
            MailService.send(email, subject, htmlMessage, fromEmail, appPassword, true);
            showAlert("Succès", "Un e-mail vous a été envoyé avec votre mot de passe.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'envoi de l'e-mail. Veuillez réessayer.");
        }
    }



    @FXML
    private void handleCancelResetPassword() {
        resetPasswordForm.setVisible(false);
        loginForm.setVisible(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}