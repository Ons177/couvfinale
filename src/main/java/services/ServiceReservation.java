package services;

import entities.Reservation;
import entities.Trajet;
import entities.Utilisateur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;

public class ServiceReservation {
    private Connection connection;

    public ServiceReservation() {
        connection = MyDatabase.getInstance().getCnx();
    }

    public void ajouter(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservation (nomPassager, prenomPassager, email, telephone, nombrePlaces, id_trajet, id_utilisateur, prixTotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, reservation.getNomPassager());
            statement.setString(2, reservation.getPrenomPassager());
            statement.setString(3, reservation.getEmail());
            statement.setString(4, reservation.getTelephone());
            statement.setInt(5, reservation.getNombrePlaces());
            statement.setInt(6, reservation.getTrajet().getIdTrajet());
            statement.setInt(7, 55);
            statement.setFloat(8, reservation.getPrixTotal());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void modifier(Reservation reservation) throws SQLException {
        String query = "UPDATE reservation SET nomPassager=?, prenomPassager=?, email=?, telephone=?, nombrePlaces=?, id_trajet=?, prixTotal=? WHERE id=?";
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

    public void supprimer(Reservation reservation) throws SQLException {
        String query = "DELETE FROM reservation WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservation.getId());
            statement.executeUpdate();
        }
    }

    public List<Reservation> recuperer() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, t.* FROM reservation r JOIN trajet t ON r.id_trajet = t.id_trajet";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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
                LocalTime localTime = time != null ? time.toLocalTime() : null;
                trajet.setHeureDepart(localTime);

                trajet.setNbrPlaces(resultSet.getInt("nbr_places"));
                trajet.setPrix(resultSet.getFloat("prix"));
                trajet.setBagage(resultSet.getString("bagage"));

                reservation.setTrajet(trajet);
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public List<Reservation> recupererParTrajet(Trajet trajet) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, t.* FROM reservation r JOIN trajet t ON r.id_trajet = t.id_trajet WHERE r.id_trajet = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trajet.getIdTrajet());
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

                    Trajet t = new Trajet();
                    t.setIdTrajet(resultSet.getInt("id_trajet"));
                    t.setVilleDepart(resultSet.getString("ville_depart"));
                    t.setVilleArrivee(resultSet.getString("ville_arrivee"));
                    t.setDateDepart(resultSet.getDate("date_depart").toLocalDate());

                    Time time = resultSet.getTime("heure_depart");
                    LocalTime localTime = time != null ? time.toLocalTime() : null;
                    t.setHeureDepart(localTime);

                    t.setNbrPlaces(resultSet.getInt("nbr_places"));
                    t.setPrix(resultSet.getFloat("prix"));
                    t.setBagage(resultSet.getString("bagage"));

                    reservation.setTrajet(t);
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    public Utilisateur getUserById(int id) {
        String query = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Utilisateur trouvé dans la base de données");
                Utilisateur u = new Utilisateur(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getString("telephone"),
                        rs.getString("role"),
                        rs.getDate("date_naissance")
                );

                u.setId_utilisateur(rs.getInt("id_utilisateur"));
                u.setApproved(rs.getBoolean("isApproved"));

                System.out.println("Utilisateur créé avec succès : " + u);
                return u;
            } else {
                System.err.println("Aucun utilisateur trouvé avec l'ID : " + id);
                // Vérifions si la table existe et contient des données
                try (Statement checkStmt = connection.createStatement()) {
                    ResultSet countRs = checkStmt.executeQuery("SELECT COUNT(*) FROM utilisateur");
                    if (countRs.next()) {
                        System.out.println("Nombre total d'utilisateurs dans la base : " + countRs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}