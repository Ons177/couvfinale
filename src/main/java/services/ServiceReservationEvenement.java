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
        String sql = "INSERT INTO reservationevenement(id_evenement, id_utilisateur, nb_places, date_reservation, statut, prix) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, r.getIdEvenement());
        ps.setInt(2, r.getIdUtilisateur());
        ps.setInt(3, r.getNbPlaces());
        ps.setDate(4, Date.valueOf(r.getDateReservation()));
        ps.setString(5, r.getStatut());
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
                    rs.getString("statut"),
                    rs.getDouble("prix")
            );
            liste.add(r);
        }

        return liste;
    }

    public void accepterReservation(int idReservation) throws SQLException {
        String sql = "UPDATE reservationevenement SET statut = 'acceptée' WHERE id_reservation = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idReservation);
        ps.executeUpdate();
    }

    public void refuserReservation(int idReservation) throws SQLException {
        String sql = "UPDATE reservationevenement SET statut = 'refusée' WHERE id_reservation = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idReservation);
        ps.executeUpdate();
    }

    public void updateStatut(int idReservation, String nouveauStatut) throws SQLException {
        String sql = "UPDATE reservationevenement SET statut = ? WHERE id_reservation = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, nouveauStatut);
        ps.setInt(2, idReservation);
        ps.executeUpdate();
    }

}
