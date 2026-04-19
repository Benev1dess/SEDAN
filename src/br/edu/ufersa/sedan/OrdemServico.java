package br.edu.ufersa.sedan;

import java.time.LocalDate;

public class OrdemServico {

    private Orcamento orcamento;
    private boolean finalizada;
    private boolean pago;
    private LocalDate data;

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public LocalDate getData() {
        return data;
    }

    public boolean isPago() {
        return pago;
    }

    public boolean isFinalizada() {
        return finalizada;
    }


}