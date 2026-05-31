package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements BaseDAO<Usuario> {

    private Connection con;

    @Override
    public void inserir(Usuario usuario) {
        con = BaseDAO.getConnection();
        // Atualizado para incluir todos os campos do Model
        String sql = "INSERT INTO usuario(nome, login, senha, email, cpf, salario, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getLogin());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getCpf());
            ps.setDouble(6, usuario.getSalario());
            ps.setString(7, usuario.getTipo().name()); // Salva o Enum como String ("ADM" ou "FUNCIONARIO")

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Usuario usuario) {
        con = BaseDAO.getConnection();
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Usuario usuario) {
        con = BaseDAO.getConnection();
        // Atualizado para alterar todos os campos do Model
        String sql = "UPDATE usuario SET nome = ?, login = ?, senha = ?, email = ?, cpf = ?, salario = ?, tipo = ? WHERE idUsuario = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getLogin());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getCpf());
            ps.setDouble(6, usuario.getSalario());
            ps.setString(7, usuario.getTipo().name());
            ps.setInt(8, usuario.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM usuario";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(); // Agora funciona porque adicionamos o construtor vazio!

                usuario.setId(rs.getInt("idUsuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setSalario(rs.getDouble("salario"));

                // Pega a String do banco ("ADM" ou "FUNCIONARIO") e joga no método seguro do seu Model
                usuario.setTipoString(rs.getString("tipo"));

                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios; // Regra do professor mantida: retorna a lista pura
    }
}
