package services;

import entities.feedbackreponserec;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceFeedbackRepRec {
    private Connection connection;

    public ServiceFeedbackRepRec() {
        connection = MyDatabase.getInstance().getCnx();
    }

    public void ajouterReaction(feedbackreponserec feedback) throws SQLException {
        String sql = "INSERT INTO feedbackreponserec (id_reponse, reaction_rep, date_feedbackrep) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, feedback.getIdReponse());
        ps.setString(2, feedback.getReaction());
        ps.setString(3, feedback.getDateFeedbackrep());
        ps.executeUpdate();
    }

    public feedbackreponserec getFeedbackByReponseId(int idReponse) throws SQLException {
        String sql = "SELECT * FROM feedbackreponserec WHERE id_reponse = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idReponse);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            feedbackreponserec feedback = new feedbackreponserec();
            feedback.setIdFeedbackreponserec(rs.getInt("id_feedbackreponserec"));
            feedback.setIdReponse(rs.getInt("id_reponse"));
            feedback.setReaction(rs.getString("reaction_rep"));
            feedback.setDateFeedbackrep(rs.getString("date_feedbackrep"));
            return feedback;
        }

        return null;
    }
    public void supprimerReactionByReponseId(int idReponse) throws SQLException {
        String sql = "DELETE FROM feedbackreponserec WHERE id_reponse = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idReponse);
        ps.executeUpdate();
    }

}
