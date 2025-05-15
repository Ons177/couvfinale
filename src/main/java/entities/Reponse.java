package entities;

public class Reponse {
    private int idReponse;
    private int idReclamation;
    private int idUtilisateur;
    private String contenu;
    private String dateReponse;
    private String reaction; // 👍 👎 😐
    private String dateFeedbackrep;
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

    // Getters & Setters
    public int getIdReponse() { return idReponse; }
    public void setIdReponse(int idReponse) { this.idReponse = idReponse; }

    public int getIdReclamation() { return idReclamation; }
    public void setIdReclamation(int idReclamation) { this.idReclamation = idReclamation; }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getDateReponse() { return dateReponse; }
    public void setDateReponse(String dateReponse) { this.dateReponse = dateReponse; }

    public String getReaction() { return reaction; }
    public void setReaction(String reaction) { this.reaction = reaction; }

    public String getDateFeedbackrep() { return dateFeedbackrep; }
    public void setDateFeedbackrep(String dateFeedbackrep) { this.dateFeedbackrep = dateFeedbackrep; }

    public Reclamation getReclamation() { return this.currentReclamation; }

    @Override
    public String toString() {
        return "Reponse{" +
                "idReponse=" + idReponse +
                ", idReclamation=" + idReclamation +
                ", idUtilisateur=" + idUtilisateur +
                ", contenu='" + contenu + '\'' +
                ", dateReponse='" + dateReponse + '\'' +
                ", reaction='" + reaction + '\'' +
                ", dateFeedbackrep='" + dateFeedbackrep + '\'' +
                '}';
    }
}
