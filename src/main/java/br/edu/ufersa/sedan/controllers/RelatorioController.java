package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.services.RelatorioFaturamento;
import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.services.Relatorio;
import br.edu.ufersa.sedan.model.services.RelatorioFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class RelatorioController {

    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private Label lblTotalFaturado;
    @FXML private Label lblTotalPecas;
    @FXML private Label lblTotalServicos;
    @FXML private Label lblClientesAtendidos;

    @FXML private TableView<OrdemServico> tabelaRelatorio;
    @FXML private TableColumn<OrdemServico, String> colOS;
    @FXML private TableColumn<OrdemServico, String> colVeiculo;
    @FXML private TableColumn<OrdemServico, String> colCliente;
    @FXML private TableColumn<OrdemServico, String> colData;
    @FXML private TableColumn<OrdemServico, String> colValor;

    private final Relatorio relatorioFaturamento = RelatorioFactory.obterRelatorio("FATURAMENTO");

    @FXML
    public void initialize() {
        // Inicializa o filtro mostrando por padrão os últimos 30 dias de movimentação
        dpInicio.setValue(LocalDate.now().minusDays(30));
        dpFim.setValue(LocalDate.now());

        configurarColunas();
        carregarDadosRelatorio();
    }

    private void configurarColunas() {
        colOS.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getOrcamento() != null ? String.valueOf(c.getValue().getOrcamento().getId()) : "—"));

        colVeiculo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getOrcamento() != null && c.getValue().getOrcamento().getVeiculo() != null
                        ? c.getValue().getOrcamento().getVeiculo().getPlaca() : "—"));

        colCliente.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getOrcamento() != null && c.getValue().getOrcamento().getVeiculo() != null && c.getValue().getOrcamento().getVeiculo().getDono() != null
                        ? c.getValue().getOrcamento().getVeiculo().getDono().getNome() : "—"));

        colData.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getData() != null ? c.getValue().getData().toString() : "—"));

        colValor.setCellValueFactory(c -> {
            double valor = c.getValue().getOrcamento() != null ? c.getValue().getOrcamento().calcularTotal() : 0.0;
            return new SimpleStringProperty(String.format("R$ %.2f", valor));
        });
    }

    @FXML
    public void carregarDadosRelatorio() {
        if (relatorioFaturamento != null) {
            LocalDate inicio = dpInicio.getValue();
            LocalDate fim = dpFim.getValue();

            // Proteção básica contra campos nulos
            if (inicio == null) inicio = LocalDate.now().minusDays(30);
            if (fim == null) fim = LocalDate.now();

            // Puxa os dados injetando o período do filtro do requisito
            RelatorioFaturamento dados = relatorioFaturamento.gerar(inicio, fim);

            lblTotalFaturado.setText(String.format("R$ %.2f", dados.getTotalFaturado()));
            lblTotalPecas.setText(String.format("R$ %.2f", dados.getTotalPecas()));
            lblTotalServicos.setText(String.format("R$ %.2f", dados.getTotalServicos()));
            lblClientesAtendidos.setText(String.valueOf(dados.getClientesAtendidos()));

            tabelaRelatorio.setItems(FXCollections.observableArrayList(dados.getOrdensComputadas()));
        }
    }

    // MÉTODOS DE NAVEGAÇÃO COMPATÍVEIS COM OS BOTÕES DO SEU SIDEBAR
    @FXML private void onClientes() { navegarPara("/br/edu/ufersa/sedan/views/clienteView.fxml"); }
    @FXML private void onVeiculos() { navegarPara("/br/edu/ufersa/sedan/views/veiculoView.fxml"); }
    @FXML private void onPecas() { navegarPara("/br/edu/ufersa/sedan/views/pecaView.fxml"); }
    @FXML private void onServicos() { navegarPara("/br/edu/ufersa/sedan/views/servicoView.fxml"); }
    @FXML private void onOrcamentos() { navegarPara("/br/edu/ufersa/sedan/views/orcamentoView.fxml"); }
    @FXML private void onOrdemServico() { navegarPara("/br/edu/ufersa/sedan/views/ordemServicoView.fxml"); }
    @FXML private void onFuncionarios() { navegarPara("/br/edu/ufersa/sedan/views/funcionarioView.fxml"); }
    @FXML private void onSair() { ((Stage) tabelaRelatorio.getScene().getWindow()).close(); }

    private void navegarPara(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) tabelaRelatorio.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}