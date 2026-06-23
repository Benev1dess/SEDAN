package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Servico;
import br.edu.ufersa.sedan.model.services.ServicoService;
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

public class ServicoController {

    // ── FXML ──────────────────────────────────────────────────
    @FXML private ImageView logoImage;

    @FXML private CheckBox  chkNomeServico;
    @FXML private TextField txtBusca;

    @FXML private TableView<Servico>           tabelaServicos;
    @FXML private TableColumn<Servico, String> colNomeServico;
    @FXML private TableColumn<Servico, String> colPreco;
    @FXML private TableColumn<Servico, Void>   colAcoes;

    private final ServicoService servicoService = new ServicoService();
    private ObservableList<Servico> listaMaster = FXCollections.observableArrayList();

    // ── Init ──────────────────────────────────────────────────
    @FXML
    public void initialize() {
        carregarLogo();
        configurarColunas();
        carregarServicos();
    }

    private void carregarLogo() {
        try {
            Image logo = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/br/edu/ufersa/sedan/images/logo.png")
            ));
            logoImage.setImage(logo);
        } catch (Exception ignored) {}
    }

    // ── Colunas ───────────────────────────────────────────────
    private void configurarColunas() {
        colNomeServico.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNome()));

        colPreco.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("R$ %.2f", c.getValue().getPreco())));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar  = new Button("✏");
            private final Button btnDeletar = new Button("🗑");

            {
                btnEditar .getStyleClass().add("action-button");
                btnDeletar.getStyleClass().add("action-button-delete");

                // Passa o objeto do serviço selecionado para as modais correspondentes
                btnEditar .setOnAction(e -> abrirDialogoEditar(getTableView().getItems().get(getIndex())));
                btnDeletar.setOnAction(e -> abrirDialogoExcluir(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                HBox box = new HBox(4, btnEditar, btnDeletar);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
            }
        });
    }

    // ── Carregar do Banco ─────────────────────────────────────
    private void carregarServicos() {
        List<Servico> lista = servicoService.listarTodos();
        listaMaster = FXCollections.observableArrayList(lista);
        tabelaServicos.setItems(listaMaster);
    }

    // ── Busca Reativa ─────────────────────────────────────────
    @FXML
    private void onBuscar() {
        String termo = txtBusca.getText().trim();

        if (termo.isEmpty()) {
            tabelaServicos.setItems(listaMaster);
            return;
        }

        // Se a caixinha estiver marcada, filtra usando o método de busca por nome da Service
        if (chkNomeServico.isSelected()) {
            List<Servico> filtrados = servicoService.buscarPorNome(termo);
            tabelaServicos.setItems(FXCollections.observableArrayList(filtrados));
        } else {
            tabelaServicos.setItems(listaMaster);
        }
    }

    @FXML private void onFiltroChanged() { onBuscar(); }

    // ── Adicionar ─────────────────────────────────────────────
    @FXML
    private void onAdicionar() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/servicoAdicionarDialog.fxml")
            ));
            Parent root = loader.load();

            // Siga a mesma convenção criada nos controladores de veículo para atualizar a tabela principal pós-commit
            ServicoAdicionarController ctrl = loader.getController();
            ctrl.setAoSalvar(this::carregarServicos);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaServicos.getScene().getWindow());
            dialog.setTitle("Adicionar Serviço");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Editar ────────────────────────────────────────────────
    private void abrirDialogoEditar(Servico servico) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/servicoEditarDialog.fxml")
            ));
            Parent root = loader.load();

            ServicoEditarController ctrl = loader.getController();
            ctrl.setServico(servico);
            ctrl.setAoSalvar(this::carregarServicos);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaServicos.getScene().getWindow());
            dialog.setTitle("Editar Serviço");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Excluir ───────────────────────────────────────────────
    private void abrirDialogoExcluir(Servico servico) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/servicoExcluirDialog.fxml")
            ));
            Parent root = loader.load();

            ServicoExcluirController ctrl = loader.getController();
            ctrl.setServico(servico);
            ctrl.setAoExcluir(this::carregarServicos);

            Stage dialog = new Stage();
            ServicoExcluirController.stage = dialog;
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaServicos.getScene().getWindow());
            dialog.setTitle("Confirmar Exclusão");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Navegação da Sidebar (Controle de Estado Ativo) ──────
    @FXML private void onClientes()     { navegarPara("/br/edu/ufersa/sedan/views/clienteView.fxml"); }
    @FXML private void onVeiculos()     { navegarPara("/br/edu/ufersa/sedan/views/veiculoView.fxml"); }
    @FXML private void onPecas()        { navegarPara("/br/edu/ufersa/sedan/views/pecaView.fxml"); }
    @FXML private void onServicos()     { /* já estamos aqui */ }
    @FXML private void onOrcamentos()   { navegarPara("/br/edu/ufersa/sedan/views/orcamentoView.fxml"); }
    @FXML private void onOrdemServico() { navegarPara("/br/edu/ufersa/sedan/views/ordemServicoView.fxml"); }
    @FXML private void onRelatorios()   { navegarPara("/br/edu/ufersa/sedan/views/relatorioView.fxml"); }
    @FXML private void onFuncionarios() { navegarPara("/br/edu/ufersa/sedan/views/funcionarioView.fxml"); }

    @FXML
    private void onSair() {
        ((Stage) tabelaServicos.getScene().getWindow()).close();
    }

    private void navegarPara(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(fxmlPath))
            );
            Stage stage = (Stage) tabelaServicos.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}