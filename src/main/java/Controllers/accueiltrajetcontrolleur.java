package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import entities.Trajet;
import services.ServiceTrajet;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class accueiltrajetcontrolleur {

    @FXML
    private Button voirMesTrajetsBtn;

    @FXML
    private FlowPane flowpaneaccueil;

    @FXML
    private Button ajoutertrajetbtn;

    @FXML
    private Button ouvrirReservationbtn;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private TextField searchField;

    private ServiceTrajet serviceTrajet;
    private List<Trajet> allTrajets; // Store the full list of trajets

    @FXML
    private void initialize() {
        serviceTrajet = new ServiceTrajet();

        // Initialize ComboBox with filter and sort options
        filterComboBox.getItems().addAll(
                "Rechercher Tout",
                "Ville de Départ",
                "Ville d'Arrivée",
                "Nombre de Places (Exact)",
                "Nombre de Places (Min)",
                "Trier par Places (Croissant)",
                "Trier par Places (Décroissant)",
                "Trier par Ville de Départ (A-Z)",
                "Trier par Ville de Départ (Z-A)",
                "Trier par Ville d'Arrivée (A-Z)",
                "Trier par Ville d'Arrivée (Z-A)"
        );
        filterComboBox.setValue("Rechercher Tout"); // Default selection

        // Load all trajets initially
        loadAllTrajets();

        // Add listener to search field for real-time filtering
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTrajets();
        });

        // Add listener to ComboBox to reapply filter/sort when selection changes
        filterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterTrajets();
        });
    }

    private void loadAllTrajets() {
        try {
            allTrajets = serviceTrajet.recuperer();
            filterTrajets(); // Display filtered/sorted trajets initially
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les trajets: " + e.getMessage());
        }
    }

    private void filterTrajets() {
        flowpaneaccueil.getChildren().clear();
        String searchText = searchField.getText().trim().toLowerCase();
        String filterType = filterComboBox.getValue();

        List<Trajet> filteredTrajets = allTrajets;

        // Apply filtering based on search text
        if (!searchText.isEmpty()) {
            filteredTrajets = allTrajets.stream()
                    .filter(trajet -> {
                        switch (filterType) {
                            case "Rechercher Tout":
                                return trajet.getVilleDepart().toLowerCase().contains(searchText) ||
                                        trajet.getVilleArrivee().toLowerCase().contains(searchText);
                            case "Ville de Départ":
                                return trajet.getVilleDepart().toLowerCase().contains(searchText);
                            case "Ville d'Arrivée":
                                return trajet.getVilleArrivee().toLowerCase().contains(searchText);
                            case "Nombre de Places (Exact)":
                                try {
                                    int places = Integer.parseInt(searchText);
                                    return trajet.getNbrPlaces() == places;
                                } catch (NumberFormatException e) {
                                    return false; // Invalid number input
                                }
                            case "Nombre de Places (Min)":
                                try {
                                    int places = Integer.parseInt(searchText);
                                    return trajet.getNbrPlaces() >= places;
                                } catch (NumberFormatException e) {
                                    return false; // Invalid number input
                                }
                            default:
                                return true; // No filtering for sorting options
                        }
                    })
                    .collect(Collectors.toList());
        }

        // Apply sorting if selected
        switch (filterType) {
            case "Trier par Places (Croissant)":
                filteredTrajets = filteredTrajets.stream()
                        .sorted(Comparator.comparingInt(Trajet::getNbrPlaces))
                        .collect(Collectors.toList());
                break;
            case "Trier par Places (Décroissant)":
                filteredTrajets = filteredTrajets.stream()
                        .sorted(Comparator.comparingInt(Trajet::getNbrPlaces).reversed())
                        .collect(Collectors.toList());
                break;
            case "Trier par Ville de Départ (A-Z)":
                filteredTrajets = filteredTrajets.stream()
                        .sorted(Comparator.comparing(Trajet::getVilleDepart, String.CASE_INSENSITIVE_ORDER))
                        .collect(Collectors.toList());
                break;
            case "Trier par Ville de Départ (Z-A)":
                filteredTrajets = filteredTrajets.stream()
                        .sorted(Comparator.comparing(Trajet::getVilleDepart, String.CASE_INSENSITIVE_ORDER).reversed())
                        .collect(Collectors.toList());
                break;
            case "Trier par Ville d'Arrivée (A-Z)":
                filteredTrajets = filteredTrajets.stream()
                        .sorted(Comparator.comparing(Trajet::getVilleArrivee, String.CASE_INSENSITIVE_ORDER))
                        .collect(Collectors.toList());
                break;
            case "Trier par Ville d'Arrivée (Z-A)":
                filteredTrajets = filteredTrajets.stream()
                        .sorted(Comparator.comparing(Trajet::getVilleArrivee, String.CASE_INSENSITIVE_ORDER).reversed())
                        .collect(Collectors.toList());
                break;
        }

        // Display results
        if (filteredTrajets.isEmpty()) {
            Label noResultsLabel = new Label("Aucun trajet trouvé pour cette recherche.");
            noResultsLabel.getStyleClass().add("no-results-label");
            VBox noResultsBox = new VBox(noResultsLabel);
            noResultsBox.setAlignment(Pos.CENTER);
            noResultsBox.setPrefHeight(200);
            flowpaneaccueil.getChildren().add(noResultsBox);
        } else {
            for (Trajet trajet : filteredTrajets) {
                VBox card = createTrajetCard(trajet);
                flowpaneaccueil.getChildren().add(card);
            }
        }
    }

    public void refreshTrajets() {
        loadAllTrajets();
    }

    private VBox createTrajetCard(Trajet trajet) {
        VBox card = new VBox();
        card.getStyleClass().add("trajet-card");

        Label titleLabel = new Label(trajet.getVilleDepart() + " → " + trajet.getVilleArrivee());
        titleLabel.getStyleClass().add("trajet-title");
        VBox titleBanner = new VBox(titleLabel);
        titleBanner.getStyleClass().add("title-banner");
        titleBanner.setAlignment(Pos.CENTER);

        if (trajet.getNbrPlaces() == 0) {
            Label soldOutLabel = new Label("Attendez qu'un nouveau trajet soit publié");
            soldOutLabel.getStyleClass().add("sold-out-label");
            VBox soldOutBox = new VBox(soldOutLabel);
            soldOutBox.setAlignment(Pos.CENTER);
            soldOutBox.setPrefHeight(150);
            card.getChildren().addAll(titleBanner, soldOutBox);
        } else {
            VBox infoBox = new VBox(8);
            infoBox.getStyleClass().add("info-box");

            Label dateLabel = new Label("Date: " + trajet.getDateDepart());
            dateLabel.getStyleClass().add("info-label");

            Label heureLabel = new Label("Heure: " + trajet.getHeureDepart());
            heureLabel.getStyleClass().add("info-label");

            Label prixLabel = new Label("Prix: " + trajet.getPrix() + " TND");
            prixLabel.getStyleClass().add("info-label");

            Label placesLabel = new Label("Places: " + trajet.getNbrPlaces());
            placesLabel.getStyleClass().add("info-label");

            Label bagageLabel = new Label("Bagage: " + trajet.getBagage());
            bagageLabel.getStyleClass().add("info-label");

            infoBox.getChildren().addAll(dateLabel, heureLabel, prixLabel, placesLabel, bagageLabel);

            ScrollPane infoScroll = new ScrollPane(infoBox);
            infoScroll.getStyleClass().add("card-scroll-pane");
            infoScroll.setFitToWidth(true);
            infoScroll.setPrefHeight(150);
            infoScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            infoScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            Button reserverButton = new Button("Réserver");
            reserverButton.getStyleClass().addAll("card-button", "reserver-button");
            reserverButton.setOnAction(e -> ouvrirAjouterReservation(trajet, this));

            Button consulterMapButton = new Button("Consulter Map");
            consulterMapButton.getStyleClass().addAll("card-button", "consulter-map-button");
            consulterMapButton.setOnAction(e -> onOpenMap(trajet));

            HBox buttonBox = new HBox(10, reserverButton, consulterMapButton);
            buttonBox.getStyleClass().add("button-container");
            buttonBox.setAlignment(Pos.CENTER);

            card.getChildren().addAll(titleBanner, infoScroll, buttonBox);
        }

        return card;
    }

    private void ouvrirAjouterReservation(Trajet trajet, accueiltrajetcontrolleur parentController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Parent root = loader.load();

            AjouterReservationController controller = loader.getController();
            controller.setTrajet(trajet);
            controller.setParentController(parentController);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une Réservation");
            stage.initOwner((Stage) flowpaneaccueil.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout de réservation: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirAjouterTrajet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTrajet.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ajoutertrajetbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Trajet");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout de trajet: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirAfficherTrajet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherTrajet.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) voirMesTrajetsBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Trajets");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des trajets: " + e.getMessage());
        }
    }

    @FXML
    void afficherreservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ouvrirReservationbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Réservations");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la liste des réservations: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void onOpenMap(Trajet trajet) {
        try {
            String villeDepart = trajet.getVilleDepart();
            String villeArrivee = trajet.getVilleArrivee();

            String encodedDepart = URLEncoder.encode(villeDepart, StandardCharsets.UTF_8);
            String encodedArrivee = URLEncoder.encode(villeArrivee, StandardCharsets.UTF_8);

            // 📍 Mettez ici le chemin réel vers votre fichier HTML
            File htmlFile = new File("src/main/resources/carte.html");
            String url = htmlFile.toURI().toString() + "?depart=" + encodedDepart + "&arrivee=" + encodedArrivee;

            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}