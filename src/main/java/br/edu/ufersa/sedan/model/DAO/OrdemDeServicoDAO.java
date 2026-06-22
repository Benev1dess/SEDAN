package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.OrdemServico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdemDeServicoDAO {
    private final static String URL = "jdbc:mysql://localhost:3306/sedanbd";
    private final static String USER = "root";
    private final static String PASS = "root";
    private static Connection con = null;

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public OrdemServico inserir(OrdemServico os) {
        con = getConnection();
        String sql = "INSERT INTO ordem_servico(id_orcamento, finalizada, pago, data_os) VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // Pegando o id do orçamento associado
            ps.setInt(1, os.getOrcamento() != null ? os.getOrcamento().getId() : 0);
            ps.setBoolean(2, os.isFinalizada());
            ps.setBoolean(3, os.isPago());
            ps.setDate(4, Date.valueOf(os.getData())); // Converte LocalDate para java.sql.Date
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                // os.setId(rs.getInt(1)); // Caso possua setId na model
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return os;
    }

    public void atualizarStatus(OrdemServico os) {
        con = getConnection();
        String sql = "UPDATE ordem_servico SET finalizada=?, pago=? WHERE id_orcamento=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, os.isFinalizada());
            ps.setBoolean(2, os.isPago());
            ps.setInt(3, os.getOrcamento() != null ? os.getOrcamento().getId() : 0);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int idOrcamento) {
        con = getConnection();
        String sql = "DELETE FROM ordem_servico WHERE id_orcamento=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idOrcamento);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ── NOVO MÉTODO EXIGIDO PELA SERVICE ──────────────────────────────
    public List<OrdemServico> listarTodos() {
        con = getConnection();
        String sql = "SELECT * FROM ordem_servico";
        List<OrdemServico> lista = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                OrdemServico os = new OrdemServico();

                // Reconstrói a associação com Orçamento que a View necessita
                Orcamento o = new Orcamento();
                o.setId(rs.getInt("id_orcamento"));
                os.setOrcamento(o);

                os.setFinalizada(rs.getBoolean("finalizada"));
                os.setPago(rs.getBoolean("pago"));

                // Converte java.sql.Date com segurança para LocalDate
                Date dataBanco = rs.getDate("data_os");
                if (dataBanco != null) {
                    os.setData(dataBanco.toLocalDate());
                }

                lista.add(os);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}