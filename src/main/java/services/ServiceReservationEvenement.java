package services;

import entities.ReservationEvenement;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservationEvenement {
    private Connection connection;

    public ServiceReservationEvenement() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void reserver(ReservationEvenement r) throws SQLException {
        String sql = "INSERT INTO reservationevenement(id_evenement, id_utilisateur, nb_places, date_reservation, statut_reservation, prix) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, r.getIdEvenement());
        ps.setInt(2, r.getIdUtilisateur());
        ps.setInt(3, r.getNbPlaces());
        ps.setDate(4, Date.valueOf(r.getDateReservation()));
        ps.setString(5, r.getStatut_reservation());
        ps.setDouble(6, r.getPrix());
        ps.executeUpdate();
    }

    public List<ReservationEvenement> recuperer() throws SQLException {
        List<ReservationEvenement> liste = new ArrayList<>();
        String sql = "SELECT * FROM reservationevenement";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            ReservationEvenement r = new ReservationEvenement(
                    rs.getInt("id_reservation"),
                    rs.getInt("id_evenement"),
                    rs.getInt("id_utilisateur"),
                    rs.getInt("nb_places"),
                    rs.getDate("date_reservation").toLocalDate(),
                    rs.getString("statut_reservation"),  // ✅ corrigé ici
                    rs.getDouble("prix")
            );
            liste.add(r);
        }

        return liste;
    }

    public void accepterReservation(int idReservation) throws SQLException {
        String sql = "UPDATE reservationevenement SET statut_reservation = 'acceptée' WHERE id_reservation = ?"; // ✅ corrigé ici
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idReservation);
        ps.executeUpdate();
    }

    public void refuserReservation(int idReservation) throws SQLException {
        String sql = "UPDATE reservationevenement SET statut_reservation = 'refusée' WHERE id_reservation = ?"; // ✅ corrigé ici
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idReservation);
        ps.executeUpdate();
    }

    public void updateStatut_reservation(int idReservation, String nouveauStatut_reservation) throws SQLException {
        String sql = "UPDATE reservationevenement SET statut_reservation = ? WHERE id_reservation = ?"; // ✅ corrigé ici
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, nouveauStatut_reservation);
        ps.setInt(2, idReservation);
        ps.executeUpdate();
    }
    public List<ReservationEvenement> getReservationsAvecNomsEtTitresPourConducteur(int idConducteur) {
        List<ReservationEvenement> liste = new ArrayList<>();
        String sql = "SELECT r.*, u.nom AS nom_utilisateur, e.titre AS titre_evenement " +
                "FROM reservationevenement r " +
                "JOIN utilisateur u ON r.id_utilisateur = u.id_utilisateur " +
                "JOIN evenement e ON r.id_evenement = e.id_evenement " +
                "WHERE e.id_createur = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idConducteur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReservationEvenement r = new ReservationEvenement();
                r.setId(rs.getInt("id_reservation"));
                r.setIdEvenement(rs.getInt("id_evenement"));
                r.setIdUtilisateur(rs.getInt("id_utilisateur"));
                r.setNbPlaces(rs.getInt("nb_places"));
                r.setDateReservation(rs.getDate("date_reservation").toLocalDate());
                r.setStatut_reservation(rs.getString("statut_reservation"));
                r.setPrix(rs.getDouble("prix"));
                r.setNomUtilisateur(rs.getString("nom_utilisateur"));
                r.setTitreEvenement(rs.getString("titre_evenement"));
                liste.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }


}