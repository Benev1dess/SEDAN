package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.TipoUsuario;
import br.edu.ufersa.sedan.util.Sessao;
import br.edu.ufersa.sedan.model.entities.Usuario;
import br.edu.ufersa.sedan.model.services.UsuarioService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class FuncionarioController {

    @FXML private TableView<Usuario> tabelaFuncionario;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colLogin;
    @FXML private TableColumn<Usuario, String> colTipo;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, Double> colSalario;
    @FXML private TableColumn<Usuario, Void> colAcoes;

    @FXML private void onClientes()     { navegarPara("/br/edu/ufersa/sedan/views/clienteView.fxml"); }
    @FXML private void onVeiculos()     { navegarPara("/br/edu/ufersa/sedan/views/veiculoView.fxml");}
    @FXML private void onPecas()        { navegarPara("/br/edu/ufersa/sedan/views/pecaView.fxml"); }
    @FXML private void onServicos()     { navegarPara("/br/edu/ufersa/sedan/views/servicoView.fxml"); }
    @FXML private void onOrcamentos()   { navegarPara("/br/edu/ufersa/sedan/views/orcamentoView.fxml"); }
    @FXML private void onOrdemServico() { navegarPara("/br/edu/ufersa/sedan/views/ordemServicoView.fxml"); }
    @FXML private void onRelatorios()   { navegarPara("/br/edu/ufersa/sedan/views/relatorioView.fxml"); }
    @FXML private void onFuncionarios() { }

    @FXML private TextField txtBusca;
    @FXML private CheckBox chkNome, chkLogin;

    private UsuarioService usuarioService = new UsuarioService();
    private ObservableList<Usuario> masterData;
    private FilteredList<Usuario> filteredData;

    @FXML
    public void initialize() {

        if (Sessao.getInstance().getUsuario().getTipo() != TipoUsuario.ADM) {

            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Acesso negado. Apenas administradores podem acessar esta tela."
            );

            alert.showAndWait();

            navegarPara("/br/edu/ufersa/sedan/views/clienteView.fxml");
            return;
        }

        colNome.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNome()));

        colLogin.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getLogin()));
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo().name()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colSalario.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        cell.getValue().getSalario()
                ));

        configurarColunaAcoes();

        masterData = FXCollections.observableArrayList(usuarioService.listarTodos());
        filteredData = new FilteredList<>(masterData, p -> true);
        tabelaFuncionario.setItems(filteredData);
    }

    private void configurarColunaAcoes() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("✏");
            private final Button btnExcluir = new Button("🗑");

            {
                btnEditar.setOnAction(e -> editarUsuario(getTableView().getItems().get(getIndex())));
                btnExcluir.setOnAction(e -> excluirUsuario(getTableView().getItems().get(getIndex())));
                btnEditar.setStyle("-fx-background-color: transparent; -fx-text-fill: blue;");
                btnExcluir.setStyle("-fx-background-color: transparent; -fx-text-fill: red;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(new HBox(5, btnEditar, btnExcluir));
            }
        });
    }

    @FXML
    public void onBuscar() {
        String busca = txtBusca.getText().toLowerCase();
        filteredData.setPredicate(usuario -> {
            if (busca.isEmpty()) return true;

            boolean match = false;
            if (chkNome.isSelected() && usuario.getNome().toLowerCase().contains(busca)) match = true;
            if (chkLogin.isSelected() && usuario.getLogin().toLowerCase().contains(busca)) match = true;

            return match;
        });
    }

    @FXML
    public void onFiltroChanged() {
        onBuscar(); // Re-aplica o filtro
    }

    @FXML
    public void onAdicionar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/br/edu/ufersa/sedan/views/funcionarioAdicionarDialog.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Funcionário");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            masterData.setAll(usuarioService.listarTodos());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editarUsuario(Usuario usuario) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/br/edu/ufersa/sedan/views/funcionarioEditarDialog.fxml"
                    )
            );

            Parent root = loader.load();

            FuncionarioEditarController controller =
                    loader.getController();

            controller.setUsuario(usuario);

            Stage stage = new Stage();
            stage.setTitle("Editar Funcionário");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            masterData.setAll(usuarioService.listarTodos());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excluirUsuario(Usuario usuario) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/br/edu/ufersa/sedan/views/funcionarioExcluirDialog.fxml"
                    )
            );

            Parent root = loader.load();

            FuncionarioExcluirController controller =
                    loader.getController();

            controller.setUsuario(usuario);

            Stage stage = new Stage();
            stage.setTitle("Excluir Funcionário");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            masterData.setAll(usuarioService.listarTodos());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void navegarPara(String fxmlPath) {
        try {
            // 1. Tente capturar a URL
            java.net.URL url = getClass().getResource(fxmlPath);

            // 2. Se for null, o problema é o caminho!
            if (url == null) {
                System.err.println("ERRO FATAL: Arquivo não encontrado.");
                System.err.println("Caminho buscado: " + fxmlPath);
                System.err.println("DICA: Verifique se o arquivo está na pasta 'src/main/resources'");
                return; // Interrompe a execução antes do erro acontecer
            }

            // 3. Se passou pelo if, o arquivo existe!
            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) tabelaFuncionario.getScene().getWindow(); // Certifique-se que tabelaClientes existe
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onSair() {
        // Fecha a aplicação completamente
        Platform.exit();
        System.exit(0);
    }
}
