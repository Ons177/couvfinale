package Controllers;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ReclamationAdminCardController {
    @FXML private Label typeLabel;
    @FXML private Label dateLabel;
    @FXML private Label descLabel;
    @FXML
    public Button voirReclamationBtn;
    @FXML private Circle priorityDot;  // Reference to the dot in the FXML

    private Reclamation reclamation;

    public void setData(Reclamation rec) {
        this.reclamation = rec;
        typeLabel.setText(rec.getTypeReclamation());
        dateLabel.setText(rec.getDateCreation());

        // Truncate the description to 15 characters and add "..." if it exceeds
        String description = rec.getDescription();
        if (description.length() > 15) {
            descLabel.setText(description.substring(0, 15) + "...");
        } else {
            descLabel.setText(description);
        }

        // Set the dot color based on the priority
        setDotColor(rec.getPriorite());
    }

    private void setDotColor(String priority) {
        switch (priority.toLowerCase()) {
            case "haute":
                priorityDot.setFill(Color.RED);  // Red dot for high priority
                break;
            case "moyenne":
                priorityDot.setFill(Color.YELLOW);  // Yellow dot for medium priority
                break;
            case "basse":
                priorityDot.setFill(Color.GREEN);  // Green dot for low priority
                break;
            default:
                priorityDot.setFill(Color.GRAY);  // Default color if priority is unknown
                break;
        }
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void initialize() {
        if (voirReclamationBtn != null) {
            // Hover effect: darker blue
            voirReclamationBtn.setOnMouseEntered(e -> voirReclamationBtn.setStyle(
                    "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 16;"
            ));
            // Restore original green when mouse exits
            voirReclamationBtn.setOnMouseExited(e -> voirReclamationBtn.setStyle(
                    "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 16;"
            ));
            // Keep pressed effect as well if you want
            voirReclamationBtn.setOnMousePressed(e -> voirReclamationBtn.setStyle(
                    "-fx-background-color: #0d47a1; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 16;"
            ));
            voirReclamationBtn.setOnMouseReleased(e -> voirReclamationBtn.setStyle(
                    "-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 16;"
            ));
        }
    }
}
