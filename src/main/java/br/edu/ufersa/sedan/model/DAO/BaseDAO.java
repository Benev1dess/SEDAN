package br.edu.ufersa.sedan.model.DAO;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface BaseDAO <E>{
     final static String URL = "jdbc:mysql://localhost:3306/SedanDB";
     final static String USER = "root";
     final static String PASS = "root";
    static Connection con = getConnection();

    public static Connection getConnection() {
            try {
                return DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
    }
    public static void closeConnection(){
        if (con != null){
            try {
                con.close();
            } catch (SQLException e) {e.printStackTrace();}
        }
    }

    public E inserir(E entities);
    public void deletar(E entities);
    public void alterar(E entities);
    public ResultSet buscar(int id);
    public ResultSet listar();
}
