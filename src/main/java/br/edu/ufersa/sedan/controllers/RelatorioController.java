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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RelatorioController {

    @FXML private Label lblTotalFaturado;
    @FXML private Label lblTotalPecas;
    @FXML private Label lblTotalServicos;
    @FXML private Label lblClientesAtendidos;
    @FXML private ImageView logoImage; // Tratado diretamente pelo FXML agora

    @FXML private TableView<OrdemServico> tabelaRelatorio;
    @FXML private TableColumn<OrdemServico, String> colOS;
    @FXML private TableColumn<OrdemServico, String> colVeiculo;
    @FXML private TableColumn<OrdemServico, String> colCliente;
    @FXML private TableColumn<OrdemServico, String> colData;
    @FXML private TableColumn<OrdemServico, String> colValor;

    // APLICANDO O FACTORY METHOD: O controlador conhece apenas a interface genérica
    private final Relatorio relatorioFaturamento = RelatorioFactory.obterRelatorio("FATURAMENTO");

    @FXML
    public void initialize() {
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
            // Obtemos o DTO encapsulado gerado pela Factory/Service
            RelatorioFaturamento dados = relatorioFaturamento.gerar();

            // Atualiza os elementos da tela baseados no DTO
            lblTotalFaturado.setText(String.format("R$ %.2f", dados.getTotalFaturado()));
            lblTotalPecas.setText(String.format("R$ %.2f", dados.getTotalPecas()));
            lblTotalServicos.setText(String.format("R$ %.2f", dados.getTotalServicos()));
            lblClientesAtendidos.setText(String.valueOf(dados.getClientesAtendidos()));

            // Popula a tabela de apoio
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