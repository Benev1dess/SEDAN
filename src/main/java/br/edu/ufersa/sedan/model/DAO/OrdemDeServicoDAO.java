package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.OrdemServico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdemDeServicoDAO {

    // Instancia o DAO de orçamentos para carregar os dados completos (veículo, cliente, peças, serviços)
    private final OrcamentoDAO orcamentoDAO = new OrcamentoDAO();

    private Connection obterConexao() throws SQLException {
        Connection conexao = BaseDAO.getConnection();
        if (conexao == null) {
            throw new SQLException("Conexão com o banco de dados está nula.");
        }
        return conexao;
    }

    public OrdemServico inserir(OrdemServico os) {
        String sql = "INSERT INTO ordem_servico(id_orcamento, finalizada, pago, data_os) VALUES(?,?,?,?)";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, os.getOrcamento() != null ? os.getOrcamento().getId() : 0);
            ps.setBoolean(2, os.isFinalizada());
            ps.setBoolean(3, os.isPago());

            if (os.getData() != null) {
                ps.setDate(4, Date.valueOf(os.getData()));
            } else {
                ps.setDate(4, Date.valueOf(java.time.LocalDate.now()));
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    // Se a sua model possuir o atributo ID próprio da OS, descomente a linha abaixo:
                    // os.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("ERRO AO INSERIR ORDEM DE SERVIÇO:");
            e.printStackTrace();
        }
        return os;
    }

    public void atualizarStatus(OrdemServico os) {
        String sql = "UPDATE ordem_servico SET finalizada=?, pago=? WHERE id_orcamento=?";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, os.isFinalizada());
            ps.setBoolean(2, os.isPago());
            ps.setInt(3, os.getOrcamento() != null ? os.getOrcamento().getId() : 0);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO AO ATUALIZAR STATUS DA O.S.:");
            e.printStackTrace();
        }
    }

    public void deletar(int idOrcamento) {
        String sql = "DELETE FROM ordem_servico WHERE id_orcamento=?";

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOrcamento);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO AO DELETAR O.S.:");
            e.printStackTrace();
        }
    }

    public List<OrdemServico> listarTodos() {
        String sql = "SELECT * FROM ordem_servico";
        List<OrdemServico> lista = new ArrayList<>();

        // Carrega a listagem completa dos orçamentos do sistema de uma vez só para vincular de forma eficiente
        List<Orcamento> orcamentosDoBanco = orcamentoDAO.listar();

        try (Connection con = obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrdemServico os = new OrdemServico();
                int idOrcamentoBanco = rs.getInt("id_orcamento");

                // CRUCIAL: Procura o orçamento completo na lista recuperada da OrcamentoDAO
                Orcamento orcamentoCompleto = orcamentosDoBanco.stream()
                        .filter(o -> o.getId() == idOrcamentoBanco)
                        .findFirst()
                        .orElse(null);

                // Se não achar o objeto preenchido, cria um fallback básico com o ID para não quebrar a aplicação
                if (orcamentoCompleto == null) {
                    orcamentoCompleto = new Orcamento();
                    orcamentoCompleto.setId(idOrcamentoBanco);
                }

                os.setOrcamento(orcamentoCompleto);
                os.setFinalizada(rs.getBoolean("finalizada"));
                os.setPago(rs.getBoolean("pago"));

                Date dataBanco = rs.getDate("data_os");
                if (dataBanco != null) {
                    os.setData(dataBanco.toLocalDate());
                }

                lista.add(os);
            }
        } catch (SQLException e) {
            System.out.println("ERRO AO LISTAR ORDENS DE SERVIÇO:");
            e.printStackTrace();
        }
        return lista;
    }
}