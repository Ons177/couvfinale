package entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Trajet {

    private int id_trajet;
    private float prix;
    private LocalDate date_depart;
    private LocalTime heure_depart;
    private  String ville_depart;
    private  String ville_arrivee;
    private int nbr_places;
    private String bagage;

    // Constructeur par défaut
    public Trajet() {
    }

    public Trajet(int id_trajet, float prix, LocalDate date_depart, LocalTime heure_depart,
                  String ville_depart, String ville_arrivee, int nbr_places, String bagage) {
        this.id_trajet = id_trajet;
        this.prix = prix;
        this.date_depart = date_depart;
        this.heure_depart = heure_depart;
        this.ville_depart = ville_depart;
        this.ville_arrivee = ville_arrivee;
        this.nbr_places = nbr_places;
        this.bagage = bagage;
    }

    public Trajet(float prix, LocalDate date_depart, LocalTime heure_depart,
                  String ville_depart, String ville_arrivee, int nbr_places, String bagage) {
        this.prix = prix;
        this.date_depart = date_depart;
        this.heure_depart = heure_depart;
        this.ville_depart = ville_depart;
        this.ville_arrivee = ville_arrivee;
        this.nbr_places = nbr_places;
        this.bagage = bagage;
    }

    public int getIdTrajet() { return id_trajet; }
    public void setIdTrajet(int id_trajet) { this.id_trajet = id_trajet; }
    public float getPrix() { return prix; }
    public void setPrix(float prix) { this.prix = prix; }
    public LocalDate getDateDepart() { return date_depart; }
    public void setDateDepart(LocalDate dateDepart) { this.date_depart = dateDepart; }
    public LocalTime getHeureDepart() { return heure_depart; }
    public void setHeureDepart(LocalTime heureDepart) { this.heure_depart = heureDepart; }
    public String getVilleDepart() { return ville_depart; }
    public void setVilleDepart(String villeDepart) { this.ville_depart = villeDepart; }
    public String getVilleArrivee() { return ville_arrivee; }
    public void setVilleArrivee(String villeArrivee) { this.ville_arrivee = villeArrivee; }
    public int getNbrPlaces() { return nbr_places; }
    public void setNbrPlaces(int nbrPlaces) { this.nbr_places = nbrPlaces; }
    public String getBagage() { return bagage; }
    public void setBagage(String bagage) { this.bagage = bagage; }

    @Override
    public String toString() {
        return "Trajet{" +
                "id_trajet=" + id_trajet +
                ", prix=" + prix +
                ", dateDepart=" + date_depart +
                ", heureDepart=" + heure_depart +
                ", villeDepart='" + ville_depart + '\'' +
                ", villeArrivee='" + ville_arrivee + '\'' +
                ", nbrPlaces=" + nbr_places +
                ", bagage='" + bagage + '\'' +
                '}';
    }
}