package tests;

import entities.Post;
import entities.Commentaire;
import services.ServicePost;
import services.ServiceCommentaire;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MainPost {
    public static void main(String[] args) {
        ServicePost sp = new ServicePost();
        ServiceCommentaire sc = new ServiceCommentaire();

        try {

            Post p = new Post("Trajet vers Esprit", "Je propose un trajet tous les matins", LocalDateTime.now());
            sp.ajouter(p);


            List<Post> posts = sp.recuperer();
            for (int i = 0; i < posts.size(); i++) {
                System.out.println(posts.get(i));
            }


            if (posts.size() > 0) {
                Post postModif = posts.get(0);
                postModif.setTitre("Titre modifié");
                postModif.setPost_contenu("Contenu modifié");
                sp.modifier(postModif);
            }


            if (posts.size() > 0) {
                Post postSupp = posts.get(posts.size() - 1);
                sp.supprimer(postSupp);
            }


            if (posts.size() > 0) {
                int postId = posts.get(0).getPost_id();
                Commentaire c = new Commentaire(postId, "Super idée ! Je suis intéressé", LocalDateTime.now());
                sc.ajouter(c);
            }


            List<Commentaire> commentaires = sc.recuperer();
            for (int i = 0; i < commentaires.size(); i++) {
                System.out.println(commentaires.get(i));
            }


            if (commentaires.size() > 0) {
                Commentaire comModif = commentaires.get(0);
                comModif.setComment_contenu("Message modifié !");
                sc.modifier(comModif);
            }


            if (commentaires.size() > 0) {
                Commentaire comSupp = commentaires.get(commentaires.size() - 1);
                sc.supprimer(comSupp);
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }
}
