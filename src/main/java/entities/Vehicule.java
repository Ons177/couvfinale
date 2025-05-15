package entities;

public class Vehicule {

    private int id_vehicule;
    private Utilisateur utilisateur;
    private String marque;
    private String modele;
    private String couleur;
    private String matricule;
    private int nbre_places;

    // Constructeur avec id_vehicule (utile pour l'update par exemple)
    public Vehicule(int id_vehicule, Utilisateur utilisateur, String marque, String modele, String couleur,
                    String matricule, int nbre_places) {
        this.id_vehicule = id_vehicule;
        this.utilisateur = utilisateur;
        this.marque = marque;
        this.modele = modele;
        this.couleur = couleur;
        this.matricule = matricule;
        this.nbre_places = nbre_places;
    }

    // Constructeur sans id_vehicule (utile pour l'ajout)
    public Vehicule(Utilisateur utilisateur, String marque, String modele, String couleur,
                    String matricule, int nbre_places) {
        this.utilisateur = utilisateur;
        this.marque = marque;
        this.modele = modele;
        this.couleur = couleur;
        this.matricule = matricule;
        this.nbre_places = nbre_places;
    }

    public int getId_vehicule() {
        return id_vehicule;
    }

    public void setId_vehicule(int id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public int getNbre_places() {
        return nbre_places;
    }

    public void setNbre_places(int nbre_places) {
        this.nbre_places = nbre_places;
    }

    @Override
    public String toString() {
        return "Vehicule{" +
                "id_vehicule=" + id_vehicule +
                ", utilisateur_id=" + (utilisateur != null ? utilisateur.getId_utilisateur() : "null") +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", couleur='" + couleur + '\'' +
                ", matricule='" + matricule + '\'' +
                ", nbre_places=" + nbre_places +
                '}';
    }
}

