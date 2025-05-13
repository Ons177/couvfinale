package entities;

import java.time.LocalDate;

public class ReservationEvenement {
    private int id;  // id_reservation
    private int idEvenement;
    private int idUtilisateur;
    private int nbPlaces;
    private LocalDate dateReservation;
    private String statut_reservation;
    private double prix;

    public ReservationEvenement() {
    }

    public ReservationEvenement(int idEvenement, int idUtilisateur, int nbPlaces, LocalDate dateReservation, String statut, double prix) {
        this.idEvenement = idEvenement;
        this.idUtilisateur = idUtilisateur;
        this.nbPlaces = nbPlaces;
        this.dateReservation = dateReservation;
        this.statut_reservation = statut;
        this.prix = prix;
    }

    public ReservationEvenement(int id, int idEvenement, int idUtilisateur, int nbPlaces, LocalDate dateReservation, String statut, double prix) {
        this.id = id;
        this.idEvenement = idEvenement;
        this.idUtilisateur = idUtilisateur;
        this.nbPlaces = nbPlaces;
        this.dateReservation = dateReservation;
        this.statut_reservation = statut;
        this.prix = prix;
    }

    // Getters
    public int getId() { return id; }
    public int getIdReservation() { return id; } // 👈 ajouté pour compatibilité avec le controller
    public int getIdEvenement() { return idEvenement; }
    public int getIdUtilisateur() { return idUtilisateur; }
    public int getNbPlaces() { return nbPlaces; }
    public LocalDate getDateReservation() { return dateReservation; }
    public String getStatut() { return statut_reservation; }
    public double getPrix() { return prix; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdEvenement(int idEvenement) { this.idEvenement = idEvenement; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public void setNbPlaces(int nbPlaces) { this.nbPlaces = nbPlaces; }
    public void setDateReservation(LocalDate dateReservation) { this.dateReservation = dateReservation; }
    public void setStatut(String statut) { this.statut_reservation = statut; }
    public void setPrix(double prix) { this.prix = prix; }

    @Override
    public String toString() {
        return "ReservationEvenement{" +
                "id=" + id +
                ", idEvenement=" + idEvenement +
                ", idUtilisateur=" + idUtilisateur +
                ", nbPlaces=" + nbPlaces +
                ", dateReservation=" + dateReservation +
                ", statut='" + statut_reservation + '\'' +
                ", prix=" + prix +
                '}';
    }
}

