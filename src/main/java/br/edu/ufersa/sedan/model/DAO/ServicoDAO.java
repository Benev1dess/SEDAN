package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Servico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {
    private final static String URL = "jdbc:mysql://localhost:3306/SedanDB";
    private final static String USER = "root";
    private final static String PASS = "root";
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

    public Servico inserir(Servico entities) {
        con = getConnection();
        String sql = "INSERT INTO servico(nome, preco) VALUES(?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entities.getNome());
            ps.setDouble(2, entities.getPreco());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                // entities.setId(rs.getInt(1));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public List<Servico> listarTodos() {
        con = getConnection();
        String sql = "SELECT * FROM servico";
        List<Servico> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Servico s = new Servico();
                s.setNome(rs.getString("nome"));
                s.setPreco(rs.getDouble("preco"));
                lista.add(s);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(String nomeAntigo, Servico novo) {
        con = getConnection();
        String sql = "UPDATE servico SET nome=?, preco=? WHERE nome=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, novo.getNome());
            ps.setDouble(2, novo.getPreco());
            ps.setString(3, nomeAntigo);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(String nome) {
        con = getConnection();
        String sql = "DELETE FROM servico WHERE nome=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nome);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Servico> buscarPorNome(String nome) {
        List<Servico> lista = new ArrayList<>();
        con = getConnection();
        String sql = "SELECT * FROM servico WHERE nome LIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Servico s = new Servico();
                    s.setNome(rs.getString("nome"));
                    s.setPreco(rs.getDouble("preco"));
                    lista.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}