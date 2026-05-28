package br.edu.ufersa.sedan.service;
import br.edu.ufersa.sedan.model.Cliente;
import br.edu.ufersa.sedan.model.Endereco; // Para podermos alterar o endereço do cliente
import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private List<Cliente> clientes = new ArrayList<>(); // Guardar clientes existentes

    // Cadastrar
    public void cadastrarCliente(Cliente cliente){
        if(cliente != null && buscarPorCpf(cliente.getCpf()) == null){
            clientes.add(cliente);
        }
    }

    // Pesquisar por CPF
    public Cliente buscarPorCpf(String cpf){
        for(Cliente c : clientes){
            if(c.getCpf().equals(cpf)){
                return c;
            }
        }
        return null;
    }

    // Listar clientes
    public List<Cliente> listarClientes(){
        return new ArrayList<>(clientes);
    }

    // Editar TUDO
    public void alterarCliente(String cpfAntigo, String novoNome, String novoCpf, Endereco novoEndereco){
        Cliente c = buscarPorCpf(cpfAntigo);
        if(c != null){
            if(cpfAntigo.equals(novoCpf) || buscarPorCpf(novoCpf) == null){
                c.setNome(novoNome);
                c.setCpf(novoCpf);
                c.setEndereco(novoEndereco);
            }
        }
    }

    //Editar nome
    public void alterarNome(String cpf, String novoNome){
        Cliente c = buscarPorCpf(cpf);
        if(c != null){
            c.setNome(novoNome);
        }
    }

    //Editar CPF
    public void alterarCpf(String cpfAtual, String novoCpf){
        Cliente c = buscarPorCpf(cpfAtual);
        if(c != null && buscarPorCpf(novoCpf) == null){
            c.setCpf(novoCpf);
        }
    }

    // Deletar
    public boolean removerCliente(String cpf){
        Cliente c = buscarPorCpf(cpf);
        if(c != null){
            clientes.remove(c);
            return true;
        }
        return false;
    }

    // MÉTODOS DE ENDEREÇO ABAIXO
    // Não implementei cadastrar pois os sets de endereço já fazem isso
    // Não implementei deletar pois o cliente DEVE ter um endereço

    // Alterar endereço inteiro
    public void alterarEndereco(String cpf, Endereco novoEndereco) {
        Cliente c = buscarPorCpf(cpf);
        if (c != null) {
            c.setEndereco(novoEndereco);
        }
    }

    // Alterar somente rua
    public void alterarRua(String cpf, String novaRua){
        Cliente c = buscarPorCpf(cpf);
        if(c != null){
            c.getEndereco().setRua(novaRua);
        }
    }

    // Alterar somente bairro
    public void alterarBairro(String cpf, String novoBairro){
        Cliente c = buscarPorCpf(cpf);
        if(c != null){
            c.getEndereco().setBairro(novoBairro);
        }

    }

    // Alterar somente número
    public void alterarNumero(String cpf, int novoNumero){
        Cliente c = buscarPorCpf(cpf);
        if(c != null){
            c.getEndereco().setNum(novoNumero);
        }

    }
}
