package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.DAO.OrcamentoDAO;
import br.edu.ufersa.sedan.model.services.OrdemServicoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class OrdemServicoAdicionarController {

    @FXML private ComboBox<Orcamento> cbOrcamentos;
    @FXML private Label lblResumo;
    @FXML private Label lblErro;

    private final OrdemServicoService osService = new OrdemServicoService();
    private final OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
    private Runnable aoSalvar;

    public void setAoSalvar(Runnable aoSalvar) {
        this.aoSalvar = aoSalvar;
    }

    @FXML
    public void initialize() {
        carregarOrcamentosDisponiveis();

        cbOrcamentos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                String cliente = (novo.getVeiculo() != null && novo.getVeiculo().getDono() != null)
                        ? novo.getVeiculo().getDono().getNome() : "Não identificado";
                String placa = (novo.getVeiculo() != null) ? novo.getVeiculo().getPlaca() : "Sem placa";

                lblResumo.setText("Placa: " + placa + "\nCliente: " + cliente + "\nTotal: R$ " + novo.calcularTotal());
            } else {
                lblResumo.setText("Selecione um orçamento para ver os detalhes.");
            }
        });
    }

    private void carregarOrcamentosDisponiveis() {
        try {
            List<Orcamento> orcamentos = orcamentoDAO.listar();
            cbOrcamentos.setItems(FXCollections.observableArrayList(orcamentos));
        } catch (Exception e) {
            lblErro.setText("Erro ao carregar lista de orçamentos.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        Orcamento selecionado = cbOrcamentos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            lblErro.setText("É necessário selecionar um orçamento para gerar a O.S.");
            return;
        }

        try {
            // Instancia a entidade populando com as propriedades da tela
            OrdemServico novaOS = new OrdemServico();
            novaOS.setOrcamento(selecionado);
            novaOS.setData(LocalDate.now());
            novaOS.setFinalizada(false);
            novaOS.setPago(false);

            // Envia o objeto montado para a service tratada
            osService.adicionar(novaOS);

            if (aoSalvar != null) aoSalvar.run();
            onCancelar();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            lblErro.setText(ex.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        ((Stage) cbOrcamentos.getScene().getWindow()).close();
    }
}