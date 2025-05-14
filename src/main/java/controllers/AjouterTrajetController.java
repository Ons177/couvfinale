package controllers;

import entities.Trajet;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceTrajet;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AjouterTrajetController {

    @FXML
    private TextField depart_id;
    @FXML
    private TextField destination_id;
    @FXML
    private DatePicker date_id;
    @FXML
    private TextField heure_id;
    @FXML
    private TextField prix_id;
    @FXML
    private TextField place_id;
    @FXML
    private ComboBox<String> bagage_id;
    @FXML
    private Label departErrorLabel;
    @FXML
    private Label destinationErrorLabel;
    @FXML
    private Label dateErrorLabel;
    @FXML
    private Label heureErrorLabel;
    @FXML
    private Label prixErrorLabel;
    @FXML
    private Label placeErrorLabel;
    @FXML
    private Label bagageErrorLabel;

    private ServiceTrajet serviceTrajet = new ServiceTrajet();
    private Trajet trajetToModify;
    private boolean isModificationMode = false;

    public void setTrajetData(Trajet trajet) {
        this.trajetToModify = trajet;
        this.isModificationMode = true;

        // Pre-fill fields with trajet data
        if (trajet != null) {
            depart_id.setText(trajet.getVilleDepart());
            destination_id.setText(trajet.getVilleArrivee());
            date_id.setValue(trajet.getDateDepart());
            heure_id.setText(trajet.getHeureDepart() != null ? trajet.getHeureDepart().toString() : "");
            prix_id.setText(String.valueOf(trajet.getPrix()));
            place_id.setText(String.valueOf(trajet.getNbrPlaces()));
            bagage_id.setValue(trajet.getBagage());
            System.out.println("Trajet set for modification: ID=" + trajet.getIdTrajet());
        } else {
            System.out.println("Error: Trajet is null");
        }
    }

    @FXML
    private void initialize() {
        bagage_id.setItems(FXCollections.observableArrayList("Autorisé", "Non autorisé"));
        System.out.println("AjouterTrajetController initialized");
    }

    @FXML
    private void ajoutertrajet(ActionEvent event) {
        System.out.println("Attempting to " + (isModificationMode ? "update" : "save") + " trajet...");

        // Reset error states
        clearErrorStyles();

        // Validate inputs
        String villeDepart = depart_id.getText().trim();
        String villeArrivee = destination_id.getText().trim();
        LocalDate dateDepart = date_id.getValue();
        String heureDepartText = heure_id.getText().trim();
        String prixText = prix_id.getText().trim();
        String nbrPlacesText = place_id.getText().trim();
        String bagage = bagage_id.getValue();

        boolean hasError = false;

        // Validate ville_depart
        if (villeDepart.isEmpty()) {
            departErrorLabel.setText("La ville de départ est requise.");
            departErrorLabel.setVisible(true);
            departErrorLabel.setManaged(true);
            depart_id.getStyleClass().add("error");
            hasError = true;
        } else if (!villeDepart.matches("[a-zA-Z\\s]+")) {
            departErrorLabel.setText("La ville de départ ne doit contenir que des lettres et espaces.");
            departErrorLabel.setVisible(true);
            departErrorLabel.setManaged(true);
            depart_id.getStyleClass().add("error");
            hasError = true;
        }

        // Validate ville_arrivee
        if (villeArrivee.isEmpty()) {
            destinationErrorLabel.setText("La ville d'arrivée est requise.");
            destinationErrorLabel.setVisible(true);
            destinationErrorLabel.setManaged(true);
            destination_id.getStyleClass().add("error");
            hasError = true;
        } else if (!villeArrivee.matches("[a-zA-Z\\s]+")) {
            destinationErrorLabel.setText("La ville d'arrivée ne doit contenir que des lettres et espaces.");
            destinationErrorLabel.setVisible(true);
            destinationErrorLabel.setManaged(true);
            destination_id.getStyleClass().add("error");
            hasError = true;
        }

        // Validate date_depart
        if (dateDepart == null) {
            dateErrorLabel.setText("La date de départ est requritus.");
            dateErrorLabel.setVisible(true);
            dateErrorLabel.setManaged(true);
            date_id.getStyleClass().add("error");
            hasError = true;
        } else if (dateDepart.isBefore(LocalDate.now())) {
            dateErrorLabel.setText("La date de départ ne peut pas être dans le passé.");
            dateErrorLabel.setVisible(true);
            dateErrorLabel.setManaged(true);
            date_id.getStyleClass().add("error");
            hasError = true;
        }

        // Validate heure_depart
        LocalTime heureDepart = null;
        if (heureDepartText.isEmpty()) {
            heureErrorLabel.setText("L'heure de départ est requise.");
            heureErrorLabel.setVisible(true);
            heureErrorLabel.setManaged(true);
            heure_id.getStyleClass().add("error");
            hasError = true;
        } else {
            try {
                heureDepart = LocalTime.parse(heureDepartText, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                heureErrorLabel.setText("Format d'heure invalide (ex: HH:MM).");
                heureErrorLabel.setVisible(true);
                heureErrorLabel.setManaged(true);
                heure_id.getStyleClass().add("error");
                hasError = true;
            }
        }

        // Validate prix
        float prix = 0;
        try {
            prix = Float.parseFloat(prixText);
            if (prix <= 0) {
                prixErrorLabel.setText("Le prix doit être positif.");
                prixErrorLabel.setVisible(true);
                prixErrorLabel.setManaged(true);
                prix_id.getStyleClass().add("error");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            prixErrorLabel.setText("Le prix doit être un nombre valide.");
            prixErrorLabel.setVisible(true);
            prixErrorLabel.setManaged(true);
            prix_id.getStyleClass().add("error");
            hasError = true;
        }

        // Validate nbr_places
        int nbrPlaces = 0;
        try {
            nbrPlaces = Integer.parseInt(nbrPlacesText);
            if (nbrPlaces <= 0) {
                placeErrorLabel.setText("Le nombre de places doit être positif.");
                placeErrorLabel.setVisible(true);
                placeErrorLabel.setManaged(true);
                place_id.getStyleClass().add("error");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            placeErrorLabel.setText("Le nombre de places doit être un nombre valide.");
            placeErrorLabel.setVisible(true);
            placeErrorLabel.setManaged(true);
            place_id.getStyleClass().add("error");
            hasError = true;
        }

        // Validate bagage
        if (bagage == null) {
            bagageErrorLabel.setText("Le type de bagage est requis.");
            bagageErrorLabel.setVisible(true);
            bagageErrorLabel.setManaged(true);
            bagage_id.getStyleClass().add("error");
            hasError = true;
        }

        // Stop if validation failed
        if (hasError) {
            System.out.println("Trajet " + (isModificationMode ? "update" : "save") + " aborted due to validation errors");
            return;
        }

        try {
            if (isModificationMode && trajetToModify != null) {
                // Update existing trajet
                trajetToModify.setPrix(prix);
                trajetToModify.setDateDepart(dateDepart);
                trajetToModify.setHeureDepart(heureDepart);
                trajetToModify.setVilleDepart(villeDepart);
                trajetToModify.setVilleArrivee(villeArrivee);
                trajetToModify.setNbrPlaces(nbrPlaces);
                trajetToModify.setBagage(bagage);

                System.out.println("Updating trajet: " + trajetToModify);
                serviceTrajet.modifier(trajetToModify);

                // Verify update
                List<Trajet> trajets = serviceTrajet.recuperer();
                LocalTime finalHeureDepart = heureDepart;
                float finalPrix = prix;
                int finalNbrPlaces = nbrPlaces;
                boolean updated = trajets.stream().anyMatch(t ->
                        t.getIdTrajet() == trajetToModify.getIdTrajet() &&
                                t.getVilleDepart().equals(villeDepart) &&
                                t.getVilleArrivee().equals(villeArrivee) &&
                                t.getDateDepart().equals(dateDepart) &&
                                t.getHeureDepart().equals(finalHeureDepart) &&
                                t.getPrix() == finalPrix &&
                                t.getNbrPlaces() == finalNbrPlaces &&
                                t.getBagage().equals(bagage));

                if (updated) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Trajet mis à jour avec succès !");
                    Stage stage = (Stage) depart_id.getScene().getWindow();
                    stage.close();
                } else {
                    bagageErrorLabel.setText("Erreur: Le trajet n'a pas été mis à jour.");
                    bagageErrorLabel.setVisible(true);
                    bagageErrorLabel.setManaged(true);
                    System.out.println("Trajet not found in database after update attempt");
                }
            } else {
                // Create new trajet
                Trajet trajet = new Trajet(prix, dateDepart, heureDepart, villeDepart, villeArrivee, nbrPlaces, bagage);
                System.out.println("Saving trajet: " + trajet);
                serviceTrajet.ajouter(trajet);

                // Verify save
                List<Trajet> trajets = serviceTrajet.recuperer();
                LocalTime finalHeureDepart1 = heureDepart;
                float finalPrix1 = prix;
                int finalNbrPlaces1 = nbrPlaces;
                boolean saved = trajets.stream().anyMatch(t ->
                        t.getVilleDepart().equals(villeDepart) &&
                                t.getVilleArrivee().equals(villeArrivee) &&
                                t.getDateDepart().equals(dateDepart) &&
                                t.getHeureDepart().equals(finalHeureDepart1) &&
                                t.getPrix() == finalPrix1 &&
                                t.getNbrPlaces() == finalNbrPlaces1 &&
                                t.getBagage().equals(bagage));

                if (saved) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Trajet ajouté avec succès !");
                    Stage stage = (Stage) depart_id.getScene().getWindow();
                    stage.close();
                } else {
                    bagageErrorLabel.setText("Erreur: Le trajet n'a pas été enregistré.");
                    bagageErrorLabel.setVisible(true);
                    bagageErrorLabel.setManaged(true);
                    System.out.println("Trajet not found in database after save attempt");
                }
            }
        } catch (SQLException e) {
            bagageErrorLabel.setText("Erreur lors de l'enregistrement: " + e.getMessage());
            bagageErrorLabel.setVisible(true);
            bagageErrorLabel.setManaged(true);
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            bagageErrorLabel.setText("Erreur de saisie: Vérifiez les champs.");
            bagageErrorLabel.setVisible(true);
            bagageErrorLabel.setManaged(true);
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void annulertrajet(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/accueiltrajets.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des trajets");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error returning to trajet list: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à la liste des trajets: " + e.getMessage());
        }
    }


    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/accueiltrajets.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error returning to home: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de retourner à l'accueil: " + e.getMessage());
        }
    }

    private void clearErrorStyles() {
        depart_id.getStyleClass().remove("error");
        destination_id.getStyleClass().remove("error");
        date_id.getStyleClass().remove("error");
        heure_id.getStyleClass().remove("error");
        prix_id.getStyleClass().remove("error");
        place_id.getStyleClass().remove("error");
        bagage_id.getStyleClass().remove("error");

        departErrorLabel.setText("");
        departErrorLabel.setVisible(false);
        departErrorLabel.setManaged(false);

        destinationErrorLabel.setText("");
        destinationErrorLabel.setVisible(false);
        destinationErrorLabel.setManaged(false);

        dateErrorLabel.setText("");
        dateErrorLabel.setVisible(false);
        dateErrorLabel.setManaged(false);

        heureErrorLabel.setText("");
        heureErrorLabel.setVisible(false);
        heureErrorLabel.setManaged(false);

        prixErrorLabel.setText("");
        prixErrorLabel.setVisible(false);
        prixErrorLabel.setManaged(false);

        placeErrorLabel.setText("");
        placeErrorLabel.setVisible(false);
        placeErrorLabel.setManaged(false);

        bagageErrorLabel.setText("");
        bagageErrorLabel.setVisible(false);
        bagageErrorLabel.setManaged(false);

        System.out.println("Error styles cleared");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}