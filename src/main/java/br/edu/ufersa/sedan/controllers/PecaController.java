package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.services.PecaService;
import javafx.beans.property.SimpleIntegerProperty;
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

public class PecaController {

    @FXML private ImageView logoImage;
    @FXML private CheckBox chkNome;
    @FXML private CheckBox chkFabricante;
    @FXML private TextField txtBusca;

    @FXML private TableView<Peca> tabelaPecas;
    @FXML private TableColumn<Peca, Integer> colId;
    @FXML private TableColumn<Peca, String> colNome;
    @FXML private TableColumn<Peca, String> colFabricante;
    @FXML private TableColumn<Peca, String> colPreco;
    @FXML private TableColumn<Peca, Void> colAcoes;

    private final PecaService pecaService = new PecaService();
    private final ObservableList<Peca> listaMaster = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarLogo();
        configurarColunas();
        sincronizarBanco();
    }

    private void carregarLogo() {
        try {
            logoImage.setImage(new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/br/edu/ufersa/sedan/images/logo.png")
            )));
        } catch (Exception ignored) {}
    }

    private void configurarColunas() {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colFabricante.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFabricante()));
        colPreco.setCellValueFactory(c -> new SimpleStringProperty(String.format("R$ %.2f", c.getValue().getPreco())));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("✏");
            private final Button btnDeletar = new Button("🗑");
            {
                btnEditar.getStyleClass().add("action-button");
                btnDeletar.getStyleClass().add("action-button-delete");
                btnEditar.setOnAction(e -> abrirEditar(getTableView().getItems().get(getIndex())));
                btnDeletar.setOnAction(e -> abrirExcluir(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); }
                else {
                    HBox box = new HBox(6, btnEditar, btnDeletar);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
    }

    public void sincronizarBanco() {
        List<Peca> doBanco = pecaService.listarTodos();
        listaMaster.clear();
        listaMaster.addAll(doBanco);
        tabelaPecas.setItems(listaMaster);
    }

    @FXML
    private void onBuscar() {
        String termo = txtBusca.getText().trim().toLowerCase();
        if (termo.isEmpty()) {
            tabelaPecas.setItems(listaMaster);
            return;
        }

        boolean porNome = chkNome.isSelected();
        boolean porFabricante = chkFabricante.isSelected();

        List<Peca> filtrados = listaMaster.stream().filter(p -> {
            boolean mNome = porNome && p.getNome() != null && p.getNome().toLowerCase().contains(termo);
            boolean mFab = porFabricante && p.getFabricante() != null && p.getFabricante().toLowerCase().contains(termo);
            return mNome || mFab;
        }).toList();

        tabelaPecas.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML private void onFiltroChanged() { onBuscar(); }

    @FXML
    private void onAdicionar() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/pecaadicionardialog.fxml")
            ));
            Parent root = loader.load();
            PecaAdicionarController ctrl = loader.getController();
            ctrl.setDependencias(pecaService, this::sincronizarBanco);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaPecas.getScene().getWindow());
            dialog.setTitle("Cadastrar Peça");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void abrirEditar(Peca p) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/pecaeditardialog.fxml")
            ));
            Parent root = loader.load();
            PecaEditarController ctrl = loader.getController();
            ctrl.setDados(p, pecaService, this::sincronizarBanco);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaPecas.getScene().getWindow());
            dialog.setTitle("Modificar Dados da Peça");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void abrirExcluir(Peca p) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/pecaexcluirdialog.fxml")
            ));
            Parent root = loader.load();
            PecaExcluirController ctrl = loader.getController();
            Stage dialog = new Stage();
            ctrl.setTarget(p, pecaService, this::sincronizarBanco, dialog);

            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaPecas.getScene().getWindow());
            dialog.setTitle("Confirmar Exclusão");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML private void onClientes()     { navegarPara("/br/edu/ufersa/sedan/views/clienteView.fxml"); }
    @FXML private void onVeiculos()     { navegarPara("/br/edu/ufersa/sedan/views/veiculoView.fxml"); }
    @FXML private void onPecas()        { /* Já está aqui */ }
    @FXML private void onServicos()     { navegarPara("/br/edu/ufersa/sedan/views/servicoView.fxml"); }
    @FXML private void onOrcamentos()   { navegarPara("/br/edu/ufersa/sedan/views/orcamentoView.fxml"); }
    @FXML private void onOrdemServico() { navegarPara("/br/edu/ufersa/sedan/views/ordemServicoView.fxml"); }
    @FXML private void onRelatorios()   { navegarPara("/br/edu/ufersa/sedan/views/relatorioView.fxml"); }
    @FXML private void onFuncionarios() { navegarPara("/br/edu/ufersa/sedan/views/funcionarioView.fxml"); }
    @FXML private void onSair() { ((Stage) tabelaPecas.getScene().getWindow()).close(); }

    private void navegarPara(String path) {
        try {
            Parent r = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            ((Stage) tabelaPecas.getScene().getWindow()).getScene().setRoot(r);
        } catch (IOException e) { e.printStackTrace(); }
    }
}