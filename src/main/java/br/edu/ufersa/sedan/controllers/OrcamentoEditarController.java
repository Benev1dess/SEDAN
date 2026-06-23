package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.DAO.OrcamentoDAO;
import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.entities.Servico;
import br.edu.ufersa.sedan.model.services.OrcamentoService;
import br.edu.ufersa.sedan.model.services.PecaService;
import br.edu.ufersa.sedan.model.services.ServicoService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class OrcamentoEditarController {

    @FXML private TextField tfVeiculo;
    @FXML private DatePicker dpData;
    @FXML private ListView<Peca> lvPecas;
    @FXML private ListView<Servico> lvServicos;
    @FXML private Label lblTotal;
    @FXML private Label lblErro;

    private Orcamento orcamentoTarget;
    private OrcamentoService orcamentoService;
    private final OrcamentoDAO dao = new OrcamentoDAO();
    private Runnable aoSalvar;

    public void setDados(Orcamento o, OrcamentoService os, PecaService ps, ServicoService ss, Runnable callback) {
        this.orcamentoTarget = o;
        this.orcamentoService = os;
        this.aoSalvar = callback;

        tfVeiculo.setText(o.getPlacaVeiculo());
        dpData.setValue(o.getData());

        lvPecas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvServicos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        lvPecas.setItems(FXCollections.observableArrayList(ps.listarTodos()));
        lvServicos.setItems(FXCollections.observableArrayList(ss.listarTodos()));

        marcarItensPreviamenteSalvos();
        atualizarTotalEstimado();

        lvPecas.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> atualizarTotalEstimado());
        lvServicos.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> atualizarTotalEstimado());
    }

    private void marcarItensPreviamenteSalvos() {
        if (orcamentoTarget.getListaPecas() != null) {
            for (Peca p : orcamentoTarget.getListaPecas()) {
                for (Peca item : lvPecas.getItems()) {
                    if (item != null && p != null && item.getId() == p.getId()) {
                        lvPecas.getSelectionModel().select(item);
                    }
                }
            }
        }

        if (orcamentoTarget.getServicos() != null) {
            for (Servico s : orcamentoTarget.getServicos()) {
                for (Servico item : lvServicos.getItems()) {
                    // CORREÇÃO DEFINITIVA: Remove o .getId() do Servico e compara os objetos diretamente
                    if (item != null && s != null && item.equals(s)) {
                        lvServicos.getSelectionModel().select(item);
                    }
                }
            }
        }
    }

    private void atualizarTotalEstimado() {
        double total = 0;
        for (Peca p : lvPecas.getSelectionModel().getSelectedItems()) { if (p != null) total += p.getPreco(); }
        for (Servico s : lvServicos.getSelectionModel().getSelectedItems()) { if (s != null) total += s.getPreco(); }
        lblTotal.setText(String.format("R$ %.2f", total));
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        if (dpData.getValue() == null) {
            lblErro.setText("A data de emissão não pode ficar vazia.");
            return;
        }

        orcamentoTarget.setData(dpData.getValue());
        orcamentoTarget.setDataOrcamento(dpData.getValue());

        orcamentoTarget.setListaPecas(new ArrayList<>(lvPecas.getSelectionModel().getSelectedItems()));
        orcamentoTarget.setServicos(new ArrayList<>(lvServicos.getSelectionModel().getSelectedItems()));

        dao.alterar(orcamentoTarget);
        if (aoSalvar != null) aoSalvar.run();
        fechar();
    }

    @FXML private void onCancelar() { fechar(); }
    private void fechar() { ((Stage) tfVeiculo.getScene().getWindow()).close(); }
}