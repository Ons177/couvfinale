package Controllers;

import services.ServicePost;
import entities.Post;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListPosts {

    @FXML
    private FlowPane postsFlowPane;
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private Button addButton;

    private ServicePost servicePost = new ServicePost();
    @FXML
    public void initialize() {
        // Load posts when the view is initialized
        Platform.runLater(this::loadPosts);

        // Configurer le bouton d'ajout si disponible
        if (addButton != null) {
            addButton.setOnAction(event -> handleAddArticle());
        }
    }


    private void loadPosts() {
        // Clear existing posts
        postsFlowPane.getChildren().clear();

        try {
            // Get posts from database
            List<Post> posts = servicePost.recuperer();

            // Create a card for each post
            for (Post post : posts) {
                try {
                    // Load the post card template
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PostCard.fxml"));
                    VBox postCardView = loader.load();

                    // Get controller and set post data
                    PostCardController controller = loader.getController();
                    controller.setPost(post);

                    // Add card to flow pane
                    postsFlowPane.getChildren().add(postCardView);
                } catch (IOException e) {
                    System.err.println("Error loading post card: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // If no posts found, display a message
            if (posts.isEmpty()) {
                System.out.println("No posts found in database");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddArticle() {
        try {
            // Chargement de la vue d'ajout d'article
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddPosts.fxml"));
            AnchorPane addPostsView = loader.load();

            // Récupérer le contrôleur et lui passer une référence à cette classe
            AddPosts addPostsController = loader.getController();
            addPostsController.setListPostsController(this);

            // Récupérer la référence au conteneur parent (qui contient la vue actuelle)
            Pane parentContainer = (Pane) mainContainer.getParent();

            // Remplacer le contenu actuel par la vue d'ajout
            if (parentContainer != null) {
                // Supprimer la vue actuelle
                parentContainer.getChildren().remove(mainContainer);

                // Ajouter la vue d'ajout au même endroit
                parentContainer.getChildren().add(addPostsView);

                // Configurer les contraintes AnchorPane pour le nouveau contenu
                AnchorPane.setTopAnchor(addPostsView, 0.0);
                AnchorPane.setRightAnchor(addPostsView, 0.0);
                AnchorPane.setBottomAnchor(addPostsView, 0.0);
                AnchorPane.setLeftAnchor(addPostsView, 0.0);
            } else {
                // Fallback: remplacer toute la scène (moins recommandé)
                Scene scene = mainContainer.getScene();
                if (scene != null) {
                    scene.setRoot(addPostsView);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement de l'interface d'ajout: " + e.getMessage()).show();
        }
    }

    // Méthode pour revenir à la liste des articles (utilisée par AddPosts)
    public void refreshAndShowList() {
        try {
            // Rafraîchir la liste des posts
            loadPosts();

            // Si la vue a été remplacée et qu'on veut restaurer cette vue
            Pane parentContainer = (Pane) mainContainer.getParent();

            // Si mainContainer est détaché de la hiérarchie, recharger la vue depuis zéro
            if (parentContainer == null) {
                // L'AnchorPane n'est pas attaché à un parent, donc nous devons vérifier si la scène existe
                Scene scene = mainContainer.getScene();

                if (scene != null) {
                    // Si la scène existe, on peut utiliser son root
                    parentContainer = (Pane) scene.getRoot();

                    // Supprimer la vue d'ajout
                    parentContainer.getChildren().clear();

                    // Ajouter cette vue
                    parentContainer.getChildren().add(mainContainer);

                    // Configurer les contraintes AnchorPane
                    AnchorPane.setTopAnchor(mainContainer, 0.0);
                    AnchorPane.setRightAnchor(mainContainer, 0.0);
                    AnchorPane.setBottomAnchor(mainContainer, 0.0);
                    AnchorPane.setLeftAnchor(mainContainer, 0.0);
                } else {
                    // La scène est null, il faut recharger toute la vue ListPosts
                    reloadListPostsView();
                }
            }
        } catch (Exception e) {
            System.err.println("Error in refreshAndShowList: " + e.getMessage());
            e.printStackTrace();

            // En cas d'erreur, on essaie de recharger la vue complètement
            reloadListPostsView();
        }
    }

    // Méthode pour recharger complètement la vue ListPosts en cas de problème
    private void reloadListPostsView() {
        try {
            // Charger la vue ListPosts depuis le FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ListPosts.fxml"));
            AnchorPane newListPostsView = loader.load();

            // Obtenir la scène actuelle ou créer une nouvelle si nécessaire
            Scene currentScene = null;
            Stage stage = null;

            // Essayer d'obtenir la scène depuis mainContainer
            if (mainContainer.getScene() != null) {
                currentScene = mainContainer.getScene();
                stage = (Stage) currentScene.getWindow();
            }

            // Si on a trouvé une scène, on peut remplacer son contenu
            if (stage != null) {
                currentScene.setRoot(newListPostsView);
            } else {
                // Si on ne trouve pas de scène, afficher un message d'erreur
                System.err.println("Could not find Scene or Stage to reload view");
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors du rechargement de la liste des articles: " + e.getMessage()).show();
        }
    }
}