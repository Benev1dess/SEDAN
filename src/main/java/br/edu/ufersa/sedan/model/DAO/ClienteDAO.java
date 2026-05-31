package br.edu.ufersa.sedan.model.DAO;
import br.edu.ufersa.sedan.model.entities.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements BaseDAO<Cliente> {

    private Connection con;

    @Override
    public void inserir(Cliente cliente) {

        con = BaseDAO.getConnection();
        String sql = "INSERT INTO cliente(nome, cpf) VALUES (?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public List<Cliente> listar() {

        ArrayList<Cliente> clientes = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM cliente";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("idCliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));

                clientes.add(cliente);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public ArrayList<Cliente> buscarId(int id) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM cliente WHERE idCliente = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("idCliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));

                clientes.add(cliente);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public ArrayList<Cliente> buscarCpf(String cpf) {

        ArrayList<Cliente> clientes = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM cliente WHERE cpf = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("idCliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));

                clientes.add(cliente);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public ArrayList<Cliente> buscarNome(String nome) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM cliente WHERE nome = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Cliente cliente = new Cliente();

                cliente.setId(rs.getInt("idCliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));

                clientes.add(cliente);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public ArrayList<Cliente> buscar(String nome, String cpf) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM cliente WHERE nome = ? AND cpf = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setString(2, cpf);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Cliente cliente = new Cliente();

                cliente.setId(rs.getInt("idCliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));

                clientes.add(cliente);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
}