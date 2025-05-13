package controllers;

import entities.Utilisateur;
import entities.Vehicule;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import services.VehicleService;

public class AddVehicleController {
    @FXML
    private TextField marqueField;
    @FXML
    private TextField modeleField;
    @FXML
    private TextField couleurField;
    @FXML
    private TextField matriculeField;
    @FXML
    private Spinner<Integer> placesSpinner;
    @FXML
    private Button addVehicleButton;



    private VehicleService vehicleService = new VehicleService();
    private Utilisateur currentUser;

    @FXML
    public void initialize() {
        // Configurer le Spinner pour le nombre de places (1-8)
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 4);
        placesSpinner.setValueFactory(valueFactory);

        addVehicleButton.setOnAction(event -> handleAddVehicle());
    }

    public void setUser(Utilisateur user) {
        this.currentUser = user;
    }

    private void handleAddVehicle() {
        String marque = marqueField.getText();
        String modele = modeleField.getText();
        String couleur = couleurField.getText();
        String matricule = matriculeField.getText();
        int nbrePlaces = placesSpinner.getValue();  // Récupère la valeur du Spinner

        if (marque.isEmpty() || modele.isEmpty() || couleur.isEmpty() || matricule.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        Vehicule vehicule = new Vehicule(currentUser, marque, modele, couleur, matricule, nbrePlaces);

        if (vehicleService.addVehicle(vehicule)) {
            showAlert("Succès", "Véhicule ajouté avec succès");
            clearFields();
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du véhicule");
        }
    }

    private void clearFields() {
        marqueField.clear();
        modeleField.clear();
        couleurField.clear();
        matriculeField.clear();
        placesSpinner.getValueFactory().setValue(4);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
