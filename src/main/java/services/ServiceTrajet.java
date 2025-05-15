package services;

import entities.Trajet;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceTrajet {
    private Connection connection;

    public ServiceTrajet() {
        connection = MyDatabase.getInstance().getConnection();
    }


    public void ajouter(Trajet trajet) throws SQLException {
        String query = "INSERT INTO trajet (ville_depart, ville_arrivee, date_depart, heure_depart, nbr_places, prix, bagage, id_utilisateur) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, trajet.getVilleDepart());
            statement.setString(2, trajet.getVilleArrivee());
            statement.setDate(3, Date.valueOf(trajet.getDateDepart()));
            statement.setTime(4, Time.valueOf(trajet.getHeureDepart()));
            statement.setInt(5, trajet.getNbrPlaces());
            statement.setFloat(6, trajet.getPrix());
            statement.setString(7, trajet.getBagage());

            // ✅ Récupération de l'utilisateur courant
            int userId = controllers.UserSession.getCurrentUser().getId_utilisateur();
            statement.setInt(8, userId);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trajet.setIdTrajet(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void modifier(Trajet trajet) throws SQLException {
        String query = "UPDATE trajet SET ville_depart=?, ville_arrivee=?, date_depart=?, heure_depart=?, nbr_places=?, prix=?, bagage=? WHERE id_trajet=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, trajet.getVilleDepart());
            statement.setString(2, trajet.getVilleArrivee());
            statement.setDate(3, Date.valueOf(trajet.getDateDepart()));
            statement.setTime(4, Time.valueOf(trajet.getHeureDepart()));
            statement.setInt(5, trajet.getNbrPlaces());
            statement.setFloat(6, trajet.getPrix());
            statement.setString(7, trajet.getBagage());
            statement.setInt(8, trajet.getIdTrajet());

            statement.executeUpdate();
        }
    }

    public void supprimer(Trajet trajet) throws SQLException {
        String query = "DELETE FROM trajet WHERE id_trajet=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trajet.getIdTrajet());
            statement.executeUpdate();
        }
    }

    public List<Trajet> recuperer() throws SQLException {
        List<Trajet> trajets = new ArrayList<>();

        int userId = controllers.UserSession.getCurrentUser().getId_utilisateur();
        String role = controllers.UserSession.getCurrentUser().getRole(); // suppose qu’il y a une méthode getRole()

        String query;
        if ("conducteur".equalsIgnoreCase(role)) {
            query = "SELECT * FROM trajet WHERE id_utilisateur = ?";
        } else {
            query = "SELECT * FROM trajet";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            if ("conducteur".equalsIgnoreCase(role)) {
                statement.setInt(1, userId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Trajet trajet = new Trajet();
                    trajet.setIdTrajet(resultSet.getInt("id_trajet"));
                    trajet.setVilleDepart(resultSet.getString("ville_depart"));
                    trajet.setVilleArrivee(resultSet.getString("ville_arrivee"));
                    trajet.setDateDepart(resultSet.getDate("date_depart").toLocalDate());

                    Time time = resultSet.getTime("heure_depart");
                    LocalTime localTime = time != null ? time.toLocalTime() : null;
                    trajet.setHeureDepart(localTime);

                    trajet.setNbrPlaces(resultSet.getInt("nbr_places"));
                    trajet.setPrix(resultSet.getFloat("prix"));
                    trajet.setBagage(resultSet.getString("bagage"));

                    trajets.add(trajet);
                }

                System.out.println("Voici les trajets récupérés :");
                for (Trajet t : trajets) {
                    System.out.println(t);
                }
            }
        }

        return trajets;
    }

}
