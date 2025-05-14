package services;

import entities.Utilisateur;
import entities.Vehicule;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleService {
    private Connection connection;
    public VehicleService() {
        connection = MyDatabase.getInstance().getCnx();
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
    private Vehicule createVehiculeFromResultSet(ResultSet resultSet) throws SQLException {
        // Ici, tu dois adapter selon ton constructeur Vehicule
        // Supposons que tu as un constructeur avec tous les champs
        UserService us = new UserService();
        Utilisateur user = us.getUserById(resultSet.getInt("id_utilisateur"));

        Vehicule v = new Vehicule(
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
    // Dans la classe VehicleService
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
