package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Accueil.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AccueilConducteur.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/fxml/AccueilAdmin.fxml"));
            Scene scene = new Scene(root);


            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("Gestion des Événements");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}