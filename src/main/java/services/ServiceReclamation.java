package services;

import entities.Reclamation;
import utils.MyDatabase;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ServiceReclamation {
    private Connection con;

    public ServiceReclamation() {
        con = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO reclamation (id_utilisateur, type_reclamation, description, date_creation, statut, priorite) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, reclamation.getIdUtilisateur());
            ps.setString(2, reclamation.getTypeReclamation());
            ps.setString(3, reclamation.getDescription());
            ps.setString(4, reclamation.getDateCreation());
            ps.setString(5, reclamation.getStatut());
            ps.setString(6, reclamation.getPriorite());

            ps.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réclamation: " + e.getMessage());
            throw e;
        }
    }

    public List<Reclamation> recuperer(int userId) throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation WHERE id_utilisateur = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idReclamation = rs.getInt("id_reclamation");
                int idUtilisateur = rs.getInt("id_utilisateur");
                String typeReclamation = rs.getString("type_reclamation");
                String description = rs.getString("description");
                String dateCreation = rs.getString("date_creation");
                String statut = rs.getString("statut");
                String priorite = rs.getString("priorite");

                Reclamation reclamation = new Reclamation(idReclamation, idUtilisateur, typeReclamation, description, dateCreation, statut, priorite);
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réclamations: " + e.getMessage());
            throw e;
        }

        return reclamations;
    }

public void modifier(Reclamation reclamation) throws SQLException {
        String req = "UPDATE reclamation SET id_utilisateur = ?, type_reclamation = ?, description = ?, date_creation = ?, statut = ?, priorite = ? WHERE id_reclamation = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, reclamation.getIdUtilisateur());
            ps.setString(2, reclamation.getTypeReclamation());
            ps.setString(3, reclamation.getDescription());
            ps.setDate(4, Date.valueOf(reclamation.getDateCreation()));
            ps.setString(5, reclamation.getStatut());
            ps.setString(6, reclamation.getPriorite());
            ps.setInt(7, reclamation.getIdReclamation());
            ps.executeUpdate();
            System.out.println("Réclamation modifiée");
        }
    }

    public void supprimer(Reclamation reclamation) throws SQLException {
        String req = "DELETE FROM reclamation WHERE id_reclamation = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, reclamation.getIdReclamation());
            ps.executeUpdate();
            System.out.println("Réclamation supprimée");
        }
    }

    public List<Reclamation> recuperer() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM reclamation";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                int idReclamation = rs.getInt("id_reclamation");
                int idUtilisateur = rs.getInt("id_utilisateur");
                String typeReclamation = rs.getString("type_reclamation");
                String description = rs.getString("description");
                String dateCreation = rs.getString("date_creation");
                String statut = rs.getString("statut");
                String priorite = rs.getString("priorite");
                Reclamation reclamation = new Reclamation(idReclamation, idUtilisateur, typeReclamation, description, dateCreation, statut, priorite);
                reclamations.add(reclamation);
            }
        }
        return reclamations;
    }

    public List<Reclamation> recupererParUtilisateur(int userId) throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM reclamation WHERE id_utilisateur = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idReclamation = rs.getInt("id_reclamation");
                    int idUtilisateur = rs.getInt("id_utilisateur");
                    String typeReclamation = rs.getString("type_reclamation");
                    String description = rs.getString("description");
                    String dateCreation = rs.getString("date_creation");
                    String statut = rs.getString("statut");
                    String priorite = rs.getString("priorite");
                    Reclamation reclamation = new Reclamation(idReclamation, idUtilisateur, typeReclamation, description, dateCreation, statut, priorite);
                    reclamations.add(reclamation);
                }
            }
        }
        return reclamations;
    }

    public void update(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamation SET statut = ? WHERE id_reclamation = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, reclamation.getStatut()); // Exemple de champ mis à jour
            ps.setInt(2, reclamation.getIdReclamation());
            ps.executeUpdate();
        }
    }
}
