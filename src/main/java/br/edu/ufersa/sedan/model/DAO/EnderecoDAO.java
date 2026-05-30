package br.edu.ufersa.sedan.model.DAO;
import br.edu.ufersa.sedan.model.entities.Endereco;
import java.sql.ResultSet;
import java.sql.*;

public class EnderecoDAO implements BaseDAO<Endereco>{
    private Connection con = null;

    public Endereco inserir(Endereco entities) {
        con = BaseDAO.getConnection();
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

    @Override
    public void deletar(Endereco entities) {
        con = BaseDAO.getConnection();

        String sql = "DELETE FROM endereco WHERE idEndereco = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, entities.getIdEndereco());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Endereco entities) {
        con = BaseDAO.getConnection();

        String sql = "UPDATE endereco SET rua = ?, bairro = ?, num = ? WHERE idEndereco = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, entities.getRua());
            ps.setString(2, entities.getBairro());
            ps.setInt(3, entities.getNum());
            ps.setInt(4, entities.getIdEndereco());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet listar() {
        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM endereco";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ResultSet buscar(int id) {
        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM endereco WHERE idEndereco = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            return ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet buscarPorRua(String param){
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM endereco AS e  WHERE e.rua=?";
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            rs = ps.executeQuery();;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet buscarPorBairro(String bairro) {
        con = BaseDAO.getConnection();
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

}
