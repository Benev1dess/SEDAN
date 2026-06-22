package br.edu.ufersa.sedan.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import br.edu.ufersa.sedan.model.entities.Peca;

import java.io.IOException;

public class PecaController {

    @FXML private TextField searchField;
    @FXML private TableView<Peca> tabelaPecas;
    @FXML private TableColumn<Peca, String> colNome;
    @FXML private TableColumn<Peca, String> colPreco;
    @FXML private TableColumn<Peca, String> colFabricante;
    @FXML private TableColumn<Peca, String> colCarro;
    @FXML private TableColumn<Peca, String> colCliente;

    // Coluna de Ações mapeada no FXML
    @FXML private TableColumn<Peca, Void> colAcoes;

    @FXML private Button btnAdicionar;

    private ObservableList<Peca> listaDePecas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Mapeamento das colunas textuais com lambdas seguras
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colPreco.setCellValueFactory(cellData -> new SimpleStringProperty("R$ " + String.format("%.2f", cellData.getValue().getPreco())));
        colFabricante.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFabricante()));
        colCarro.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarro()));
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente()));

        // Configuração da coluna de botões de Ação (Editar e Excluir) livre de bugs de encoding
        configurarColunaAcoes();

        tabelaPecas.setItems(listaDePecas);
        btnAdicionar.setOnAction(e -> abrirModalAdicionarPeca());
    }

    private void configurarColunaAcoes() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            // Códigos Unicode nativos para garantir renderização idêntica em qualquer Windows/Linux
            private final Button btnEditar = new Button("\uD83D\uDCDD"); // 📝
            private final Button btnExcluir = new Button("\uD83D\uDDD1"); // 🗑️
            private final HBox container = new HBox(12, btnEditar, btnExcluir);

            {
                // Estilização limpa e profissional para os botões de ação do Sedan
                btnEditar.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 15px; -fx-padding: 0;");
                btnExcluir.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 15px; -fx-text-fill: #D32F2F; -fx-padding: 0;");
                container.setStyle("-fx-alignment: center;");

                // Ação do Botão EDITAR
                btnEditar.setOnAction(event -> {
                    Peca pecaSelecionada = getTableView().getItems().get(getIndex());
                    lidarComEditarPeca(pecaSelecionada);
                });

                // Ação do Botão EXCLUIR
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

    private void lidarComEditarPeca(Peca peca) {
        // TODO: Chamar o modal de edição passando o objeto 'peca' para preencher os campos
        System.out.println("Editar a peça: " + peca.getNome());
    }

    private void lidarComExcluirPeca(Peca peca) {
        // Remove visualmente da tabela na hora
        listaDePecas.remove(peca);

        // TODO: Chamar o seu pecaService.deletar(peca.getNome()) para apagar do banco definitivo
        System.out.println("Excluído com sucesso: " + peca.getNome());
    }

    @FXML
    private void abrirModalAdicionarPeca() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufersa/sedan/views/pecaAdicionarDialog.fxml"));
            Parent root = loader.load();

            PecaAdicionarController modalController = loader.getController();
            modalController.setListaPecas(listaDePecas);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Adicionar Nova Peça");
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}