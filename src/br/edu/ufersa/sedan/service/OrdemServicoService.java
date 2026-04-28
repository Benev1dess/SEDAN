package br.edu.ufersa.sedan.service;

import br.edu.ufersa.sedan.model.OrdemServico;
import br.edu.ufersa.sedan.model.Servico;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoService {

    // Regra: Adicionar serviço à ordem
    public void adicionarServico(OrdemServico ordem, Servico s) {
        if (ordem.isFinalizada()) {
            throw new IllegalStateException("Não se pode adicionar serviços a uma ordem finalizada.");
        }
        ordem.getServicos().add(s);
    }

    // Regra: Finalizar a ordem (Exige orçamento)
    public void finalizarOrdem(OrdemServico ordem) {
        if (ordem.getOrcamento() == null) {
            throw new IllegalStateException("Impossível finalizar: Ordem sem orçamento vinculado.");
        }
        ordem.setFinalizada(true);
    }

    // Regra: Pagar a ordem (Deve estar finalizada)
    public void registrarPagamento(OrdemServico ordem) {
        if (!ordem.isFinalizada()) {
            throw new IllegalStateException("A ordem deve ser FINALIZADA antes do pagamento.");
        }
        ordem.setPago(true);
    }

    // Regra: Reabrir ordem (Impossível se já estiver paga)
    public void reabrirOrdem(OrdemServico ordem) {
        if (ordem.isPago()) {
            throw new IllegalStateException("Ordem já paga não pode ser reaberta.");
        }
        ordem.setFinalizada(false);
    }

    // --- Métodos de Gestão (Baseados no seu Diagrama) ---

    public void editarOrdem(int id, OrdemServico nova) {
        // No IntelliJ, aqui implementaria a lógica de persistência (BD)
        System.out.println("A atualizar ordem ID: " + id);
    }

    public void excluirOrdem(int id) {
        // Lógica para apagar a ordem
    }

    public List<OrdemServico> buscarOrdemPorVeiculo(String placa) {
        // Simulação de procura
        return new ArrayList<>();
    }

    public List<OrdemServico> buscarOrdemPorCliente(String cpf) {
        // Simulação de procura
        return new ArrayList<>();
    }
}