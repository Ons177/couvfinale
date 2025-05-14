package Controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class mapanocontrolleur {

    @FXML
    private WebView webmapf;

    // Méthode pour charger la carte dans le WebView
    public void chargerCarte(double latitude, double longitude) {
        WebEngine webEngine = webmapf.getEngine();

        try {
            // Charger le fichier HTML de la carte
            String url = getClass().getResource("/map.html").toExternalForm();
            url += "?lat=" + latitude + "&lon=" + longitude;

            // Charger l'URL avec les paramètres
            webEngine.load(url);
        } catch (Exception e) {
            System.out.println("Erreur de chargement de la carte : " + e.getMessage());
        }
    }
}
