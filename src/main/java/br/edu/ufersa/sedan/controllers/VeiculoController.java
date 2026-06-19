package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Veiculo;
import br.edu.ufersa.sedan.model.services.VeiculoService;
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

public class VeiculoController {

    // ── FXML ──────────────────────────────────────────────────
    @FXML private ImageView logoImage;

    @FXML private CheckBox  chkNomeProprietario;
    @FXML private CheckBox  chkPlaca;
    @FXML private TextField txtBusca;

    @FXML private TableView<Veiculo>           tabelaVeiculos;
    @FXML private TableColumn<Veiculo, String> colPlaca;
    @FXML private TableColumn<Veiculo, String> colProprietario;
    @FXML private TableColumn<Veiculo, String> colMarca;
    @FXML private TableColumn<Veiculo, String> colCor;
    @FXML private TableColumn<Veiculo, String> colAno;
    @FXML private TableColumn<Veiculo, String> colKm;
    @FXML private TableColumn<Veiculo, Void>   colAcoes;

    private final VeiculoService veiculoService = new VeiculoService();
    private ObservableList<Veiculo> listaMaster = FXCollections.observableArrayList();

    // ── Init ──────────────────────────────────────────────────
    @FXML
    public void initialize() {
        carregarLogo();
        configurarColunas();
        carregarVeiculos();
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
        colPlaca.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPlaca()));

        colProprietario.setCellValueFactory(c -> {
            var dono = c.getValue().getDono();
            return new SimpleStringProperty(dono != null ? dono.getNome() : "—");
        });

        colMarca.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMarca()));

        colCor.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCor()));

        colAno.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getAno())));

        colKm.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getKm())));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar  = new Button("✏");
            private final Button btnDeletar = new Button("🗑");

            {
                btnEditar .getStyleClass().add("action-button");
                btnDeletar.getStyleClass().add("action-button-delete");
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

    // ── Carregar ──────────────────────────────────────────────
    private void carregarVeiculos() {
        List<Veiculo> lista = veiculoService.listarVeiculos();
        listaMaster = FXCollections.observableArrayList(lista);
        tabelaVeiculos.setItems(listaMaster);
    }

    // ── Busca ─────────────────────────────────────────────────
    @FXML
    private void onBuscar() {
        String termo = txtBusca.getText().trim().toLowerCase();
        if (termo.isEmpty()) { tabelaVeiculos.setItems(listaMaster); return; }

        boolean porNome  = chkNomeProprietario.isSelected();
        boolean porPlaca = chkPlaca.isSelected();

        List<Veiculo> filtrados = listaMaster.stream()
                .filter(v -> {
                    boolean mNome  = porNome  && v.getDono() != null
                            && v.getDono().getNome().toLowerCase().contains(termo);
                    boolean mPlaca = porPlaca && v.getPlaca().toLowerCase().contains(termo);
                    return mNome || mPlaca;
                })
                .collect(Collectors.toList());

        tabelaVeiculos.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML private void onFiltroChanged() { onBuscar(); }

    // ── Adicionar ─────────────────────────────────────────────
    @FXML
    private void onAdicionar() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/veiculoAdicionarDialog.fxml")
            ));
            Parent root = loader.load();

            VeiculoAdicionarController ctrl = loader.getController();
            ctrl.setAoSalvar(this::carregarVeiculos);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaVeiculos.getScene().getWindow());
            dialog.setTitle("Adicionar Veículo");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Editar ────────────────────────────────────────────────
    private void abrirDialogoEditar(Veiculo veiculo) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/veiculoEditarDialog.fxml")
            ));
            Parent root = loader.load();

            VeiculoEditarController ctrl = loader.getController();
            ctrl.setVeiculo(veiculo);
            ctrl.setAoSalvar(this::carregarVeiculos);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaVeiculos.getScene().getWindow());
            dialog.setTitle("Editar Veículo");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Excluir ───────────────────────────────────────────────
    private void abrirDialogoExcluir(Veiculo veiculo) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/veiculoExcluirDialog.fxml")
            ));
            Parent root = loader.load();

            VeiculoExcluirController ctrl = loader.getController();
            ctrl.setVeiculo(veiculo);
            ctrl.setAoExcluir(this::carregarVeiculos);

            Stage dialog = new Stage();
            VeiculoExcluirController.stage = dialog; // passa referência para o controller fechar
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaVeiculos.getScene().getWindow());
            dialog.setTitle("Confirmar Exclusão");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Navegação da Sidebar ──────────────────────────────────
    @FXML private void onClientes()     { navegarPara("/br/edu/ufersa/sedan/views/clienteView.fxml"); }
    @FXML private void onVeiculos()     { /* já estamos aqui */ }
    @FXML private void onPecas()        { navegarPara("/br/edu/ufersa/sedan/views/pecaView.fxml"); }
    @FXML private void onServicos()     { navegarPara("/br/edu/ufersa/sedan/views/servicoView.fxml"); }
    @FXML private void onOrcamentos()   { navegarPara("/br/edu/ufersa/sedan/views/orcamentoView.fxml"); }
    @FXML private void onOrdemServico() { navegarPara("/br/edu/ufersa/sedan/views/ordemServicoView.fxml"); }
    @FXML private void onRelatorios()   { navegarPara("/br/edu/ufersa/sedan/views/relatorioView.fxml"); }
    @FXML private void onFuncionarios() { navegarPara("/br/edu/ufersa/sedan/views/funcionarioView.fxml"); }

    @FXML
    private void onSair() {
        ((Stage) tabelaVeiculos.getScene().getWindow()).close();
    }

    private void navegarPara(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(fxmlPath))
            );
            Stage stage = (Stage) tabelaVeiculos.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}