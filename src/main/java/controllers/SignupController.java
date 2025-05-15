package controllers;

import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.UserService;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupController {

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
    private Button signupButton;
    @FXML
    private Button backbutton;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("PASSAGER", "CONDUCTEUR");

        roleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isDriver = "CONDUCTEUR".equals(newVal);
            driverFields.setVisible(isDriver);
            driverFields.setManaged(isDriver);
        });

        // Empêche les dates trop récentes (moins de 18 ans)
        dateNaissanceField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now().minusYears(18)));
            }
        });

        signupButton.setOnAction(event -> handleSignup());
    }

    private void handleSignup() {
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

        // Validation âge
        if (!isAtLeast18YearsOld(dateNaissance)) {
            showAlert("Erreur", "Vous devez avoir au moins 18 ans pour vous inscrire.");
            return;
        }

        // Validation email
        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'email saisi n'est pas valide.");
            return;
        }

        // Validation téléphone tunisien : 8 chiffres et commence par 2, 4, 5, 7 ou 9
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

            // Validation du CIN : exactement 8 chiffres
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
            showAlert("Succès", "Inscription réussie !");
            loadLoginPage();
        } else {
            showAlert("Erreur", "L'inscription a échoué. Veuillez réessayer.");
        }
    }
        private boolean isAtLeast18YearsOld(LocalDate dateNaissance) {
        LocalDate today = LocalDate.now();
        return dateNaissance != null && (dateNaissance.plusYears(18).isBefore(today) || dateNaissance.plusYears(18).isEqual(today));
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



    private void loadLoginPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void onbackbuttonclicked(ActionEvent event) {
        // Obtenir la fenêtre actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Ouvrir la fenêtre de connexion (Login)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            Scene scene = new Scene(root);

            newStage.setScene(scene);
            newStage.setTitle("Connexion - SmartRides");

            // Adapter la taille à l'écran
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            newStage.setWidth(screenBounds.getWidth());
            newStage.setHeight(screenBounds.getHeight());

            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}