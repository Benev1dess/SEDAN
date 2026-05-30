package br.edu.ufersa.sedan.model.DAO;
import java.sql.*;
import java.net.URL;

public class ClienteDAO {
    private final static String URL = "jdbc:mysql://localhost:3306/Sedan";
    private final static String USER = "acsa";
    private final static String PASS = "2007";
    private static Connection con = null;

    public static Connection getConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return con;
    }
    public static void closeConnection(){
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e) {e.printStackTrace();}
            }
        }
}
