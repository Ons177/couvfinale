package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MyDatabase {
    private final String URL = "jdbc:mysql://localhost:3306/couvoiturage";
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private Connection cnx ;
    private static MyDatabase instance;

    private MyDatabase() {
        try {
            System.out.println("Tentative de connexion à la base de données...");
            System.out.println("URL: " + URL);
            System.out.println("Username: " + USERNAME);
            cnx = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("Connexion à la base de données réussie!");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static MyDatabase getInstance(){
        if(instance == null)
            instance = new MyDatabase();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
