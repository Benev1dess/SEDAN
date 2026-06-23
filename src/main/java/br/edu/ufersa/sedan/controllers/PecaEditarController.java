package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.services.PecaService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PecaEditarController {

    @FXML private TextField txtNome;
    @FXML private TextField txtFabricante;
    @FXML private TextField txtPreco;
    @FXML private Label lblErro;

    private Peca pecaAlvo;
    private PecaService pecaService;
    private Runnable aoSalvar;

    public void setDados(Peca p, PecaService ps, Runnable callback) {
        this.pecaAlvo = p;
        this.pecaService = ps;
        this.aoSalvar = callback;

        txtNome.setText(p.getNome());
        txtFabricante.setText(p.getFabricante());
        txtPreco.setText(String.valueOf(p.getPreco()));
    }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        String fabricante = txtFabricante.getText().trim();
        String precoStr = txtPreco.getText().trim().replace(",", ".");

        if (fabricante.isEmpty() || precoStr.isEmpty()) {
            lblErro.setText("Campos de preço e fabricante não podem estar em branco.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr);
            pecaAlvo.setFabricante(fabricante);
            pecaAlvo.setPreco(preco);

            pecaService.alterar(pecaAlvo);
            if (aoSalvar != null) aoSalvar.run();
            fechar();
        } catch (NumberFormatException e) {
            lblErro.setText("O preço digitado é inválido.");
        } catch (IllegalArgumentException e) {
            lblErro.setText(e.getMessage());
        }
    }

    @FXML private void onCancelar() { fechar(); }
    private void fechar() { ((Stage) txtNome.getScene().getWindow()).close(); }
}