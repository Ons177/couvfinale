package services;

import entities.Vehicule;
import entities.Utilisateur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleService {
    private Connection connection;

    public VehicleService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    // Ajouter un véhicule
    public boolean addVehicle(Vehicule vehicule) {
        String query = "INSERT INTO vehicule (id_utilisateur, marque, modele, couleur, matricule, nbre_places) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, vehicule.getUtilisateur().getId_utilisateur());
            statement.setString(2, vehicule.getMarque());
            statement.setString(3, vehicule.getModele());
            statement.setString(4, vehicule.getCouleur());
            statement.setString(5, vehicule.getMatricule());
            statement.setInt(6, vehicule.getNbre_places());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    vehicule.setId_vehicule(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un véhicule
    // Méthode pour supprimer un véhicule
    public boolean supprimer(Vehicule vehicule) {
        String query = "DELETE FROM vehicule WHERE id_vehicule = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Supprime le véhicule en fonction de son ID
            statement.setInt(1, vehicule.getId_vehicule());

            // Retourne true si la suppression a été effectuée
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Modifier un véhicule
    public boolean modifier(Vehicule vehicule) {
        String query = "UPDATE vehicule SET marque=?, modele=?, couleur=?, matricule=?, nbre_places=? WHERE id_vehicule=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, vehicule.getMarque());
            statement.setString(2, vehicule.getModele());
            statement.setString(3, vehicule.getCouleur());
            statement.setString(4, vehicule.getMatricule());
            statement.setInt(5, vehicule.getNbre_places());
            statement.setInt(6, vehicule.getId_vehicule());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Afficher tous les véhicules
    public List<Vehicule> recuperer() {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM vehicule";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Vehicule v = createVehiculeFromResultSet(resultSet);
                vehicules.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicules;
    }

    // Utilitaire pour créer un véhicule à partir d'un ResultSet
    private Vehicule createVehiculeFromResultSet(ResultSet resultSet) throws SQLException {
        // Ici, tu dois adapter selon ton constructeur Vehicule
        // Supposons que tu as un constructeur avec tous les champs
        Utilisateur user = new Utilisateur(
                "", "", "", "", "", "", null // Remplis si tu veux charger l'utilisateur complet
        );
        user.setId_utilisateur(resultSet.getInt("id_utilisateur"));
        Vehicule v= new Vehicule(
                user,
                resultSet.getString("marque"),
                resultSet.getString("modele"),
                resultSet.getString("couleur"),
                resultSet.getString("matricule"),
                resultSet.getInt("nbre_places")
        );
        v.setId_vehicule(resultSet.getInt("id_vehicule"));
      return v;
    }
    public Vehicule getVehiculeByUserId(int userId) {
        Vehicule vehicule = null;
        String req = "SELECT * FROM vehicule WHERE id_utilisateur = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                vehicule = new Vehicule(
                        rs.getInt("id_vehicule"),
                        new Utilisateur(userId),// ou récupère plus d'infos si tu veux
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getString("couleur"),
                        rs.getString("matricule"),
                        rs.getInt("nbre_places")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicule;
    }
    public List<Vehicule> getVehiculesByIdUtilisateur(int idUtilisateur) {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM vehicule WHERE id_utilisateur = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idUtilisateur);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Vehicule v = createVehiculeFromResultSet(resultSet);
                vehicules.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicules;
    }
    public Vehicule getVehiculeByMatricule(String matricule) {
        String query = "SELECT * FROM vehicule WHERE matricule = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matricule);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Créer un objet Vehicule à partir du ResultSet
                return createVehiculeFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si aucun véhicule n'est trouvé
    }
    public Vehicule getVehiculeByIdVehicule(int idVehicule) {
        String query = "SELECT * FROM vehicule WHERE id_vehicule = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idVehicule);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Créer un objet Vehicule à partir du ResultSet
                return createVehiculeFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si aucun véhicule trouvé
    }

}