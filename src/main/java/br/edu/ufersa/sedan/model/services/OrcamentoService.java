package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.entities.Endereco;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrcamentoService {

    private List<Endereco.Orcamento> orcamentos = new ArrayList<>();

    // --- OPERAÇÕES BÁSICAS (CRUD) ---

    public void adicionar(Endereco.Orcamento o) {
        orcamentos.add(o);
    }

    public List<Endereco.Orcamento> listarTodos() {
        return new ArrayList<>(orcamentos);
    }


    public boolean editarPorPlaca(String placa, Endereco.Orcamento novoOrcamento) {
        for (int i = 0; i < orcamentos.size(); i++) {
            if (orcamentos.get(i).getVeiculo().getPlaca().equalsIgnoreCase(placa)) {
                orcamentos.set(i, novoOrcamento);
                return true;
            }
        }
        return false;
    }

    public boolean excluirPorPlaca(String placa) {
        // Remove todos os orçamentos associados àquela placa
        return orcamentos.removeIf(o -> o.getVeiculo().getPlaca().equalsIgnoreCase(placa));
    }

    // --- PESQUISAS ESPECÍFICAS ---


    public List<Endereco.Orcamento> buscarPorVeiculo(String placa) {
        List<Endereco.Orcamento> resultado = new ArrayList<>();
        for (Endereco.Orcamento o : orcamentos) {
            if (o.getVeiculo().getPlaca().equalsIgnoreCase(placa)) {
                resultado.add(o);
            }
        }
        return resultado;
    }


    public List<Endereco.Orcamento> buscarPorCliente(String nomeCliente) {
        List<Endereco.Orcamento> resultado = new ArrayList<>();
        for (Endereco.Orcamento o : orcamentos) {
            String nomeDono = o.getVeiculo().getCliente().getNome();
            if (nomeDono.toLowerCase().contains(nomeCliente.toLowerCase())) {
                resultado.add(o);
            }
        }
        return resultado;
    }

    public List<Endereco.Orcamento> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Endereco.Orcamento> resultado = new ArrayList<>();
        for (Endereco.Orcamento o : orcamentos) {
            LocalDate dataDoc = o.getData();
            // Verifica se está dentro do intervalo
            if ((dataDoc.isEqual(inicio) || dataDoc.isAfter(inicio)) &&
                    (dataDoc.isEqual(fim) || dataDoc.isBefore(fim))) {
                resultado.add(o);
            }
        }
        return resultado;
    }
}