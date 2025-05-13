package entities;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private String nomPassager;
    private String prenomPassager;
    private String email;
    private String telephone;
    private int nombrePlaces;
    private Trajet trajet;
    private int id_utilisateur;
    private float prixTotal;

    public Reservation() {
    }

    public Reservation(String nomPassager, String prenomPassager, String email, String telephone,
                       int nombrePlaces, Trajet trajet, int id_utilisateur, float prixTotal) {
        this.nomPassager = nomPassager;
        this.prenomPassager = prenomPassager;
        this.email = email;
        this.telephone = telephone;
        this.nombrePlaces = nombrePlaces;
        this.trajet = trajet;
        this.id_utilisateur = id_utilisateur;
        this.prixTotal = prixTotal;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomPassager() {
        return nomPassager;
    }

    public void setNomPassager(String nomPassager) {
        this.nomPassager = nomPassager;
    }

    public String getPrenomPassager() {
        return prenomPassager;
    }

    public void setPrenomPassager(String prenomPassager) {
        this.prenomPassager = prenomPassager;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public float getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(float prixTotal) {
        this.prixTotal = prixTotal;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", nomPassager='" + nomPassager + '\'' +
                ", prenomPassager='" + prenomPassager + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", nombrePlaces=" + nombrePlaces +
                ", trajet=" + trajet +
                ", prixTotal=" + prixTotal +
                '}';
    }
}