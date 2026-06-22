package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Cliente;
import br.edu.ufersa.sedan.model.entities.Veiculo;
import br.edu.ufersa.sedan.model.services.ClienteService;
import br.edu.ufersa.sedan.model.services.VeiculoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VeiculoAdicionarController {

    @FXML private TextField txtBuscaCliente;
    @FXML private ListView<Cliente> listaSugestoes;

    @FXML private TextField tfAutomovel;
    @FXML private TextField tfPlaca;
    @FXML private TextField tfMarca;
    @FXML private TextField tfAno;
    @FXML private TextField tfKm;
    @FXML private Label     lblErro;

    private final VeiculoService veiculoService = new VeiculoService();
    private final ClienteService clienteService = new ClienteService();

    private Cliente clienteSelecionado = null;
    private Runnable aoSalvar;

    public void setAoSalvar(Runnable r) { this.aoSalvar = r; }

    @FXML
    public void initialize() {
        // Exibe o Nome e o CPF do cliente na lista com tamanho expandido e espaçamento confortável
        listaSugestoes.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(c.getNome() + " — " + (c.getCpf() != null ? c.getCpf() : "Sem CPF"));
                    // Aplica um estofamento (padding) generoso para desgrudar as linhas
                    setStyle("-fx-padding: 8px 12px; -fx-font-size: 13px;");
                }
            }
        });

        // Evento de clique na lista de sugestões
        listaSugestoes.setOnMouseClicked(e -> {
            Cliente selecionado = listaSugestoes.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                selecionarCliente(selecionado);
            }
        });
    }

    @FXML
    private void onBuscarCliente() {
        String termo = txtBuscaCliente.getText().trim();
        clienteSelecionado = null; // Reseta seleção antiga enquanto digita

        if (termo.isEmpty()) {
            esconderSugestoes();
            return;
        }

        // Recupera os clientes usando a sua estrutura real de Service
        List<Cliente> todosClientes = clienteService.listarClientes();
        if (todosClientes == null) {
            esconderSugestoes();
            return;
        }

        String termoLower = termo.toLowerCase();
        ArrayList<Cliente> encontrados = new ArrayList<>();

        // Filtra os clientes dinamicamente
        for (Cliente c : todosClientes) {
            boolean matchNome = c.getNome() != null && c.getNome().toLowerCase().contains(termoLower);
            boolean matchCpf  = c.getCpf() != null && c.getCpf().contains(termo);

            if (matchNome || matchCpf) {
                encontrados.add(c);
            }
        }

        // Exibe ou esconde o painel baseado no resultado
        if (encontrados.isEmpty()) {
            esconderSugestoes();
        } else {
            listaSugestoes.setItems(FXCollections.observableArrayList(encontrados));
            listaSugestoes.setVisible(true);
            listaSugestoes.setManaged(true);
        }
    }

    private void selecionarCliente(Cliente c) {
        clienteSelecionado = c;
        txtBuscaCliente.setText(c.getNome());
        esconderSugestoes();
    }

    private void esconderSugestoes() {
        listaSugestoes.setVisible(false);
        listaSugestoes.setManaged(false);
    }

    @FXML
    private void onAdicionarNovoCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/br/edu/ufersa/sedan/views/clienteFormView.fxml")
            ));
            Parent root = loader.load();


            ClienteFormController formCtrl = loader.getController();


            formCtrl.setDados(null, clienteService, null);

            Stage stage = new Stage();
            stage.setTitle("Novo Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(txtBuscaCliente.getScene().getWindow());
            stage.showAndWait();

            List<Cliente> todos = clienteService.listarClientes();
            if (todos != null && !todos.isEmpty()) {
                selecionarCliente(todos.get(todos.size() - 1));
            }

        } catch (IOException e) {
            lblErro.setText("Erro ao abrir formulário de cliente.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        try {
            String placa     = tfPlaca.getText().trim();
            String automovel = tfAutomovel.getText().trim();
            String marca     = tfMarca.getText().trim();

            if (tfAno.getText().trim().isEmpty() || tfKm.getText().trim().isEmpty()) {
                lblErro.setText("Ano e KM não podem ser vazios.");
                return;
            }

            int ano = Integer.parseInt(tfAno.getText().trim());
            int km  = Integer.parseInt(tfKm.getText().trim());

            if (placa.isEmpty() || marca.isEmpty() || automovel.isEmpty()) {
                lblErro.setText("Preencha Automóvel, Placa e Marca.");
                return;
            }

            Veiculo novo = new Veiculo();
            novo.setPlaca(placa);
            novo.setMarca(marca);
            novo.setCor(automovel); // Mantido o mapeamento seguro
            novo.setAno(ano);
            novo.setKm(km);

            if (clienteSelecionado != null) {
                novo.setDono(clienteSelecionado);
            }

            veiculoService.cadastrarVeiculo(novo);

            if (aoSalvar != null) aoSalvar.run();
            fechar();

        } catch (NumberFormatException ex) {
            lblErro.setText("Ano e KM precisam ser números inteiros.");
        }
    }

    @FXML private void onCancelar() { fechar(); }

    private void fechar() {
        ((Stage) tfPlaca.getScene().getWindow()).close();
    }
}