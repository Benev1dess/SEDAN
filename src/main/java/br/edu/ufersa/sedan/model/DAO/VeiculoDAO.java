package br.edu.ufersa.sedan.model.DAO;
import br.edu.ufersa.sedan.model.entities.Cliente;
import br.edu.ufersa.sedan.model.entities.Veiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO implements BaseDAO<Veiculo> {

    private Connection con = null;

    private Veiculo criarVeiculo(ResultSet rs) throws SQLException {

        Veiculo veiculo = new Veiculo();

        veiculo.setId(rs.getInt("id"));
        veiculo.setMarca(rs.getString("marca"));
        veiculo.setCor(rs.getString("cor"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setKm(rs.getInt("km"));

        int idCliente = rs.getInt("idCliente");
        if (!rs.wasNull()) {
            ClienteDAO clienteDAO = new ClienteDAO();
            ArrayList<Cliente> clientes = clienteDAO.buscarId(idCliente);
            if (!clientes.isEmpty()) {
                veiculo.setDono(clientes.get(0));
            }
        }

        return veiculo;
    }

    @Override
    public void inserir(Veiculo veiculo) {

        con = BaseDAO.getConnection();

        String sql =
                "INSERT INTO veiculo(marca, cor, placa, ano, km, idCliente) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, veiculo.getMarca());
            ps.setString(2, veiculo.getCor());
            ps.setString(3, veiculo.getPlaca());
            ps.setInt(4, veiculo.getAno());
            ps.setInt(5, veiculo.getKm());
            if (veiculo.getDono() != null) {
                ps.setInt(6, veiculo.getDono().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Veiculo veiculo) {
        con = BaseDAO.getConnection();
        String sql = "DELETE FROM veiculo WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, veiculo.getId());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Veiculo veiculo) {

        con = BaseDAO.getConnection();
        String sql =
                "UPDATE veiculo " +  "SET marca = ?, cor = ?, placa = ?, ano = ?, km = ?, idCliente = ? " + "WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, veiculo.getMarca());
            ps.setString(2, veiculo.getCor());
            ps.setString(3, veiculo.getPlaca());
            ps.setInt(4, veiculo.getAno());
            ps.setInt(5, veiculo.getKm());

            if (veiculo.getDono() != null) {
                ps.setInt(6, veiculo.getDono().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setInt(7, veiculo.getId());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Veiculo> listar() {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                veiculos.add(criarVeiculo(rs));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return veiculos;
    }

    public ArrayList<Veiculo> buscarId(int id) {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                veiculos.add(criarVeiculo(rs));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return veiculos;
    }

    public ArrayList<Veiculo> buscarPorPlaca(String placa) {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE placa = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, placa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                veiculos.add(criarVeiculo(rs));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veiculos;
    }

    public ArrayList<Veiculo> buscarPorMarca(String marca) {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE marca = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, marca);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                veiculos.add(criarVeiculo(rs));
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return veiculos;
    }

    public ArrayList<Veiculo> buscarPorCliente(int idCliente) {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE idCliente = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                veiculos.add(criarVeiculo(rs));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veiculos;
    }

    public ArrayList<Veiculo> buscarPorCor(String cor) {

        ArrayList<Veiculo> veiculos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE cor = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cor);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                veiculos.add(criarVeiculo(rs));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veiculos;
    }

    public ArrayList<Veiculo> buscarPorAno(int ano) {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE ano = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ano);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                veiculos.add(criarVeiculo(rs));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return veiculos;
    }

    public ArrayList<Veiculo> buscarPorKm(int kmMin, int kmMax) {
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE km BETWEEN ? AND ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, kmMin);
            ps.setInt(2, kmMax);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                veiculos.add(criarVeiculo(rs));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return veiculos;
    }

    public void deletarPorPlaca(String placa) {
        con = BaseDAO.getConnection();
        String sql = "DELETE FROM veiculo WHERE placa = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, placa);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existePlaca(String placa) {
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE placa = ?";

        try {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, placa);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            rs.close();
            ps.close();
            return existe;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}