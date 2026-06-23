package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Servico;
import br.edu.ufersa.sedan.model.services.ServicoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServicoAdicionarController {

    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private Label lblErro;

    private final ServicoService servicoService = new ServicoService();
    private Runnable aoSalvar;

    public void setAoSalvar(Runnable aoSalvar) {
        this.aoSalvar = aoSalvar;
    }

    @FXML
    private void onSalvar() {
        lblErro.setText("");
        try {
            String nome = txtNome.getText().trim();
            String precoStr = txtPreco.getText().trim();

            if (nome.isEmpty() || precoStr.isEmpty()) {
                lblErro.setText("Preencha todos os campos.");
                return;
            }

            double preco = Double.parseDouble(precoStr.replace(",", "."));

            Servico novo = new Servico(nome, preco);
            servicoService.adicionar(novo);

            if (aoSalvar != null) {
                aoSalvar.run(); // Atualiza a tabela principal
            }
            onCancelar(); // Fecha a modal

        } catch (NumberFormatException ex) {
            lblErro.setText("Preço inválido. Use apenas números.");
        } catch (IllegalArgumentException ex) {
            lblErro.setText(ex.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }
}