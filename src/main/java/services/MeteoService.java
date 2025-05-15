package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MeteoService {

    private static final String API_KEY = "95acb0ed9e8a703af8fd828bf87dbc4b";

    public static String getMeteoParVille(String ville) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + ville +
                    "&appid=" + API_KEY + "&units=metric&lang=fr";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body(); // format JSON
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Méthode lisible pour JavaFX : température + description
    public static String getTemperatureEtDescription(String ville) {
        try {
            String json = getMeteoParVille(ville);
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            double temp = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
            String description = jsonObject.getAsJsonArray("weather")
                    .get(0).getAsJsonObject().get("description").getAsString();

            return "🌡 " + temp + "°C — " + description;
        } catch (Exception e) {
            return "❌ Météo indisponible";
        }
    }
}
