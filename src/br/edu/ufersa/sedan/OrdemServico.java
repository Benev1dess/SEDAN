package br.edu.ufersa.sedan;

import java.time.LocalDate;

public class OrdemServico {

    private Orcamento orcamento;
    private boolean finalizada;
    private boolean pago;
    private LocalDate data;

    public void setFinalizada(boolean finalizada) {

        if (!finalizada && this.pago) {
            throw new IllegalStateException("Não é possível reabrir uma ordem que já foi paga.");
        }

        if (finalizada && this.orcamento == null) {
            throw new IllegalStateException("Não é possível finalizar uma ordem sem um orçamento vinculado.");
        }

        this.finalizada = finalizada;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("A data não pode ser nula.");
        }
        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data da ordem não pode ser no futuro.");
        }
        this.data = data;
    }

    public void setOrcamento(Orcamento orcamento) {
        if (orcamento == null) {
            throw new IllegalArgumentException("Toda ordem de serviço precisa de um orçamento.");
        }
        this.orcamento = orcamento;
    }

    public void setPago(boolean pago) {

        if (pago && !this.finalizada) {
            throw new IllegalStateException("Operação inválida: A ordem de serviço deve estar FINALIZADA antes de registrar o pagamento.");
        }

        this.pago = pago;
    }

}