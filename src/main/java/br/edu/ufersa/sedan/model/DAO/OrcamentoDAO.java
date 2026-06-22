package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.Peca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoDAO implements BaseDAO<Orcamento> {

    private Connection con;

    @Override
    public void inserir(Orcamento orcamento) {
        con = BaseDAO.getConnection();
        String sqlOrcamento = "INSERT INTO orcamento(placaVeiculo, dataOrcamento) VALUES (?, ?)";


        try (PreparedStatement ps = con.prepareStatement(sqlOrcamento, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, orcamento.getPlacaVeiculo());
            ps.setDate(2, Date.valueOf(orcamento.getDataOrcamento())); // Converte LocalDate para Date do SQL
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idOrcamentoGerado = generatedKeys.getInt(1);

                    // Salva as peças vinculadas a esse orçamento na tabela muitos-para-muitos
                    if (orcamento.getListaPecas() != null) {
                        salvarPecasDoOrcamento(idOrcamentoGerado, orcamento.getListaPecas());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void salvarPecasDoOrcamento(int idOrcamento, List<Peca> pecas) throws SQLException {
        String sqlVinculo = "INSERT INTO orcamento_pecas(idOrcamento, idPeca) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlVinculo)) {
            for (Peca peca : pecas) {
                ps.setInt(1, idOrcamento);
                ps.setInt(2, peca.getId());
                ps.addBatch(); // Otimiza para enviar todas as peças de uma vez só
            }
            ps.executeBatch();
        }
    }

    @Override
    public void deletar(Orcamento orcamento) {
        con = BaseDAO.getConnection();
        String sql = "DELETE FROM orcamento WHERE idOrcamento = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orcamento.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Orcamento orcamento) {
        con = BaseDAO.getConnection();
        String sql = "UPDATE orcamento SET placaVeiculo = ?, dataOrcamento = ? WHERE idOrcamento = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, orcamento.getPlacaVeiculo());
            ps.setDate(2, Date.valueOf(orcamento.getDataOrcamento()));
            ps.setInt(3, orcamento.getId());
            ps.executeUpdate();

            // Em cenários reais, aqui você também atualizaria/limparia a tabela associativa de peças se elas mudassem
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Orcamento> listar() {
        List<Orcamento> orcamentos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM orcamento";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Orcamento o = new Orcamento();
                int id = rs.getInt("idOrcamento");
                o.setId(id);
                o.setPlacaVeiculo(rs.getString("placaVeiculo"));
                o.setDataOrcamento(rs.getDate("dataOrcamento").toLocalDate());

                // Carrega a lista de peças associadas resgatando-as como ArrayList limpa
                o.setListaPecas(buscarPecasDoOrcamento(id));

                orcamentos.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos; // Retorna a lista purinha!
    }

    private List<Peca> buscarPecasDoOrcamento(int idOrcamento) throws SQLException {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT p.* FROM peca p " +
                "JOIN orcamento_pecas op ON p.idPeca = op.idPeca " +
                "WHERE op.idOrcamento = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Peca p = new Peca();
                    p.setId(rs.getInt("idPeca"));
                    p.setNome(rs.getString("nome"));
                    p.setPreco(rs.getDouble("preco"));
                    p.setFabricante(rs.getString("fabricante"));
                    pecas.add(p);
                }
            }
        }
        return pecas;
    }
}
