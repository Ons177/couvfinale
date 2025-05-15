package controllers;

import entities.Commentaire;
import entities.Post;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import services.ServiceCommentaire;
import services.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PostCardAdmin {
    @FXML
    private VBox postCard;

    @FXML
    private ImageView postImage;

    @FXML
    private Label titleLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button commentButton;

    @FXML
    private VBox commentSection;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button sendCommentButton;

    @FXML
    private VBox commentsContainer;

    @FXML
    private Button viewCommentsButton;
    private Utilisateur currentuser = UserSession.getCurrentUser();
    private Post post;
    private ServiceCommentaire serviceCommentaire = new ServiceCommentaire();


    private final String DEFAULT_IMAGE_PATH = "/Assets/default.png";
    private boolean commentsVisible = false;

    @FXML
    public void initialize() {
        // Ajouter un gestionnaire d'événement de clic sur la carte entière
        postCard.setOnMouseClicked(this::handlePostCardClick);
    }

    public void setPost(Post post) {
        this.post = post;

        // Set post data to UI components
        titleLabel.setText(post.getTitre());
        // Format date from LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateLabel.setText(post.getDate_post().format(formatter));

        // Set description (limiting to first 100 characters if needed)
        String content = post.getPost_contenu();
        if (content.length() > 100) {
            content = content.substring(0, 97) + "...";
        }
        descriptionLabel.setText(content);

        // Charger l'image du post ou l'image par défaut
        loadPostImage();

        // S'assurer que la section de commentaire est cachée au départ
        commentSection.setVisible(false);
        commentSection.setManaged(false);

        // S'assurer que le container de commentaires est initialement vide
        if (commentsContainer != null) {
            commentsContainer.getChildren().clear();
        }
    }

    private void loadPostImage() {
        try {
            String imagePath = post.getImage();

            if (imagePath != null && !imagePath.isEmpty()) {
                // Vérifier si l'image est un chemin absolu ou relatif
                File imageFile = new File(imagePath);

                if (imageFile.exists()) {
                    // Si c'est un chemin de fichier valide, charger l'image
                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        postImage.setImage(new Image(fis));
                        return; // Si l'image est chargée avec succès, sortir de la méthode
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image depuis le chemin: " + imagePath);
                        e.printStackTrace();
                    }
                } else {
                    // Essayer de charger l'image depuis les ressources
                    InputStream resourceStream = getClass().getResourceAsStream(imagePath);
                    if (resourceStream != null) {
                        postImage.setImage(new Image(resourceStream));
                        return; // Si l'image est chargée avec succès, sortir de la méthode
                    }
                }
            }

            // Si l'image n'a pas pu être chargée ou est NULL, utiliser l'image par défaut
            loadDefaultImage();

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
            e.printStackTrace();
            loadDefaultImage();
        }
    }

    private void loadDefaultImage() {
        try {
            // Charger l'image par défaut depuis les ressources
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

    @FXML
    public void handleCommentButton() {
        // Afficher/masquer la section commentaire
        boolean isVisible = commentSection.isVisible();
        commentSection.setVisible(!isVisible);
        commentSection.setManaged(!isVisible);

        if (!isVisible) {
            // Montrer la zone de commentaire et mettre le focus dessus
            commentArea.requestFocus();
            commentArea.clear(); // Effacer tout texte précédent

            // Charger les commentaires existants
            loadComments();
        }
    }

    // Charger les commentaires depuis la base de données
    private void loadComments() {
        try {
            List<Commentaire> comments = serviceCommentaire.getCommentairesByPostId(post.getPost_id());

            // Vider le container de commentaires
            commentsContainer.getChildren().clear();

            // Ajouter chaque commentaire au container
            for (Commentaire comment : comments) {
                displayComment(comment);
            }

            // Définir la visibilité des commentaires selon l'état actuel
            commentsContainer.setVisible(commentsVisible);
            commentsContainer.setManaged(commentsVisible);

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des commentaires: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleViewComments() {
        // Inverser l'état de visibilité des commentaires
        commentsVisible = !commentsVisible;

        // Mettre à jour le texte du bouton
        viewCommentsButton.setText(commentsVisible ? "Hide Comments" : "View All Comments");

        // Afficher ou masquer les commentaires
        commentsContainer.setVisible(commentsVisible);
        commentsContainer.setManaged(commentsVisible);
    }

    // Méthode pour gérer le clic sur la carte de publication
    private void handlePostCardClick(MouseEvent event) {
        // Empêcher le traitement de l'événement si le clic était sur un bouton ou la zone de commentaire
        if (event.getTarget() instanceof Button ||
                event.getTarget() instanceof TextArea ||
                commentSection.isVisible()) {
            return;
        }

        // Ouvrir la vue détaillée de la publication
        openPostDetail();
    }

    // Méthode pour ouvrir la vue détaillée de la publication dans le content_area du SideNavBar
    private void openPostDetail() {
        try {
            // Trouver le Pane content_area dans le SideNavBar
            Pane contentArea = findContentArea();

            if (contentArea != null) {
                // Charger le fichier FXML pour PostDetail
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PostDetail.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de PostDetail et lui passer l'objet Post
                PostDetailControllerAdmin detailController = loader.getController();
                detailController.setPost(post);

                // Effacer le contenu actuel et ajouter la vue détaillée
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } else {
                System.err.println("Impossible de trouver le content_area dans le SideNavBar");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la vue détaillée: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour trouver le Pane content_area dans la hiérarchie des parents
    private Pane findContentArea() {
        // Remonter dans la hiérarchie des parents pour trouver le content_area
        Parent parent = postCard.getParent();
        while (parent != null) {
            // Vérifier si un parent contient un nœud nommé "content_area"
            if (parent.lookup("#content_area") instanceof Pane) {
                return (Pane) parent.lookup("#content_area");
            }
            // Continuer à remonter dans la hiérarchie
            if (parent.getParent() instanceof Parent) {
                parent = parent.getParent();
            } else {
                break;
            }
        }
        return null;
    }

    // Méthode pour afficher un commentaire dans l'UI avec un bouton de suppression
    private void displayComment(Commentaire comment) {
        String nom = "";
        // Création d'un conteneur pour le commentaire
        HBox commentBox = new HBox(10);
        commentBox.setPadding(new Insets(5));
        commentBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        // Informations du commentaire
        VBox commentInfo = new VBox(3);
        HBox.setHgrow(commentInfo, Priority.ALWAYS);

            UserService userservice=new UserService();
            Utilisateur user = userservice. getById(comment.getUser_id());
            nom = user.getNom();


        // Vous pourriez ajouter un nom d'utilisateur réel ici si disponible
        Label userLabel = new Label("User " + nom );
        userLabel.setFont(Font.font("System", 12));
        userLabel.setTextFill(Color.BLUE);

        // Le contenu du commentaire
        Label contentLabel = new Label(comment.getComment_contenu());
        contentLabel.setWrapText(true);

        // Formater la date du commentaire
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Label dateCommentLabel = new Label(comment.getDate_commentaire().format(formatter));
        dateCommentLabel.setFont(Font.font("System", 10));
        dateCommentLabel.setTextFill(Color.GRAY);

        // Ajouter tous les éléments au conteneur de commentaire
        commentInfo.getChildren().addAll(userLabel, contentLabel, dateCommentLabel);

        // Créer un bouton de suppression
        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setPrefSize(24, 24);
        deleteButton.setMinSize(24, 24);
        deleteButton.setMaxSize(24, 24);

        // Ajouter une action pour supprimer le commentaire
        deleteButton.setOnAction(e -> deleteComment(comment, commentBox));

        // Créer un conteneur pour aligner le bouton verticalement
        VBox deleteButtonContainer = new VBox(deleteButton);
        deleteButtonContainer.setAlignment(Pos.TOP_RIGHT);

        // Ajouter les éléments au conteneur de commentaire
        commentBox.getChildren().addAll(commentInfo, deleteButtonContainer);

        // Ajouter l'ID du commentaire comme propriété utilisateur du HBox pour faciliter la suppression
        commentBox.setUserData(comment.getCommentaire_id());

        // Ajouter le commentaire au conteneur de commentaires
        commentsContainer.getChildren().add(commentBox);
    }

    // Méthode pour supprimer un commentaire
    private void deleteComment(Commentaire comment, HBox commentBox) {
        try {
            // Demander confirmation avant de supprimer
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le commentaire");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce commentaire ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    try {
                        // Supprimer le commentaire de la base de données
                        serviceCommentaire.supprimer(comment.getCommentaire_id());

                        // Supprimer le commentaire de l'interface utilisateur
                        commentsContainer.getChildren().remove(commentBox);

                        // Afficher un message de succès
                        System.out.println("Commentaire supprimé avec succès !");
                    } catch (SQLException ex) {
                        System.err.println("Erreur lors de la suppression du commentaire: " + ex.getMessage());
                        ex.printStackTrace();

                        // Afficher une alerte d'erreur
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Erreur");
                        errorAlert.setHeaderText("Erreur de suppression");
                        errorAlert.setContentText("Impossible de supprimer le commentaire: " + ex.getMessage());
                        errorAlert.showAndWait();
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du commentaire: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSendComment() {
        String commentText = commentArea.getText().trim();
        if (!commentText.isEmpty() && post != null) {
            try {
                // Créer et ajouter le commentaire à la base de données avec user_id
                Commentaire newComment = new Commentaire(
                        0, // ID sera généré par la base de données
                        post.getPost_id(),
                        commentText,
                        currentuser.getId_utilisateur(), // Utiliser le STATIC_USER_ID
                        LocalDateTime.now()
                );

                serviceCommentaire.ajouter(newComment);

                // Récupérer l'ID généré du commentaire
                int commentId = serviceCommentaire.getLastInsertedId();
                newComment.setCommentaire_id(commentId);

                // Vider le champ de texte
                commentArea.clear();

                // Ajouter le commentaire au container
                displayComment(newComment);

                // Assurer que les commentaires sont visibles après l'ajout d'un nouveau
                commentsVisible = true;
                commentsContainer.setVisible(true);
                commentsContainer.setManaged(true);
                viewCommentsButton.setText("Hide Comments");

            } catch (SQLException e) {
                System.err.println("Erreur lors de l'ajout du commentaire: " + e.getMessage());
                e.printStackTrace();

                // Afficher une alerte d'erreur
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Erreur d'ajout");
                errorAlert.setContentText("Impossible d'ajouter le commentaire: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    public VBox getPostCard() {
        return postCard;
    }
}