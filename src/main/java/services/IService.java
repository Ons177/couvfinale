package services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter(T var1) throws SQLException;

    void modifier(T var1) throws SQLException;

    void supprimer(T var1) throws SQLException;

    List<T> recuperer() throws SQLException;


    // Not used
    boolean getAll();
}
