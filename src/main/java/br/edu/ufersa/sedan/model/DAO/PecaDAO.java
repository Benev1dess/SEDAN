package br.edu.ufersa.sedan.model.DAO;

import br.edu.ufersa.sedan.model.entities.Peca;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;



public class PecaDAO implements BaseDAO<Peca> {



    private Connection con;



    @Override

    public void inserir(Peca peca) {

        con = BaseDAO.getConnection();

        String sql = "INSERT INTO peca(nome, preco, fabricante) VALUES (?, ?, ?)";



        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, peca.getNome());

            ps.setDouble(2, peca.getPreco());

            ps.setString(3, peca.getFabricante());

            ps.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void deletar(Peca peca) {

        con = BaseDAO.getConnection();

        String sql = "DELETE FROM peca WHERE idPeca = ?";



        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, peca.getId()); // Certifique-se de que o método no seu Model é getId() ou getIdPeca()

            ps.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }



    @Override

    public void alterar(Peca peca) {

        con = BaseDAO.getConnection();

        String sql = "UPDATE peca SET nome = ?, preco = ?, fabricante = ? WHERE idPeca = ?";



        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, peca.getNome());

            ps.setDouble(2, peca.getPreco());

            ps.setString(3, peca.getFabricante());

            ps.setInt(4, peca.getId());

            ps.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }



    @Override

    public List<Peca> listar() {

        List<Peca> pecas = new ArrayList<>();

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM peca";



        try (PreparedStatement ps = con.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {



            while (rs.next()) {

                Peca peca = new Peca();

                peca.setId(rs.getInt("idPeca"));

                peca.setNome(rs.getString("nome"));

                peca.setPreco(rs.getDouble("preco"));

                peca.setFabricante(rs.getString("fabricante"));



                pecas.add(peca);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return pecas;

    }


    public Peca buscarId(int id) {

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM peca WHERE idPeca = ?";



        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Peca peca = new Peca();

                    peca.setId(rs.getInt("idPeca"));

                    peca.setNome(rs.getString("nome"));

                    peca.setPreco(rs.getDouble("preco"));

                    peca.setFabricante(rs.getString("fabricante"));

                    return peca;

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }



    public List<Peca> buscarNome(String nome) {

        List<Peca> pecas = new ArrayList<>();

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM peca WHERE nome LIKE ?";



        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nome + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Peca peca = new Peca();

                    peca.setId(rs.getInt("idPeca"));

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

        con = BaseDAO.getConnection();

        String sql = "SELECT * FROM peca WHERE fabricante LIKE ?";



        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + fabricante + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Peca peca = new Peca();

                    peca.setId(rs.getInt("idPeca"));

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

        con = BaseDAO.getConnection();



        StringBuilder sql = new StringBuilder(

                "SELECT DISTINCT p.idPeca, p.nome, p.preco, p.fabricante " +

                        "FROM peca p " +

                        "LEFT JOIN orcamento_pecas op ON p.idPeca = op.idPeca " +

                        "LEFT JOIN orcamento o ON op.idOrcamento = o.idOrcamento " +

                        "LEFT JOIN veiculo v ON o.placaVeiculo = v.placa " +

                        "WHERE 1=1"

        );



        if (nome != null && !nome.trim().isEmpty()) {

            sql.append(" AND p.nome LIKE ?");

        }

        if (fabricante != null && !fabricante.trim().isEmpty()) {

            sql.append(" AND p.fabricante LIKE ?");

        }

        if (automovel != null && !automovel.trim().isEmpty()) {

            sql.append(" AND v.marca LIKE ?"); // Filtra pela marca/modelo do veículo associado

        }



        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;



            if (nome != null && !nome.trim().isEmpty()) {

                ps.setString(paramIndex++, "%" + nome + "%");

            }

            if (fabricante != null && !fabricante.trim().isEmpty()) {

                ps.setString(paramIndex++, "%" + fabricante + "%");

            }

            if (automovel != null && !automovel.trim().isEmpty()) {

                ps.setString(paramIndex++, "%" + automovel + "%");

            }



            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Peca peca = new Peca();

                    peca.setId(rs.getInt("idPeca"));

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
