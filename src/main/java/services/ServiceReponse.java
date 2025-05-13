package services;
import entities.Reponse;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceReponse implements IService<Reponse> {
    private static Connection con;

    public ServiceReponse() {
        con = MyDatabase.getInstance().getCnx();
    }

    public void ajouter(Reponse reponse) throws SQLException {
        String req = "INSERT INTO reponse(id_reclamation, id_utilisateur, contenu, date_reponse)" +
                " VALUES ("+reponse.getIdReclamation()+", "+reponse.getIdUtilisateur()+", '"+reponse.getContenu()+"', '"+reponse.getDateReponse()+"')";
        Statement st = con.createStatement();
        st.executeUpdate(req);
        System.out.println("réponse ajoutée");
    }


    @Override
    public void modifier(Reponse reponse) throws SQLException {
        String req = "UPDATE reponse SET id_reclamation=?, id_utilisateur=?, contenu=?, date_reponse=? WHERE id_reponse=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getIdReclamation());
        ps.setInt(2, reponse.getIdUtilisateur());
        ps.setString(3, reponse.getContenu());
        ps.setString(4, reponse.getDateReponse());
        ps.setInt(5, reponse.getIdReponse());
        ps.executeUpdate();
        System.out.println("Réponse modifiée");
    }

    @Override
    public void supprimer(Reponse reponse) throws SQLException {
        String req = "DELETE FROM reponse WHERE id_reponse=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getIdReponse());
        ps.executeUpdate();
        System.out.println("Réponse supprimée");
    }

    @Override
    public List<Reponse> recuperer() throws SQLException {
        List<Reponse> reponses = new ArrayList<>();
        String req = "SELECT * FROM reponse";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            int idReponse = rs.getInt("id_reponse");
            int idReclamation = rs.getInt("id_reclamation");
            int idUtilisateur = rs.getInt("id_utilisateur");
            String contenu = rs.getString("contenu");
            String dateReponse = rs.getString("date_reponse");
            Reponse reponse = new Reponse(idReponse, idReclamation, idUtilisateur, contenu, dateReponse);
            reponses.add(reponse);
        }
        return reponses;
    }
}