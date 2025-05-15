package controllers;

import entities.ReservationEvenement;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceReservationEvenement;

import java.util.List;

public class ConsulterReservationController {

    @FXML private TableView<ReservationEvenement> tableReservation;
    // @FXML private TableColumn<ReservationEvenement, Integer> colId;
    @FXML private TableColumn<ReservationEvenement, String> colEvent;
    @FXML private TableColumn<ReservationEvenement, String> colUser;
    @FXML private TableColumn<ReservationEvenement, Integer> colPlaces;
    @FXML private TableColumn<ReservationEvenement, String> colDate;
    @FXML private TableColumn<ReservationEvenement, String> colStatut_reservation;
    @FXML private TableColumn<ReservationEvenement, Double> colPrix;
    @FXML private TableColumn<ReservationEvenement, Void> colActions;
    @FXML private Button retourAccueil;

    private final ServiceReservationEvenement service = new ServiceReservationEvenement();

    @FXML
    public void initialize() {
        try {
            Utilisateur user = UserSession.getCurrentUser(); // Utilisateur connecté
            //colId.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
            colEvent.setCellValueFactory(new PropertyValueFactory<>("titreEvenement"));
            colUser.setCellValueFactory(new PropertyValueFactory<>("nomUtilisateur"));
            colPlaces.setCellValueFactory(new PropertyValueFactory<>("nbPlaces"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
            colStatut_reservation.setCellValueFactory(new PropertyValueFactory<>("statut_reservation"));
            colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

            addButtonToTable();

            List<ReservationEvenement> reservations = service.getReservationsAvecNomsEtTitresPourConducteur(user.getId_utilisateur());
            ObservableList<ReservationEvenement> data = FXCollections.observableArrayList(reservations);
            tableReservation.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les réservations.", Alert.AlertType.ERROR);
        }
    }

    private void addButtonToTable() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnAccepter = new Button("Accepter");
            private final Button btnRefuser = new Button("Refuser");
            private final HBox pane = new HBox(10, btnAccepter, btnRefuser);

            {
                btnAccepter.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnRefuser.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                btnAccepter.setOnAction(event -> {
                    ReservationEvenement r = getTableView().getItems().get(getIndex());
                    try {
                        service.accepterReservation(r.getIdReservation());
                        r.setStatut_reservation("acceptée");
                        tableReservation.refresh();
                    } catch (Exception e) {
                        showAlert("Erreur", "Erreur lors de l'acceptation.", Alert.AlertType.ERROR);
                    }
                });

                btnRefuser.setOnAction(event -> {
                    ReservationEvenement r = getTableView().getItems().get(getIndex());
                    try {
                        service.refuserReservation(r.getIdReservation());
                        r.setStatut_reservation("refusée");
                        tableReservation.refresh();
                    } catch (Exception e) {
                        showAlert("Erreur", "Erreur lors du refus.", Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    @FXML
    void retourAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccueilConducteur.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'accueil.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String titre, String contenu, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}