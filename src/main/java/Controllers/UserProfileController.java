package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.VehicleService;
import entities.Utilisateur;
import entities.Vehicule;

import java.io.IOException;
import java.util.Optional;

public class UserProfileController {

    @FXML private VBox vehicleSection, addVehiclePanel, vehicleInfoPanel;
    @FXML private Button addVehicleButton, deleteVehicleButton, modifyVehicleButton, saveVehicleButton;
    @FXML private TextField marqueField, modeleField, couleurField, matriculeField;
    @FXML private TextField marqueFieldd, modeleFieldd, couleurFieldd, matriculeFieldd;
    @FXML private Spinner<Integer> placesSpinner, placesSpinnerr;
    @FXML private Label marqueLabel, modeleLabel, couleurLabel, matriculeLabel, placesLabel;
    @FXML private TextField nomField, prenomField, emailField, telField, naissanceField, roleField;
    @FXML private Button logoutButton, modifyButton;

    private Utilisateur currentUser;
    private final VehicleService vehicleService = new VehicleService();

    @FXML
    public void initialize() {
        addVehiclePanel.setVisible(false);
        vehicleInfoPanel.setVisible(false);

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 4);
        placesSpinner.setValueFactory(valueFactory);
        placesSpinner.setEditable(true);
    }

    public void setUser(Utilisateur user) {
        this.currentUser = user;
        updateProfileFields();

        if ("CONDUCTEUR".equalsIgnoreCase(user.getRole())) {
            Vehicule vehicule = vehicleService.getVehiculeByUserId(user.getId_utilisateur());
            if (vehicule != null) {
                user.setVehicule(vehicule);
                displayVehicleDetails(vehicule);
                toggleVehiclePanels(true);
                populateVehicleFields(vehicule);
            } else {
                toggleVehiclePanels(false); // Aucun véhicule
            }
            vehicleSection.setVisible(true); // Affiche la section véhicule
        } else {
            // Cacher toute la section véhicule pour les passagers
            vehicleSection.setVisible(false);
        }
    }

    private void updateProfileFields() {
        if (currentUser != null) {
            nomField.setText(currentUser.getNom());
            prenomField.setText(currentUser.getPrenom());
            emailField.setText(currentUser.getEmail());
            telField.setText(currentUser.getTelephone());
            naissanceField.setText(String.valueOf(currentUser.getDate_naissance()));
            roleField.setText(currentUser.getRole());
        }
    }

    private void displayVehicleDetails(Vehicule vehicule) {
        marqueLabel.setText(vehicule.getMarque());
        modeleLabel.setText(vehicule.getModele());
        couleurLabel.setText(vehicule.getCouleur());
        matriculeLabel.setText(vehicule.getMatricule());
        placesLabel.setText(String.valueOf(vehicule.getNbre_places()));
    }

    private void populateVehicleFields(Vehicule vehicule) {
        marqueField.setText(vehicule.getMarque());
        modeleField.setText(vehicule.getModele());
        couleurField.setText(vehicule.getCouleur());
        matriculeField.setText(vehicule.getMatricule());
        placesSpinner.getValueFactory().setValue(vehicule.getNbre_places());
    }

    public void toggleVehiclePanels(boolean hasVehicle) {
        addVehiclePanel.setVisible(!hasVehicle);
        addVehicleButton.setVisible(!hasVehicle);
        vehicleInfoPanel.setVisible(hasVehicle);
    }

    @FXML
    private void onLogoutButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onModifyButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyProfile.fxml"));
            Parent root = loader.load();
            ModifyProfileController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            Stage stage = (Stage) modifyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddVehicleButtonClick() {
        addVehiclePanel.setVisible(!addVehiclePanel.isVisible());
    }

    @FXML
    private void onSubmitVehicleButtonClick() {
        String marque = marqueField.getText();
        String modele = modeleField.getText();
        String couleur = couleurField.getText();
        String matricule = matriculeField.getText();
        int nbrePlaces = placesSpinner.getValue();

        if (marque.isEmpty() || modele.isEmpty() || couleur.isEmpty() || matricule.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        Vehicule vehicule = new Vehicule(currentUser, marque, modele, couleur, matricule, nbrePlaces);

        if (vehicleService.addVehicle(vehicule)) {
            showAlert("Succès", "Véhicule ajouté avec succès");
            clearFields();
            currentUser.setVehicule(vehicule);
            setUser(currentUser);
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du véhicule");
        }
    }

    @FXML
    public void handleModifyVehicle() {
        marqueFieldd.setText(marqueField.getText());
        modeleFieldd.setText(modeleField.getText());
        couleurFieldd.setText(couleurField.getText());
        matriculeFieldd.setText(matriculeField.getText());

        if (placesSpinnerr.getValueFactory() == null) {
            SpinnerValueFactory<Integer> valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 4);
            placesSpinnerr.setValueFactory(valueFactory);
        }

        placesSpinnerr.getValueFactory().setValue(placesSpinner.getValue());

        marqueLabel.setVisible(false);
        modeleLabel.setVisible(false);
        couleurLabel.setVisible(false);
        matriculeLabel.setVisible(false);
        placesLabel.setVisible(false);

        marqueFieldd.setVisible(true);
        modeleFieldd.setVisible(true);
        couleurFieldd.setVisible(true);
        matriculeFieldd.setVisible(true);
        placesSpinnerr.setVisible(true);

        modifyVehicleButton.setVisible(false);
        saveVehicleButton.setVisible(true);
    }

    @FXML
    public void handleSaveVehicle() {
        String marque = marqueFieldd.getText();
        String modele = modeleFieldd.getText();
        String couleur = couleurFieldd.getText();
        String matricule = matriculeFieldd.getText();
        int places = placesSpinnerr.getValue();

        if (marque.isEmpty() || modele.isEmpty() || couleur.isEmpty() || matricule.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        Vehicule vehicule = currentUser.getVehicule();
        vehicule.setMarque(marque);
        vehicule.setModele(modele);
        vehicule.setCouleur(couleur);
        vehicule.setMatricule(matricule);
        vehicule.setNbre_places(places);

        if (vehicleService.modifier(vehicule)) {
            showAlert("Succès", "Véhicule modifié avec succès.");
            displayVehicleDetails(vehicule);
            populateVehicleFields(vehicule);

            marqueLabel.setVisible(true);
            modeleLabel.setVisible(true);
            couleurLabel.setVisible(true);
            matriculeLabel.setVisible(true);
            placesLabel.setVisible(true);

            marqueFieldd.setVisible(false);
            modeleFieldd.setVisible(false);
            couleurFieldd.setVisible(false);
            matriculeFieldd.setVisible(false);
            placesSpinnerr.setVisible(false);

            modifyVehicleButton.setVisible(true);
            saveVehicleButton.setVisible(false);
        } else {
            showAlert("Erreur", "Erreur lors de la mise à jour.");
        }
    }

    @FXML
    public void handleDeleteVehicle() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Suppression du véhicule");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer votre véhicule ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Vehicule vehicule = currentUser.getVehicule();
            if (vehicleService.supprimer(vehicule)) {
                showAlert("Succès", "Véhicule supprimé avec succès.");
                currentUser.setVehicule(null);
                toggleVehiclePanels(false);
                clearFields(); // <-- Ajout ici
            } else {
                showAlert("Erreur", "Erreur lors de la suppression.");
            }

        }


    }

    private void clearFields() {
        marqueField.clear();
        modeleField.clear();
        couleurField.clear();
        matriculeField.clear();
        placesSpinner.getValueFactory().setValue(4);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


