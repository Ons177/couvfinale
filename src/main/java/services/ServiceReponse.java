package services;

import entities.Reponse;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReponse {
    private static Connection con;

    public ServiceReponse() {
        con = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Reponse reponse) throws SQLException {
        String req = "INSERT INTO reponse(id_reclamation, id_utilisateur, contenu, date_reponse, reaction, date_feedbackrep)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getIdReclamation());
        ps.setInt(2, reponse.getIdUtilisateur());
        ps.setString(3, reponse.getContenu());
        ps.setString(4, reponse.getDateReponse());
        ps.setString(5, reponse.getReaction());
        ps.setString(6, reponse.getDateFeedbackrep());
        ps.executeUpdate();
        System.out.println("Réponse ajoutée");
    }

    public void modifier(Reponse reponse) throws SQLException {
        String req = "UPDATE reponse SET id_reclamation=?, id_utilisateur=?, contenu=?, date_reponse=?, reaction=?, date_feedbackrep=? WHERE id_reponse=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getIdReclamation());
        ps.setInt(2, reponse.getIdUtilisateur());
        ps.setString(3, reponse.getContenu());
        ps.setString(4, reponse.getDateReponse());
        ps.setString(5, reponse.getReaction());
        ps.setString(6, reponse.getDateFeedbackrep());
        ps.setInt(7, reponse.getIdReponse());
        ps.executeUpdate();
        System.out.println("Réponse modifiée");
    }

    public void supprimer(Reponse reponse) throws SQLException {
        String req = "DELETE FROM reponse WHERE id_reponse=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getIdReponse());
        ps.executeUpdate();
        System.out.println("Réponse supprimée");
    }

    public List<Reponse> recuperer() throws SQLException {
        List<Reponse> reponses = new ArrayList<>();
        String req = "SELECT * FROM reponse";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Reponse reponse = new Reponse(
                    rs.getInt("id_reponse"),
                    rs.getInt("id_reclamation"),
                    rs.getInt("id_utilisateur"),
                    rs.getString("contenu"),
                    rs.getString("date_reponse")
            );
            reponse.setReaction(rs.getString("reaction"));
            reponse.setDateFeedbackrep(rs.getString("date_feedbackrep"));
            reponses.add(reponse);
        }
        return reponses;
    }

    public void ajouterFeedback(int idReponse, String reaction, String dateFeedbackrep) throws SQLException {
        String req = "UPDATE reponse SET reaction = ?, date_feedbackrep = ? WHERE id_reponse = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, reaction);
        ps.setString(2, dateFeedbackrep);
        ps.setInt(3, idReponse);
        ps.executeUpdate();
    }

    public Reponse getReponseById(int idReponse) throws SQLException {
        String req = "SELECT * FROM reponse WHERE id_reponse = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, idReponse);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Reponse reponse = new Reponse(
                    rs.getInt("id_reponse"),
                    rs.getInt("id_reclamation"),
                    rs.getInt("id_utilisateur"),
                    rs.getString("contenu"),
                    rs.getString("date_reponse")
            );
            reponse.setReaction(rs.getString("reaction"));
            reponse.setDateFeedbackrep(rs.getString("date_feedbackrep"));
            return reponse;
        }
        return null;
    }
}
