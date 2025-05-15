package entities;


public class Reclamation {
    private int idReclamation;
    private int idUtilisateur;
    private String typeReclamation;
    private String description;
    private String dateCreation;
    private String statut;
    private String priorite;
    private String contenuReponse;
    private String dateReponse;

    public Reclamation(int idReclamation, int idUtilisateur, String typeReclamation,
                       String description, String dateCreation, String statut, String priorite) {
        this.idReclamation = idReclamation;
        this.idUtilisateur = idUtilisateur;
        this.typeReclamation = typeReclamation;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.priorite = priorite;
    }

    public Reclamation(int idUtilisateur, String typeReclamation, String description,
                       String dateCreation, String statut, String priorite) {
        this.idUtilisateur = idUtilisateur;
        this.typeReclamation = typeReclamation;
        this.description = description;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.priorite = priorite;
    }

    public int getIdReclamation() {
        return idReclamation;
    }

    public void setIdReclamation(int idReclamation) {
        this.idReclamation = idReclamation;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getTypeReclamation() {
        return typeReclamation;
    }

    public void setTypeReclamation(String typeReclamation) {
        this.typeReclamation = typeReclamation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getContenuReponse() {
        return contenuReponse;
    }

    public void setContenuReponse(String contenuReponse) {
        this.contenuReponse = contenuReponse;
    }

    public String getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(String dateReponse) {
        this.dateReponse = dateReponse;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "idReclamation=" + idReclamation +
                ", idUtilisateur=" + idUtilisateur +
                ", typeReclamation='" + typeReclamation + '\'' +
                ", description='" + description + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", statut='" + statut + '\'' +
                ", priorite='" + priorite + '\'' +
                ", contenuReponse='" + contenuReponse + '\'' +
                ", dateReponse='" + dateReponse + '\'' +
                '}';
    }

}
