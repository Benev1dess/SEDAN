package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Cliente;
import br.edu.ufersa.sedan.model.entities.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements BaseDAO<Cliente> {

    private Connection con;

    @Override
    public void inserir(Cliente cliente) {
        con = BaseDAO.getConnection();
        String sqlEndereco = "INSERT INTO endereco(rua, bairro, num) VALUES (?, ?, ?)";
        String sqlCliente = "INSERT INTO cliente(nome, cpf, idEndereco) VALUES (?, ?, ?)";

        try {
            int idEnderecoGerado = -1;

            if (cliente.getEndereco() != null) {
                PreparedStatement psEnd = con.prepareStatement(sqlEndereco, Statement.RETURN_GENERATED_KEYS);
                psEnd.setString(1, cliente.getEndereco().getRua());
                psEnd.setString(2, cliente.getEndereco().getBairro());
                psEnd.setInt(3, cliente.getEndereco().getNum());
                psEnd.executeUpdate();

                ResultSet rsKeys = psEnd.getGeneratedKeys();
                if (rsKeys.next()) {
                    idEnderecoGerado = rsKeys.getInt(1);
                    cliente.getEndereco().setIdEndereco(idEnderecoGerado);
                }
                rsKeys.close();
                psEnd.close();
            }

            PreparedStatement psCli = con.prepareStatement(sqlCliente);
            psCli.setString(1, cliente.getNome());
            psCli.setString(2, cliente.getCpf());

            if (idEnderecoGerado != -1) {
                psCli.setInt(3, idEnderecoGerado);
            } else {
                psCli.setNull(3, Types.INTEGER);
            }

            psCli.executeUpdate();
            psCli.close();
        } catch (SQLException e) {
            System.err.println("❌ ERRO AO INSERIR CLIENTE NO BD: " + e.getMessage());
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
            System.err.println("❌ ERRO AO DELETAR CLIENTE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Cliente cliente) {
        con = BaseDAO.getConnection();
        String sqlCliente = "UPDATE cliente SET nome = ?, cpf = ?, idEndereco = ? WHERE idCliente = ?";
        String sqlInsereEnd = "INSERT INTO endereco(rua, bairro, num) VALUES (?, ?, ?)";
        String sqlAtualizaEnd = "UPDATE endereco SET rua = ?, bairro = ?, num = ? WHERE id = ?";

        try {
            Integer idEndereco = null;
            Endereco end = cliente.getEndereco();

            if (end != null) {
                if (end.getIdEndereco() > 0) {
                    PreparedStatement psEnd = con.prepareStatement(sqlAtualizaEnd);
                    psEnd.setString(1, end.getRua());
                    psEnd.setString(2, end.getBairro());
                    psEnd.setInt(3, end.getNum());
                    psEnd.setInt(4, end.getIdEndereco());
                    psEnd.executeUpdate();
                    psEnd.close();
                    idEndereco = end.getIdEndereco();
                } else {
                    PreparedStatement psEnd = con.prepareStatement(sqlInsereEnd, Statement.RETURN_GENERATED_KEYS);
                    psEnd.setString(1, end.getRua());
                    psEnd.setString(2, end.getBairro());
                    psEnd.setInt(3, end.getNum());
                    psEnd.executeUpdate();

                    ResultSet rsKeys = psEnd.getGeneratedKeys();
                    if (rsKeys.next()) {
                        idEndereco = rsKeys.getInt(1);
                        end.setIdEndereco(idEndereco);
                    }
                    rsKeys.close();
                    psEnd.close();
                }
            }

            PreparedStatement psCli = con.prepareStatement(sqlCliente);
            psCli.setString(1, cliente.getNome());
            psCli.setString(2, cliente.getCpf());
            if (idEndereco != null) {
                psCli.setInt(3, idEndereco);
            } else {
                psCli.setNull(3, Types.INTEGER);
            }
            psCli.setInt(4, cliente.getId());
            psCli.executeUpdate();
            psCli.close();
        } catch (SQLException e) {
            System.err.println("❌ ERRO AO ALTERAR CLIENTE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Cliente> listar() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        con = BaseDAO.getConnection();

        // Ajustado para buscar mapeando c.idEndereco com e.id
        String sql = "SELECT c.*, e.rua, e.bairro, e.num FROM cliente c " +
                "LEFT JOIN endereco e ON c.idEndereco = e.id";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("❌ ERRO AO LISTAR CLIENTES DO BD: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }

    public ArrayList<Cliente> buscarId(int id) {
        ArrayList<Cliente> clientes = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT c.*, e.rua, e.bairro, e.num FROM cliente c " +
                "LEFT JOIN endereco e ON c.idEndereco = e.id WHERE c.idCliente = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
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
        String sql = "SELECT c.*, e.rua, e.bairro, e.num FROM cliente c " +
                "LEFT JOIN endereco e ON c.idEndereco = e.id WHERE c.cpf = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("idCliente"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));

        int idEnd = rs.getInt("idEndereco");
        if (!rs.wasNull()) {
            Endereco endereco = new Endereco();
            endereco.setIdEndereco(idEnd);
            endereco.setRua(rs.getString("rua"));
            endereco.setBairro(rs.getString("bairro"));
            endereco.setNum(rs.getInt("num"));
            cliente.setEndereco(endereco);
        }
        return cliente;
    }
}