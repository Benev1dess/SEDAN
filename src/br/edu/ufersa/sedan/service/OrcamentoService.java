package br.edu.ufersa.sedan.service;

import br.edu.ufersa.sedan.model.Orcamento;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrcamentoService {

    private List<Orcamento> orcamentos = new ArrayList<>();

    // --- OPERAÇÕES BÁSICAS (CRUD) ---

    public void adicionar(Orcamento o) {
        orcamentos.add(o);
    }

    public List<Orcamento> listarTodos() {
        return new ArrayList<>(orcamentos);
    }


    public boolean editarPorPlaca(String placa, Orcamento novoOrcamento) {
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


    public List<Orcamento> buscarPorVeiculo(String placa) {
        List<Orcamento> resultado = new ArrayList<>();
        for (Orcamento o : orcamentos) {
            if (o.getVeiculo().getPlaca().equalsIgnoreCase(placa)) {
                resultado.add(o);
            }
        }
        return resultado;
    }


    public List<Orcamento> buscarPorCliente(String nomeCliente) {
        List<Orcamento> resultado = new ArrayList<>();
        for (Orcamento o : orcamentos) {
            String nomeDono = o.getVeiculo().getCliente().getNome();
            if (nomeDono.toLowerCase().contains(nomeCliente.toLowerCase())) {
                resultado.add(o);
            }
        }
        return resultado;
    }

    public List<Orcamento> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Orcamento> resultado = new ArrayList<>();
        for (Orcamento o : orcamentos) {
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