package controllers;

import entities.Reclamation;
import entities.Utilisateur;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class ReclamationReponduCardController {

    @FXML private Label dateLabel;
    @FXML private Label descLabel;
    @FXML private Label typeLabel;
    @FXML public Button voirReponseBtn;
    private Utilisateur user;

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
    }

    /**
     * Sets an action event handler for the view response button
     * @param eventHandler The event handler to execute when the button is clicked
     */
    public void setOnViewResponseButtonAction(EventHandler<ActionEvent> eventHandler) {
        voirReponseBtn.setOnAction(eventHandler);
    }

    public Reclamation getReclamation() {
        return reclamation;
    }

    public void initialize() {
        if (voirReponseBtn != null) {
            // Default style - using teal color to indicate "responded" status
            String defaultStyle = "-fx-background-color: #38B2AC; -fx-text-fill: white; -fx-background-radius: 18; -fx-font-weight: bold; -fx-font-size: 13px;";
            String hoverStyle = "-fx-background-color: #2C7A7B; -fx-text-fill: white; -fx-background-radius: 18; -fx-font-weight: bold; -fx-font-size: 13px;";
            String pressedStyle = "-fx-background-color: #234E52; -fx-text-fill: white; -fx-background-radius: 18; -fx-font-weight: bold; -fx-font-size: 13px;";

            voirReponseBtn.setStyle(defaultStyle);

            // Hover effects with animation
            voirReponseBtn.setOnMouseEntered(e -> {
                voirReponseBtn.setStyle(hoverStyle);

                // Scale animation on hover
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), voirReponseBtn);
                scaleTransition.setToX(1.05);
                scaleTransition.setToY(1.05);
                scaleTransition.play();
            });

            voirReponseBtn.setOnMouseExited(e -> {
                voirReponseBtn.setStyle(defaultStyle);

                // Scale back to normal
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), voirReponseBtn);
                scaleTransition.setToX(1.0);
                scaleTransition.setToY(1.0);
                scaleTransition.play();
            });

            // Press effects
            voirReponseBtn.setOnMousePressed(e -> voirReponseBtn.setStyle(pressedStyle));
            voirReponseBtn.setOnMouseReleased(e -> {
                if (voirReponseBtn.isHover()) {
                    voirReponseBtn.setStyle(hoverStyle);
                } else {
                    voirReponseBtn.setStyle(defaultStyle);
                }
            });
        }

        // Add a subtle fade-in animation when the card appears
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), voirReponseBtn.getParent());
        fadeIn.setFromValue(0.7);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}