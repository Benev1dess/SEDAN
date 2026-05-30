package br.edu.ufersa.sedan.model.DAO;
import br.edu.ufersa.sedan.model.entities.Endereco;
import java.sql.ResultSet;
import java.sql.*;

import static br.edu.ufersa.sedan.model.DAO.ClienteDAO.getConnection;

public class EnderecoDAO{
    private final static String URL = "jdbc:mysql://localhost:3306/SedamDB";
    private final static String USER = "root";
    private final static String PASS = "root";
    private static Connection con = null;

    public Endereco inserir(Endereco entities) {
        con = getConnection();
        String sql = "INSERT INTO endereco(rua, bairro, num)" + "VALUES(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entities.getRua());
            ps.setString(2, entities.getBairro());
            ps.setInt(3, entities.getNum());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int idGen = rs.getInt(1);
                entities.setIdEndereco(idGen);
            }
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public ResultSet buscarPorRua(String param){
        con = getConnection();
        String sql = "SELECT * FROM endereco AS e  WHERE e.rua=?";
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            rs = ps.executeQuery();;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet buscarPorBairro(String bairro) {
        String sql = "SELECT * FROM endereco WHERE bairro = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, bairro);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscar(String rua, String bairro, int num) {
        con = getConnection();

        String sql = "SELECT * FROM endereco WHERE rua = ? AND bairro = ? AND num = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, rua);
            ps.setString(2, bairro);
            ps.setInt(3, num);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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
