package br.edu.ufersa.sedan.model.DAO;
import br.edu.ufersa.sedan.model.entities.Cliente;
import java.sql.*;
import java.net.URL;

public class ClienteDAO {
    private final static String URL = "jdbc:mysql://localhost:3306/Sedan";
    private final static String USER = "root";
    private final static String PASS = "root";
    private static Connection con = null;

    private int inserir(Cliente c) throws SQLException {

        String sql = "INSERT INTO cliente(nome, cpf) VALUES (?,?)";

        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, c.getNome());
        ps.setString(2, c.getCpf());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        rs.close();
        ps.close();

        return id;
    }


    public ResultSet buscarCpf(String param){
        con = getConnection();
        String sql = "SELECT * FROM cliente AS e WHERE e.cpf=?";
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

    public ResultSet buscarNome(String param){
        con = getConnection();
        String sql = "SELECT * FROM cliente AS e WHERE e.nome=?";
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

    public ResultSet buscar(String nome, String cpf) {
        con = getConnection();

        String sql = "SELECT * FROM endereco WHERE nome = ? AND cpf = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nome);
            ps.setString(2, cpf);
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
