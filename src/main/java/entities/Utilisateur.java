package entities;

import java.sql.Date;

public class Utilisateur {
    private int id_utilisateur;
    private String nom, prenom, email, mdp, telephone;
    private String role;
    private Date date_naissance;
    private String cin;
    private String permis;
    private boolean isApproved;

    public Utilisateur() {
        // Initialiser les champs avec des valeurs par défaut si nécessaire
    }

    public Utilisateur(String nom, String prenom, String email, String mdp, String telephone, String role, Date date_naissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.telephone = telephone;
        this.role = role;
        this.date_naissance = date_naissance;

    }




    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(Date date_naissance) {
        this.date_naissance = date_naissance;
    }


    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
    public Utilisateur(String nom, String prenom, String email, String mdp, String telephone, String role, Date date_naissance, String cin, String permis) {
        this(nom, prenom, email, mdp, telephone, role, date_naissance);
        this.cin = cin;
        this.permis = permis;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id_utilisateur=" + id_utilisateur +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", role='" + role + '\'' +
                ", date_naissance=" + date_naissance +
                ", isApproved=" + isApproved +
                '}';
    }

    private Vehicule vehicule;

    // Autres attributs et méthodes...

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
    public String getCin() {
    return cin;}

    public void setCin(String cin){
    this.cin = cin;
   }
   public String getPermis() {
        return permis;
   }
   public void setPermis(String permis) {
        this.permis = permis;
   }
}
