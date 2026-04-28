package br.edu.ufersa.sedan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoService {

    private Orcamento orcamento;
    private boolean finalizada;
    private boolean pago;
    private LocalDate data;
    private List<Servico> servicos = new ArrayList<>();

    public OrdemServicoService() {
        this.data = LocalDate.now();
        this.finalizada = false;
        this.pago = false;
    }

    // --- Métodos de Integridade (Validações) ---

    public void setOrcamento(Orcamento orcamento) {
        if (orcamento == null) {
            throw new IllegalArgumentException("Toda ordem de serviço precisa de um orçamento vinculado.");
        }
        this.orcamento = orcamento;
    }

    public void setFinalizada(boolean finalizada) {
        if (!finalizada && this.pago) {
            throw new IllegalStateException("Não é possível reabrir uma ordem que já foi paga.");
        }

        if (finalizada && this.orcamento == null) {
            throw new IllegalStateException("Não é possível finalizar uma ordem sem um orçamento aprovado.");
        }

        this.finalizada = finalizada;
    }

    public void setPago(boolean pago) {
        if (pago && !this.finalizada) {
            throw new IllegalStateException("A ordem deve ser finalizada antes de registrar o pagamento.");
        }
        this.pago = pago;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("A data não pode ser nula.");
        }
        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data da ordem não pode ser uma data futura.");
        }
        this.data = data;
    }

    // --- Métodos de Operação (Lógica de Negócio) ---

    public void adicionarServico(Servico s) {
        if (s == null) {
            throw new IllegalArgumentException("O serviço a ser adicionado não pode ser nulo.");
        }
        if (this.finalizada) {
            throw new IllegalStateException("Não é permitido adicionar serviços a uma ordem já finalizada.");
        }
        this.servicos.add(s);
    }

    public void editarOrdem(int id, OrdemServicoService novaOrdem) {
        if (this.pago) {
            throw new IllegalStateException("Ordens pagas não podem ser editadas.");
        }
        // Lógica para atualizar os campos da ordem atual com os dados da novaOrdem
    }

    public void excluirOrdem(int id) {
        if (this.pago) {
            throw new IllegalStateException("Não é permitido excluir uma ordem que já foi faturada (paga).");
        }
        // Lógica para remover do banco de dados ou lista
    }

    public List<OrdemServicoService> buscarOrdemPorVeiculo(String placa) {
        if (placa == null || placa.isEmpty()) {
            throw new IllegalArgumentException("A placa é obrigatória para a busca.");
        }
        return new ArrayList<>();
    }

    public List<OrdemServicoService> buscarOrdemPorCliente(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("O CPF é obrigatório para a busca.");
        }
        return new ArrayList<>();
    }

    // --- Getters ---

    public Orcamento getOrcamento() { return orcamento; }
    public boolean isFinalizada() { return finalizada; }
    public boolean isPago() { return pago; }
    public LocalDate getData() { return data; }
    public List<Servico> getServicos() { return new ArrayList<>(servicos); }
}