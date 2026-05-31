package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.DAO.PecaDAO;
import br.edu.ufersa.sedan.model.entities.Peca;

import java.util.List;

public class PecaService {

    private PecaDAO pecaDAO = new PecaDAO();

    public void adicionar(Peca peca) {
        if (peca == null) {
            throw new IllegalArgumentException("A peça não pode ser nula.");
        }
        pecaDAO.inserir(peca);
    }

    public void alterar(Peca peca) {
        if (peca == null) {
            throw new IllegalArgumentException("A peça não pode ser nula.");
        }
        if (pecaDAO.buscarId(peca.getId()) == null) {
            throw new IllegalArgumentException("Peça não encontrada para alteração.");
        }
        pecaDAO.alterar(peca);
    }

    public void deletar(Peca peca) {
        if (peca == null) {
            throw new IllegalArgumentException("A peça não pode ser nula.");
        }
        if (pecaDAO.buscarId(peca.getId()) == null) {
            throw new IllegalArgumentException("Peça não encontrada para exclusão.");
        }
        pecaDAO.deletar(peca);
    }

    public List<Peca> listarTodos() {
        return pecaDAO.listar();
    }

    public Peca buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return pecaDAO.buscarId(id);
    }

    public List<Peca> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome para busca não pode ser vazio.");
        }
        return pecaDAO.buscarNome(nome);
    }

    public List<Peca> buscarPorFabricante(String fabricante) {
        if (fabricante == null || fabricante.trim().isEmpty()) {
            throw new IllegalArgumentException("O fabricante para busca não pode ser vazio.");
        }
        return pecaDAO.buscarFabricante(fabricante);
    }

    public List<Peca> pesquisar(String nome, String fabricante, String automovel) {
        return pecaDAO.pesquisar(nome, fabricante, automovel);
    }
}