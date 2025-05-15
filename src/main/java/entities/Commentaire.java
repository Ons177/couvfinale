package entities;

import java.time.LocalDateTime;


public class Commentaire {
    private int commentaire_id;
    private int post_id;
    private String comment_contenu;
    private LocalDateTime date_comment;
    private int user_id;
    public Commentaire() {}

    public Commentaire(int commentaire_id, int post_id, String comment_contenu, int user_id, LocalDateTime date_comment) {
        this.commentaire_id = commentaire_id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.comment_contenu = comment_contenu;
        this.date_comment = date_comment;
    }
    public Commentaire(int post_id, String comment_contenu, LocalDateTime date_comment) {
        this.post_id = post_id;
        this.comment_contenu = comment_contenu;
        this.date_comment = date_comment;
    }

    public Commentaire(int commentaire_id, int post_id, String comment_contenu, LocalDateTime date_comment) {
        this.commentaire_id = commentaire_id;
        this.post_id = post_id;
        this.comment_contenu = comment_contenu;
        this.date_comment = date_comment;
    }

    public int getCommentaire_id() {
        return commentaire_id;
    }

    public void setCommentaire_id(int comment_id) {
        this.commentaire_id = comment_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getComment_contenu() {
        return comment_contenu;
    }

    public void setComment_contenu(String comment_contenu) {
        this.comment_contenu = comment_contenu;
    }

    public LocalDateTime getDate_commentaire() {
        return date_comment;
    }

    public void setDate_commentaire(LocalDateTime date_comment) {
        this.date_comment = date_comment;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "commentaire_id=" + commentaire_id +
                ", post_id=" + post_id +
                ", user_id=" + user_id +
                ", comment_contenu='" + comment_contenu + '\'' +
                ", date_commentaire=" + date_comment +
                '}';
    }
}
