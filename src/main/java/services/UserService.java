package services;

import entities.Utilisateur;
import utils.MyDatabase;

import java.sql.*;

public class UserService {

    private Connection connection;

    // Constructeur pour établir la connexion avec la base de données
    public UserService() {

            // Remplace par ta propre configuration de connexion JDBC
            connection = MyDatabase.getInstance().getCnx();

    }

    public Utilisateur getUserById(int id) {
        String query = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Utilisateur trouvé dans la base de données");
                Utilisateur u = new Utilisateur(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("mdp"),
                    rs.getString("telephone"),
                    rs.getString("role"),
                    rs.getDate("date_naissance")
                );
                
                u.setId_utilisateur(rs.getInt("id_utilisateur"));
                u.setApproved(rs.getBoolean("isApproved"));
                
                System.out.println("Utilisateur créé avec succès : " + u);
                return u;
            } else {
                System.err.println("Aucun utilisateur trouvé avec l'ID : " + id);
                // Vérifions si la table existe et contient des données
                try (Statement checkStmt = connection.createStatement()) {
                    ResultSet countRs = checkStmt.executeQuery("SELECT COUNT(*) FROM utilisateur");
                    if (countRs.next()) {
                        System.out.println("Nombre total d'utilisateurs dans la base : " + countRs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
