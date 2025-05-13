package entities;


public class Reponse {
    private int idReponse;
    private int idReclamation;
    private int idUtilisateur;
    private String contenu;
    private String dateReponse;
    private Reclamation reclamation;
    private Reclamation currentReclamation;

    public Reponse(int idReponse, int idReclamation, int idUtilisateur, String contenu, String dateReponse) {
        this.idReponse = idReponse;
        this.idReclamation = idReclamation;
        this.idUtilisateur = idUtilisateur;
        this.contenu = contenu;
        this.dateReponse = dateReponse;
    }

    public Reponse(int idReclamation, int idUtilisateur, String contenu, String dateReponse) {
        this.idReclamation = idReclamation;
        this.idUtilisateur = idUtilisateur;
        this.contenu = contenu;
        this.dateReponse = dateReponse;
    }

    public int getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(int idReponse) {
        this.idReponse = idReponse;
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

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(String dateReponse) {
        this.dateReponse = dateReponse;
    }



    @Override
    public String toString() {
        return "Reponse{" +
                "idReponse=" + idReponse +
                ", idReclamation=" + idReclamation +
                ", idUtilisateur=" + idUtilisateur +
                ", contenu='" + contenu + '\'' +
                ", dateReponse='" + dateReponse + '\'' +
                '}';
    }

    public Reclamation getReclamation() {
        return this.currentReclamation;
    }
}
