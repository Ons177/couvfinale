package Controllers;

import services.ServicePost;
import entities.Post;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListPostsAdmin {
    @FXML
    private FlowPane postsFlowPane;

    private ServicePost servicePost = new ServicePost();

    @FXML
    public void initialize() {
        // Load posts when the view is initialized
        Platform.runLater(this::loadPosts);
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin/PostCardAdmin.fxml"));
                    VBox postCardView = loader.load();

                    // Get controller and set post data
                    PostCardAdmin controller = loader.getController();
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
}
