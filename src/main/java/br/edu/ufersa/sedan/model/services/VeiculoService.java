package br.edu.ufersa.sedan.model.services;
import br.edu.ufersa.sedan.model.DAO.VeiculoDAO;
import br.edu.ufersa.sedan.model.entities.Veiculo;
import java.util.ArrayList;
import java.util.List;

public class VeiculoService {
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    public void cadastrarVeiculo(Veiculo veiculo) {
        if (veiculo != null &&
                buscarPorPlaca(veiculo.getPlaca()) == null) {
            veiculoDAO.inserir(veiculo);
        }
    }

    public Veiculo buscarPorPlaca(String placa) {
        ArrayList<Veiculo> veiculos =
                veiculoDAO.buscarPorPlaca(placa);
        if (veiculos.isEmpty()) {
            return null;
        }

        return veiculos.get(0);
    }

    public List<Veiculo> buscarPorDono(String cpf) {
        ArrayList<Veiculo> encontrados = new ArrayList<>();
        for (Veiculo v : veiculoDAO.listar()) {
            if (v.getDono() != null &&
                    v.getDono().getCpf().equals(cpf)) {
                encontrados.add(v);
            }
        }

        return encontrados;
    }

    public List<Veiculo> listarVeiculos() {
        return veiculoDAO.listar();
    }

    public void alterarVeiculo(String placaAntiga, String novaMarca, String novaCor, String novaPlaca, int novoAno, int novoKm) {
        Veiculo v = buscarPorPlaca(placaAntiga);
        if (v != null) {

            if (placaAntiga.equals(novaPlaca) || buscarPorPlaca(novaPlaca) == null) {

                v.setMarca(novaMarca);
                v.setCor(novaCor);
                v.setPlaca(novaPlaca);
                v.setAno(novoAno);
                v.setKm(novoKm);

                veiculoDAO.alterar(v);
            }
        }
    }

    public void alterarMarca(String placa, String novaMarca) {
        Veiculo v = buscarPorPlaca(placa);

        if (v != null) {
            v.setMarca(novaMarca);
            veiculoDAO.alterar(v);
        }
    }

    public void alterarCor(String placa, String novaCor) {

        Veiculo v = buscarPorPlaca(placa);
        if (v != null) {

            v.setCor(novaCor);

            veiculoDAO.alterar(v);
        }
    }

    public void alterarPlaca(String placa, String novaPlaca) {
        Veiculo v = buscarPorPlaca(placa);
        if (v != null && buscarPorPlaca(novaPlaca) == null) {
            v.setPlaca(novaPlaca);
            veiculoDAO.alterar(v);
        }
    }

    public void alterarAno(String placa, int novoAno) {
        Veiculo v = buscarPorPlaca(placa);
        if (v != null) {
            v.setAno(novoAno);
            veiculoDAO.alterar(v);
        }
    }

    public void alterarKm(String placa, int novoKm) {
        Veiculo v = buscarPorPlaca(placa);
        if (v != null) {
            v.setKm(novoKm);
            veiculoDAO.alterar(v);
        }
    }

    public boolean removerVeiculo(String placa) {
        Veiculo v = buscarPorPlaca(placa);
        if (v != null) {

            veiculoDAO.deletar(v);
            return true;
        }
        return false;
    }
}
