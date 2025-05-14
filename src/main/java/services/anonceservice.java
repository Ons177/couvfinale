package services;
import entities.Anonce;
import utils.MyDatabase;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class anonceservice implements IService<Anonce> {
    private Connection con;
    public anonceservice() {

        con = MyDatabase.getInstance().getCnx();
    }
    public void add(Anonce anonce) throws SQLException {
        String req = "INSERT INTO anonces (titre_a, description_a, datedispo, prixj, vehicule_id, utilisateur_id, photo_va, adresse, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, anonce.getTitre());
        pstmt.setString(2, anonce.getDescription());
        pstmt.setDate(3, anonce.getDateDisponibilite());  // java.sql.Date
        pstmt.setDouble(4, anonce.getPrixParJour());
        pstmt.setInt(5, anonce.getVehiculeId());
        pstmt.setInt(6, anonce.getUtilisateurId());
        pstmt.setString(7, anonce.getPhotoVehicule());
        pstmt.setString(8, anonce.getAdresse());
        pstmt.setDouble(9, anonce.getLatitude());
        pstmt.setDouble(10, anonce.getLongitude());

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                anonce.setIdAnonce(generatedId);
                System.out.println("Annonce ajoutée avec succès ! ID généré : " + generatedId);
            }
        } else {
            System.out.println("Erreur lors de l'ajout de l'annonce.");
        }

        pstmt.close();
    }


    public void update(Anonce anonce) throws SQLException {
    String req = "UPDATE anonces SET "
            + "titre_a = ?, "
            + "description_a = ?, "
            + "datedispo = ?, "
            + "prixj = ?, "
            + "vehicule_id = ?, "
            + "utilisateur_id = ?, "
            + "photo_va = ?, "
            + "adresse = ?, "
            + "latitude = ?, "
            + "longitude = ? "
            + "WHERE id_anonce = ?";

    PreparedStatement pstmt = con.prepareStatement(req);

    pstmt.setString(1, anonce.getTitre());
    pstmt.setString(2, anonce.getDescription());
    pstmt.setDate(3, new java.sql.Date(anonce.getDateDisponibilite().getTime()));  // Conversion de Date en SQL Date
    pstmt.setDouble(4, anonce.getPrixParJour());
    pstmt.setInt(5, anonce.getVehiculeId());
    pstmt.setInt(6, anonce.getUtilisateurId());
    pstmt.setString(7, anonce.getPhotoVehicule());
    pstmt.setString(8, anonce.getAdresse());
    pstmt.setDouble(9, anonce.getLatitude());
    pstmt.setDouble(10, anonce.getLongitude());
    pstmt.setInt(11, anonce.getIdAnonce());

    int rowsAffected = pstmt.executeUpdate();

    if (rowsAffected > 0) {
        System.out.println("Annonce mise à jour avec succès !");
    } else {
        System.out.println("Aucune annonce trouvée avec cet ID ou échec de la mise à jour.");
    }

    pstmt.close();
  }

  public void delete(Anonce anonce) throws SQLException {
        String req = "DELETE FROM anonces WHERE id_anonce = ?";
        PreparedStatement pstmt = con.prepareStatement(req);
        pstmt.setInt(1,anonce.getIdAnonce());
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Anonce supprimé avec succés");
        }
        else {
            System.out.println("echec ");
        }
        pstmt.close();
  }

    public List<Anonce> getAll() throws SQLException {
        String req = "SELECT * FROM anonces";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(req);
        List<Anonce> anonces = new ArrayList<>();
        while (rs.next()) {
            int id_anonce = rs.getInt("id_anonce");
            String titre = rs.getString("titre_a");
            String description = rs.getString("description_a");
            Date dateDisponibilite = rs.getDate("datedispo");
            double prixj = rs.getDouble("prixj");
            int vehiculeId = rs.getInt("vehicule_id");
            int utilisateurId = rs.getInt("utilisateur_id");
            String photoVehicule = rs.getString("photo_va");
            String adresse = rs.getString("adresse");
            double latitude = rs.getDouble("latitude");
            double longitude = rs.getDouble("longitude");
            Anonce anonce= new Anonce(id_anonce,titre,description,dateDisponibilite,prixj,vehiculeId,utilisateurId,photoVehicule,adresse);
            anonces.add(anonce);

        }
        return anonces;
  }
    public Anonce findById(int id) throws SQLException {
        String sql = "SELECT * FROM anonces WHERE id_anonce = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String titre       = rs.getString("titre_a");
                String description = rs.getString("description_a");
                Date   dateDisp    = rs.getDate("datedispo");
                double prixj       = rs.getDouble("prixj");
                int    vehiculeId  = rs.getInt("vehicule_id");
                int    utilisateurId = rs.getInt("utilisateur_id");
                String photo       = rs.getString("photo_va");
                String adresse     = rs.getString("adresse");
                double lat         = rs.getDouble("latitude");
                double lon         = rs.getDouble("longitude");

                Anonce a = new Anonce(id, titre, description, dateDisp, prixj,
                        vehiculeId, utilisateurId, photo,
                        adresse);
                a.setLatitude(lat);
                a.setLongitude(lon);
                return a;
            }
            return null;
        }
    }
    public List<Anonce> rechercherParMotCle(String motCle) throws SQLException {
        List<Anonce> resultats = new ArrayList<>();
        String requete = "SELECT * FROM anonces WHERE titre_a LIKE ? OR description_a LIKE ? OR adresse LIKE ?";

        try (PreparedStatement ps = con.prepareStatement(requete)) {
            String motClePattern = "%" + motCle + "%";
            ps.setString(1, motClePattern);
            ps.setString(2, motClePattern);
            ps.setString(3, motClePattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id_anonce = rs.getInt("id_anonce");
                String titre = rs.getString("titre_a");
                String description = rs.getString("description_a");
                Date dateDisponibilite = rs.getDate("datedispo");
                double prixj = rs.getDouble("prixj");
                int vehiculeId = rs.getInt("vehicule_id");
                int utilisateurId = rs.getInt("utilisateur_id");
                String photoVehicule = rs.getString("photo_va");
                String adresse = rs.getString("adresse");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");

                Anonce anonce = new Anonce(id_anonce, titre, description, dateDisponibilite, prixj, vehiculeId,
                        utilisateurId, photoVehicule, adresse);
                anonce.setLatitude(latitude);
                anonce.setLongitude(longitude);

                resultats.add(anonce);
            }
        }
        return resultats;
    }
    public List<Anonce> getAnoncesParConducteur(int conducteurId) throws SQLException {
        List<Anonce> annonces = new ArrayList<>();
        String sql = "SELECT * FROM anonces WHERE utilisateur_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, conducteurId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_anonce");
                String titre = rs.getString("titre_a");
                String description = rs.getString("description_a");
                Date dateDisp = rs.getDate("datedispo");
                double prixj = rs.getDouble("prixj");
                int vehiculeId = rs.getInt("vehicule_id");
                String photo = rs.getString("photo_va");
                String adresse = rs.getString("adresse");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");

                Anonce a = new Anonce(id, titre, description, dateDisp, prixj, vehiculeId, conducteurId, photo, adresse);
                // Si tu as des setters pour latitude/longitude
                a.setLatitude(lat);
                a.setLongitude(lon);

                annonces.add(a);
            }
        }

        return annonces;
    }




}
