package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.services.OrdemServicoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class OrdemServicoAdicionarController {

    @FXML private TextField txtBuscaOrcamento;
    @FXML private CheckBox chkFinalizada;
    @FXML private CheckBox chkPago;
    @FXML private Label lblErro;

    private final OrdemServicoService osService = new OrdemServicoService();
    private Orcamento orcamentoSelecionado; // Definido ao buscar/selecionar na regra do seu sistema
    private Runnable aoSalvar;

    public void setAoSalvar(Runnable aoSalvar) { this.aoSalvar = aoSalvar; }

    @FXML
    private void onBuscarOrcamento() {
        lblErro.setText("");
        String idText = txtBuscaOrcamento.getText().trim();
        if(idText.isEmpty()) return;
        try {
            int idOrcamento = Integer.parseInt(idText);
            // Simulação de busca do orçamento ativo de acordo com seu ecossistema
            Orcamento o = new Orcamento();
            o.setId(idOrcamento);
            this.orcamentoSelecionado = o;
            lblErro.setText("Orçamento " + idOrcamento + " vinculado com sucesso.");
        } catch (NumberFormatException e) {
            lblErro.setText("Digite um ID de Orçamento válido.");
        }
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        try {
            if (orcamentoSelecionado == null) {
                lblErro.setText("Toda ordem de serviço precisa de um orçamento vinculado.");
                return;
            }

            OrdemServico novaOS = new OrdemServico();
            novaOS.setOrcamento(orcamentoSelecionado);

            // Usando validações estritas da sua Service
            osService.setOrcamento(orcamentoSelecionado);
            osService.setFinalizada(chkFinalizada.isSelected());
            osService.setPago(chkPago.isSelected());

            novaOS.setFinalizada(chkFinalizada.isSelected());
            novaOS.setPago(chkPago.isSelected());

            osService.salvarNoBanco(novaOS);

            if (aoSalvar != null) aoSalvar.run();
            onCancelar();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            lblErro.setText(ex.getMessage());
        }
    }

    @FXML private void onCancelar() { ((Stage) txtBuscaOrcamento.getScene().getWindow()).close(); }
}