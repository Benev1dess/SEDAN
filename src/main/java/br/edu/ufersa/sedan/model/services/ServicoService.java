package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.entities.Endereco;
import br.edu.ufersa.sedan.model.entities.Servico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServicoService {

    private List<Servico> servicos = new ArrayList<>();


    public void adicionar(Servico s) {
        servicos.add(s);
    }

    public List<Servico> listar() {
        return new ArrayList<>(servicos);
    }

    public boolean editar(String nomeAntigo, Servico novo) {
        for (int i = 0; i < servicos.size(); i++) {
            if (servicos.get(i).getNome().equalsIgnoreCase(nomeAntigo)) {
                servicos.set(i, novo);
                return true;
            }
        }
        return false;
    }

    public boolean excluir(String nome) {
        return servicos.removeIf(s -> s.getNome().equalsIgnoreCase(nome));
    }


    public List<Servico> pesquisarPorVeiculo(String placa, List<Endereco.Orcamento> todosOrcamentos) {
        List<Servico> resultado = new ArrayList<>();
        for (Endereco.Orcamento o : todosOrcamentos) {
            if (o.getVeiculo().getPlaca().equalsIgnoreCase(placa)) {
                resultado.addAll(o.getServicos());
            }
        }
        return resultado;
    }

    public List<Servico> pesquisarPorCliente(String nomeCliente, List<Endereco.Orcamento> todosOrcamentos) {
        List<Servico> resultado = new ArrayList<>();
        for (Endereco.Orcamento o : todosOrcamentos) {
            if (o.getVeiculo().getDono().getNome().equalsIgnoreCase(nomeCliente)) {
                resultado.addAll(o.getServicos());
            }
        }
        return resultado;
    }

    public List<Servico> pesquisarPorPeriodo(LocalDate inicio, LocalDate fim, List<Endereco.Orcamento> todosOrcamentos) {
        List<Servico> resultado = new ArrayList<>();
        for (Endereco.Orcamento o : todosOrcamentos) {
            LocalDate data = o.getData();
            if ((data.isEqual(inicio) || data.isAfter(inicio)) && (data.isEqual(fim) || data.isBefore(fim))) {
                resultado.addAll(o.getServicos());
            }
        }
        return resultado;
    }
}