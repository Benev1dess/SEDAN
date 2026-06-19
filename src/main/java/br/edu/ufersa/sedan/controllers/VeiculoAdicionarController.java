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
import java.util.Objects;

public class VeiculoAdicionarController {

    @FXML private TextField txtBuscaCliente;
    @FXML private ListView<Cliente> listaSugestoes;

    @FXML private TextField tfAutomovel;
    @FXML private TextField tfPlaca;
    @FXML private TextField tfMarca;
    @FXML private TextField tfAno;
    @FXML private TextField tfKm;
    @FXML private TextField tfServico;
    @FXML private TextField tfPeca;
    @FXML private TextField tfValor;
    @FXML private Label     lblErro;

    private final VeiculoService veiculoService = new VeiculoService();
    private final ClienteService clienteService = new ClienteService();

    // Cliente selecionado na busca (ou recém-criado pelo formulário)
    private Cliente clienteSelecionado = null;

    // Callback executado após salvar (atualiza tabela no VeiculoController)
    private Runnable aoSalvar;
    public void setAoSalvar(Runnable r) { this.aoSalvar = r; }

    @FXML
    public void initialize() {
        // Mostra "Nome — CPF" em cada item da lista de sugestões
        listaSugestoes.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNome() + " — " + c.getCpf());
            }
        });

        // Clique em sugestão seleciona o cliente
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
        clienteSelecionado = null;

        if (termo.isEmpty()) {
            esconderSugestoes();
            return;
        }

        // Busca por nome OU CPF entre todos os clientes (usa apenas
        // o que já existe em ClienteService: listarClientes())
        ArrayList<Cliente> todos = (ArrayList<Cliente>) clienteService.listarClientes();
        String termoLower = termo.toLowerCase();

        ArrayList<Cliente> encontrados = new ArrayList<>();
        for (Cliente c : todos) {
            boolean matchNome = c.getNome() != null && c.getNome().toLowerCase().contains(termoLower);
            boolean matchCpf  = c.getCpf()  != null && c.getCpf().contains(termo);
            if (matchNome || matchCpf) {
                encontrados.add(c);
            }
        }

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

    /**
     * Abre o formulário real de cadastro de cliente (clienteFormView.fxml +
     * ClienteFormController), igual ao usado na tela de Clientes.
     * Ao salvar com sucesso, o cliente recém-criado é localizado pelo CPF
     * digitado e automaticamente selecionado aqui no diálogo de veículo.
     */
    @FXML
    private void onAdicionarNovoCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(
                            getClass().getResource("/br/edu/ufersa/sedan/views/clienteFormView.fxml")
                    )
            );
            Parent root = loader.load();

            ClienteFormController formCtrl = loader.getController();
            // null => modo "novo cliente"; o callback é chamado após salvar
            formCtrl.setDados(null, clienteService, null);

            Stage stage = new Stage();
            stage.setTitle("Novo Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(txtBuscaCliente.getScene().getWindow());
            stage.showAndWait();

            // Após o formulário fechar, tenta localizar o cliente mais
            // recentemente cadastrado e selecioná-lo automaticamente.
            recarregarESelecionarUltimoCliente();

        } catch (IOException e) {
            lblErro.setText("Erro ao abrir formulário de cliente: " + e.getMessage());
        }
    }

    private void recarregarESelecionarUltimoCliente() {
        ArrayList<Cliente> todos = (ArrayList<Cliente>) clienteService.listarClientes();
        if (!todos.isEmpty()) {
            // O cliente mais recente é o último da lista retornada pelo DAO
            Cliente ultimo = todos.get(todos.size() - 1);
            selecionarCliente(ultimo);
        }
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        try {
            String placa     = tfPlaca.getText().trim();
            String automovel = tfAutomovel.getText().trim(); // cor / modelo
            String marca     = tfMarca.getText().trim();
            int    ano       = Integer.parseInt(tfAno.getText().trim());
            int    km        = Integer.parseInt(tfKm .getText().trim());

            if (placa.isEmpty() || marca.isEmpty() || automovel.isEmpty()) {
                lblErro.setText("Preencha Automóvel, Placa e Marca.");
                return;
            }

            Veiculo novo = new Veiculo();
            novo.setPlaca(placa);
            novo.setCor(automovel);   // campo "Automóvel" mapeado como cor/modelo
            novo.setMarca(marca);
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

    @FXML
    private void onCancelar() { fechar(); }

    private void fechar() {
        ((Stage) tfPlaca.getScene().getWindow()).close();
    }
}