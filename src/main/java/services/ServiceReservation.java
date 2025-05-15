package services;

import entities.Reservation;
import entities.Trajet;
import entities.Utilisateur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation {
    private Connection connection;

    public ServiceReservation() {
        connection = MyDatabase.getInstance().getConnection();
    }

    // Ajouter une réservation
    public void ajouter(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservation (nomPassager, prenomPassager, email, telephone, nombrePlaces, id_trajet, id_utilisateur, prixTotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, reservation.getNomPassager());
            statement.setString(2, reservation.getPrenomPassager());
            statement.setString(3, reservation.getEmail());
            statement.setString(4, reservation.getTelephone());
            statement.setInt(5, reservation.getNombrePlaces());
            statement.setInt(6, reservation.getTrajet().getIdTrajet());
            statement.setInt(7, reservation.getUtilisateur().getId_utilisateur()); // ID utilisateur
            statement.setFloat(8, reservation.getPrixTotal());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getInt(1)); // Récupère l'ID généré
                }
            }
        }
    }

    // Récupérer les réservations d'un utilisateur
    public List<Reservation> recupererParUtilisateur(Utilisateur utilisateur) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, t.* FROM reservation r " +
                "JOIN trajet t ON r.id_trajet = t.id_trajet " +
                "WHERE r.id_utilisateur = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, utilisateur.getId_utilisateur());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(resultSet.getInt("id"));
                    reservation.setNomPassager(resultSet.getString("nomPassager"));
                    reservation.setPrenomPassager(resultSet.getString("prenomPassager"));
                    reservation.setEmail(resultSet.getString("email"));
                    reservation.setTelephone(resultSet.getString("telephone"));
                    reservation.setNombrePlaces(resultSet.getInt("nombrePlaces"));
                    reservation.setPrixTotal(resultSet.getFloat("prixTotal"));

                    Trajet trajet = new Trajet();
                    trajet.setIdTrajet(resultSet.getInt("id_trajet"));
                    trajet.setVilleDepart(resultSet.getString("ville_depart"));
                    trajet.setVilleArrivee(resultSet.getString("ville_arrivee"));
                    trajet.setDateDepart(resultSet.getDate("date_depart").toLocalDate());

                    Time time = resultSet.getTime("heure_depart");
                    trajet.setHeureDepart(time != null ? time.toLocalTime() : null);

                    trajet.setNbrPlaces(resultSet.getInt("nbr_places"));
                    trajet.setPrix(resultSet.getFloat("prix"));
                    trajet.setBagage(resultSet.getString("bagage"));

                    reservation.setTrajet(trajet);
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    // Récupérer les réservations pour un trajet spécifique et un utilisateur spécifique
    public List<Reservation> recupererParTrajetEtUtilisateur(Trajet trajet, Utilisateur utilisateur) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation WHERE id_trajet = ? AND id_utilisateur = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trajet.getIdTrajet());
            statement.setInt(2, utilisateur.getId_utilisateur());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(resultSet.getInt("id"));
                    reservation.setNomPassager(resultSet.getString("nomPassager"));
                    reservation.setPrenomPassager(resultSet.getString("prenomPassager"));
                    reservation.setEmail(resultSet.getString("email"));
                    reservation.setTelephone(resultSet.getString("telephone"));
                    reservation.setNombrePlaces(resultSet.getInt("nombrePlaces"));
                    reservation.setPrixTotal(resultSet.getFloat("prixTotal"));

                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    // Supprimer une réservation
    public void supprimer(Reservation reservation) throws SQLException {
        String query = "DELETE FROM reservation WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservation.getId());
            statement.executeUpdate();
        }
    }

    // Mettre à jour une réservation
    public void modifier(Reservation reservation) throws SQLException {
        String query = "UPDATE reservation SET nomPassager = ?, prenomPassager = ?, email = ?, telephone = ?, nombrePlaces = ?, id_trajet = ?, prixTotal = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reservation.getNomPassager());
            statement.setString(2, reservation.getPrenomPassager());
            statement.setString(3, reservation.getEmail());
            statement.setString(4, reservation.getTelephone());
            statement.setInt(5, reservation.getNombrePlaces());
            statement.setInt(6, reservation.getTrajet().getIdTrajet());
            statement.setFloat(7, reservation.getPrixTotal());
            statement.setInt(8, reservation.getId());

            statement.executeUpdate();
        }
    }
}
