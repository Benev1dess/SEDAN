package br.edu.ufersa.sedan.model.services;
import br.edu.ufersa.sedan.model.DAO.ClienteDAO;
import br.edu.ufersa.sedan.model.DAO.EnderecoDAO;
import br.edu.ufersa.sedan.model.entities.Cliente;
import br.edu.ufersa.sedan.model.entities.Endereco;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private ClienteDAO clienteDAO = new ClienteDAO();
    private EnderecoDAO enderecoDAO = new EnderecoDAO();

    public void cadastrarCliente(Cliente cliente) {

        if (cliente == null) {
            return;
        }

        // valida CPF duplicado
        if (buscarPorCpf(cliente.getCpf()) != null) {
            System.out.println("CPF já cadastrado!");
            return;
        }

        // salva endereço primeiro (se existir DAO funcionando pra isso)
        Endereco endereco = cliente.getEndereco();

        if (endereco != null) {
            enderecoDAO.inserir(endereco);
        }

        // salva cliente
        clienteDAO.inserir(cliente);
    }

    public Cliente buscarPorCpf(String cpf) {
        ArrayList<Cliente> clientes = clienteDAO.buscarCpf(cpf);
        if (clientes.isEmpty()) {
            return null;
        }
        return clientes.get(0);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listar();
    }

    public void alterarCliente(String cpfAntigo,
                               String novoNome,
                               String novoCpf,
                               Endereco novoEndereco) {
        Cliente cliente = buscarPorCpf(cpfAntigo);
        if (cliente != null) {
            Cliente cpfExistente = buscarPorCpf(novoCpf);
            if (cpfAntigo.equals(novoCpf) || cpfExistente == null) {

                cliente.setNome(novoNome);
                cliente.setCpf(novoCpf);
                cliente.setEndereco(novoEndereco);

                clienteDAO.alterar(cliente);
            }
        }
    }

    public void alterarNome(String cpf, String novoNome) {
        Cliente cliente = buscarPorCpf(cpf);
        if (cliente != null) {
            cliente.setNome(novoNome);
            clienteDAO.alterar(cliente);
        }
    }

    public void alterarCpf(String cpfAtual, String novoCpf) {
        Cliente cliente = buscarPorCpf(cpfAtual);
        if (cliente != null &&
                buscarPorCpf(novoCpf) == null) {
            cliente.setCpf(novoCpf);
            clienteDAO.alterar(cliente);
        }
    }

    public boolean removerCliente(String cpf) {
        Cliente cliente = buscarPorCpf(cpf);
        if (cliente != null) {
            clienteDAO.deletar(cliente);
            return true;
        }

        return false;
    }

    public void alterarEndereco(String cpf, Endereco novoEndereco) {
        Cliente cliente = buscarPorCpf(cpf);
        if (cliente != null) {
            cliente.setEndereco(novoEndereco);
            clienteDAO.alterar(cliente);
        }
    }

    public void alterarRua(String cpf, String novaRua) {
        Cliente cliente = buscarPorCpf(cpf);
        if (cliente != null && cliente.getEndereco() != null) {
            cliente.getEndereco().setRua(novaRua);
            enderecoDAO.alterar(cliente.getEndereco());
        }
    }

    public void alterarBairro(String cpf, String novoBairro) {
        Cliente cliente = buscarPorCpf(cpf);
        if (cliente != null && cliente.getEndereco() != null) {
            cliente.getEndereco().setBairro(novoBairro);
            enderecoDAO.alterar(cliente.getEndereco());
        }
    }

    public void alterarNumero(String cpf, int novoNumero) {
        Cliente cliente = buscarPorCpf(cpf);
        if (cliente != null && cliente.getEndereco() != null) {
            cliente.getEndereco().setNum(novoNumero);
            enderecoDAO.alterar(cliente.getEndereco());
        }
    }
}