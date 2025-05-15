package controllers;

import entities.Reclamation;
import entities.Reponse;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.FrenchProfanityAPI;
import services.ServiceReclamation;
import services.ServiceReponse;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetailReclamationsAdmin {

    @FXML
    private Label DateRecDetail;

    @FXML
    private Label DescRecDetail;

    @FXML
    private Label NomUser;

    @FXML
    private Label PrenomUser;

    @FXML
    private Label PrioriteRecDetail;

    @FXML
    private Button AjouterReponse;

    @FXML
    private Button AnnulerAjoutReponse;

    @FXML
    private Label StatutRecDetail;

    @FXML
    private Label TypeRecDetail;
    @FXML
    private TextArea Contenu;

    @FXML
    private DatePicker DateReponse;

    private Reclamation currentReclamation;
    private Utilisateur user;


    public void setReclamation(Reclamation rec) {
        UserService utilisateurService = new UserService();
        Utilisateur utilisateur = utilisateurService.getById(rec.getIdUtilisateur());
        this.currentReclamation = rec;
        TypeRecDetail.setText(rec.getTypeReclamation());
        DescRecDetail.setText(rec.getDescription());
        DateRecDetail.setText(rec.getDateCreation());
        PrioriteRecDetail.setText(rec.getPriorite());
        StatutRecDetail.setText(rec.getStatut());
        PrenomUser.setText(utilisateur.getPrenom());
        NomUser.setText(utilisateur.getNom());
    }
    @FXML
    public void initialize() {
        // Initialize the service
        ServiceReponse serviceReponse = new ServiceReponse();
        user = UserSession.getCurrentUser();


        // Set default date to today
        //DateReponse.setValue(LocalDate.now());
    }

    public void OnAnnulerAjoutReponse(ActionEvent actionEvent) {
        try {
        // Load the AcceuilReclamation FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilReponseAdmin.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);

        // Get the current stage
        Stage stage = (Stage) AnnulerAjoutReponse.getScene().getWindow();

        // Set the new scene
        stage.setScene(scene);
        stage.setTitle("Gestion des Réclamations");
        stage.show();
    } catch (IOException e) {
        System.err.println("Error loading AcceuilReclamation.fxml: " + e.getMessage());
        e.printStackTrace();
    }

    }

    public void OnAjouterReponse(ActionEvent actionEvent) {
        // Validate inputs
        if (Contenu.getText().isEmpty() /*|| DateReponse.getValue() == null*/ ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        try {
            String contenu = Contenu.getText();
            if (FrenchProfanityAPI.containsProfanity(contenu)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText("Contenu inapproprié détecté");
                alert.setContentText("Votre réponse contient des mots inappropriés. Veuillez les modifier.");
                alert.showAndWait();
                return;  // Exit the method if profanity is found
            }
            // Create new response with the current reclamation's ID
            Reponse reponse = new Reponse(
                    currentReclamation.getIdReclamation(), // Use the current reclamation's ID
                    user.getId_utilisateur(), // Admin user ID
                    contenu,
                    LocalDate.now().format(DateTimeFormatter.ISO_DATE)// current date
            );

            // Add response to database
            ServiceReponse serviceReponse = new ServiceReponse();
            serviceReponse.ajouter(reponse);

            // Update reclamation status to "traité"
            currentReclamation.setStatut("traité");
            ServiceReclamation serviceReclamation = new ServiceReclamation();
            serviceReclamation.modifier(currentReclamation);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Réponse ajoutée");
            alert.setContentText("Votre réponse a été ajoutée avec succès et la réclamation a été marquée comme traitée.");
            alert.showAndWait();

            // Return to previous screen
            OnAnnulerAjoutReponse(actionEvent);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ajout");
            alert.setContentText("Une erreur est survenue lors de l'ajout de la réponse: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

