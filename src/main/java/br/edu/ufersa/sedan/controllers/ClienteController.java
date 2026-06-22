package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Cliente;
import br.edu.ufersa.sedan.model.entities.Endereco;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClienteController implements Initializable {

    @FXML private ImageView logoImage;
    @FXML private CheckBox chkNome;
    @FXML private CheckBox chkCpf;
    @FXML private TextField txtBusca;

    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colCpf;
    @FXML private TableColumn<Cliente, String> colEndereco;
    @FXML private TableColumn<Cliente, Void> colAcoes;
    @FXML private Button btnAdicionar;

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private final ClienteService clienteService = new ClienteService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar logo caso necessário (opcional, replicando comportamento do veículo)
        // logoImage.setImage(new Image(getClass().getResourceAsStream("/br/edu/ufersa/sedan/images/logo.png")));

        configurarColunas();
        carregarDados();
        tabelaClientes.setItems(listaClientes);
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colCpf.setCellValueFactory(c -> new SimpleStringProperty(formatarCpf(c.getValue().getCpf())));

        colEndereco.setCellValueFactory(c -> {
            Endereco e = c.getValue().getEndereco();
            if (e == null || e.getRua() == null || e.getRua().isEmpty()) {
                return new SimpleStringProperty("—");
            }
            return new SimpleStringProperty(e.getRua() + ", " + e.getNum() + " - " + e.getBairro());
        });

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("✏");
            private final Button btnExcluir = new Button("🗑");
            private final HBox box = new HBox(8, btnEditar, btnExcluir);

            {
                box.setAlignment(Pos.CENTER);
                btnEditar.getStyleClass().add("action-button");
                btnExcluir.getStyleClass().add("action-button-delete");

                btnEditar.setOnAction(e -> abrirFormulario(getTableView().getItems().get(getIndex())));
                btnExcluir.setOnAction(e -> confirmarExclusao(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void carregarDados() {
        listaClientes.setAll(clienteService.listarClientes());
    }

    @FXML
    private void onBuscar() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            carregarDados();
            return;
        }

        // Lógica simples de filtro local ou via service com base nos CheckBoxes selecionados
        ObservableList<Cliente> filtrados = FXCollections.observableArrayList();
        for (Cliente c : clienteService.listarClientes()) {
            boolean bateNome = chkNome.isSelected() && c.getNome().toLowerCase().contains(termo.toLowerCase());
            boolean bateCpf = chkCpf.isSelected() && c.getCpf().contains(termo);
            if (bateNome || bateCpf) {
                filtrados.add(c);
            }
        }
        listaClientes.setAll(filtrados);
    }

    @FXML
    private void onFiltroChanged() {
        onBuscar();
    }

    @FXML
    private void onAdicionar() {
        abrirFormulario(null);
    }

    private void abrirFormulario(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufersa/sedan/views/clienteFormView.fxml"));
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
                if (clienteService.removerCliente(cliente.getCpf())) {
                    carregarDados();
                } else {
                    mostrarErro("Não foi possível remover o cliente.");
                }
            }
        });
    }

    // ── Navegação da Sidebar ─────────────────────────────────────
    @FXML private void onClientes() {}
    @FXML private void onVeiculos() { navegarPara("veiculoView.fxml"); }
    @FXML private void onPecas() { navegarPara("pecaView.fxml"); }
    @FXML private void onServicos() { navegarPara("servicoView.fxml"); }
    @FXML private void onOrcamentos() { navegarPara("orcamentoView.fxml"); }
    @FXML private void onOrdemServico() { navegarPara("ordemServicoView.fxml"); }
    @FXML private void onRelatorios() { navegarPara("relatorioView.fxml"); }
    @FXML private void onFuncionarios() { navegarPara("funcionarioView.fxml"); }

    @FXML
    private void onSair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sair");
        alert.setContentText("Deseja realmente sair?");
        alert.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                ((Stage) tabelaClientes.getScene().getWindow()).close();
            }
        });
    }

    private void navegarPara(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/ufersa/sedan/views/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) tabelaClientes.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            mostrarErro("Erro ao navegar: " + e.getMessage());
        }
    }

    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}