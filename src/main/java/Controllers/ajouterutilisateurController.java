package controllers;

import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ajouterutilisateurController {
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField telephoneField;
    @FXML
    private DatePicker dateNaissanceField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private VBox driverFields;
    @FXML
    private TextField cinField;
    @FXML
    private TextField permisField;
    @FXML
    private Button adduserButton;

    private UserService userService = new UserService();
    private AdminDashboardController adminDashboardController;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("PASSAGER", "CONDUCTEUR", "ADMIN");
        roleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            driverFields.setVisible("CONDUCTEUR".equals(newVal));
            driverFields.setManaged("CONDUCTEUR".equals(newVal));
        });

        adduserButton.setOnAction(event -> handleAddUser());
    }

    private void handleAddUser() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String telephone = telephoneField.getText();
        LocalDate dateNaissance = dateNaissanceField.getValue();
        String role = roleComboBox.getValue();

        // Validation des champs
        if (isAnyFieldEmpty(nom, prenom, email, password, telephone, dateNaissance, role)) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        // Validation email
        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'email saisi n'est pas valide.");
            return;
        }

        // Validation des conducteurs
        if ("CONDUCTEUR".equals(role) && (cinField.getText().isEmpty() || permisField.getText().isEmpty())) {
            showAlert("Erreur", "Pour les conducteurs, le CIN et le permis sont obligatoires.");
            return;
        }

        // Création de l'utilisateur
        Utilisateur user;
        if ("CONDUCTEUR".equals(role)) {
            user = new Utilisateur(nom, prenom, email, password, telephone, role,
                    Date.valueOf(dateNaissance), cinField.getText(), permisField.getText());
        } else {
            user = new Utilisateur(nom, prenom, email, password, telephone, role, Date.valueOf(dateNaissance));
        }

        // Enregistrement de l'utilisateur
        if (userService.register(user)) {
            showAlert("Succès", "Utilisateur ajouté avec succès !");
            refreshDashboard();  // Rafraîchir la fenêtre du tableau de bord
        } else {
            showAlert("Erreur", "L'ajout a échoué. Veuillez réessayer.");
        }
    }

    private boolean isAnyFieldEmpty(String nom, String prenom, String email, String password, String telephone, LocalDate dateNaissance, String role) {
        return nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() ||
                telephone.isEmpty() || dateNaissance == null || role == null;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void refreshDashboard() {
        reloadAdminDashboard();  // Ouvre une nouvelle fenêtre pour le tableau de bord
        closeCurrentStage();     // Ferme la fenêtre actuelle de création d'utilisateur
    }


    private void reloadAdminDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/AdminDashboard.fxml"));
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.setMaximized(true);
            newStage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger le tableau de bord admin.");
            e.printStackTrace();
        }
    }


    private void closeCurrentStage() {
        Stage currentStage = (Stage) adduserButton.getScene().getWindow();
        currentStage.close();  // Ferme la fenêtre actuelle de création d'utilisateur
    }

    // Methode pour définir l'instance du contrôleur AdminDashboard
    public void setAdminDashboardController(AdminDashboardController controller) {
        this.adminDashboardController = controller;
    }
}

