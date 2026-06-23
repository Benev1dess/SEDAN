package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.DAO.OrdemDeServicoDAO;
import br.edu.ufersa.sedan.model.entities.OrdemServico;
import java.util.List;

public class OrdemServicoService {

    private final OrdemDeServicoDAO osDAO = new OrdemDeServicoDAO();

    // Método definitivo de inserção de O.S. a partir da entidade preenchida na View
    public void adicionar(OrdemServico os) {
        if (os == null) {
            throw new IllegalArgumentException("A Ordem de Serviço não pode ser nula.");
        }
        if (os.getOrcamento() == null) {
            throw new IllegalArgumentException("A O.S. precisa estar vinculada a um orçamento válido.");
        }

        // Verifica se já existe uma O.S. para este mesmo orçamento para evitar duplicidade
        boolean jaExiste = osDAO.listarTodos().stream()
                .anyMatch(ordem -> ordem.getOrcamento() != null && ordem.getOrcamento().getId() == os.getOrcamento().getId());

        if (jaExiste) {
            throw new IllegalStateException("Já existe uma Ordem de Serviço gerada para este Orçamento.");
        }

        osDAO.inserir(os);
    }

    public void editarOrdem(OrdemServico os) {
        if (os == null || os.getOrcamento() == null) {
            throw new IllegalArgumentException("Dados inválidos para alteração.");
        }
        osDAO.atualizarStatus(os);
    }

    public void excluirOrdem(int idOrcamento) {
        if (idOrcamento <= 0) {
            throw new IllegalArgumentException("ID de orçamento inválido.");
        }
        osDAO.deletar(idOrcamento);
    }

    public List<OrdemServico> listarTodasOrdens() {
        return osDAO.listarTodos();
    }
}