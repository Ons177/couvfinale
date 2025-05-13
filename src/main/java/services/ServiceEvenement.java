package services;

import entities.Evenement;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement {
    private Connection connection;

    public ServiceEvenement() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Evenement e) throws SQLException {
        String sql = "INSERT INTO evenement (titre, description, lieu, date_debut, date_fin, heure, id_createur, type_evenement) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, e.getTitre());
        ps.setString(2, e.getDescription());
        ps.setString(3, e.getLieu());
        ps.setDate(4, e.getDateDebut() != null ? Date.valueOf(e.getDateDebut()) : null);
        ps.setDate(5, e.getDateFin() != null ? Date.valueOf(e.getDateFin()) : null);
        ps.setTimestamp(6, e.getHeure() != null ? Timestamp.valueOf(e.getHeure()) : null);
        ps.setInt(7, e.getIdCreateur());
        ps.setString(8, e.getTypeEvenement());

        int affectedRows = ps.executeUpdate();
        System.out.println("Rows affected by insert: " + affectedRows);

        if (affectedRows > 0) {
            ResultSet generatedKeys = null;
            try {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Generated ID: " + generatedId);
                    e.setIdEvenement(generatedId);
                } else {
                    System.out.println("No ID was generated");
                }
            } finally {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
            }
        }
    }

    public List<Evenement> getAll() throws SQLException {
        List<Evenement> liste = new ArrayList<>();
        String sql = "SELECT * FROM evenement";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id_evenement");
            System.out.println("Retrieved event with ID: " + id);

            Evenement e = new Evenement(
                    id,
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getString("lieu"),
                    rs.getDate("date_debut").toLocalDate(),
                    rs.getDate("date_fin").toLocalDate(),
                    rs.getTimestamp("heure").toLocalDateTime(),
                    rs.getInt("id_createur"),
                    rs.getString("type_evenement")
            );
            liste.add(e);
        }

        return liste;
    }

    public void supprimer(int id) throws SQLException {
        // First check if the event exists
        String checkSql = "SELECT COUNT(*) FROM evenement WHERE id_evenement = ?";
        PreparedStatement checkPs = connection.prepareStatement(checkSql);
        checkPs.setInt(1, id);
        ResultSet rs = checkPs.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        if (count == 0) {
            throw new SQLException("L'événement avec l'ID " + id + " n'existe pas");
        }

        // If it exists, proceed with deletion
        String deleteSql = "DELETE FROM evenement WHERE id_evenement = ?";
        PreparedStatement deletePs = connection.prepareStatement(deleteSql);
        deletePs.setInt(1, id);
        int rowsAffected = deletePs.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("Aucune ligne n'a été supprimée");
        }
    }

    public void modifier(Evenement e) throws SQLException {
        String sql = "UPDATE evenement SET titre = ?, description = ?, lieu = ?, date_debut = ?, date_fin = ?, heure = ?, id_createur = ?, type_evenement = ? WHERE id_evenement = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, e.getTitre());
        ps.setString(2, e.getDescription());
        ps.setString(3, e.getLieu());
        ps.setDate(4, e.getDateDebut() != null ? Date.valueOf(e.getDateDebut()) : null);
        ps.setDate(5, e.getDateFin() != null ? Date.valueOf(e.getDateFin()) : null);
        ps.setTimestamp(6, e.getHeure() != null ? Timestamp.valueOf(e.getHeure()) : null);
        ps.setInt(7, e.getIdCreateur());
        ps.setString(8, e.getTypeEvenement());
        ps.setInt(9, e.getIdEvenement());

        int rowsAffected = ps.executeUpdate();
        System.out.println("Rows affected by update: " + rowsAffected);
    }


    public void accepterEvenement(int id) throws SQLException {
        String query = "UPDATE evenement SET statut_evenement = 'Accepté' WHERE id_evenement = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public void refuserEvenement(int id) throws SQLException {
        String query = "UPDATE evenement SET statut_evenement = 'Refusé' WHERE id_evenement = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public List<Evenement> getEnAttente() throws SQLException {
        List<Evenement> liste = new ArrayList<>();
        String sql = "SELECT * FROM evenement WHERE statut_evenement = 'En attente'";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Evenement e = new Evenement(
                    rs.getInt("id_evenement"),
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getString("lieu"),
                    rs.getDate("date_debut").toLocalDate(),
                    rs.getDate("date_fin").toLocalDate(),
                    rs.getTimestamp("heure").toLocalDateTime(),
                    rs.getInt("id_createur"),
                    rs.getString("type_evenement")
            );
            liste.add(e);
        }

        return liste;
    }


}
