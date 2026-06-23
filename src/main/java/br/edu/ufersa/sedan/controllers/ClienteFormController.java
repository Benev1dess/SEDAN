package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Cliente;
import br.edu.ufersa.sedan.model.entities.Endereco;
import br.edu.ufersa.sedan.model.services.ClienteService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ClienteFormController {

    // formulário
    @FXML private Label    lblTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtRua;
    @FXML private TextField txtNumero;
    @FXML private TextField txtBairro;

    // Estado
    private Cliente        clienteEditando; // null = novo cadastro
    private ClienteService clienteService;
    private Runnable       onSucesso;       // callback para atualizar a tabela pai

    public void setDados(Cliente cliente, ClienteService clienteService, Runnable onSucesso) {
        this.clienteEditando = cliente;
        this.clienteService  = clienteService;
        this.onSucesso       = onSucesso;

        if (cliente != null) {
            // Modo edição: preenche os campos
            lblTitulo.setText("Editar Cliente");
            txtNome.setText(cliente.getNome());
            txtCpf .setText(cliente.getCpf());
            txtCpf .setDisable(true); // CPF é a chave de busca, bloqueia edição direta

            Endereco e = cliente.getEndereco();
            if (e != null) {
                txtRua   .setText(e.getRua());
                txtNumero.setText(String.valueOf(e.getNum()));
                txtBairro.setText(e.getBairro());
            }
        } else {
            lblTitulo.setText("Novo Cliente");
        }
    }

    // Ações

    @FXML
    private void onSalvar() {
        if (!validar()) return;

        String nome   = txtNome  .getText().trim();
        String cpf    = txtCpf   .getText().replaceAll("[^0-9]", ""); // remove máscara
        String rua    = txtRua   .getText().trim();
        int    numero = Integer.parseInt(txtNumero.getText().trim());
        String bairro = txtBairro.getText().trim();

        Endereco endereco = new Endereco();
        endereco.setRua(rua);
        endereco.setNum(numero);
        endereco.setBairro(bairro);

        if (clienteEditando == null) {
            // Novo cliente
            Cliente novo = new Cliente();
            novo.setNome(nome);
            novo.setCpf(cpf);
            novo.setEndereco(endereco);
            clienteService.cadastrarCliente(novo);
        } else {
            //Atualizar cliente existente
            clienteService.alterarCliente(
                    clienteEditando.getCpf(),
                    nome,
                    clienteEditando.getCpf(), // CPF desabilitado: mantém o mesmo
                    endereco
            );
        }

        if (onSucesso != null) onSucesso.run();
        fechar();
    }

    @FXML
    private void onCancelar() {
        fechar();
    }

    //  Validação

    private boolean validar() {
        StringBuilder erros = new StringBuilder();

        if (txtNome.getText().trim().isEmpty())
            erros.append("• Nome é obrigatório.\n");

        String cpf = txtCpf.getText().replaceAll("[^0-9]", "");
        if (cpf.length() != 11)
            erros.append("• CPF deve ter 11 dígitos.\n");

        if (txtRua.getText().trim().isEmpty())
            erros.append("• Rua é obrigatória.\n");

        if (txtNumero.getText().trim().isEmpty()) {
            erros.append("• Número é obrigatório.\n");
        } else {
            try { Integer.parseInt(txtNumero.getText().trim()); }
            catch (NumberFormatException e) { erros.append("• Número deve ser inteiro.\n"); }
        }

        if (txtBairro.getText().trim().isEmpty())
            erros.append("• Bairro é obrigatório.\n");

        if (!erros.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos inválidos");
            alert.setHeaderText(null);
            alert.setContentText(erros.toString());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void fechar() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }
}
