package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.services.OrdemServicoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class OrdemServicoExcluirController {

    public static Stage stage;
    @FXML private Label lblMensagem;

    private final OrdemServicoService osService = new OrdemServicoService();
    private OrdemServico osParaExcluir;
    private Runnable aoExcluir;

    public void setAoExcluir(Runnable aoExcluir) { this.aoExcluir = aoExcluir; }

    public void setOrdemServico(OrdemServico os) {
        this.osParaExcluir = os;
        if (os != null && os.getOrcamento() != null) {
            lblMensagem.setText("Deseja realmente excluir a Ordem de Serviço vinculada ao Orçamento Nº " + os.getOrcamento().getId() + "?");
        }
    }

    @FXML
    private void onConfirmar() {
        if (osParaExcluir != null && osParaExcluir.getOrcamento() != null) {
            try {
                osService.excluirOrdem(osParaExcluir.getOrcamento().getId());
                if (aoExcluir != null) aoExcluir.run();
                onCancelar();
            } catch (IllegalStateException | IllegalArgumentException e) {
                lblMensagem.setText(e.getMessage());
            }
        }
    }

    @FXML private void onCancelar() { if (stage != null) stage.close(); }
}