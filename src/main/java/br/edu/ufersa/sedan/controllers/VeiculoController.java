package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Veiculo;
import br.edu.ufersa.sedan.model.services.VeiculoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    // ── Service ───────────────────────────────────────────────
    private final VeiculoService veiculoService = new VeiculoService();

    // ── Lista master ──────────────────────────────────────────
    private ObservableList<Veiculo> listaMaster = FXCollections.observableArrayList();

    // ── Inicialização ─────────────────────────────────────────
    @FXML
    public void initialize() {
        carregarLogo();
        configurarColunas();
        carregarVeiculos();
    }

    private void carregarLogo() {
        try {
            Image logo = new Image(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream("/br/edu/ufersa/sedan/images/logo.png")
                    )
            );
            logoImage.setImage(logo);
        } catch (Exception e) {
            // Logo não encontrada — header fica sem imagem, sem quebrar a tela
        }
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

        // Coluna ações: ✏ editar | 🗑 deletar
        colAcoes.setCellFactory(col -> new TableCell<>() {

            private final Button btnEditar  = new Button("✏");
            private final Button btnDeletar = new Button("🗑");

            {
                btnEditar .getStyleClass().add("action-button");
                btnDeletar.getStyleClass().add("action-button-delete");

                btnEditar .setOnAction(e ->
                        abrirDialogo(getTableView().getItems().get(getIndex())));
                btnDeletar.setOnAction(e ->
                        confirmarExclusao(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(4, btnEditar, btnDeletar);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
    }

    // ── Carregar do BD via Service ────────────────────────────
    private void carregarVeiculos() {
        List<Veiculo> lista = veiculoService.listarVeiculos();
        listaMaster = FXCollections.observableArrayList(lista);
        tabelaVeiculos.setItems(listaMaster);
    }

    // ── Busca / Filtro ────────────────────────────────────────
    @FXML
    private void onBuscar() {
        String termo = txtBusca.getText().trim().toLowerCase();

        if (termo.isEmpty()) {
            tabelaVeiculos.setItems(listaMaster);
            return;
        }

        boolean porNome  = chkNomeProprietario.isSelected();
        boolean porPlaca = chkPlaca.isSelected();

        List<Veiculo> filtrados = listaMaster.stream()
                .filter(v -> {
                    boolean mNome  = porNome
                            && v.getDono() != null
                            && v.getDono().getNome().toLowerCase().contains(termo);
                    boolean mPlaca = porPlaca
                            && v.getPlaca().toLowerCase().contains(termo);
                    return mNome || mPlaca;
                })
                .collect(Collectors.toList());

        tabelaVeiculos.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML private void onFiltroChanged() { onBuscar(); }

    // ── Botão Adicionar ───────────────────────────────────────
    @FXML
    private void onAdicionar() {
        abrirDialogo(null);
    }

    // ── Diálogo Adicionar / Editar ────────────────────────────
    private void abrirDialogo(Veiculo existente) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(existente == null ? "Adicionar Veículo" : "Editar Veículo");
        dialog.setResizable(false);

        TextField tfPlaca = campo("Ex: ABC1234");
        TextField tfMarca = campo("Ex: Fiat Uno");
        TextField tfCor   = campo("Ex: Azul");
        TextField tfAno   = campo("Ex: 2020");
        TextField tfKm    = campo("Ex: 45000");

        if (existente != null) {
            tfPlaca.setText(existente.getPlaca());
            tfMarca.setText(existente.getMarca());
            tfCor  .setText(existente.getCor());
            tfAno  .setText(String.valueOf(existente.getAno()));
            tfKm   .setText(String.valueOf(existente.getKm()));
        }

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill:#cc0000;-fx-font-size:12px;");

        Button btnSalvar   = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");
        btnSalvar  .getStyleClass().add("registro-button");
        btnCancelar.getStyleClass().add("add-button");

        btnSalvar.setOnAction(e -> {
            lblErro.setText("");
            try {
                String placa = tfPlaca.getText().trim();
                String marca = tfMarca.getText().trim();
                String cor   = tfCor  .getText().trim();
                int    ano   = Integer.parseInt(tfAno.getText().trim());
                int    km    = Integer.parseInt(tfKm .getText().trim());

                if (placa.isEmpty() || marca.isEmpty() || cor.isEmpty()) {
                    lblErro.setText("Preencha todos os campos obrigatórios.");
                    return;
                }

                if (existente == null) {
                    Veiculo novo = new Veiculo();
                    novo.setPlaca(placa);
                    novo.setMarca(marca);
                    novo.setCor(cor);
                    novo.setAno(ano);
                    novo.setKm(km);
                    veiculoService.cadastrarVeiculo(novo);
                } else {
                    veiculoService.alterarVeiculo(
                            existente.getPlaca(), marca, cor, placa, ano, km);
                }

                carregarVeiculos();
                dialog.close();

            } catch (NumberFormatException ex) {
                lblErro.setText("Ano e KM precisam ser números inteiros.");
            }
        });

        btnCancelar.setOnAction(e -> dialog.close());

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(24));
        grid.setStyle("-fx-background-color:#d9d9d9;");

        String[]    rotulos = {"Placa *", "Marca *", "Cor *", "Ano *", "KM *"};
        TextField[] campos  = {tfPlaca, tfMarca, tfCor, tfAno, tfKm};

        for (int i = 0; i < rotulos.length; i++) {
            Label lbl = new Label(rotulos[i]);
            lbl.getStyleClass().add("field-label");
            grid.add(lbl,      0, i);
            grid.add(campos[i], 1, i);
        }

        grid.add(lblErro, 0, rotulos.length, 2, 1);

        HBox botoes = new HBox(10, btnCancelar, btnSalvar);
        botoes.setAlignment(Pos.CENTER_RIGHT);
        botoes.setPadding(new Insets(8, 0, 0, 0));
        grid.add(botoes, 0, rotulos.length + 1, 2, 1);

        Scene scene = new Scene(grid, 380, 330);
        scene.getStylesheets().add(
                getClass().getResource("/br/edu/ufersa/sedan/css/style.css").toExternalForm()
        );
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private TextField campo(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("text-input");
        tf.setPrefWidth(220);
        return tf;
    }

    // ── Confirmar exclusão ────────────────────────────────────
    private void confirmarExclusao(Veiculo veiculo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir veículo");
        alert.setContentText(
                "Deseja excluir o veículo de placa " + veiculo.getPlaca() + "?\n" +
                        "Esta ação não pode ser desfeita.");

        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            boolean ok = veiculoService.removerVeiculo(veiculo.getPlaca());
            if (!ok) {
                new Alert(Alert.AlertType.ERROR,
                        "Não foi possível remover o veículo.", ButtonType.OK)
                        .showAndWait();
            } else {
                carregarVeiculos();
            }
        }
    }

    // ── Navegação da Sidebar ──────────────────────────────────
    // Implemente os métodos abaixo seguindo o mesmo padrão
    // do ClienteController (trocar a cena pelo FXML correspondente)

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
        Stage stage = (Stage) tabelaVeiculos.getScene().getWindow();
        stage.close();
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