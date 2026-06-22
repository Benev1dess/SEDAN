package br.edu.ufersa.sedan.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.services.PecaService;

import java.io.IOException;
import java.util.List;

public class PecaController {

    @FXML private TextField searchField;
    @FXML private CheckBox chkNome;
    @FXML private CheckBox chkFabricante;
    @FXML private CheckBox chkAutomovel;

    @FXML private TableView<Peca> tabelaPecas;
    @FXML private TableColumn<Peca, String> colNome;
    @FXML private TableColumn<Peca, String> colPreco;
    @FXML private TableColumn<Peca, String> colFabricante;
    @FXML private TableColumn<Peca, String> colCarro;
    @FXML private TableColumn<Peca, String> colCliente;
    @FXML private TableColumn<Peca, Void> colAcoes;

    @FXML private Button btnAdicionar;

    private ObservableList<Peca> listaDePecas = FXCollections.observableArrayList();
    private final PecaService pecaService = new PecaService();

    @FXML
    public void initialize() {
        // Mapeamento das colunas
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getNome() != null ? cellData.getValue().getNome() : ""
        ));

        colPreco.setCellValueFactory(cellData -> {
            double preco = cellData.getValue().getPreco();
            return new SimpleStringProperty(String.format("R$ %.2f", preco));
        });

        colFabricante.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getFabricante() != null ? cellData.getValue().getFabricante() : ""
        ));

        colCarro.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCarro() != null ? cellData.getValue().getCarro() : ""
        ));

        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente() != null ? cellData.getValue().getCliente() : ""
        ));

        configurarColunaAcoes();
        tabelaPecas.setItems(listaDePecas);

        // Carrega dados iniciais do banco
        atualizarTabelaDoBanco();
    }

    public void atualizarTabelaDoBanco() {
        try {
            // Chamando o método correto definido no seu Service
            List<Peca> pecasDoBanco = pecaService.listarTodos();

            if (pecasDoBanco != null) {
                listaDePecas.setAll(pecasDoBanco);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados do banco para a tabela.");
            e.printStackTrace();
        }
    }

    private void configurarColunaAcoes() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("\u270F");
            private final Button btnExcluir = new Button("\uD83D\uDDD1");
            private final HBox container = new HBox(14, btnEditar, btnExcluir);

            {
                btnEditar.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14px; -fx-text-fill: #333333; -fx-padding: 0; -fx-font-weight: bold;");
                btnExcluir.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 15px; -fx-text-fill: #D32F2F; -fx-padding: 0;");
                container.setStyle("-fx-alignment: center;");

                btnEditar.setOnAction(event -> {
                    Peca pecaSelecionada = getTableView().getItems().get(getIndex());
                    lidarComEditarPeca(pecaSelecionada);
                });

                btnExcluir.setOnAction(event -> {
                    Peca pecaSelecionada = getTableView().getItems().get(getIndex());
                    lidarComExcluirPeca(pecaSelecionada);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    private void onBuscar() {
        if (searchField == null) return;
        String termoBusca = searchField.getText();
        System.out.println("Buscando por: " + termoBusca);
    }

    @FXML
    private void onFiltroChanged() { onBuscar(); }

    @FXML private void onClientes() { System.out.println("Navegando para Clientes..."); }
    @FXML private void onVeiculos() { System.out.println("Navegando para Veículos..."); }
    @FXML private void onPecas() { System.out.println("Você já está na tela de Peças."); }
    @FXML private void onServicos() { System.out.println("Navegando para Serviços..."); }
    @FXML private void onOrcamentos() { System.out.println("Navegando para Orçamentos..."); }
    @FXML private void onOrdemServico() { System.out.println("Navegando para Ordens de Serviço..."); }
    @FXML private void onRelatorios() { System.out.println("Navegando para Relatórios..."); }
    @FXML private void onFuncionarios() { System.out.println("Navegando para Funcionários..."); }
    @FXML private void onSair() { System.out.println("Fechando o sistema..."); }

    private void lidarComEditarPeca(Peca peca) {
        System.out.println("Iniciando edição da peça: " + peca.getNome());
    }

    private void lidarComExcluirPeca(Peca peca) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufersa/sedan/views/pecaExcluirDialog.fxml"));
            Parent root = loader.load();
            PecaExcluirController modalController = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmar Exclusão");
            stage.setResizable(false);
            stage.showAndWait();

            if (modalController != null && modalController.isConfirmado()) {
                atualizarTabelaDoBanco();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirModalAdicionarPeca() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufersa/sedan/views/pecaAdicionarDialog.fxml"));
            Parent root = loader.load();
            PecaAdicionarController modalController = loader.getController();

            if (modalController != null) {
                modalController.setListaPecas(listaDePecas);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Adicionar Nova Peça");
            stage.setResizable(false);
            stage.showAndWait();

            // Atualiza a tabela logo após fechar a modal
            atualizarTabelaDoBanco();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}