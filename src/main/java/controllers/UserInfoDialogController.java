package controllers;

import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Period;

public class UserInfoDialogController {

    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label emailLabel;
    @FXML private Label roleLabel;
    @FXML private Label telephoneLabel;
    @FXML private Label ageLabel;
    @FXML private Label cinLabel;
    @FXML private Label permisLabel;

    private Stage dialogStage;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setUser(Utilisateur user) {
        nomLabel.setText("Nom : " + (user.getNom() != null ? user.getNom() : "inconnu"));
        prenomLabel.setText("Prénom : " + (user.getPrenom() != null ? user.getPrenom() : "inconnu"));
        emailLabel.setText("Email : " + (user.getEmail() != null ? user.getEmail() : "inconnu"));
        roleLabel.setText("Rôle : " + (user.getRole() != null ? user.getRole() : "inconnu"));
        telephoneLabel.setText("Téléphone : " + (user.getTelephone() != null ? user.getTelephone() : "inconnu"));

        if (user.getDate_naissance() != null) {
            int age = Period.between(user.getDate_naissance().toLocalDate(), LocalDate.now()).getYears();
            ageLabel.setText("Âge : " + age + " ans");
        } else {
            ageLabel.setText("Âge : inconnu");
        }

        if ("conducteur".equalsIgnoreCase(user.getRole())) {
            cinLabel.setText("CIN : " + (user.getCin() != null ? user.getCin() : "inconnu"));
            permisLabel.setText("Numéro permis : " + (user.getPermis() != null ? user.getPermis() : "inconnu"));
        } else {
            cinLabel.setText("");
            permisLabel.setText("");
        }
    }

    @FXML
    private void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}
