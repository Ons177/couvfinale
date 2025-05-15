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
import java.time.Period;
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
        // Ajout des rôles dans la ComboBox
        roleComboBox.getItems().addAll("PASSAGER", "CONDUCTEUR", "ADMIN");

        // Afficher uniquement les champs conducteur si rôle = CONDUCTEUR
        roleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isDriver = "CONDUCTEUR".equals(newVal);
            driverFields.setVisible(isDriver);
            driverFields.setManaged(isDriver);
        });

        // Optionnel : interdire la sélection d’une date plus récente que 18 ans (date max)
        dateNaissanceField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Désactive toutes les dates après aujourd'hui moins 18 ans
                setDisable(empty || date.isAfter(LocalDate.now().minusYears(18)));
            }
        });

        // Action sur bouton Ajouter
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

        // Validation téléphone tunisien : 8 chiffres, commence par 2, 4, 5, 7 ou 9
        if (!telephone.matches("^[24579]\\d{7}$")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres et commencer par 2, 4, 5, 7 ou 9.");
            return;
        }

        // Validation des conducteurs
        if ("CONDUCTEUR".equals(role)) {
            String cin = cinField.getText().trim();
            String permis = permisField.getText().trim();

            if (cin.isEmpty() || permis.isEmpty()) {
                showAlert("Erreur", "Pour les conducteurs, le CIN et le permis sont obligatoires.");
                return;
            }

            // Validation CIN : exactement 8 chiffres
            if (!cin.matches("\\d{8}")) {
                showAlert("Erreur", "Le CIN doit comporter exactement 8 chiffres.");
                return;
            }
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

    private void enregistrerUtilisateur(Utilisateur user) {
        if (userService.register(user)) {
            showAlert("Succès", "Utilisateur ajouté avec succès !");
            refreshDashboard();
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

    private boolean isAtLeast18YearsOld(LocalDate birthDate) {
        if (birthDate == null) return false;
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }



        private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void refreshDashboard() {
        reloadAdminDashboard();
        closeCurrentStage();
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
        currentStage.close();
    }

    // Setter pour le contrôleur admin (non utilisé dans cet exemple mais peut servir)
    public void setAdminDashboardController(AdminDashboardController controller) {
        this.adminDashboardController = controller;
    }
}