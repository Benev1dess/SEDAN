package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.services.PecaService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PecaAdicionarController {

    @FXML private TextField txtNome;
    @FXML private TextField txtFabricante;
    @FXML private TextField txtPreco;
    @FXML private Label lblErro;

    private PecaService pecaService;
    private Runnable aoSalvar;

    public void setDependencias(PecaService ps, Runnable callback) {
        this.pecaService = ps;
        this.aoSalvar = callback;
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        String nome = txtNome.getText().trim();
        String fabricante = txtFabricante.getText().trim();
        String precoStr = txtPreco.getText().trim().replace(",", ".");

        if (nome.isEmpty() || fabricante.isEmpty() || precoStr.isEmpty()) {
            lblErro.setText("Todos os campos estruturais devem ser preenchidos.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr);
            Peca novaPeca = new Peca();
            novaPeca.setNome(nome);
            novaPeca.setFabricante(fabricante);
            novaPeca.setPreco(preco);

            pecaService.adicionar(novaPeca);
            if (aoSalvar != null) aoSalvar.run();
            fechar();
        } catch (NumberFormatException e) {
            lblErro.setText("O formato do preço está inválido. Use números (ex: 150.50).");
        } catch (IllegalArgumentException e) {
            lblErro.setText(e.getMessage());
        }
    }

    @FXML private void onCancelar() { fechar(); }
    private void fechar() { ((Stage) txtNome.getScene().getWindow()).close(); }
}