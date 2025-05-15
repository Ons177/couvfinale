package controllers;

import entities.Commentaire;
import entities.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import services.ServiceCommentaire;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailControllerAdmin {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox rootContainer;

    @FXML
    private ImageView postImage;

    @FXML
    private Label titleLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label contentLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button loveButton;

    @FXML
    private Button commentButton;

    @FXML
    private VBox commentSection;

    @FXML
    private HBox commentInputContainer;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button sendCommentButton;

    @FXML
    private VBox commentsContainer;

    private Post post;
    private ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private int currentUserId; // Will store the current user's ID
    private final String DEFAULT_IMAGE_PATH = "/Assets/default.png";

    // Track which comment is being edited
    private Commentaire currentEditingComment = null;
    // Map to track edit text areas for each comment
    private Map<Integer, TextArea> commentEditAreas = new HashMap<>();

    @FXML
    public void initialize() {
        // Apply custom CSS to the ScrollPane
        if (scrollPane != null) {
            scrollPane.getStyleClass().add("custom-scroll");

            // Apply CSS file (assuming the CSS file is in the resources folder)
            String cssPath = getClass().getResource("/css/postDetailStyle.css").toExternalForm();
            scrollPane.getStylesheets().add(cssPath);
        }

        // Initialize the comment input container to be hidden initially
        if (commentInputContainer != null) {
            commentInputContainer.setVisible(false);
            commentInputContainer.setManaged(false);
        }

        // Make sure comment section is visible by default
        if (commentSection != null) {
            commentSection.setVisible(true);
            commentSection.setManaged(true);
        }

        if (commentsContainer == null) {
            commentsContainer = new VBox(5);
            commentsContainer.setPadding(new Insets(5));

            if (commentSection != null) {
                commentSection.getChildren().add(commentsContainer);
            }
        }

        // Make sure comments container is visible by default
        if (commentsContainer != null) {
            commentsContainer.setVisible(true);
            commentsContainer.setManaged(true);
        }

        // Set the send comment button text appropriately
        updateSendCommentButtonText();
    }

    public void setPost(Post post) {
        this.post = post;

        // Get the current user ID from the post
        this.currentUserId = post.getUser_id();

        // Display post data
        titleLabel.setText(post.getTitre());

        // Format the date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateLabel.setText(post.getDate_post().format(formatter));

        // Full content (not truncated like in the card)
        contentLabel.setText(post.getPost_contenu());

        // Load the image
        loadPostImage();

        // Load existing comments
        loadComments();
    }

    // Method to set the current user ID explicitly if needed
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    private void loadPostImage() {
        try {
            String imagePath = post.getImage();

            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);

                if (imageFile.exists()) {
                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        postImage.setImage(new Image(fis));
                        return;
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image depuis le chemin: " + imagePath);
                        e.printStackTrace();
                    }
                } else {
                    InputStream resourceStream = getClass().getResourceAsStream(imagePath);
                    if (resourceStream != null) {
                        postImage.setImage(new Image(resourceStream));
                        return;
                    }
                }
            }

            // Default image if needed
            loadDefaultImage();

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
            e.printStackTrace();
            loadDefaultImage();
        }
    }

    private void loadDefaultImage() {
        try {
            InputStream defaultImageStream = getClass().getResourceAsStream(DEFAULT_IMAGE_PATH);
            if (defaultImageStream != null) {
                postImage.setImage(new Image(defaultImageStream));
            } else {
                System.err.println("L'image par défaut n'a pas pu être trouvée: " + DEFAULT_IMAGE_PATH);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image par défaut: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadComments() {
        try {
            // Get comments for this post
            List<Commentaire> comments = serviceCommentaire.getCommentairesByPostId(post.getPost_id());

            // Clear the comments container
            commentsContainer.getChildren().clear();

            // Display each comment
            for (Commentaire comment : comments) {
                displayComment(comment);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des commentaires: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayComment(Commentaire comment) {
        // Create a container for the comment
        VBox commentBox = new VBox(5);
        commentBox.setPadding(new Insets(5));
        commentBox.getStyleClass().add("comment-box");
        commentBox.setId("comment-" + comment.getCommentaire_id());

        // User and comment info section
        HBox commentHeader = new HBox(10);
        commentHeader.setAlignment(Pos.CENTER_LEFT);

        // You could add a real username here if available
        Label userLabel = new Label("User " + comment.getUser_id());
        userLabel.getStyleClass().add("comment-user");

        // Format the comment date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Label dateCommentLabel = new Label(comment.getDate_commentaire().format(formatter));
        dateCommentLabel.getStyleClass().add("comment-date");

        // Add spacer to push action buttons to the right
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        commentHeader.getChildren().addAll(userLabel, spacer, dateCommentLabel);

        // Comment content
        Label contentLabel = new Label(comment.getComment_contenu());
        contentLabel.getStyleClass().add("comment-content");
        contentLabel.setWrapText(true);

        // Add the main elements to the comment box
        commentBox.getChildren().addAll(commentHeader, contentLabel);

        // Create action buttons container
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        // Create delete button (circular with trash icon)
        Button deleteButton = createCircularButton("/Assets/delete.png", "delete-button");
        deleteButton.setTooltip(new Tooltip("Supprimer"));

        // Add action handler
        deleteButton.setOnAction(e -> handleDeleteComment(comment, commentBox));

        actionButtons.getChildren().add(deleteButton);

        // Add action buttons to the comment box
        commentBox.getChildren().add(actionButtons);

        // Add the comment to the comments container
        commentsContainer.getChildren().add(commentBox);
    }

    private Button createCircularButton(String iconPath, String styleClass) {
        Button button = new Button();
        button.getStyleClass().add(styleClass);

        // Set the size of the button
        button.setPrefSize(30, 30);
        button.setMinSize(30, 30);
        button.setMaxSize(30, 30);

        try {
            // Load the icon
            InputStream iconStream = getClass().getResourceAsStream(iconPath);
            if (iconStream != null) {
                ImageView icon = new ImageView(new Image(iconStream));
                icon.setFitHeight(16);
                icon.setFitWidth(16);
                button.setGraphic(icon);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'icône: " + e.getMessage());
            button.setText(styleClass.equals("delete-button") ? "X" : "E");
        }

        // Make it circular with CSS
        button.setStyle("-fx-background-radius: 15; -fx-min-width: 30; -fx-min-height: 30; -fx-max-width: 30; -fx-max-height: 30;");

        return button;
    }

    private void handleDeleteComment(Commentaire comment, VBox commentBox) {
        // Create a confirmation dialog (using Alert instead of Popup for better consistency)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le commentaire");
        alert.setContentText("Voulez-vous vraiment supprimer ce commentaire ?");

        // Customize the buttons
        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Custom styling (optional)
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/postDetailStyle.css").toExternalForm());
        dialogPane.getStyleClass().add("delete-dialog");

        // Show the dialog and process the result
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                try {
                    // Delete from database
                    serviceCommentaire.supprimer(comment);

                    // Remove from UI
                    commentsContainer.getChildren().remove(commentBox);

                    // Show confirmation (optional)
                    showNotification("Succès", "Le commentaire a été supprimé avec succès.");

                } catch (SQLException ex) {
                    System.err.println("Erreur lors de la suppression du commentaire: " + ex.getMessage());
                    ex.printStackTrace();

                    // Show error message
                    showNotification("Erreur", "Impossible de supprimer le commentaire: " + ex.getMessage(), true);
                }
            }
        });
    }

    private void showNotification(String title, String message) {
        showNotification(title, message, false);
    }

    private void showNotification(String title, String message, boolean isError) {
        Alert alert = isError ? new Alert(Alert.AlertType.ERROR) : new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void handleEditComment(Commentaire comment, Label contentLabel, VBox commentBox) {
        // Check if we're already editing this comment
        if (commentEditAreas.containsKey(comment.getCommentaire_id())) {
            // We're already editing, so cancel the edit
            TextArea editArea = commentEditAreas.get(comment.getCommentaire_id());
            int indexOfEditArea = commentBox.getChildren().indexOf(editArea);
            if (indexOfEditArea >= 0) {
                commentBox.getChildren().remove(indexOfEditArea);
                commentBox.getChildren().add(indexOfEditArea, contentLabel);
            }
            commentEditAreas.remove(comment.getCommentaire_id());
            currentEditingComment = null;
            updateSendCommentButtonText();
            return;
        }

        // Set this as the current editing comment
        currentEditingComment = comment;

        // Create a text area with the current comment content
        TextArea editArea = new TextArea(comment.getComment_contenu());
        editArea.setWrapText(true);
        editArea.setPrefHeight(60);

        // Store this edit area
        commentEditAreas.put(comment.getCommentaire_id(), editArea);

        // Replace the content label with the edit area
        int indexOfContentLabel = commentBox.getChildren().indexOf(contentLabel);
        if (indexOfContentLabel >= 0) {
            commentBox.getChildren().remove(indexOfContentLabel);
            commentBox.getChildren().add(indexOfContentLabel, editArea);
        }

        // Create save button for the edit
        Button saveButton = new Button("Enregistrer");
        saveButton.getStyleClass().add("save-button");
        saveButton.setOnAction(e -> {
            try {
                // Get the updated text
                String updatedText = editArea.getText().trim();
                if (!updatedText.isEmpty()) {
                    // Update the comment object
                    comment.setComment_contenu(updatedText);
                    comment.setDate_commentaire(LocalDateTime.now()); // Update timestamp

                    // Update in database
                    serviceCommentaire.modifier(comment);

                    // Update the display
                    contentLabel.setText(updatedText);

                    // Switch back to normal view
                    int idx = commentBox.getChildren().indexOf(editArea);
                    if (idx >= 0) {
                        commentBox.getChildren().remove(idx);
                        commentBox.getChildren().add(idx, contentLabel);
                    }

                    // Remove the save button if it exists
                    commentBox.getChildren().removeIf(node -> node instanceof Button &&
                            ((Button) node).getText().equals("Enregistrer"));

                    // Reset editing state
                    commentEditAreas.remove(comment.getCommentaire_id());
                    currentEditingComment = null;
                }
            } catch (SQLException ex) {
                System.err.println("Erreur lors de la modification du commentaire: " + ex.getMessage());
                ex.printStackTrace();

                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de modification");
                alert.setContentText("Impossible de modifier le commentaire: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        // Add the save button to the comment box
        commentBox.getChildren().add(saveButton);

        // Focus on the edit area
        editArea.requestFocus();
    }

    private void updateSendCommentButtonText() {
        if (currentEditingComment != null) {
            sendCommentButton.setText("Mettre à jour");
        } else {
            sendCommentButton.setText("Envoyer");
        }
    }

    @FXML
    public void handleBackButton() {
        try {
            // Find the content_area in the SideNavBar
            Pane contentArea = findContentArea();

            if (contentArea != null) {
                // Load the post list view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/ListPostsAdmin.fxml"));
                Parent root = loader.load();

                // Clear content_area and add the post list
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } else {
                System.err.println("Impossible de trouver le content_area dans le SideNavBar");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du retour à la liste des posts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to find the Pane content_area in the parent hierarchy
    private Pane findContentArea() {
        // Go up in the parent hierarchy to find content_area
        Parent parent = rootContainer.getParent();
        while (parent != null) {
            // Check if a parent contains a node named "content_area"
            if (parent.lookup("#content_area") instanceof Pane) {
                return (Pane) parent.lookup("#content_area");
            }
            // Continue up in the hierarchy
            if (parent.getParent() instanceof Parent) {
                parent = parent.getParent();
            } else {
                break;
            }
        }
        return null;
    }

    @FXML
    public void handleLoveButton() {
        // Implementation for the "Love" button
        System.out.println("Post " + post.getPost_id() + " loved by user " + currentUserId);
        loveButton.setStyle("-fx-background-color: #ff6b6b;");
    }

    @FXML
    public void handleCommentButton() {
        // Reset the current editing comment
        currentEditingComment = null;
        updateSendCommentButtonText();

        // Clear the comment area
        commentArea.clear();

        // Toggle only the comment input container visibility
        boolean isVisible = commentInputContainer.isVisible();
        commentInputContainer.setVisible(!isVisible);
        commentInputContainer.setManaged(!isVisible);

        if (!isVisible) {
            // Show comment area and focus on it
            commentArea.requestFocus();
        }
    }

    @FXML
    public void handleSendComment() {
        String commentText = commentArea.getText().trim();

        if (commentText.isEmpty()) {
            return;
        }

        try {
            // Only handle adding new comments here
            // (editing is now handled directly in the handleEditComment method)
            if (post != null) {
                // Create and add a new comment
                Commentaire newComment = new Commentaire(
                        0, // ID will be generated by the database
                        post.getPost_id(),
                        commentText,
                        currentUserId, // Use the current user ID
                        LocalDateTime.now()
                );

                // Add to database
                serviceCommentaire.ajouter(newComment);

                // Show confirmation
                showNotification("Succès", "Votre commentaire a été ajouté.");

                // Add the comment to the UI
                displayComment(newComment);

                // Clear the text area
                commentArea.clear();

                // Hide the comment input area
                commentInputContainer.setVisible(false);
                commentInputContainer.setManaged(false);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du commentaire: " + e.getMessage());
            e.printStackTrace();

            // Show error message
            showNotification("Erreur", "Impossible d'ajouter le commentaire: " + e.getMessage(), true);
        }
    }
}