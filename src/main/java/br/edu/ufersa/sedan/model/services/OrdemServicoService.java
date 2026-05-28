package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.entities.Servico;

import java.util.ArrayList;
import java.util.List;

public class OrdemServicoService {

    public void adicionarServico(Servico.OrdemServico ordem, Servico s) {
        if (ordem.isFinalizada()) {
            throw new IllegalStateException("Não é permitido adicionar serviços a uma ordem finalizada.");
        }
        ordem.getServicos().add(s);
    }

    public void finalizarOrdem(Servico.OrdemServico ordem) {
        if (ordem.getOrcamento() == null) {
            throw new IllegalStateException("A ordem não pode ser finalizada sem um orçamento vinculado.");
        }
        ordem.setFinalizada(true);
    }

    public void registrarPagamento(Servico.OrdemServico ordem) {
        if (!ordem.isFinalizada()) {
            throw new IllegalStateException("O pagamento só pode ser registrado se a ordem estiver finalizada.");
        }
        ordem.setPago(true);
    }

    public void reabrirOrdem(Servico.OrdemServico ordem) {
        if (ordem.isPago()) {
            throw new IllegalStateException("Uma ordem já paga não pode ser reaberta para modificações.");
        }
        ordem.setFinalizada(false);
    }

    public void editarOrdem(int id, Servico.OrdemServico nova) {
        // Lógica de persistência para atualização dos dados da ordem
        System.out.println("Atualizando dados da ordem identificada pelo ID: " + id);
    }

    public void excluirOrdem(int id) {
        // Lógica para remoção da ordem do sistema
    }

    public List<Servico.OrdemServico> buscarOrdemPorVeiculo(String placa) {
        // Lógica de filtragem de ordens por placa do veículo
        return new ArrayList<>();
    }

    public List<Servico.OrdemServico> buscarOrdemPorCliente(String cpf) {
        // Lógica de filtragem de ordens por identificação do cliente
        return new ArrayList<>();
    }
}
