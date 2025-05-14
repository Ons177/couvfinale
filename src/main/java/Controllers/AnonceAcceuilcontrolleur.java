package Controllers;

import entities.Anonce;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import services.anonceservice;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AnonceAcceuilcontrolleur implements Initializable {

    @FXML
    private VBox vboxmenuann;

    @FXML
    private TextField rechercheana;

    @FXML
    private FlowPane flowid;

    private Utilisateur currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            System.err.println("Utilisateur non connecté !");
            return;
        }
        System.out.println("Utilisateur connecté : " + currentUser);
        System.out.println("Rôle de l'utilisateur : " + currentUser.getRole());
        creerMenuDynamique();
        if ("passager".equals(currentUser.getRole().toLowerCase())) {
            afficherAnonces(); // montrer toutes les annonces pour passager
        } else  {
            afficherMesAnonces(); // montrer uniquement ses propres annonces
        }
        rechercheana.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercherEtAfficherAnnonces(newValue.trim());
        });

    }

    private void creerMenuDynamique() {
        vboxmenuann.getChildren().clear();
        String role = currentUser.getRole().toLowerCase();

        if ("passager".equals(role)) {
            System.out.println("Création du menu pour un passager");
            Button reservationsBtn = new Button("Voir mes réservations");
            reservationsBtn.setPrefWidth(280);
            reservationsBtn.setOnAction(this::voirmesreservations);
            vboxmenuann.getChildren().add(reservationsBtn);
        } else if ("conducteur".equals(role)) {
            System.out.println("Création du menu pour un conducteur");
            String[] items = {"Voir mes réservations", "Publier une annonce", "Voir mes annonces"};
            for (String item : items) {
                Button btn = new Button(item);
                btn.setPrefWidth(280);
                btn.setOnAction(this::handleMenuAction);
                vboxmenuann.getChildren().add(btn);
            }
        } else {
            String[] items = {"Voir les réservations","Voir les annonces"};
            for (String item : items) {
                Button btn = new Button(item);
                btn.setPrefWidth(280);
                btn.setOnAction(this::handleMenuAction);
                vboxmenuann.getChildren().add(btn);
            }        }
    }

    private void handleMenuAction(ActionEvent event) {
        String action = ((Button) event.getSource()).getText();
        switch (action) {
            case "Voir mes réservations":
                voirmesreservations(event);
                break;
            case "Voir les réservations": // ← Ajout ici pour l'admin
                voirmesreservations(event);
                break;
            case "Publier une annonce":
                publieranonce(event);
                break;
            case "Voir mes annonces":
                afficherMesAnonces();
                break;

        }
    }

    private void afficherAnonces() {
        anonceservice service = new anonceservice();
        try {
            List<Anonce> annonces = service.getAll();
            afficherAnoncesDansFlowPane(annonces, "/AnonceItem.fxml");
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des annonces : " + e.getMessage());
        }
    }

    private void afficherMesAnonces() {
        anonceservice service = new anonceservice();
        try {
            if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                // L'admin voit toutes les réservations
                List<Anonce> mesAnonces = service.getAll();
                afficherAnoncesDansFlowPane(mesAnonces, "/Useranonceitem.fxml");

            } else {
                // Les autres (passager, conducteur, etc.) voient leurs propres réservations
                List<Anonce> mesAnonces = service.getAnoncesParConducteur(currentUser.getId_utilisateur());
                afficherAnoncesDansFlowPane(mesAnonces, "/Useranonceitem.fxml");
            }
        }
     catch (SQLException e) {
            System.out.println("Erreur lors du chargement de vos annonces : " + e.getMessage());
        }
    }

    private void afficherAnoncesDansFlowPane(List<Anonce> annonces, String fxmlPath) {
        flowid.getChildren().clear();
        System.out.println("Affichage de " + annonces.size() + " annonces avec le template " + fxmlPath);

        for (Anonce a : annonces) {
            try {
                System.out.println("Chargement du FXML : " + fxmlPath);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();

                if (fxmlPath.equals("/AnonceItem.fxml")) {
                    Anonceitemcontrolleur controller = loader.getController();
                    controller.initialize(a);
                } else if (fxmlPath.equals("/Useranonceitem.fxml")) {
                    useranonceitemcontrolleur controller = loader.getController();
                    controller.setAnonce(a);
                }

                flowid.getChildren().add(root);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'affichage d'une annonce : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void publieranonce(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ajouteranonce.fxml"));
            flowid.getChildren().clear();
            flowid.getChildren().add(root);
        } catch (IOException e) {
            System.out.println("Erreur navigation vers ajout annonce : " + e.getMessage());
        }
    }

    @FXML
    void voirmesreservations(ActionEvent event) {
        try {
            String fxml;
            if ("conducteur".equalsIgnoreCase(currentUser.getRole())) {
                fxml = "/voirreservationsconducteur.fxml";
            } else  {
                fxml = "/voirmesreservations.fxml";
            }

            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            flowid.getChildren().clear();
            flowid.getChildren().add(root);
        } catch (IOException e) {
            System.out.println("Erreur navigation vers mes réservations : " + e.getMessage());
        }
    }


    @FXML
    void rechercheann(ActionEvent event) {
        String motCle = rechercheana.getText().trim();
        rechercherEtAfficherAnnonces(motCle);


    }

    private void rechercherEtAfficherAnnonces(String motCle) {
        anonceservice service = new anonceservice();
        String role = currentUser.getRole().toLowerCase();

        try {
            List<Anonce> resultats;
            if (motCle.isEmpty()) {
                resultats = service.getAll(); // Récupère toutes les annonces
            } else {
                resultats = service.rechercherParMotCle(motCle);
            }
            if ("passager".equals(role)) {
                afficherAnoncesDansFlowPane(resultats, "/AnonceItem.fxml");
            } else if ("conducteur".equals(role)) {
                afficherAnoncesDansFlowPane(resultats, "/Useranonceitem.fxml");
            }
        } catch (SQLException e) {
            System.out.println("Erreur de recherche : " + e.getMessage());
        }

    }
}

