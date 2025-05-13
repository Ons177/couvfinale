package services;

import entities.Utilisateur;
import javafx.scene.control.Alert;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserService {



    public boolean ajouter(Utilisateur user) {
        return register(user);
    }

    public boolean register(Utilisateur user) {
        String query = "INSERT INTO utilisateur (nom, prenom, email, mdp, telephone, role, date_naissance, cin, permis, isApproved) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getMdp());
            statement.setString(5, user.getTelephone());
            statement.setString(6, user.getRole());
            statement.setDate(7, user.getDate_naissance());
            statement.setString(8, user.getCin());
            statement.setString(9, user.getPermis());
            statement.setBoolean(10, user.isApproved());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(Utilisateur user) {
        String query = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, user.getId_utilisateur());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(Utilisateur user) {
        String query = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mdp=?, telephone=?, role=?, date_naissance=?, cin=?, permis=?, isApproved=? WHERE id_utilisateur=?";
        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getMdp());
            statement.setString(5, user.getTelephone());
            statement.setString(6, user.getRole());
            statement.setDate(7, user.getDate_naissance());
            statement.setString(8, user.getCin());
            statement.setString(9, user.getPermis());
            statement.setBoolean(10, user.isApproved());
            statement.setInt(11, user.getId_utilisateur());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<Utilisateur> recuperer() {
        List<Utilisateur> users = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";

        try (Connection connection = MyDatabase.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                users.add(createUserFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public Utilisateur login(String email, String password) {
        String query = "SELECT * FROM utilisateur WHERE email = ? AND mdp = ?";
        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // L'utilisateur existe, on le crée et le retourne
                    return createUserFromResultSet(resultSet);
                } else {
                    // Aucun utilisateur trouvé, login échoué
                    return null;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // En cas d'erreur de base de données
        }
    }

    private void handleSuccessfulLogin(Utilisateur user) {
        // Exemple : redirection vers un tableau de bord en fonction du rôle
        String role = user.getRole();

        if ("ADMIN".equals(role)) {
            // Rediriger vers la vue Admin
            System.out.println("Connexion en tant qu'ADMIN");
            // Code pour rediriger l'utilisateur vers la vue Admin
        } else if ("CONDUCTEUR".equals(role)) {
            // Rediriger vers la vue Conducteur
            System.out.println("Connexion en tant que CONDUCTEUR");
            // Code pour rediriger l'utilisateur vers la vue Conducteur
        } else if ("PASSAGER".equals(role)) {
            // Rediriger vers la vue Passager
            System.out.println("Connexion en tant que PASSAGER");
            // Code pour rediriger l'utilisateur vers la vue Passager
        } else {
            showAlert("Erreur", "Rôle inconnu.");
        }
    }

    public List<Utilisateur> getPendingDriverRequests() {
        List<Utilisateur> requests = new ArrayList<>();
        String query = "SELECT * FROM utilisateur WHERE role = 'CONDUCTEUR' AND isApproved = false";

        try (Connection connection = MyDatabase.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                requests.add(createUserFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public boolean approveDriver(Utilisateur user) {
        String query = "UPDATE utilisateur SET isApproved = true WHERE id_utilisateur = ?";

        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, user.getId_utilisateur());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean rejectDriver(Utilisateur user) {
        String query = "DELETE FROM utilisateur WHERE id_utilisateur = ?";

        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, user.getId_utilisateur());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Utilisateur> getAllUsers() {
        List<Utilisateur> list = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Connection connection = MyDatabase.getInstance().getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
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
                u.setCin(rs.getString("cin"));
                u.setPermis(rs.getString("permis"));
                u.setApproved(rs.getBoolean("isApproved"));
                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private Utilisateur createUserFromResultSet(ResultSet resultSet) throws SQLException {
        Utilisateur user = new Utilisateur(
                resultSet.getString("nom"),
                resultSet.getString("prenom"),
                resultSet.getString("email"),
                resultSet.getString("mdp"),
                resultSet.getString("telephone"),
                resultSet.getString("role"),
                resultSet.getDate("date_naissance")
        );
        user.setId_utilisateur(resultSet.getInt("id_utilisateur"));
        user.setCin(resultSet.getString("cin"));
        user.setPermis(resultSet.getString("permis"));
        user.setApproved(resultSet.getBoolean("isApproved"));
        return user;
    }
    public Utilisateur getUtilisateurByEmail(String email) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId_utilisateur(rs.getInt("id_utilisateur"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setMdp(rs.getString("mdp"));
                u.setRole(rs.getString("role"));
                return u;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
