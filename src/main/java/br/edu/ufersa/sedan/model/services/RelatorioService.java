package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.entities.Servico;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioService {

    protected final OrdemServicoService osService = new OrdemServicoService();

    public List<OrdemServico> obterOrdensFaturadas() {
        return osService.listarTodasOrdens().stream()
                .filter(os -> os.isFinalizada() && os.isPago())
                .collect(Collectors.toList());
    }

    public double calcularFaturamentoTotal(List<OrdemServico> ordens) {
        return ordens.stream()
                .mapToDouble(os -> os.getOrcamento() != null ? os.getOrcamento().calcularTotal() : 0.0)
                .sum();
    }

    public double calcularTotalPecas(List<OrdemServico> ordens) {
        double total = 0.0;
        for (OrdemServico os : ordens) {
            if (os.getOrcamento() != null && os.getOrcamento().getPecas() != null) {
                for (Peca p : os.getOrcamento().getPecas()) {
                    total += p.getPreco();
                }
            }
        }
        return total;
    }

    public double calcularTotalServicos(List<OrdemServico> ordens) {
        double total = 0.0;
        for (OrdemServico os : ordens) {
            if (os.getOrcamento() != null && os.getOrcamento().getServicos() != null) {
                for (Servico s : os.getOrcamento().getServicos()) {
                    total += s.getPreco();
                }
            }
        }
        return total;
    }

    public long contarClientesUnicos(List<OrdemServico> ordens) {
        return ordens.stream()
                .filter(os -> os.getOrcamento() != null && os.getOrcamento().getVeiculo() != null && os.getOrcamento().getVeiculo().getDono() != null)
                .map(os -> os.getOrcamento().getVeiculo().getDono().getCpf())
                .distinct()
                .count();
    }
}