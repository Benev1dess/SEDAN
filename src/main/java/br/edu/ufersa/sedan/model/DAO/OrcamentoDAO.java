package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.entities.Servico;
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
            ps.setDate(2, Date.valueOf(orcamento.getDataOrcamento()));
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idOrcamentoGerado = generatedKeys.getInt(1);

                    // Salva as peças vinculadas
                    if (orcamento.getListaPecas() != null) {
                        salvarPecasDoOrcamento(idOrcamentoGerado, orcamento.getListaPecas());
                    }

                    // Salva os serviços vinculados
                    if (orcamento.getServicos() != null) {
                        salvarServicosDoOrcamento(idOrcamentoGerado, orcamento.getServicos());
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
            for (Peca p : pecas) {
                ps.setInt(1, idOrcamento);
                ps.setInt(2, p.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void salvarServicosDoOrcamento(int idOrcamento, List<Servico> servicos) throws SQLException {
        String sqlVinculo = "INSERT INTO orcamento_servicos(idOrcamento, idServico) VALUES (?, ?)";
        String sqlDescobrirId = "SELECT idServico FROM servico WHERE nome = ? LIMIT 1";

        try (PreparedStatement psVinculo = con.prepareStatement(sqlVinculo);
             PreparedStatement psBuscaId = con.prepareStatement(sqlDescobrirId)) {

            for (Servico servico : servicos) {
                psBuscaId.setString(1, servico.getNome());
                int idServicoReal = 0;

                try (ResultSet rs = psBuscaId.executeQuery()) {
                    if (rs.next()) {
                        idServicoReal = rs.getInt("idServico");
                    }
                }

                if (idServicoReal > 0) {
                    psVinculo.setInt(1, idOrcamento);
                    psVinculo.setInt(2, idServicoReal);
                    psVinculo.addBatch();
                }
            }
            psVinculo.executeBatch();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Orcamento> listar() {
        List<Orcamento> orcamentos = new ArrayList<>();
        con = BaseDAO.getConnection();

        String sql = "SELECT o.*, v.marca, v.cor, v.ano, v.km, c.idCliente, c.nome AS nome_cliente, c.cpf " +
                "FROM orcamento o " +
                "LEFT JOIN veiculo v ON o.placaVeiculo = v.placa " +
                "LEFT JOIN cliente c ON v.idCliente = c.idCliente";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Orcamento o = new Orcamento();
                int id = rs.getInt("idOrcamento");
                o.setId(id);
                o.setPlacaVeiculo(rs.getString("placaVeiculo"));
                o.setDataOrcamento(rs.getDate("dataOrcamento").toLocalDate());

                if (o.getVeiculo() == null) {
                    o.setVeiculo(new br.edu.ufersa.sedan.model.entities.Veiculo());
                }
                br.edu.ufersa.sedan.model.entities.Veiculo v = o.getVeiculo();
                v.setPlaca(rs.getString("placaVeiculo"));
                v.setMarca(rs.getString("marca"));
                v.setCor(rs.getString("cor"));

                String nomeCli = rs.getString("nome_cliente");
                if (nomeCli != null) {
                    br.edu.ufersa.sedan.model.entities.Cliente cliente = new br.edu.ufersa.sedan.model.entities.Cliente();
                    cliente.setNome(nomeCli);
                    cliente.setCpf(rs.getString("cpf"));
                    v.setDono(cliente);
                }

                // Carrega as duas listas de forma explícita
                List<Peca> pecasDoBanco = buscarPecasDoOrcamento(id);
                o.setPecas(pecasDoBanco);
                o.setListaPecas(pecasDoBanco);

                o.setServicos(buscarServicosDoOrcamento(id));

                orcamentos.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos;
    }

    private List<Servico> buscarServicosDoOrcamento(int idOrcamento) throws SQLException {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT s.nome, s.preco FROM servico s " +
                "JOIN orcamento_servicos os ON s.idServico = os.idServico " +
                "WHERE os.idOrcamento = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    if (nome != null) {
                        servicos.add(new Servico(nome, preco));
                    }
                }
            }
        }
        return servicos;
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