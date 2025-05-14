package tests;

import services.UserService;
import Controllers.UserSession;
import entities.Utilisateur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class mainFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        UserService u = new UserService();
        Utilisateur u1 = u.getUserById(52);
        if (u1 != null) {
            System.out.println("utilisateur trouvé" + u1);
            UserSession.setCurrentUser(u1);
        }
        else {
            System.out.println("utilisateur n'existe pas");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SideNavBar.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SideNavBarAdmin.fxml"));


        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("LinkUp");

        primaryStage.show();
    }
    public static void main(String[] args) throws GeneralSecurityException, IOException {

        launch(args);
    }
}
