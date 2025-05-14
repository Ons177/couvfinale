package services;

import entities.Post;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePost implements IService<Post> {
    private Connection con = MyDatabase.getInstance().getCnx();

    @Override
    public void ajouter(Post post) throws SQLException {
        String req = "INSERT INTO post(titre, post_contenu, date_post,user_id , image) VALUES (?, ?, ?,?,?)";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, post.getTitre());
        ps.setString(2, post.getPost_contenu());
        ps.setTimestamp(3, Timestamp.valueOf(post.getDate_post()));
        ps.setInt(4, post.getUser_id());
        ps.setString(5, post.getImage());
        ps.executeUpdate();
        System.out.println("Post ajouté");
    }

    @Override
    public void supprimer(Post post) throws SQLException {
        String req = "DELETE FROM post WHERE post_id=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, post.getPost_id());
        ps.executeUpdate();
        System.out.println("Post supprimé");
    }

    @Override
    public void modifier(Post post) throws SQLException {
        String req = "UPDATE post SET titre=?, post_contenu=?, date_post=? , user_id=? , image=? WHERE post_id=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, post.getTitre());
        ps.setString(2, post.getPost_contenu());
        ps.setTimestamp(3, Timestamp.valueOf(post.getDate_post()));
        ps.setInt(4, post.getUser_id());
        ps.setString(5, post.getImage());
        ps.setInt(6, post.getPost_id());

        ps.executeUpdate();
        System.out.println("Post modifié");
    }

    @Override
    public List<Post> recuperer() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String req = "SELECT * FROM post";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            posts.add(new Post(
                    rs.getInt("post_id"),
                    rs.getString("titre"),
                    rs.getString("post_contenu"),
                    rs.getTimestamp("date_post").toLocalDateTime(),
                    rs.getInt("user_id"),
                    rs.getString("image")
            ));
        }
        return posts;
    }
}
