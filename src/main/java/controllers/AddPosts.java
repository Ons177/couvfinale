package controllers;

import entities.Post;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServicePost;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class AddPosts {

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private TextField titreField;

    @FXML
    private TextArea contenuArea;

    @FXML
    private Button uploadImageBtn;

    @FXML
    private Label imageNameLabel;

    @FXML
    private Button annulerBtn;

    @FXML
    private Button ajouterBtn;

    @FXML
    private Button retourBtn;

    @FXML
    private Label pageTitle;

    // Labels d'erreur pour chaque champ
    @FXML
    private Label titreErrorLabel;

    @FXML
    private Label contenuErrorLabel;
    @FXML
    private VBox rootContainer;


    private File selectedImageFile;
    private String uploadedImagePath;
    private String existingImagePath; // Pour conserver le chemin de l'image existante en mode édition
    private ListPosts listPostsController; // Référence au contrôleur de la liste
    private ServicePost servicePost = new ServicePost();

    private boolean editMode = false;
    private Post postToEdit;

    private Runnable onPostUpdatedCallback;

    private Utilisateur currentuser = UserSession.getCurrentUser();

    @FXML
    public void initialize() {
        // Configuration des événements des boutons
        uploadImageBtn.setOnAction(event -> handleImageUpload());
        ajouterBtn.setOnAction(event -> handleSavePost());
        annulerBtn.setOnAction(event -> handleCancel());

        if (retourBtn != null) {
            retourBtn.setOnAction(event -> retourToListPosts(event));
        }

        setupFieldValidation();
    }


    public void configureForEditMode() {
        editMode = true;

        // Changer le texte du bouton et le titre de la page
        if (ajouterBtn != null) {
            ajouterBtn.setText("Modifier l'article");
        }

        try {
            if (pageTitle != null) {
                pageTitle.setText("Modifier Article");
            } else {
                Label titleLabel = (Label) mainContainer.lookup(".page-title");
                if (titleLabel != null) {
                    titleLabel.setText("Modifier Article");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du titre: " + e.getMessage());
        }
    }

    /**
     * Définit le post à éditer et préremplie les champs
     */
    public void setPostForEdit(Post post) {
        this.postToEdit = post;
        this.editMode = true;

        // Préremplir les champs avec les données du post
        if (post != null) {
            titreField.setText(post.getTitre());
            contenuArea.setText(post.getPost_contenu());

            // Conserver le chemin de l'image existante
            existingImagePath = post.getImage();

            // Afficher le nom de l'image existante
            if (existingImagePath != null && !existingImagePath.isEmpty()) {
                File imageFile = new File(existingImagePath);
                imageNameLabel.setText(imageFile.getName());
            }
        }
    }

    /**
     * Définit un callback à exécuter après la mise à jour réussie d'un post
     */
    public void setOnPostUpdated(Runnable callback) {
        this.onPostUpdatedCallback = callback;
    }

    private void setupFieldValidation() {
        // Validation du titre en temps réel
        titreField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                showFieldError(titreErrorLabel, "Le titre est obligatoire");
            } else if (newValue.length() < 3) {
                showFieldError(titreErrorLabel, "Le titre doit contenir au moins 3 caractères");
            } else {
                hideFieldError(titreErrorLabel);
            }
        });

        // Validation du contenu en temps réel
        contenuArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                showFieldError(contenuErrorLabel, "Le contenu est obligatoire");
            } else if (newValue.trim().length() < 10) {
                showFieldError(contenuErrorLabel, "Le contenu doit contenir au moins 10 lettres");
            } else {
                hideFieldError(contenuErrorLabel);
            }
        });
    }

    private void showFieldError(Label errorLabel, String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
        }
    }

    private void hideFieldError(Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        // Validation du titre
        if (titreField.getText().trim().isEmpty()) {
            showFieldError(titreErrorLabel, "Le titre est obligatoire");
            isValid = false;
        } else if (titreField.getText().length() < 3) {
            showFieldError(titreErrorLabel, "Le titre doit contenir au moins 3 caractères");
            isValid = false;
        } else {
            hideFieldError(titreErrorLabel);
        }

        // Validation du contenu
        String contenuText = contenuArea.getText().trim();
        if (contenuText.isEmpty()) {
            showFieldError(contenuErrorLabel, "Le contenu est obligatoire");
            isValid = false;
        } else if (contenuText.length() < 10) {
            showFieldError(contenuErrorLabel, "Le contenu doit contenir au moins 10 caractéres");
            isValid = false;
        } else {
            hideFieldError(contenuErrorLabel);
        }

        return isValid;
    }

    public void setListPostsController(ListPosts controller) {
        this.listPostsController = controller;
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) mainContainer.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            imageNameLabel.setText(selectedImageFile.getName());
        }
    }

    private void handleSavePost() {
        // Validation de tous les champs
        if (!validateAllFields()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez corriger les erreurs dans le formulaire.");
            return;
        }

        try {
            String imagePath = null;

            // Copier l'image si sélectionnée
            if (selectedImageFile != null) {
                imagePath = saveImageToStorage(selectedImageFile);
            } else if (editMode && existingImagePath != null && !existingImagePath.isEmpty()) {
                // En mode édition, conserver l'image existante si aucune nouvelle n'est sélectionnée
                imagePath = existingImagePath;
            }

            // Image par défaut si aucune n'est sélectionnée et pas d'image existante
            if (imagePath == null || imagePath.isEmpty()) {
                imagePath = "uploads/images/default.jpg";
            }

            if (editMode && postToEdit != null) {
                // Mode édition - Mettre à jour le post existant
                postToEdit.setTitre(titreField.getText().trim());
                postToEdit.setPost_contenu(contenuArea.getText().trim());
                postToEdit.setImage(imagePath);

                // La date n'est pas mise à jour lors de l'édition
                // postToEdit.setDate_post(LocalDateTime.now());

                // Mettre à jour le post dans la base de données
                servicePost.modifier(postToEdit);

                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'article a été modifié avec succès.");

                // Si un callback est défini, l'exécuter pour retourner à la liste
                if (onPostUpdatedCallback != null) {
                    onPostUpdatedCallback.run();
                    return;
                }
            } else {
                // Mode ajout - Créer un nouvel objet Post
                Post newPost = new Post();
                newPost.setTitre(titreField.getText().trim());
                newPost.setPost_contenu(contenuArea.getText().trim());

                // Utilisation automatique de la date et heure actuelles
                newPost.setDate_post(LocalDateTime.now());

                // Définir l'ID utilisateur statique
                newPost.setUser_id(currentuser.getId_utilisateur());

                // Définir l'image
                newPost.setImage(imagePath);

                // Enregistrer le post dans la base de données
                servicePost.ajouter(newPost);

                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Succès", "L'article a été ajouté avec succès.");
            }

            // Retourner à la liste des articles
            retourToListPosts(new ActionEvent());

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'opération: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue s'est produite: " + e.getMessage());
        }
    }

    private String saveImageToStorage(File sourceFile) throws Exception {
        // Créer un nom de fichier unique pour éviter les conflits
        String uniqueFileName = UUID.randomUUID().toString() + "_" + sourceFile.getName();

        // Définir le chemin de destination
        String uploadDir = "uploads/images/";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        Path sourcePath = sourceFile.toPath();
        Path targetPath = Paths.get(uploadDir + uniqueFileName);

        // Copier le fichier
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath.toString();
    }

    private void handleCancel() {
        // Utiliser la même méthode de retour pour l'annulation
        retourToListPosts(new ActionEvent());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void retourToListPosts(ActionEvent event) {
        try {
            Pane contentArea = findContentArea();

            if (contentArea != null) {
                // Load the post list view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListPosts.fxml"));
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


}