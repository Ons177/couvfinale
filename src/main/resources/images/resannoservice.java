package services;

import entities.Anonce;
import entities.Resanonce;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class resannoservice implements IService<Resanonce> {

    private Connection con;
    private final anonceservice annonceService;



    public resannoservice() {
        con = MyDatabase.getInstance().getCnx();
        annonceService = new anonceservice();
    }



    public void add(Resanonce resanonce) throws SQLException {
        String req = "INSERT INTO reservationloc (id_annonce, id_utilisateur, date_debut, date_fin, statut) "
                + "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstmt = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, resanonce.getAnonce().getIdAnonce());
        pstmt.setInt(2, resanonce.getUtilisateurId());
        pstmt.setDate(3, resanonce.getDateDebuta());
        pstmt.setDate(4, resanonce.getDateFina());
        pstmt.setString(5, resanonce.getStatutano().name());
        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            // Récupérer l'ID généré automatiquement
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                resanonce.setIdReservation(rs.getInt(1));
                System.out.println("Réservation ajoutée avec succès ! ID généré : " + resanonce.getIdReservationa());
            }
        }
        else {
            System.out.println("Erreur lors de l'ajout de la réservation.");
        }

        pstmt.close();
    }


    public void update(Resanonce resanonce) throws SQLException {
        String req = "UPDATE reservationloc SET id_annonce = ?, id_utilisateur = ?, date_debut = ?, date_fin = ?, statut = ? "
                + "WHERE id_reservloc = ?";
        PreparedStatement pstmt = con.prepareStatement(req);


        pstmt.setInt(1, resanonce.getAnonce().getIdAnonce());
        pstmt.setInt(2, resanonce.getUtilisateurId());
        pstmt.setDate(3, resanonce.getDateDebuta());
        pstmt.setDate(4, resanonce.getDateFina());
        pstmt.setString(5, resanonce.getStatutano().name());
        pstmt.setInt(6, resanonce.getIdReservationa());

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Réservation mise à jour avec succès !");
        } else {
            System.out.println("Aucune réservation trouvée avec cet ID ou échec de la mise à jour.");
        }

        pstmt.close();
    }

    public void delete(Resanonce resanonce) throws SQLException {
        String req = "DELETE FROM reservationloc WHERE id_reservloc  = ?";

        PreparedStatement pstmt = con.prepareStatement(req);
        pstmt.setInt(1, resanonce.getIdReservationa());


        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Réservation supprimée avec succès !");
        } else {
            System.out.println("Aucune réservation trouvée avec cet ID ou échec de la suppression.");
        }

        pstmt.close();
    }


    public List<Resanonce> getAll() throws SQLException {
        String req = "SELECT * FROM reservationloc";

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(req);

        List<Resanonce> resanonces = new ArrayList<>();
        while (rs.next()) {
            int idReservationa = rs.getInt("id_reservloc");
            int anonceId = rs.getInt("id_annonce");
            int utilisateurId = rs.getInt("id_utilisateur");
            Date dateDebuta = rs.getDate("date_debut");
            Date dateFina = rs.getDate("date_fin");
            String statut = rs.getString("statut");
            Timestamp dateReservationa = rs.getTimestamp("date_reservloc");

            Resanonce.StatutReservation statutano = Resanonce.StatutReservation.valueOf(statut);
            Anonce anonce = annonceService.findById(anonceId); // ★


            Resanonce resanonce = new Resanonce(idReservationa, dateDebuta, dateFina, statutano, utilisateurId, anonce, dateReservationa);
            resanonces.add(resanonce);
        }

        return resanonces;
    }
    public List<Resanonce> getReservationsPourUtilisateur(int idUtilisateur) throws SQLException {
        List<Resanonce> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservationloc WHERE id_utilisateur = ?";

        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, idUtilisateur);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Resanonce r = new Resanonce();
            r.setIdReservation(rs.getInt("id_reservloc"));
            r.setUtilisateurId(rs.getInt("id_utilisateur"));
            r.setDateDebuta(rs.getDate("date_debut"));
            r.setDateFina(rs.getDate("date_fin"));
            r.setDateReservationa(rs.getTimestamp("date_reservloc"));
            r.setStatuta(Resanonce.StatutReservation.valueOf(rs.getString("statut")));

            // Tu dois aussi charger l’annonce : écris un petit service pour ça
            int anonceId = rs.getInt("id_annonce");
            anonceservice as = new anonceservice();
              Anonce anonce = as.findById(anonceId);
            r.setAnonceId(anonce);

            reservations.add(r);
        }

        return reservations;
    }
    public List<Resanonce> getReservationsPourConducteur(int idConducteur) throws SQLException {
        List<Resanonce> reservations = new ArrayList<>();
        String req = "SELECT r.* FROM reservationloc r " +
                "JOIN anonces a ON r.id_annonce = a.id_anonce " +
                "WHERE a.utilisateur_id  = ?";

        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, idConducteur);

        ResultSet rs = ps.executeQuery();
        anonceservice as = new anonceservice();

        while (rs.next()) {
            Resanonce r = new Resanonce();
            r.setIdReservation(rs.getInt("id_reservloc"));
            r.setUtilisateurId(rs.getInt("id_utilisateur"));
            r.setDateDebuta(rs.getDate("date_debut"));
            r.setDateFina(rs.getDate("date_fin"));
            r.setDateReservationa(rs.getTimestamp("date_reservloc"));
            r.setStatuta(Resanonce.StatutReservation.valueOf(rs.getString("statut")));

            int anonceId = rs.getInt("id_annonce");
            Anonce anonce = as.findById(anonceId);
            r.setAnonceId(anonce);

            reservations.add(r);
        }

        return reservations;
    }
    public boolean isDateRangeAvailable(int anonceId, LocalDate newStart, LocalDate newEnd) throws SQLException {
        String query = "SELECT COUNT(*) FROM reservationloc WHERE id_annonce = ? AND statut = 'confirmée' " +
                "AND NOT (date_fin < ? OR date_debut > ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, anonceId);
            ps.setDate(2, Date.valueOf(newStart));
            ps.setDate(3, Date.valueOf(newEnd));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }

        return false;
    }



}
