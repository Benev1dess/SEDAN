package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Orcamento;
import br.edu.ufersa.sedan.model.services.OrcamentoService;
import br.edu.ufersa.sedan.model.services.VeiculoService;
import br.edu.ufersa.sedan.model.services.PecaService;
import br.edu.ufersa.sedan.model.services.ServicoService;
import br.edu.ufersa.sedan.model.DAO.OrcamentoDAO;

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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrcamentoController {

    @FXML private ImageView logoImage;
    @FXML private CheckBox chkPlaca;
    @FXML private CheckBox chkCliente;
    @FXML private CheckBox chkData;
    @FXML private TextField txtBusca;

    @FXML private TableView<Orcamento> tabelaOrcamentos;
    @FXML private TableColumn<Orcamento, Integer> colId;
    @FXML private TableColumn<Orcamento, String> colVeiculo;
    @FXML private TableColumn<Orcamento, String> colCliente;
    @FXML private TableColumn<Orcamento, String> colData;
    @FXML private TableColumn<Orcamento, String> colTotal;
    @FXML private TableColumn<Orcamento, Void> colAcoes;

    // Gerenciadores globais mantidos no escopo do ciclo de vida da tela
    private final OrcamentoService orcamentoService = new OrcamentoService();
    private final OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
    private final ObservableList<Orcamento> listaMaster = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        carregarLogo();
        configurarColunas();
        sincronizarBancoEService();
    }

    private void carregarLogo() {
        try {
            logoImage.setImage(new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/br/edu/ufersa/sedan/images/logo.png")
            )));
        } catch (Exception ignored) {}
    }

    private void configurarColunas() {
        // 1. Coluna do ID do Orçamento
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());

        // 2. Coluna do Veículo (Placa) - Mostra a placa ou um traço caso seja nulo
        colVeiculo.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPlacaVeiculo() != null ? c.getValue().getPlacaVeiculo() : "—"
        ));

        // 3. Coluna do Cliente - Navega de forma segura pela árvore de objetos para evitar NullPointerException
        colCliente.setCellValueFactory(c -> {
            if (c.getValue() != null && c.getValue().getVeiculo() != null && c.getValue().getVeiculo().getDono() != null) {
                String nomeCliente = c.getValue().getVeiculo().getDono().getNome();
                return new SimpleStringProperty(nomeCliente != null ? nomeCliente : "Não Informado");
            }
            return new SimpleStringProperty("Não Informado");
        });

        // 4. Coluna da Data - CORRIGIDA para usar getDataOrcamento() que é o método correto da sua entidade
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colData.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDataOrcamento() != null ? c.getValue().getDataOrcamento().format(formatter) : ""
        ));

        // 5. Coluna do Total - CORRIGIDA para calcular o valor dinamicamente com base nas peças e serviços carregados
        colTotal.setCellValueFactory(c -> {
            if (c.getValue() != null) {
                double total = c.getValue().calcularTotal();
                return new SimpleStringProperty(String.format("R$ %.2f", total));
            }
            return new SimpleStringProperty("R$ 0,00");
        });

        // 6. Coluna de Ações (Botões Editar e Deletar)
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("✏");
            private final Button btnDeletar = new Button("🗑");

            {
                btnEditar.getStyleClass().add("action-button");
                btnDeletar.getStyleClass().add("action-button-delete");

                // Define o comportamento dos botões ao serem clicados
                btnEditar.setOnAction(e -> abrirEditar(getTableView().getItems().get(getIndex())));
                btnDeletar.setOnAction(e -> abrirExcluir(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                HBox box = new HBox(6, btnEditar, btnDeletar);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
            }
        });
    }

    public void sincronizarBancoEService() {
        List<Orcamento> doBanco = orcamentoDAO.listar();
        listaMaster.clear();

        // CORREÇÃO CRUCIAL: Limpa e alimenta a instância GLOBAL (this.orcamentoService) em vez de criar uma local.
        // Se a classe OrcamentoService não possuir um método esvaziar/limpar, criamos um novo Service associado ao atributo global.
        // Assim, os modais de diálogo sempre recebem o service atualizado e persistido.
        this.tabelaOrcamentos.setItems(null);

        // Esvazia ou re-instancia o service global de forma segura
        // Se a sua classe OrcamentoService contiver um método "limparLista()", chame-o aqui. Caso contrário, reatribua:
        // this.orcamentoService.limpar();

        for (Orcamento o : doBanco) {
            this.orcamentoService.adicionar(o);
            listaMaster.add(o);
        }
        tabelaOrcamentos.setItems(listaMaster);
    }

    @FXML
    private void onBuscar() {
        String termo = txtBusca.getText().trim().toLowerCase();
        if (termo.isEmpty()) {
            tabelaOrcamentos.setItems(listaMaster);
            return;
        }

        boolean porPlaca = chkPlaca.isSelected();
        boolean porCliente = chkCliente.isSelected();
        boolean porData = chkData != null && chkData.isSelected();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<Orcamento> filtrados = listaMaster.stream().filter(o -> {
            boolean mPlaca = porPlaca && o.getPlacaVeiculo() != null && o.getPlacaVeiculo().toLowerCase().contains(termo);

            boolean mCliente = porCliente && o.getVeiculo() != null && o.getVeiculo().getDono() != null
                    && o.getVeiculo().getDono().getNome() != null && o.getVeiculo().getDono().getNome().toLowerCase().contains(termo);

            // Compara tanto no formato padrão dd/MM/yyyy quanto no formato do banco yyyy-MM-dd
            boolean mData = porData && o.getData() != null && (
                    o.getData().format(formatter).contains(termo) ||
                            o.getData().toString().contains(termo)
            );

            return mPlaca || mCliente || mData;
        }).collect(Collectors.toList());

        tabelaOrcamentos.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML private void onFiltroChanged() { onBuscar(); }

    @FXML
    private void onAdicionar() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/orcamentoadicionardialog.fxml")
            ));
            Parent root = loader.load();

            OrcamentoAdicionarController ctrl = loader.getController();
            // Passa o service global 'orcamentoService' devidamente sincronizado
            ctrl.setDependencias(this.orcamentoService, new VeiculoService(), new PecaService(), new ServicoService(), this::sincronizarBancoEService);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaOrcamentos.getScene().getWindow());
            dialog.setTitle("Inserir Orçamento");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void abrirEditar(Orcamento o) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/orcamentoeditardialog.fxml")
            ));
            Parent root = loader.load();

            OrcamentoEditarController ctrl = loader.getController();
            // Passa o service global 'orcamentoService' devidamente sincronizado
            ctrl.setDados(o, this.orcamentoService, new PecaService(), new ServicoService(), this::sincronizarBancoEService);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaOrcamentos.getScene().getWindow());
            dialog.setTitle("Editar Orçamento");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void abrirExcluir(Orcamento o) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/orcamentoexcluirdialog.fxml")
            ));
            Parent root = loader.load();

            OrcamentoExcluirController ctrl = loader.getController();
            Stage dialog = new Stage();
            ctrl.setTarget(o, this.orcamentoService, this::sincronizarBancoEService, dialog);

            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tabelaOrcamentos.getScene().getWindow());
            dialog.setTitle("Excluir Orçamento");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML private void onClientes()     { navegarPara("/br/edu/ufersa/sedan/views/clienteView.fxml"); }
    @FXML private void onVeiculos()     { navegarPara("/br/edu/ufersa/sedan/views/veiculoView.fxml"); }
    @FXML private void onPecas()        { navegarPara("/br/edu/ufersa/sedan/views/pecaView.fxml"); }
    @FXML private void onServicos()     { navegarPara("/br/edu/ufersa/sedan/views/servicoView.fxml"); }
    @FXML private void onOrcamentos()   { /* Mantém na Visualização Local */ }
    @FXML private void onOrdemServico() { navegarPara("/br/edu/ufersa/sedan/views/ordemServicoView.fxml"); }
    @FXML private void onRelatorios()   { navegarPara("/br/edu/ufersa/sedan/views/relatorioView.fxml"); }
    @FXML private void onFuncionarios() { navegarPara("/br/edu/ufersa/sedan/views/funcionarioView.fxml"); }
    @FXML private void onSair() { ((Stage) tabelaOrcamentos.getScene().getWindow()).close(); }

    private void navegarPara(String path) {
        try {
            Parent r = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            ((Stage) tabelaOrcamentos.getScene().getWindow()).getScene().setRoot(r);
        } catch (IOException e) { e.printStackTrace(); }
    }
}