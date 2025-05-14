package controllers;

import services.ServicePost;
import services.ServiceCommentaire;
import entities.Commentaire;
import entities.Post;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PostCardController {
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
    private ImageView qrCodeImage;

    @FXML
    private Button loveButton;

    @FXML
    private Button commentButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private VBox commentSection;

    @FXML
    private TextArea commentArea;

    @FXML
    private Button sendCommentButton;

    @FXML
    private VBox commentsContainer;
    private Utilisateur currentuser = UserSession.getCurrentUser();
    private Post post;
    private ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private ServicePost servicePost = new ServicePost();
    private final String DEFAULT_IMAGE_PATH = "/Assets/default.png";

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

        // S'assurer que le container de commentaires est initialement caché
        if (commentsContainer != null) {
            commentsContainer.setVisible(false);
            commentsContainer.setManaged(false);
        }

        // Vérifier si l'utilisateur actuel est le propriétaire du post
        // Si oui, afficher les boutons d'édition et de suppression, sinon les cacher
        boolean isOwner = (post.getUser_id() == currentuser.getId_utilisateur());
        editButton.setVisible(isOwner);
        editButton.setManaged(isOwner);
        deleteButton.setVisible(isOwner);
        deleteButton.setManaged(isOwner);
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
    public void handleLoveButton() {
        // Implémentation pour le bouton "Love"
        // Vous pourriez ajouter un "like" à la base de données ici
        System.out.println("Post " + post.getPost_id() + " loved by user " + currentuser.getId_utilisateur());
        // Changement visuel du bouton pour indiquer qu'il a été cliqué
        loveButton.setStyle("-fx-background-color: #ff6b6b;");
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

            // Si le container de commentaires existe, le cacher
            if (commentsContainer != null) {
                commentsContainer.setVisible(false);
                commentsContainer.setManaged(false);
            }
        }
    }

    @FXML
    public void handleEditButton() {
        try {
            // Trouver le Pane content_area dans le SideNavBar
            Pane contentArea = findContentArea();

            if (contentArea != null) {
                // Charger le fichier FXML pour EditPost (utilise le même FXML que AddPosts)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddPosts.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de AddPosts et configurer pour l'édition
                AddPosts editController = loader.getController();

                // Passer le post existant pour préremplir les champs
                editController.setPostForEdit(post);

                // Configurer pour le mode édition
                editController.configureForEditMode();

                // Définir un gestionnaire pour rediriger vers la liste après la mise à jour
                editController.setOnPostUpdated(() -> {
                    try {
                        // Charger la vue ListPosts
                        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("/FXML/ListPosts.fxml"));
                        Parent listView = listLoader.load();

                        // Remplacer le contenu actuel par la liste des posts
                        contentArea.getChildren().clear();
                        contentArea.getChildren().add(listView);

                        // Ajuster l'ancrage pour remplir tout l'espace disponible
                        AnchorPane.setTopAnchor(listView, 0.0);
                        AnchorPane.setRightAnchor(listView, 0.0);
                        AnchorPane.setBottomAnchor(listView, 0.0);
                        AnchorPane.setLeftAnchor(listView, 0.0);

                        // Initialiser le contrôleur de la liste
                        ListPosts listController = listLoader.getController();
                        // Si nécessaire, vous pouvez appeler manuellement loadPosts pour assurer que la liste est à jour
                        listController.initialize();
                    } catch (IOException e) {
                        System.err.println("Erreur lors du chargement de la liste des posts: " + e.getMessage());
                        e.printStackTrace();
                    }
                });

                // Effacer le contenu actuel et ajouter la vue d'édition
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);

                // Ajuster l'ancrage pour remplir tout l'espace disponible
                AnchorPane.setTopAnchor(root, 0.0);
                AnchorPane.setRightAnchor(root, 0.0);
                AnchorPane.setBottomAnchor(root, 0.0);
                AnchorPane.setLeftAnchor(root, 0.0);
            } else {
                System.err.println("Impossible de trouver le content_area dans le SideNavBar");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture de la vue d'édition: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteButton() {
        // Demander confirmation avant suppression
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Suppression de l'article");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer cet article ?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer le post de la base de données
                // Passer l'objet post au lieu de seulement l'ID
                servicePost.supprimer(post);

                // Afficher un message de confirmation
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("L'article a été supprimé avec succès.");
                successAlert.showAndWait();

                // Rafraîchir la liste des posts
                refreshPostsList();

            } catch (SQLException e) {
                System.err.println("Erreur lors de la suppression du post: " + e.getMessage());
                e.printStackTrace();

                // Afficher un message d'erreur
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Une erreur s'est produite lors de la suppression de l'article.");
                errorAlert.showAndWait();
            }
        }
    }
    private void refreshPostsList() {
        try {
            // Trouver le Pane content_area dans le SideNavBar
            Pane contentArea = findContentArea();

            if (contentArea != null) {
                // Charger la vue ListPosts
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ListPosts.fxml"));
                Parent root = loader.load();

                // Ajuster la taille pour correspondre au content_area
                AnchorPane.setTopAnchor(root, 0.0);
                AnchorPane.setRightAnchor(root, 0.0);
                AnchorPane.setBottomAnchor(root, 0.0);
                AnchorPane.setLeftAnchor(root, 0.0);

                // Effacer et ajouter le nouveau contenu
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } else {
                System.err.println("Impossible de trouver le content_area dans le SideNavBar");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du rafraîchissement de la liste des posts: " + e.getMessage());
            e.printStackTrace();
        }
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PostDetail.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de PostDetail et lui passer l'objet Post
                PostDetailController detailController = loader.getController();
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

    // Méthode pour afficher un commentaire dans l'UI
    private void displayComment(Commentaire comment) {
        // S'assurer que le container de commentaires existe
        if (commentsContainer == null) {
            // Si commentsContainer n'est pas défini dans le FXML, créer un nouveau
            commentsContainer = new VBox(5);
            commentsContainer.setPadding(new Insets(5));

            // Ajouter le container à la section de commentaire
            if (commentSection != null && !commentSection.getChildren().contains(commentsContainer)) {
                commentSection.getChildren().add(commentsContainer);
            }
        }

        // Création d'un conteneur pour le commentaire
        HBox commentBox = new HBox(10);
        commentBox.setPadding(new Insets(5));
        commentBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        // Informations du commentaire
        VBox commentInfo = new VBox(3);

        // Vous pourriez ajouter un nom d'utilisateur réel ici si disponible
        Label userLabel = new Label("User " + comment.getUser_id());
        userLabel.setFont(Font.font("System", 12));
        userLabel.setTextFill(Color.BLUE);

        // Formater la date du commentaire
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Label dateCommentLabel = new Label(comment.getDate_commentaire().format(formatter));
        dateCommentLabel.setFont(Font.font("System", 10));
        dateCommentLabel.setTextFill(Color.GRAY);

        // Le contenu du commentaire
        Label contentLabel = new Label(comment.getComment_contenu());
        contentLabel.setWrapText(true);

        // Ajouter tous les éléments au conteneur de commentaire
        commentInfo.getChildren().addAll(userLabel, contentLabel, dateCommentLabel);
        commentBox.getChildren().add(commentInfo);

        // Ajouter le commentaire au conteneur de commentaires
        commentsContainer.getChildren().add(commentBox);

        // Ne pas afficher le container de commentaires comme demandé
        commentsContainer.setVisible(false);
        commentsContainer.setManaged(false);
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

                // Vider le champ de texte
                commentArea.clear();

                // Ajouter le commentaire au container, mais ne pas l'afficher
                displayComment(newComment);

                // Cacher la section de commentaire après l'envoi
                commentSection.setVisible(false);
                commentSection.setManaged(false);

                // S'assurer que le container de commentaires est caché
                if (commentsContainer != null) {
                    commentsContainer.setVisible(false);
                    commentsContainer.setManaged(false);
                }

            } catch (SQLException e) {
                System.err.println("Erreur lors de l'ajout du commentaire: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public VBox getPostCard() {
        return postCard;
    }
}