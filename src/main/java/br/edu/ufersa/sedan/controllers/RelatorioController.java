package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.services.RelatorioService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class RelatorioController {

    @FXML private Label lblTotalFaturado;
    @FXML private Label lblTotalPecas;
    @FXML private Label lblTotalServicos;
    @FXML private Label lblClientesAtendidos;
    @FXML private ImageView logoImage; // Para carregar a logo se necessário

    @FXML private TableView<OrdemServico> tabelaRelatorio;
    @FXML private TableColumn<OrdemServico, String> colOS;
    @FXML private TableColumn<OrdemServico, String> colVeiculo;
    @FXML private TableColumn<OrdemServico, String> colCliente;
    @FXML private TableColumn<OrdemServico, String> colData;
    @FXML private TableColumn<OrdemServico, String> colValor;


    private final RelatorioService relatorioService = new RelatorioService();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarDadosRelatorio();
        // Se tiver a função de carregar imagem padrão no seu sistema, chame aqui:
        // carregarLogo();
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
        List<OrdemServico> ordensFaturadas = relatorioService.obterOrdensFaturadas();

        double totalFaturado = relatorioService.calcularFaturamentoTotal(ordensFaturadas);
        double totalPecas = relatorioService.calcularTotalPecas(ordensFaturadas);
        double totalServicos = relatorioService.calcularTotalServicos(ordensFaturadas);
        long totalClientes = relatorioService.contarClientesUnicos(ordensFaturadas);

        lblTotalFaturado.setText(String.format("R$ %.2f", totalFaturado));
        lblTotalPecas.setText(String.format("R$ %.2f", totalPecas));
        lblTotalServicos.setText(String.format("R$ %.2f", totalServicos));
        lblClientesAtendidos.setText(String.valueOf(totalClientes));

        tabelaRelatorio.setItems(FXCollections.observableArrayList(ordensFaturadas));
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