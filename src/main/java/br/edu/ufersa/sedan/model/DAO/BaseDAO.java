package br.edu.ufersa.sedan.model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public interface BaseDAO <E> {
    final static String URL = "jdbc:mysql://localhost:3306/SedanBD"; // Ajustado para SedanBD
    final static String USER = "root";
    final static String PASS = "root";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Métodos abstratos que todo DAO vai ter que implementar
    public void inserir(E entity);
    public void deletar(E entity);
    public void alterar(E entity);
    public List<E> listar();
}