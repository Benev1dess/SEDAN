package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.services.OrdemServicoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class OrdemServicoEditarController {

    @FXML private TextField tfOrcamentoId;
    @FXML private CheckBox chkFinalizada;
    @FXML private CheckBox chkPago;
    @FXML private Label lblErro;

    private final OrdemServicoService osService = new OrdemServicoService();
    private OrdemServico osOriginal;
    private Runnable aoSalvar;

    public void setAoSalvar(Runnable aoSalvar) { this.aoSalvar = aoSalvar; }

    public void setOrdemServico(OrdemServico os) {
        this.osOriginal = os;
        if (os != null) {
            tfOrcamentoId.setText(os.getOrcamento() != null ? String.valueOf(os.getOrcamento().getId()) : "");
            chkFinalizada.setSelected(os.isFinalizada());
            chkPago.setSelected(os.isPago());
        }
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        try {
            // Configura os estados na service para disparar suas validações nativas
            osService.setOrcamento(osOriginal.getOrcamento());
            osService.setFinalizada(chkFinalizada.isSelected());
            osService.setPago(chkPago.isSelected());

            osOriginal.setFinalizada(chkFinalizada.isSelected());
            osOriginal.setPago(chkPago.isSelected());

            osService.editarOrdem(osOriginal.getOrcamento().getId(), osService);

            if (aoSalvar != null) aoSalvar.run();
            onCancelar();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            lblErro.setText(ex.getMessage());
        }
    }

    @FXML private void onCancelar() { ((Stage) tfOrcamentoId.getScene().getWindow()).close(); }
}