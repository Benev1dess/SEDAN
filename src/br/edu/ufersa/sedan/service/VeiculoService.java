package br.edu.ufersa.sedan.service;
import br.edu.ufersa.sedan.model.Veiculo;
import br.edu.ufersa.sedan.model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class VeiculoService {
    private List<Veiculo> veiculos = new ArrayList<>(); // Guardar veículos existentes

    // Cadastrar veículo
    public void cadastrarVeiculo(Veiculo veiculo){
        if(veiculo != null && buscarPorPlaca(veiculo.getPlaca()) == null){
            veiculos.add(veiculo);
        }
    }

    // Pesquisar por placa
    public Veiculo buscarPorPlaca(String placa){
        for(Veiculo v : veiculos){
            if(v.getPlaca().equals(placa)){
                return v;
            }
        }
        return null;
    }

    // Pesquisar usando dono
    public List<Veiculo> buscarPorDono(String cpf){
        List<Veiculo> encontrados = new ArrayList<>();
        for(Veiculo v : veiculos){
            if(v.getDono() != null &&
                    v.getDono().getCpf().equals(cpf)){
                encontrados.add(v);
            }
        }
        return encontrados;
    }

    // Listar veículos
    public List<Veiculo> listarVeiculos(){
        return new ArrayList<>(veiculos);
    }

    // Alterar TODAS AS INFORMAÇÕES
    public void alterarVeiculo(String placaAntiga, String novaMarca, String novaCor, String novaPlaca, int novoAno, double novoKm){
        Veiculo v = buscarPorPlaca(placaAntiga);
        if(v != null){
            if(
                    placaAntiga.equals(novaPlaca)
                            || buscarPorPlaca(novaPlaca) == null
            ){
                v.setMarca(novaMarca);
                v.setCor(novaCor);
                v.setPlaca(novaPlaca);
                v.setAno(novoAno);
                v.setKm(novoKm);
            }
        }
    }

    public void alterarMarca(String placa, String novaMarca){
        Veiculo v = buscarPorPlaca(placa);
        if(v != null){
            v.setMarca(novaMarca);
        }
    }

    //Alterar Cor
    public void alterarCor(String placa, String novaCor){
        Veiculo v = buscarPorPlaca(placa);
        if(v != null){
            v.setCor(novaCor);
        }
    }

    public void alterarPlaca(String placa, String novaPlaca){
        Veiculo v = buscarPorPlaca(placa);
        if(v != null && buscarPorPlaca(novaPlaca) == null){
            v.setPlaca(novaPlaca);
        }
    }

    //Alterar ano
    public void alterarAno(String placa, int novoAno){
        Veiculo v = buscarPorPlaca(placa);
        if(v != null){
            v.setAno(novoAno);
        }
    }

    // Alterar Quilometragem
    public void alterarKm(String placa, double novoKm) {
        Veiculo v = buscarPorPlaca(placa);
        if (v != null) {
            v.setKm(novoKm);
        }
    }

    // Deletar veiculo
    public boolean removerVeiculo(String placa){
        Veiculo v = buscarPorPlaca(placa);
        if(v != null){
            veiculos.remove(v);
            return true;
        }
        return false;
    }

}
