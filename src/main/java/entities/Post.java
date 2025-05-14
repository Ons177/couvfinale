package entities;

import java.time.LocalDateTime;

public class Post {
    private int post_id;
    private String titre;
    private String post_contenu;
    private LocalDateTime date_post;
    private int user_id;
    private String image;
    public Post() {}

    public Post(String titre, String post_contenu, LocalDateTime date_post) {
        this.titre = titre;
        this.post_contenu = post_contenu;
        this.date_post = date_post;
    }
    public Post(int post_id, String titre, String post_contenu, LocalDateTime date_post, int user_id, String image) {
        this.post_id = post_id;
        this.titre = titre;
        this.post_contenu = post_contenu;
        this.date_post = date_post;
        this.user_id = user_id;
        this.image = image;
    }
    public Post(String titre, String post_contenu, LocalDateTime date_post, int user_id, String image) {
        this.titre = titre;
        this.post_contenu = post_contenu;
        this.date_post = date_post;
        this.user_id = user_id;
        this.image = image;
    }
    public Post(int post_id, String titre, String post_contenu, LocalDateTime date_post) {
        this.post_id = post_id;
        this.titre = titre;
        this.post_contenu = post_contenu;
        this.date_post = date_post;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPost_contenu() {
        return post_contenu;
    }

    public void setPost_contenu(String post_contenu) {
        this.post_contenu = post_contenu;
    }

    public LocalDateTime getDate_post() {
        return date_post;
    }

    public void setDate_post(LocalDateTime date_post) {
        this.date_post = date_post;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + post_id +
                ", titre='" + titre + '\'' +
                ", post_contenu='" + post_contenu + '\'' +
                ", date_post=" + date_post +
                ", user_id=" + user_id +
                ", image='" + image + '\'' +
                '}';
    }
}
