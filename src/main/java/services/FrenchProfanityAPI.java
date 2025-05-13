package services;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FrenchProfanityAPI {

    private static final String API_USER = "441921246";  // Use your real API User
    private static final String API_SECRET = "8h2dmq7sjHoyxBL6G7dv7mYmKZxxzisa";  // Use your real API Secret


    public static boolean containsProfanity(String text) throws Exception {
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String apiUrl = "https://api.sightengine.com/1.0/text/check.json" +
                "?text=" + encodedText +
                "&lang=fr" +
                "&mode=standard" +
                "&api_user=" + API_USER +
                "&api_secret=" + API_SECRET;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("API Response: " + response.body());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode matches = root.path("profanity").path("matches");

        return matches.isArray() && matches.size() > 0;
    }

}
