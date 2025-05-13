package Controllers;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ReclamationReponduCardController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label descLabel;

    @FXML
    private Label typeLabel;
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

    @FXML
    Button voirReponseBtn;
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

}

