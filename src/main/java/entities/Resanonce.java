package entities;

import java.sql.Timestamp;
import java.sql.Date;

public class Resanonce {

    public enum StatutReservation {
        en_attente, confirmée, refusée
    }

    private int idReservationa;
    private Anonce anonce;
    private int conducteurIdanonce;
    private Date dateDebuta;
    private Date dateFina;
    private StatutReservation statutano;
    private Timestamp dateReservationa;
    public Resanonce() {
    }

    public Resanonce(Date dateDebut, Date dateFin, StatutReservation statut, int utilisateurId, Anonce anonce) {
        this.dateDebuta = dateDebut;
        this.dateFina = dateFin;
        this.statutano = statut;
        this.conducteurIdanonce = utilisateurId;
        this.anonce = anonce;
    }

    public Resanonce(int idReservation, Date dateDebut, Date dateFin, StatutReservation statut, int utilisateurId, Anonce anonce, Timestamp dateReservation) {
        this.idReservationa = idReservation;
        this.dateDebuta = dateDebut;
        this.dateFina = dateFin;
        this.statutano = statut;
        this.conducteurIdanonce = utilisateurId;
        this.anonce = anonce;
        this.dateReservationa = dateReservation;
    }

    // --- Getters & Setters ---

    public int getIdReservationa() {
        return idReservationa;
    }

    public void setIdReservation(int idReservation) {
        this.idReservationa = idReservation;
    }

    public Anonce getAnonce() {
        return anonce;
    }

    public void setAnonceId(Anonce anonce) {
        this.anonce = anonce;
    }

    public int getUtilisateurId() {
        return conducteurIdanonce;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.conducteurIdanonce = utilisateurId;
    }

    public Date getDateDebuta() {
        return dateDebuta;
    }

    public void setDateDebuta(Date dateDebut) {
        this.dateDebuta = dateDebut;
    }

    public Date getDateFina() {
        return dateFina;
    }

    public void setDateFina(Date dateFin) {
        this.dateFina = dateFin;
    }

    public StatutReservation getStatutano() {
        return statutano;
    }

    public void setStatuta(StatutReservation statut) {
        this.statutano = statut;
    }

    public Timestamp getDateReservationa() {
        return dateReservationa;
    }

    public void setDateReservationa(Timestamp dateReservation) {
        this.dateReservationa = dateReservation;
    }
    @Override
    public String toString() {
        return "Reservation anonce{" +
                "idReservationa=" + idReservationa +
                ", dateDebuta=" + dateDebuta +
                ", dateFina=" + dateFina +
                ", statutano=" + statutano +
                ", utilisateurId=" + conducteurIdanonce +
                ", anonce=" + (anonce!=null ? anonce.getIdAnonce() : "null") +
                ", dateReservationa=" + dateReservationa +
                '}';
    }
}
