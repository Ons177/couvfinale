package Controllers;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class ReclamationAdminCardController {
    @FXML private Label typeLabel;
    @FXML private Label dateLabel;
    @FXML private Label descLabel;
    @FXML private Label priorityLabel;
    @FXML public Button voirReclamationBtn;
    @FXML private Circle priorityDot;
    @FXML private Rectangle priorityBar;

    private Reclamation reclamation;

    public void setData(Reclamation rec) {
        this.reclamation = rec;
        typeLabel.setText(rec.getTypeReclamation());
        dateLabel.setText(rec.getDateCreation());

        // Truncate the description to 50 characters and add "..." if it exceeds
        String description = rec.getDescription();
        if (description.length() > 50) {
            descLabel.setText(description.substring(0, 50) + "...");
        } else {
            descLabel.setText(description);
        }

        // Set priority visual indicators
        String priority = rec.getPriorite().toLowerCase();
        setPriorityVisuals(priority);
    }

    /**
     * Sets an action event handler for the view button
     * @param eventHandler The event handler to execute when the button is clicked
     */
    public void setOnViewButtonAction(EventHandler<ActionEvent> eventHandler) {
        voirReclamationBtn.setOnAction(eventHandler);
    }

    private void setPriorityVisuals(String priority) {
        Color priorityColor;
        String priorityText;
        String bgColor;

        switch (priority) {
            case "haute":
                priorityColor = Color.web("#E53E3E"); // Red
                priorityText = "HAUTE";
                bgColor = "#FEE2E2";
                break;
            case "moyenne":
                priorityColor = Color.web("#DD6B20"); // Orange
                priorityText = "MOYENNE";
                bgColor = "#FEEBC8";
                break;
            case "basse":
                priorityColor = Color.web("#38A169"); // Green
                priorityText = "BASSE";
                bgColor = "#C6F6D5";
                break;
            default:
                priorityColor = Color.web("#718096"); // Gray
                priorityText = "STANDARD";
                bgColor = "#E2E8F0";
                break;
        }

        // Set the dot and bar color
        priorityDot.setFill(priorityColor);
        priorityBar.setFill(priorityColor);

        // Set the priority label
        priorityLabel.setText(priorityText);
        priorityLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " +
                priorityColor.toString().replace("0x", "#") +
                "; -fx-background-color: " + bgColor +
                "; -fx-background-radius: 12; -fx-padding: 4 10;");
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void initialize() {
        // Button hover and click effects
        if (voirReclamationBtn != null) {
            // Default style
            String defaultStyle = "-fx-background-color: #3182CE; -fx-text-fill: white; -fx-background-radius: 18; -fx-font-weight: bold; -fx-font-size: 13px;";
            String hoverStyle = "-fx-background-color: #2C5282; -fx-text-fill: white; -fx-background-radius: 18; -fx-font-weight: bold; -fx-font-size: 13px;";
            String pressedStyle = "-fx-background-color: #1A365D; -fx-text-fill: white; -fx-background-radius: 18; -fx-font-weight: bold; -fx-font-size: 13px;";

            voirReclamationBtn.setStyle(defaultStyle);

            // Hover effects
            voirReclamationBtn.setOnMouseEntered(e -> {
                voirReclamationBtn.setStyle(hoverStyle);

                // Scale animation on hover
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), voirReclamationBtn);
                scaleTransition.setToX(1.05);
                scaleTransition.setToY(1.05);
                scaleTransition.play();
            });

            voirReclamationBtn.setOnMouseExited(e -> {
                voirReclamationBtn.setStyle(defaultStyle);

                // Scale back to normal
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), voirReclamationBtn);
                scaleTransition.setToX(1.0);
                scaleTransition.setToY(1.0);
                scaleTransition.play();
            });

            // Press effects
            voirReclamationBtn.setOnMousePressed(e -> voirReclamationBtn.setStyle(pressedStyle));
            voirReclamationBtn.setOnMouseReleased(e -> {
                if (voirReclamationBtn.isHover()) {
                    voirReclamationBtn.setStyle(hoverStyle);
                } else {
                    voirReclamationBtn.setStyle(defaultStyle);
                }
            });
        }

        // Add a subtle fade-in animation when the card appears
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), voirReclamationBtn.getParent());
        fadeIn.setFromValue(0.7);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}