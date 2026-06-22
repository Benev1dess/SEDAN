package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.OrdemServico;
import br.edu.ufersa.sedan.model.services.OrdemServicoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrdemServicoController {

    @FXML private ImageView logoImage;
    @FXML private CheckBox chkPlaca;
    @FXML private CheckBox chkFinalizada;

    @FXML private TextField txtBusca;
    @FXML private TableView<OrdemServico> tabelaOS;
    @FXML private TableColumn<OrdemServico, String> colOrcamentoId;
    @FXML private TableColumn<OrdemServico, String> colPlaca;
    @FXML private TableColumn<OrdemServico, String> colData;
    @FXML private TableColumn<OrdemServico, String> colFinalizada;
    @FXML private TableColumn<OrdemServico, String> colPago;
    @FXML private TableColumn<OrdemServico, Void> colAcoes;

    private final OrdemServicoService osService = new OrdemServicoService();
    private ObservableList<OrdemServico> listaMaster = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarLogo();
        configurarColunas();
        carregarOrdens();
    }

    private void carregarLogo() {
        try {
            Image logo = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/br/edu/ufersa/sedan/images/logo.png")
            ));
            logoImage.setImage(logo);
        } catch (Exception ignored) {}
    }

    private void configurarColunas() {
        colOrcamentoId.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getOrcamento() != null ? String.valueOf(c.getValue().getOrcamento().getId()) : "—"));

        colPlaca.setCellValueFactory(c -> {
            // Tratando encadeamento defensivo de acordo com seu modelo
            if (c.getValue().getOrcamento() != null && c.getValue().getOrcamento().getVeiculo() != null) {
                return new SimpleStringProperty(c.getValue().getOrcamento().getVeiculo().getPlaca());
            }
            return new SimpleStringProperty("—");
        });

        colData.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getData() != null ? c.getValue().getData().toString() : "—"));

        colFinalizada.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isFinalizada() ? "SIM" : "NÃO"));

        colPago.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isPago() ? "PAGO" : "PENDENTE"));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("✏");
            private final Button btnDeletar = new Button("🗑");
            {
                btnEditar.getStyleClass().add("action-button");
                btnDeletar.getStyleClass().add("action-button-delete");
                btnEditar.setOnAction(e -> abrirDialogoEditar(getTableView().getItems().get(getIndex())));
                btnDeletar.setOnAction(e -> abrirDialogoExcluir(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(6, btnEditar, btnDeletar);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
    }

    private void carregarOrdens() {
        List<OrdemServico> lista = osService.listarTodasOrdens();
        listaMaster = FXCollections.observableArrayList(lista);
        tabelaOS.setItems(listaMaster);
    }

    @FXML
    private void onBuscar() {
        String termo = txtBusca.getText().trim().toLowerCase();

        // Se a barra de busca estiver vazia, volta a exibir a lista completa
        if (termo.isEmpty()) {
            tabelaOS.setItems(listaMaster);
            return;
        }

        boolean filtrarPorPlaca = chkPlaca.isSelected();
        boolean filtrarPorEstado = chkFinalizada.isSelected();

        List<OrdemServico> filtrados = listaMaster.stream().filter(os -> {
            // 1. Validação da busca por Placa
            boolean matchPlaca = false;
            if (filtrarPorPlaca && os.getOrcamento() != null && os.getOrcamento().getVeiculo() != null) {
                matchPlaca = os.getOrcamento().getVeiculo().getPlaca().toLowerCase().contains(termo);
            }

            // 2. Validação da busca por Estado (Finalizada: Sim / Não)
            boolean matchFinalizada = false;
            if (filtrarPorEstado) {
                String textoEstado = os.isFinalizada() ? "sim" : "não";
                matchFinalizada = textoEstado.contains(termo);
            }

            // Retorna verdadeiro se bater com qualquer um dos filtros ativos
            return matchPlaca || matchFinalizada;
        }).collect(Collectors.toList());

        // Atualiza a tabela com o resultado do filtro
        tabelaOS.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML private void onFiltroChanged() { onBuscar(); }

    @FXML
    private void onAdicionar() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/ordemServicoAdicionarDialog.fxml")
            ));
            Parent root = loader.load();

            OrdemServicoAdicionarController ctrl = loader.getController();
            ctrl.setAoSalvar(this::carregarOrdens);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaOS.getScene().getWindow());
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirDialogoEditar(OrdemServico os) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/ordemServicoEditarDialog.fxml")
            ));
            Parent root = loader.load();

            OrdemServicoEditarController ctrl = loader.getController();
            ctrl.setOrdemServico(os);
            ctrl.setAoSalvar(this::carregarOrdens);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaOS.getScene().getWindow());
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirDialogoExcluir(OrdemServico os) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/ordemServicoExcluirDialog.fxml")
            ));
            Parent root = loader.load();

            OrdemServicoExcluirController ctrl = loader.getController();
            ctrl.setOrdemServico(os);
            ctrl.setAoExcluir(this::carregarOrdens);

            Stage dialog = new Stage();
            OrdemServicoExcluirController.stage = dialog;
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaOS.getScene().getWindow());
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void onClientes() { navegarPara("/br/edu/ufersa/sedan/views/clienteView.fxml"); }
    @FXML private void onVeiculos() { navegarPara("/br/edu/ufersa/sedan/views/veiculoView.fxml"); }
    @FXML private void onPecas() { navegarPara("/br/edu/ufersa/sedan/views/pecaView.fxml"); }
    @FXML private void onServicos() { navegarPara("/br/edu/ufersa/sedan/views/servicoView.fxml"); }
    @FXML private void onOrcamentos() { navegarPara("/br/edu/ufersa/sedan/views/orcamentoView.fxml"); }
    @FXML private void onOrdemServico() { /* Página Atual */ }
    @FXML private void onRelatorios() { navegarPara("/br/edu/ufersa/sedan/views/relatorioView.fxml"); }
    @FXML private void onFuncionarios() { navegarPara("/br/edu/ufersa/sedan/views/funcionarioView.fxml"); }
    @FXML private void onSair() { ((Stage) tabelaOS.getScene().getWindow()).close(); }

    private void navegarPara(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) tabelaOS.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}