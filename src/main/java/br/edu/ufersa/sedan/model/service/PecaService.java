package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.entities.Peca;

import java.util.ArrayList;
import java.util.List;

public class PecaService {

    private List<Peca> pecas = new ArrayList<>();

    // CREATE
    public void adicionar(Peca peca) {
        pecas.add(peca);
    }

    // READ - buscar por nome (primeiro encontrado)
    public Peca buscarPorNome(String nome) {
        for (Peca p : pecas) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    // READ - listar todas
    public List<Peca> listar() {
        return new ArrayList<>(pecas);
    }

    // UPDATE
    public boolean editar(String nome, Peca nova) {
        for (int i = 0; i < pecas.size(); i++) {
            if (pecas.get(i).getNome().equalsIgnoreCase(nome)) {
                pecas.set(i, nova);
                return true;
            }
        }
        return false;
    }

    // DELETE
    public boolean excluir(String nome) {
        Peca p = buscarPorNome(nome);
        if (p != null) {
            pecas.remove(p);
            return true;
        }
        return false;
    }

    // EXTRA (muito bom)
    public List<Peca> buscarTodasPorNome(String nome) {
        List<Peca> resultado = new ArrayList<>();

        for (Peca p : pecas) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                resultado.add(p);
            }
        }

        return resultado;
    }
}
