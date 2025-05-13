package controllers;

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
import services.UserService;

import java.io.IOException;
import java.util.List;

public class AdminDashboardController {
    @FXML
    private TableView<Utilisateur> driverRequestsTable;
    @FXML
    private TableColumn<Utilisateur, String> nomColumn;
    @FXML
    private TableColumn<Utilisateur, String> prenomColumn;
    @FXML
    private TableColumn<Utilisateur, String> emailColumn;
    @FXML
    private TableColumn<Utilisateur, String> cinColumn;
    @FXML
    private TableColumn<Utilisateur, String> permisColumn;
    @FXML
    private TableColumn<Utilisateur, Void> actionsColumn;

    private UserService userService = new UserService();
    private ObservableList<Utilisateur> driverRequests = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadDriverRequests();
    }

    private void setupTableColumns() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        permisColumn.setCellValueFactory(new PropertyValueFactory<>("permis"));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button approveButton = new Button("Approuver");
            private final Button rejectButton = new Button("Rejeter");

            {
                approveButton.getStyleClass().add("btn-mon-compte");
                rejectButton.getStyleClass().add("btn-mon-compte");

                approveButton.setOnAction(event -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    handleApprove(user);
                });

                rejectButton.setOnAction(event -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    handleReject(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, approveButton, rejectButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    public void loadDriverRequests() {
        List<Utilisateur> requests = userService.getPendingDriverRequests();
        driverRequests.setAll(requests);
        driverRequestsTable.setItems(driverRequests);
    }

    private void handleApprove(Utilisateur user) {
        if (userService.approveDriver(user)) {
            showAlert("Succès", "Le conducteur a été approuvé avec succès");
            loadDriverRequests();
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de l'approbation");
        }
    }

    private void handleReject(Utilisateur user) {
        if (userService.rejectDriver(user)) {
            showAlert("Succès", "Le conducteur a été rejeté");
            loadDriverRequests();
        } else {
            showAlert("Erreur", "Une erreur est survenue lors du rejet");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAfficherUtilisateurs(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AfficherUtilisateurs.fxml"));
            Parent root = loader.load();

            // Si tu veux passer des données au contrôleur :
            // AfficherUtilisateursController controller = loader.getController();
            // controller.setAdmin(adminConnecté);

            Stage stage = new Stage();
            stage.setTitle("Liste des Utilisateurs");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Charger la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
            Parent root = loader.load();

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(true);
            // Changer la scène
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void adduser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ajouterutilisateur.fxml"));
            Parent root = loader.load();

            // Obtenir l'instance du contrôleur
            ajouterutilisateurController controller = loader.getController();
            controller.setAdminDashboardController(this);  // Passer l'instance du contrôleur AdminDashboard

            // Créer une nouvelle scène avec SignupAdmin.fxml
            Scene scene = new Scene(root);

            // Créer un nouveau stage pour afficher la fenêtre de création de l'utilisateur
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Utilisateur");
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
