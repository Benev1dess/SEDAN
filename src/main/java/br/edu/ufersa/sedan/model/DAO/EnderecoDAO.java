package br.edu.ufersa.sedan.model.DAO;
import br.edu.ufersa.sedan.model.entities.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO implements BaseDAO<Endereco> {

    private Connection con = null;

    @Override
    public void inserir(Endereco endereco) {

        con = BaseDAO.getConnection();

        String sql = "INSERT INTO endereco(rua, bairro, num) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, endereco.getRua());
            ps.setString(2, endereco.getBairro());
            ps.setInt(3, endereco.getNum());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Endereco endereco) {

        con = BaseDAO.getConnection();

        String sql = "DELETE FROM endereco WHERE idEndereco = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, endereco.getIdEndereco());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Endereco endereco) {

        con = BaseDAO.getConnection();

        String sql = "UPDATE endereco SET rua = ?, bairro = ?, num = ? WHERE idEndereco = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, endereco.getRua());
            ps.setString(2, endereco.getBairro());
            ps.setInt(3, endereco.getNum());
            ps.setInt(4, endereco.getIdEndereco());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Endereco> listar() {

        ArrayList<Endereco> enderecos = new ArrayList<>();

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM endereco";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Endereco endereco = new Endereco();

                endereco.setIdEndereco(rs.getInt("idEndereco"));
                endereco.setRua(rs.getString("rua"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setNum(rs.getInt("num"));

                enderecos.add(endereco);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enderecos;
    }

    public ArrayList<Endereco> buscarId(int id) {

        ArrayList<Endereco> enderecos = new ArrayList<>();

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM endereco WHERE idEndereco = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Endereco endereco = new Endereco();

                endereco.setIdEndereco(rs.getInt("idEndereco"));
                endereco.setRua(rs.getString("rua"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setNum(rs.getInt("num"));

                enderecos.add(endereco);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enderecos;
    }

    public ArrayList<Endereco> buscarPorRua(String rua) {

        ArrayList<Endereco> enderecos = new ArrayList<>();

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM endereco WHERE rua = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, rua);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Endereco endereco = new Endereco();

                endereco.setIdEndereco(rs.getInt("idEndereco"));
                endereco.setRua(rs.getString("rua"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setNum(rs.getInt("num"));

                enderecos.add(endereco);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enderecos;
    }

    public ArrayList<Endereco> buscarPorBairro(String bairro) {

        ArrayList<Endereco> enderecos = new ArrayList<>();

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM endereco WHERE bairro = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, bairro);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Endereco endereco = new Endereco();

                endereco.setIdEndereco(rs.getInt("idEndereco"));
                endereco.setRua(rs.getString("rua"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setNum(rs.getInt("num"));

                enderecos.add(endereco);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enderecos;
    }
}
