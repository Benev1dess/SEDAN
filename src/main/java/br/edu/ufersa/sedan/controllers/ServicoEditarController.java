package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Servico;
import br.edu.ufersa.sedan.model.services.ServicoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServicoEditarController {

    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private Label lblErro;

    private final ServicoService servicoService = new ServicoService();
    private Servico servicoOriginal;
    private Runnable aoSalvar;

    public void setAoSalvar(Runnable aoSalvar) {
        this.aoSalvar = aoSalvar;
    }

    public void setServico(Servico servico) {
        this.servicoOriginal = servico;
        if (servico != null) {
            txtNome.setText(servico.getNome());
            txtPreco.setText(String.valueOf(servico.getPreco()));
        }
    }

    @FXML
    private void onSalvar() {
        lblErro.setText("");
        try {
            String novoNome = txtNome.getText().trim();
            String precoStr = txtPreco.getText().trim();

            if (novoNome.isEmpty() || precoStr.isEmpty()) {
                lblErro.setText("Preencha todos os campos.");
                return;
            }

            double novoPreco = Double.parseDouble(precoStr.replace(",", "."));

            Servico servicoAtualizado = new Servico(novoNome, novoPreco);

            // Altera passando o nome antigo (chave de busca) e o novo objeto
            servicoService.alterar(servicoOriginal.getNome(), servicoAtualizado);

            if (aoSalvar != null) {
                aoSalvar.run();
            }
            onCancelar();

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