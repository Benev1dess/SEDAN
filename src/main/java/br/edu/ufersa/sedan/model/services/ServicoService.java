package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.DAO.ServicoDAO;
import br.edu.ufersa.sedan.model.entities.Servico;

import java.util.List;

public class ServicoService {

    private ServicoDAO servicoDAO = new ServicoDAO();

    public void adicionar(Servico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("O serviço não pode ser nulo.");
        }
        if (servico.getNome() == null || servico.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do serviço é obrigatório.");
        }
        if (servico.getPreco() < 0) {
            throw new IllegalArgumentException("O preço do serviço não pode ser negativo.");
        }
        servicoDAO.inserir(servico);
    }

    public List<Servico> listarTodos() {
        return servicoDAO.listarTodos();
    }

    public void alterar(String nomeAntigo, Servico novoServico) {
        if (nomeAntigo == null || nomeAntigo.isBlank()) {
            throw new IllegalArgumentException("O nome do serviço a ser alterado é inválido.");
        }
        if (novoServico == null) {
            throw new IllegalArgumentException("Os novos dados do serviço não podem ser nulos.");
        }
        if (novoServico.getNome() == null || novoServico.getNome().isBlank()) {
            throw new IllegalArgumentException("O novo nome do serviço é obrigatório.");
        }
        if (novoServico.getPreco() < 0) {
            throw new IllegalArgumentException("O novo preço do serviço não pode ser negativo.");
        }
        servicoDAO.atualizar(nomeAntigo, novoServico);
    }

    public void deletar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do serviço para exclusão é inválido.");
        }
        servicoDAO.deletar(nome);
    }

    public List<Servico> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome para a busca não pode ser vazio.");
        }
        return servicoDAO.buscarPorNome(nome);
    }
}