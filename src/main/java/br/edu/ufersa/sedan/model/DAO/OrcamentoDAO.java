package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.entities.Servico;
import br.edu.ufersa.sedan.model.entities.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoDAO implements BaseDAO<Orcamento> {

    private Connection con = null;

    @Override
    public void inserir(Orcamento orcamento) {
        con = BaseDAO.getConnection();
        String sql = "INSERT INTO orcamento(placaVeiculo, dataOrcamento) VALUES (?, ?)";

        try {
            // Prepara a instrução solicitando a chave gerada automaticamente (idOrcamento)
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, orcamento.getVeiculo() != null ? orcamento.getVeiculo().getPlaca() : null);
            ps.setDate(2, Date.valueOf(orcamento.getData())); // Converte LocalDate para java.sql.Date

            ps.executeUpdate();

            // Recupera o ID gerado para poder cadastrar os itens das listas vinculadas
            ResultSet rs = ps.getGeneratedKeys();
            int idGerado = 0;
            if (rs.next()) {
                idGerado = rs.getInt(1);
                // Caso seu modelo Orcamento possua setIdOrcamento, adicione aqui:
                // orcamento.setIdOrcamento(idGerado);
            }
            rs.close();
            ps.close();

            // Se o orçamento foi gravado, insere as dependências das coleções (Lists)
            if (idGerado > 0) {
                // Salva a List<Peca> na tabela associativa
                if (orcamento.getPecas() != null && !orcamento.getPecas().isEmpty()) {
                    String sqlPecas = "INSERT INTO orcamento_pecas(idOrcamento, idPeca) VALUES (?, ?)";
                    PreparedStatement psPecas = con.prepareStatement(sqlPecas);
                    for (Peca p : orcamento.getPecas()) {
                        psPecas.setInt(1, idGerado);
                        // psPecas.setInt(2, p.getIdPeca()); -- Alinhar com a PK da classe Peca
                        psPecas.addBatch();
                    }
                    psPecas.executeBatch();
                    psPecas.close();
                }

                // Salva a List<Servico> na tabela associativa
                if (orcamento.getServicos() != null && !orcamento.getServicos().isEmpty()) {
                    String sqlServicos = "INSERT INTO orcamento_servicos(idOrcamento, idServico) VALUES (?, ?)";
                    PreparedStatement psServicos = con.prepareStatement(sqlServicos);
                    for (Servico s : orcamento.getServicos()) {
                        psServicos.setInt(1, idGerado);
                        // psServicos.setInt(2, s.getIdServico()); -- Alinhar com a PK da classe Servico
                        psServicos.addBatch();
                    }
                    psServicos.executeBatch();
                    psServicos.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletar(Orcamento orcamento) {
        con = BaseDAO.getConnection();
        // Graças ao ON DELETE CASCADE configurado no banco, deletar o orçamento
        // remove automaticamente os registros associados em orcamento_pecas e orcamento_servicos
        String sql = "DELETE FROM orcamento WHERE idOrcamento = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            // ps.setInt(1, orcamento.getIdOrcamento()); -- Alinhar com seu atributo identificador de Orcamento
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void alterar(Orcamento orcamento) {
        con = BaseDAO.getConnection();
        String sql = "UPDATE orcamento SET placaVeiculo = ?, dataOrcamento = ? WHERE idOrcamento = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, orcamento.getVeiculo() != null ? orcamento.getVeiculo().getPlaca() : null);
            ps.setDate(2, Date.valueOf(orcamento.getData()));
            // ps.setInt(3, orcamento.getIdOrcamento());

            ps.executeUpdate();
            ps.close();

            // Nota: Para uma alteração completa das listas associadas, o ideal seria deletar
            // as referências antigas em orcamento_pecas/servicos e reinseri-las atualizadas.

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Orcamento> listar() {
        ArrayList<Orcamento> orcamentos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM orcamento";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Orcamento orcamento = new Orcamento();
                int idOrcamento = rs.getInt("idOrcamento");

                // Mapeia atributos primitivos e datas
                orcamento.setData(rs.getDate("dataOrcamento").toLocalDate());

                // Reconstrói a agregação do Veículo instanciando e setando a placa
                Veiculo veiculo = new Veiculo();
                veiculo.setPlaca(rs.getString("placaVeiculo"));
                orcamento.setVeiculo(veiculo);

                // Regra do professor: Povoa os ArrayLists buscando de forma isolada no banco
                orcamento.setPecas(buscarPecasDoOrcamento(idOrcamento));
                orcamento.setServicos(buscarServicosDoOrcamento(idOrcamento));

                orcamentos.add(orcamento);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orcamentos;
    }

    // Métodos de busca específicos e complementares seguindo o modelo criado por você

    public ArrayList<Orcamento> buscarPorPlacaVeiculo(String placa) {
        ArrayList<Orcamento> orcamentos = new ArrayList<>();
        con = BaseDAO.getConnection();
        String sql = "SELECT * FROM orcamento WHERE placaVeiculo = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, placa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Orcamento orcamento = new Orcamento();
                int idOrcamento = rs.getInt("idOrcamento");
                orcamento.setData(rs.getDate("dataOrcamento").toLocalDate());

                Veiculo v = new Veiculo();
                v.setPlaca(placa);
                orcamento.setVeiculo(v);

                orcamento.setPecas(buscarPecasDoOrcamento(idOrcamento));
                orcamento.setServicos(buscarServicosDoOrcamento(idOrcamento));

                orcamentos.add(orcamento);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orcamentos;
    }

    // Métodos utilitários internos para buscar e popular as listas do objeto agregado
    private List<Peca> buscarPecasDoOrcamento(int idOrcamento) throws SQLException {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT p.* FROM peca p JOIN orcamento_pecas op ON p.idPeca = op.idPeca WHERE op.idOrcamento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Peca p = new Peca();
                    p.setNome(rs.getString("nome"));
                    p.setPreco(rs.getDouble("preco"));
                    p.setFabricante(rs.getString("fabricante"));
                    pecas.add(p);
                }
            }
        }
        return pecas;
    }

    private List<Servico> buscarServicosDoOrcamento(int idOrcamento) throws SQLException {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT s.* FROM servico s JOIN orcamento_servicos os ON s.idServico = os.idServico WHERE os.idOrcamento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrcamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Servico s = new Servico();
                    s.setNome(rs.getString("nome"));
                    s.setPreco(rs.getDouble("preco"));
                    servicos.add(s);
                }
            }
        }
        return servicos;
    }
}