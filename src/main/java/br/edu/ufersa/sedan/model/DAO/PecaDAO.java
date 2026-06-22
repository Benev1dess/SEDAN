package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Peca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PecaDAO implements BaseDAO<Peca> {

    private Connection obterConexao() throws SQLException {
        Connection conexao = BaseDAO.getConnection();
        if (conexao == null) {
            throw new SQLException("Conexão com banco está nula.");
        }
        return conexao;
    }

    @Override
    public void inserir(Peca peca) {
        String sql = "INSERT INTO peca(nome, preco, fabricante) VALUES (?, ?, ?)";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, peca.getNome());
            ps.setDouble(2, peca.getPreco());
            ps.setString(3, peca.getFabricante());

            int linhas = ps.executeUpdate();
            System.out.println("SALVOU NO BANCO: " + linhas);

        } catch (SQLException e) {
            System.out.println("ERRO AO INSERIR PEÇA:");
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Peca peca) {
        String sql = "DELETE FROM peca WHERE nome = ?";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, peca.getNome());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Peca peca) {
        String sql = "UPDATE peca SET preco = ?, fabricante = ? WHERE nome = ?";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, peca.getPreco());
            ps.setString(2, peca.getFabricante());
            ps.setString(3, peca.getNome());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Peca> listar() {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT * FROM peca";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Peca peca = new Peca();
                peca.setNome(rs.getString("nome"));
                peca.setPreco(rs.getDouble("preco"));
                peca.setFabricante(rs.getString("fabricante"));
                pecas.add(peca);
            }

        } catch (SQLException e) {
            System.out.println("ERRO AO LISTAR:");
            e.printStackTrace();
        }

        System.out.println("TOTAL DE PEÇAS NO BANCO: " + pecas.size());
        return pecas;
    }

    public List<Peca> buscarNome(String nome) {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT * FROM peca WHERE nome LIKE ?";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nome + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Peca peca = new Peca();
                    peca.setNome(rs.getString("nome"));
                    peca.setPreco(rs.getDouble("preco"));
                    peca.setFabricante(rs.getString("fabricante"));
                    pecas.add(peca);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pecas;
    }

    public List<Peca> buscarFabricante(String fabricante) {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT * FROM peca WHERE fabricante LIKE ?";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + fabricante + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Peca peca = new Peca();
                    peca.setNome(rs.getString("nome"));
                    peca.setPreco(rs.getDouble("preco"));
                    peca.setFabricante(rs.getString("fabricante"));
                    pecas.add(peca);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pecas;
    }

    public List<Peca> pesquisar(String nome, String fabricante, String automovel) {
        List<Peca> pecas = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.nome, p.preco, p.fabricante " +
                        "FROM peca p " +
                        "LEFT JOIN orcamento_pecas op ON p.nome = op.nome_peca " +
                        "LEFT JOIN orcamento o ON op.idOrcamento = o.idOrcamento " +
                        "LEFT JOIN veiculo v ON o.placaVeiculo = v.placa " +
                        "WHERE 1=1"
        );

        if (nome != null && !nome.isBlank()) {
            sql.append(" AND p.nome LIKE ?");
        }
        if (fabricante != null && !fabricante.isBlank()) {
            sql.append(" AND p.fabricante LIKE ?");
        }
        if (automovel != null && !automovel.isBlank()) {
            sql.append(" AND v.marca LIKE ?");
        }

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int i = 1;

            if (nome != null && !nome.isBlank()) {
                ps.setString(i++, "%" + nome + "%");
            }
            if (fabricante != null && !fabricante.isBlank()) {
                ps.setString(i++, "%" + fabricante + "%");
            }
            if (automovel != null && !automovel.isBlank()) {
                ps.setString(i++, "%" + automovel + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Peca peca = new Peca();
                    peca.setNome(rs.getString("nome"));
                    peca.setPreco(rs.getDouble("preco"));
                    peca.setFabricante(rs.getString("fabricante"));
                    pecas.add(peca);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pecas;
    }
}