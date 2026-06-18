package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Cliente;
import br.edu.ufersa.sedan.model.entities.Endereco;
import br.edu.ufersa.sedan.model.entities.Veiculo;
import br.edu.ufersa.sedan.model.services.ClienteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClienteController implements Initializable {

    // ── Tabela ──────────────────────────────────────────────────
    @FXML private TableView<Cliente>           tabelaClientes;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colCpf;
    @FXML private TableColumn<Cliente, String> colEndereco;
    @FXML private TableColumn<Cliente, String> colAutomovel;
    @FXML private TableColumn<Cliente, Void>   colAcoes;

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private final ClienteService clienteService = new ClienteService();

    // ── Inicialização ────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColunas();
        carregarDados();
        tabelaClientes.setItems(listaClientes);
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNome()));

        colCpf.setCellValueFactory(c ->
                new SimpleStringProperty(formatarCpf(c.getValue().getCpf())));

        // Exibe endereço no formato "Rua, Número, Bairro"
        colEndereco.setCellValueFactory(c -> {
            Endereco e = c.getValue().getEndereco();
            if (e == null) return new SimpleStringProperty("—");
            String texto = e.getRua() + ", " + e.getNum() + ", " + e.getBairro();
            return new SimpleStringProperty(texto);
        });

        // Exibe os veículos separados por vírgula
        colAutomovel.setCellValueFactory(c -> {
            List<Veiculo> veiculos = c.getValue().getVeiculos();
            if (veiculos == null || veiculos.isEmpty())
                return new SimpleStringProperty("—");
            String texto = veiculos.stream()
                    .map(Veiculo::getMarca)   // ajuste para getPlaca() se preferir
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(texto);
        });

        // Coluna de ações: botões ✏ e 🗑
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar  = new Button("✏");
            private final Button btnExcluir = new Button("🗑");
            private final HBox   box        = new HBox(4, btnEditar, btnExcluir);

            {
                box.setAlignment(Pos.CENTER);
                btnEditar .getStyleClass().add("action-button");
                btnExcluir.getStyleClass().add("action-button-delete");

                btnEditar.setOnAction(e -> {
                    Cliente c = getTableView().getItems().get(getIndex());
                    abrirFormulario(c);
                });

                btnExcluir.setOnAction(e -> {
                    Cliente c = getTableView().getItems().get(getIndex());
                    confirmarExclusao(c);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    // ── Dados ────────────────────────────────────────────────────

    private void carregarDados() {
        listaClientes.setAll(clienteService.listarClientes());
    }

    // ── Ações da tela ─────────────────────────────────────────────

    @FXML
    private void onAdicionarCliente() {
        abrirFormulario(null);
    }

    /**
     * Abre o formulário modal de criação (cliente == null) ou edição.
     * O controller do formulário recebe o cliente e o callback de refresh.
     */
    private void abrirFormulario(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/sedan/views/clienteFormView.fxml"));
            Parent root = loader.load();

            ClienteFormController formCtrl = loader.getController();
            formCtrl.setDados(cliente, clienteService, this::carregarDados);

            Stage stage = new Stage();
            stage.setTitle(cliente == null ? "Novo Cliente" : "Editar Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarDados();
        } catch (IOException e) {
            mostrarErro("Erro ao abrir formulário: " + e.getMessage());
        }
    }

    private void confirmarExclusao(Cliente cliente) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar exclusão");
        alert.setHeaderText("Excluir cliente");
        alert.setContentText("Deseja realmente excluir " + cliente.getNome() + "?");

        alert.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                boolean ok = clienteService.removerCliente(cliente.getCpf());
                if (ok) {
                    carregarDados();
                } else {
                    mostrarErro("Não foi possível remover o cliente.");
                }
            }
        });
    }

    // ── Navegação da sidebar ─────────────────────────────────────

    @FXML private void onClientes()     { /* já estamos aqui */ }
    @FXML private void onVeiculos()     { navegarPara("veiculoView.fxml"); }
    @FXML private void onPecas()        { navegarPara("pecaView.fxml"); }
    @FXML private void onServicos()     { navegarPara("servicoView.fxml"); }
    @FXML private void onOrcamentos()   { navegarPara("orcamentoView.fxml"); }
    @FXML private void onOrdemServico() { navegarPara("ordemServicoView.fxml"); }
    @FXML private void onRelatorios()   { navegarPara("relatorioView.fxml"); }

    @FXML
    private void onSair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sair");
        alert.setHeaderText(null);
        alert.setContentText("Deseja realmente sair?");
        alert.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                Stage stage = (Stage) tabelaClientes.getScene().getWindow();
                stage.close();
            }
        });
    }

    private void navegarPara(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/edu/ufersa/sedan/views/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) tabelaClientes.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            mostrarErro("Erro ao navegar: " + e.getMessage());
        }
    }

    // ── Utilitários ───────────────────────────────────────────────

    /** Formata CPF de "12345678910" para "123.456.789-10". */
    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "."
                + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-"
                + cpf.substring(9);
    }

    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}