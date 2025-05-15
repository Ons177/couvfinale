
package controllers;

import entities.Resanonce;
import entities.Utilisateur;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;
import services.resannoservice;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class voirreservationsconducteur implements Initializable {
    private Utilisateur currentUser;
    resannoservice resannoservice = new resannoservice();

    @FXML
    private WebView webviewcanlenderan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = UserSession.getCurrentUser();

        // 2. Charger le fichier HTML dans la WebView
        WebEngine webEngine = webviewcanlenderan.getEngine();
        webEngine.load(getClass().getResource("/calender.html").toExternalForm());

        // 3. Quand le HTML est chargé...
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                // 4. Lier le contexte Java à la page Web (si tu veux interagir)
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", this); // optionnel ici

                // 5. Générer les événements JSON
                String eventsJson = generateReservationEventsJson();

                // 6. Appeler la fonction JS pour charger les réservations
                webEngine.executeScript("loadReservations(" + escapeForJS(eventsJson) + ")");
            }
        });
    }

    private String generateReservationEventsJson() {
        try {
            List<Resanonce> reservations = resannoservice.getReservationsPourConducteur(currentUser.getId_utilisateur());
            JSONArray events = new JSONArray();

            for (Resanonce r : reservations) {
                if (r.getStatutano() == Resanonce.StatutReservation.refusée) continue;

                JSONObject event = new JSONObject();
                event.put("id", r.getIdReservationa());
                event.put("title", r.getAnonce().getTitre()); // ou autre info à afficher
                event.put("start", r.getDateDebuta().toString());

                if (r.getDateFina() != null) {
                    event.put("end", r.getDateFina().toLocalDate().plusDays(1).toString());
                }

                switch (r.getStatutano()) {
                    case en_attente:
                        event.put("color", "orange");
                        break;
                    case confirmée:
                        event.put("color", "green");
                        break;
                }

                events.put(event);
            }

            return events.toString();

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            return "[]";
        }
    }

    private String escapeForJS(String json) {
        // Échappe correctement les guillemets pour ne pas casser le JavaScript
        return "'" + json.replace("\\", "\\\\").replace("'", "\\'") + "'";
    }
    public void ouvrirReservationParDate(String date) {
        System.out.println("Date cliquée : " + date);

        try {
            List<Resanonce> reservations = resannoservice.getReservationsPourConducteur(currentUser.getId_utilisateur());
            for (Resanonce r : reservations) {
                if (r.getDateDebuta().toString().equals(date)) {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservationconducteuritem.fxml"));
                    Parent root = loader.load();

                    // Injecte la réservation dans le contrôleur
                    reservationconducteurcontrolleur controller = loader.getController();
                    controller.setData(r);

                    Stage stage = new Stage();
                    stage.setTitle("Détails Réservation");
                    stage.setScene(new Scene(root));
                    stage.show();
                }
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


}
