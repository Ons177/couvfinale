package services;

import entities.Commentaire;
import utils.MyDatabase;
import entities.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommentaire {
    private Connection connection = MyDatabase.getInstance().getConnection();


    public void ajouter(Commentaire commentaire) throws SQLException {
        String req = "INSERT INTO commentaire(post_id, comment_contenu, date_comment, user_id) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, commentaire.getPost_id());
        ps.setString(2, commentaire.getComment_contenu());
        ps.setTimestamp(3, Timestamp.valueOf(commentaire.getDate_commentaire()));
        ps.setInt(4, commentaire.getUser_id());
        ps.executeUpdate();
        System.out.println("Commentaire ajouté");
    }


    public void supprimer(Commentaire commentaire) throws SQLException {
        String req = "DELETE FROM commentaire WHERE commentaire_id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, commentaire.getCommentaire_id());
        ps.executeUpdate();
        System.out.println("Commentaire supprimé");
    }


    public void modifier(Commentaire commentaire) throws SQLException {
        String req = "UPDATE commentaire SET post_id=?, comment_contenu=?, date_comment=?, user_id=? WHERE commentaire_id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, commentaire.getPost_id());
        ps.setString(2, commentaire.getComment_contenu());
        ps.setTimestamp(3, Timestamp.valueOf(commentaire.getDate_commentaire()));
        ps.setInt(4, commentaire.getUser_id());
        ps.setInt(5, commentaire.getCommentaire_id());
        ps.executeUpdate();
        System.out.println("Commentaire modifié");
    }


    public List<Commentaire> recuperer() throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        String req = "SELECT * FROM commentaire";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            commentaires.add(new Commentaire(
                    rs.getInt("commentaire_id"),
                    rs.getInt("post_id"),
                    rs.getString("comment_contenu"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("date_comment").toLocalDateTime()
            ));
        }
        return commentaires;
    }

    // Méthode pour récupérer les commentaires d'un post spécifique
    public List<Commentaire> recupererParPost(int postId) throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        String req = "SELECT * FROM commentaire WHERE post_id = ? ORDER BY date_comment DESC";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, postId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            commentaires.add(new Commentaire(
                    rs.getInt("commentaire_id"),
                    rs.getInt("post_id"),
                    rs.getString("comment_contenu"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("date_comment").toLocalDateTime()
            ));
        }
        return commentaires;
    }
    // Méthode pour supprimer un commentaire par ID
    public void supprimer(int commentaireId) throws SQLException {
        String req = "DELETE FROM commentaire WHERE commentaire_id=?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, commentaireId);
        ps.executeUpdate();
        System.out.println("Commentaire supprimé avec ID: " + commentaireId);
    }

    // Méthode pour obtenir le dernier ID de commentaire inséré
    public int getLastInsertedId() throws SQLException {
        String req = "SELECT LAST_INSERT_ID() as last_id";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        if (rs.next()) {
            return rs.getInt("last_id");
        }
        return -1;
    }

    // Méthode pour récupérer un commentaire spécifique par son ID
    public Commentaire getOne(int commentaireId) throws SQLException {
        String req = "SELECT * FROM commentaire WHERE commentaire_id = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, commentaireId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Commentaire(
                    rs.getInt("commentaire_id"),
                    rs.getInt("post_id"),
                    rs.getString("comment_contenu"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("date_comment").toLocalDateTime()
            );
        }
        return null;
    }
    // Alias pour recupererParPost, utilisé dans PostDetailController
    public List<Commentaire> getCommentairesByPostId(int postId) throws SQLException {
        return recupererParPost(postId);
    }
}