package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.DAO.OrcamentoDAO;
import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.services.OrcamentoService;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class OrcamentoExcluirController {

    private Orcamento target;
    private OrcamentoService service;
    private Runnable aoExcluir;
    private Stage stage;
    private final OrcamentoDAO dao = new OrcamentoDAO();

    public void setTarget(Orcamento o, OrcamentoService os, Runnable callback, Stage s) {
        this.target = o;
        this.service = os;
        this.aoExcluir = callback;
        this.stage = s;
    }

    @FXML
    private void onConfirmar() {
        if (target != null) {
            dao.deletar(target);
            if (service != null && target.getVeiculo() != null) {
                service.excluirPorPlaca(target.getPlacaVeiculo());
            }
            if (aoExcluir != null) aoExcluir.run();
        }
        fechar();
    }

    @FXML private void onCancelar() { fechar(); }
    private void fechar() { if (stage != null) stage.close(); }
}