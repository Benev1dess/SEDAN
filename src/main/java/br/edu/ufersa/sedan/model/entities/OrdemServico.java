package br.edu.ufersa.sedan.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    private Orcamento orcamento;
    private boolean finalizada;
    private boolean pago;
    private LocalDate data;
    private List<Servico> servicos = new ArrayList<>();

    public OrdemServico() {
        this.data = LocalDate.now();
        this.finalizada = false;
        this.pago = false;
    }

    // Getters
    public Orcamento getOrcamento() { return orcamento; }
    public boolean isFinalizada() { return finalizada; }
    public boolean isPago() { return pago; }
    public LocalDate getData() { return data; }
    public List<Servico> getServicos() { return servicos; }

    // Setters com garantias de integridade básica
    public void setOrcamento(Orcamento orcamento) {
        if (orcamento == null) throw new IllegalArgumentException("Orçamento não pode ser nulo.");
        this.orcamento = orcamento;
    }

    public void setData(LocalDate data) {
        if (data == null || data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data inválida ou no futuro.");
        }
        this.data = data;
    }

    public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }
    public void setPago(boolean pago) { this.pago = pago; }
}
