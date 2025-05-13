package Controllers;

import entities.Reclamation;
import entities.Reponse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class ReclamationCardController {
    @FXML private Label typeLabel;
    @FXML private Label dateLabel;
    @FXML private Label descLabel;
    @FXML public Button voirReponseBtn;
    @FXML
    private Label ContenuRep;
    @FXML
    private Label DateRep;

    private Reclamation reclamation;

    public void setData(Reclamation rec) {
        this.reclamation = rec;
        typeLabel.setText(rec.getTypeReclamation());
        dateLabel.setText(rec.getDateCreation());
        String description = rec.getDescription();
        if (description.length() > 15) {
            descLabel.setText(description.substring(0, 15) + "...");
        } else {
            descLabel.setText(description);
        }
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void initialize() {
        if (voirReponseBtn != null) {
            // Hover effect: darker blue
            voirReponseBtn.setOnMouseEntered(e -> voirReponseBtn.setStyle(
                "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 16;"
            ));
            // Restore original green when mouse exits
            voirReponseBtn.setOnMouseExited(e -> voirReponseBtn.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 16;"
            ));
            // Keep pressed effect as well if you want
            voirReponseBtn.setOnMousePressed(e -> voirReponseBtn.setStyle(
                "-fx-background-color: #0d47a1; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 16;"
            ));
            voirReponseBtn.setOnMouseReleased(e -> voirReponseBtn.setStyle(
                "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 16;"
            ));
        }
    }

    public void setReclamationAndReponse(Reclamation rec, Reponse rep) {
        // Set reclamation details as before
        // ... (your existing code for setting type, description, etc.)

        // Set response details
        if (rep != null) {
            ContenuRep.setText(rep.getContenu());
            DateRep.setText(rep.getDateReponse());
        } else {
            ContenuRep.setText("Aucune réponse");
            DateRep.setText("");
        }
    }
} 