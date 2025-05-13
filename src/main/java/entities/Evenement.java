package entities;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class Evenement {
    private int idEvenement;
    private String titre;
    private String description;
    private String lieu;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDateTime heure;
    private int idCreateur;
    private String typeEvenement;

    public Evenement(int idEvenement, String titre, String description, String lieu, LocalDate dateDebut,
                     LocalDate dateFin, LocalDateTime heure, int idCreateur, String typeEvenement) {
        this.idEvenement = idEvenement;
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heure = heure;
        this.idCreateur = idCreateur;
        this.typeEvenement = typeEvenement;
    }

    public Evenement(String titre, String description, String lieu, LocalDate dateDebut,
                     LocalDate dateFin, LocalDateTime heure, int idCreateur, String typeEvenement) {
        this.idEvenement = 0; // Initialize to 0 for new events
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heure = heure;
        this.idCreateur = idCreateur;
        this.typeEvenement = typeEvenement;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDateTime getHeure() {
        return heure;
    }

    public void setHeure(LocalDateTime heure) {
        this.heure = heure;
    }

    public int getIdCreateur() {
        return idCreateur;
    }

    public void setIdCreateur(int idCreateur) {
        this.idCreateur = idCreateur;
    }

    public String getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(String typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "idEvenement=" + idEvenement +
                ", titre='" + titre + '\'' +
                ", lieu='" + lieu + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", heure=" + heure +
                ", type='" + typeEvenement + '\'' +
                '}';
    }
}

