package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Veiculo;

import java.sql.*;

public class VeiculoDAO implements BaseDAO<Veiculo> {

    private Connection con = null;

    @Override
    public Veiculo inserir(Veiculo veiculo) {

        con = BaseDAO.getConnection();

        String sql = "INSERT INTO veiculo(marca, cor, placa, ano, km, idCliente) " + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps =
                    con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, veiculo.getMarca());
            ps.setString(2, veiculo.getCor());
            ps.setString(3, veiculo.getPlaca());
            ps.setInt(4, veiculo.getAno());
            ps.setInt(5, (int) veiculo.getKm());

            if (veiculo.getDono() != null) {
                ps.setInt(6, veiculo.getDono().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                veiculo.setId(rs.getInt(1));
            }

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return veiculo;
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
                "UPDATE veiculo " +
                        "SET marca = ?, cor = ?, placa = ?, ano = ?, km = ?, idCliente = ? " +
                        "WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, veiculo.getMarca());
            ps.setString(2, veiculo.getCor());
            ps.setString(3, veiculo.getPlaca());
            ps.setInt(4, veiculo.getAno());
            ps.setInt(5, (int) veiculo.getKm());

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
    public ResultSet buscar(int id) {

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM veiculo WHERE id = ?";

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

        String sql = "SELECT * FROM veiculo";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarPorPlaca(String placa) {
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM veiculo WHERE placa = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, placa);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarPorMarca(String marca) {

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM veiculo WHERE marca = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, marca);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarPorCliente(int idCliente) {
        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM veiculo WHERE idCliente = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idCliente);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarPorCor(String cor) {
        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM veiculo WHERE cor = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, cor);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarPorAno(int ano) {
        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM veiculo WHERE ano = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, ano);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarPorKm(int kmMin, int kmMax) {
        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM veiculo WHERE km BETWEEN ? AND ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, kmMin);
            ps.setInt(2, kmMax);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
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

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
