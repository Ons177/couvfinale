package Controllers;

import entities.Utilisateur;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
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

    private Map<String, LocalDateTime> lockTimes = new HashMap<>();
    private Map<String, Integer> failedAttempts = new HashMap<>();

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

        // Vérifie si le compte est bloqué
        if (lockTimes.containsKey(email)) {
            LocalDateTime lockTime = lockTimes.get(email);
            java.time.Duration elapsed = java.time.Duration.between(lockTime, LocalDateTime.now());

            if (elapsed.toMinutes() < 2) {
                showTemporaryError("Compte bloqué pour 2 minutes.");

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Compte bloqué");
                alert.setHeaderText(null);
                alert.setContentText("Votre compte est temporairement bloqué suite à plusieurs tentatives échouées. Réessayez dans quelques instants.");
                alert.showAndWait();
                return;
            } else {
                lockTimes.remove(email);
                failedAttempts.put(email, 0); // réinitialise les tentatives
            }
        }

        Utilisateur user = userService.login(email, password);

        if (user == null) {
            int attempts = failedAttempts.getOrDefault(email, 0) + 1;
            failedAttempts.put(email, attempts);

            if (attempts >= 3) {
                lockTimes.put(email, LocalDateTime.now());

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Compte bloqué");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez dépassé les 3 tentatives. Votre compte est bloqué pour 2 minutes.");
                alert.showAndWait();

                showTemporaryError("Compte bloqué pour 2 minutes.");
            } else {
                showTemporaryError("Identifiants incorrects. Tentative " + attempts + "/3.");
            }

            return;
        }

        // Compte trouvé et non bloqué
        failedAttempts.remove(email);
        lockTimes.remove(email);

        if ("CONDUCTEUR".equals(user.getRole()) && !user.isApproved()) {
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
                loadUserProfile(user);
                break;
            default:
                showTemporaryError("Rôle utilisateur non reconnu.");
        }
    }
    private void loadUserProfile(Utilisateur user) {
        String fxmlFile;

        switch (user.getRole().toUpperCase()) {
            case "CONDUCTEUR":
                fxmlFile = "/Views/MenuConducteur.fxml";
                break;
            case "PASSAGER":
                fxmlFile = "/Views/MenuPassager.fxml";
                break;
            default:
                showTemporaryError("Rôle utilisateur non reconnu.");
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Enregistre l’utilisateur connecté dans la session
            UserSession.setCurrentUser(user);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showTemporaryError("Erreur lors du chargement de l'interface.");
        }
    }



    private void loadAdminDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/MenuAdmin.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();

            Platform.runLater(() -> stage.setMaximized(true));
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

   /* private void loadAdminDashboard() {
        try {
            // Charger la nouvelle scène
            Parent root = FXMLLoader.load(getClass().getResource("/Views/AdminDashboard.fxml"));

            // Obtenir l'instance du Stage actuel
            Stage stage = (Stage) emailField.getScene().getWindow();

            // Changer la scène
            Scene newScene = new Scene(root);
            stage.setScene(newScene);

            // Afficher la fenêtre
            stage.show();

            // Maximiser la fenêtre après que la scène a été complètement chargée
            Platform.runLater(() -> {
                stage.setMaximized(true);  // Maximiser la fenêtre après que la scène soit affichée
            });
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger le tableau de bord admin.");
            e.printStackTrace();
        }
    }
*/


    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Signup.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Inscription - SmartRides");
            newStage.setMaximized(true);
            newStage.show();

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

        String subject = "Récupération de mot de passe - Smart Ride";
        String message = "Bonjour " + user.getPrenom() + ",\n\n"
                + "Voici votre mot de passe : " + password + "\n\n"
                + "Merci d'utiliser notre service.\n"
                + "Cordialement,\nL'équipe Smart Ride.";

        final String fromEmail = "smartrides11@gmail.com";
        final String appPassword = "akpc enam yncv sjjt";

        MailService.send(email, subject, message, fromEmail, appPassword);

        showAlert("Succès", "Un e-mail vous a été envoyé avec votre mot de passe.");
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

