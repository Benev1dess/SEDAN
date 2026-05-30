package br.edu.ufersa.sedan.model.DAO;
import br.edu.ufersa.sedan.model.entities.Endereco;

import java.sql.*;
import java.net.URL;

public class EnderecoDAO {
    private final static String URL = "jdbc:mysql://localhost:3306/SedamDB";
    private final static String USER = "acsa";
    private final static String PASS = "2007";
    private static Connection con = null;

    public Endereco inserir(Endereco entities) {
        con = getConnection();
        String sql = "INSERT INTO endereco(id, rua, bairro, num)" + "VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, entities.getIdEndereco());
            ps.setString(2, entities.getRua());
            ps.setString(3, entities.getBairro());
            ps.setInt(4, entities.getNum());
            ps.execute();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

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
