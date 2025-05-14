package services;
import java.util.List;
import java.sql.SQLException;

public interface IService<T>{
    void add(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    List<T> getAll() throws SQLException;
}