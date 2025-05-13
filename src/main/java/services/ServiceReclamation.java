package services;

import entities.Reclamation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclamation implements IService<Reclamation> {
    private Connection con;

    public ServiceReclamation() {
        con = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Reclamation reclamation) throws SQLException {
        String req = "INSERT INTO reclamation(id_utilisateur, type_reclamation, description, date_creation, statut, priorite)"
                + " VALUES (" + reclamation.getIdUtilisateur() + ",'" + reclamation.getTypeReclamation() + "','"
                + reclamation.getDescription() + "','" + reclamation.getDateCreation() + "','"
                + reclamation.getStatut() + "','" + reclamation.getPriorite() + "')";
        Statement st = con.createStatement();
        st.executeUpdate(req);
        System.out.println("Réclamation ajoutée");
    }

    @Override
    public void modifier(Reclamation reclamation) throws SQLException {
        String req = "UPDATE reclamation SET id_utilisateur=?, type_reclamation=?, description=?, date_creation=?, statut=?, priorite=? WHERE id_reclamation=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reclamation.getIdUtilisateur());
        ps.setString(2, reclamation.getTypeReclamation());
        ps.setString(3, reclamation.getDescription());
        ps.setString(4, reclamation.getDateCreation());
        ps.setString(5, reclamation.getStatut());
        ps.setString(6, reclamation.getPriorite());
        ps.setInt(7, reclamation.getIdReclamation());
        ps.executeUpdate();
        System.out.println("Réclamation modifiée");
    }

    @Override
    public void supprimer(Reclamation reclamation) throws SQLException {
        String req = "DELETE FROM reclamation WHERE id_reclamation=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reclamation.getIdReclamation());
        ps.executeUpdate();
        System.out.println("Réclamation supprimée");
    }

    @Override
    public List<Reclamation> recuperer() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM reclamation";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(req);
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
        return reclamations;
    }
    public void update(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamations SET status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, reclamation.getStatut()); // Exemple de champ mis à jour
            preparedStatement.setInt(2, reclamation.getIdReclamation());
            preparedStatement.executeUpdate();
        }
    }
}
