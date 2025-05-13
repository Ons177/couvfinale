package entities;

public class feedbackreponserec {
    private int idFeedbackreponserec;
    private int idReponse;
    private String reaction; // "helpful", "not_helpful", "neutral"
    private String dateFeedbackrep;

    // Default constructor
    public feedbackreponserec() {
    }

    // Parameterized constructor
    public feedbackreponserec(int idFeedbackreponserec, int idReponse, String reaction, String dateFeedbackrep) {
        this.idFeedbackreponserec = idFeedbackreponserec;
        this.idReponse = idReponse;
        this.reaction = reaction;
        this.dateFeedbackrep = dateFeedbackrep;
    }

    // Getters
    public int getIdFeedbackreponserec() {
        return idFeedbackreponserec;
    }

    public int getIdReponse() {
        return idReponse;
    }

    public String getReaction() {
        return reaction;
    }

    public String getDateFeedbackrep() {
        return dateFeedbackrep;
    }

    // Setters
    public void setIdFeedbackreponserec(int idFeedbackreponserec) {
        this.idFeedbackreponserec = idFeedbackreponserec;
    }

    public void setIdReponse(int idReponse) {
        this.idReponse = idReponse;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public void setDateFeedbackrep(String dateFeedbackrep) {
        this.dateFeedbackrep = dateFeedbackrep;
    }

    // toString method
    @Override
    public String toString() {
        return "feedbackreponserec{" +
                "idFeedbackreponserec=" + idFeedbackreponserec +
                ", idReclamation=" + idReponse +
                ", reaction='" + reaction + '\'' +
                ", dateFeedbackrep='" + dateFeedbackrep + '\'' +
                '}';
    }
}
