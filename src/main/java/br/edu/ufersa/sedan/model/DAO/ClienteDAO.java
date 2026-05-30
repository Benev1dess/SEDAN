package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Cliente;

import java.sql.*;

public class ClienteDAO implements BaseDAO<Cliente> {

    private Connection con = null;

    @Override
    public Cliente inserir(Cliente cliente) {

        con = BaseDAO.getConnection();

        String sql = "INSERT INTO cliente(nome, cpf) VALUES (?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                cliente.setId(rs.getInt(1));
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }

    @Override
    public void deletar(Cliente cliente) {

        con = BaseDAO.getConnection();

        String sql = "DELETE FROM cliente WHERE idCliente = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, cliente.getId());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Cliente cliente) {

        con = BaseDAO.getConnection();

        String sql = "UPDATE cliente SET nome = ?, cpf = ? WHERE idCliente = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setInt(3, cliente.getId());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet buscar(int id) {

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM cliente WHERE idCliente = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ResultSet listar() {

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM cliente";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarCpf(String cpf) {

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM cliente WHERE cpf = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, cpf);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarNome(String nome) {

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM cliente WHERE nome = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nome);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscar(String nome, String cpf) {

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM cliente WHERE nome = ? AND cpf = ?";

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
}
