package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.services.RelatorioFaturamento;
import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.entities.Servico;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioFaturamentoService implements Relatorio {

    private final OrdemServicoService osService = new OrdemServicoService();

    @Override
    public RelatorioFaturamento gerar() {
        List<OrdemServico> ordensFaturadas = osService.listarTodasOrdens().stream()
                .filter(os -> os.isFinalizada() && os.isPago())
                .collect(Collectors.toList());

        double totalFaturado = calcularFaturamentoTotal(ordensFaturadas);
        double totalPecas = calcularTotalPecas(ordensFaturadas);
        double totalServicos = calcularTotalServicos(ordensFaturadas);
        long totalClientes = contarClientesUnicos(ordensFaturadas);

        return new RelatorioFaturamento(totalFaturado, totalPecas, totalServicos, totalClientes, ordensFaturadas);
    }

    private double calcularFaturamentoTotal(List<OrdemServico> ordens) {
        return ordens.stream()
                .mapToDouble(os -> os.getOrcamento() != null ? os.getOrcamento().calcularTotal() : 0.0)
                .sum();
    }

    private double calcularTotalPecas(List<OrdemServico> ordens) {
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

    private double calcularTotalServicos(List<OrdemServico> ordens) {
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

    private long contarClientesUnicos(List<OrdemServico> ordens) {
        return ordens.stream()
                .filter(os -> os.getOrcamento() != null && os.getOrcamento().getVeiculo() != null && os.getOrcamento().getVeiculo().getDono() != null)
                .map(os -> os.getOrcamento().getVeiculo().getDono().getCpf())
                .distinct()
                .count();
    }
}