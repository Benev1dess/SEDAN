package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.entities.OrdemServico;
import java.util.List;

public class RelatorioFaturamento {
    private final double totalFaturado;
    private final double totalPecas;
    private final double totalServicos;
    private final long clientesAtendidos;
    private final List<OrdemServico> ordensComputadas;

    public RelatorioFaturamento(double totalFaturado, double totalPecas, double totalServicos, long clientesAtendidos, List<OrdemServico> ordensComputadas) {
        this.totalFaturado = totalFaturado;
        this.totalPecas = totalPecas;
        this.totalServicos = totalServicos;
        this.clientesAtendidos = clientesAtendidos;
        this.ordensComputadas = ordensComputadas;
    }

    public double getTotalFaturado() { return totalFaturado; }
    public double getTotalPecas() { return totalPecas; }
    public double getTotalServicos() { return totalServicos; }
    public long getClientesAtendidos() { return clientesAtendidos; }
    public List<OrdemServico> getOrdensComputadas() { return ordensComputadas; }
}