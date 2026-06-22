package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Veiculo;
import br.edu.ufersa.sedan.model.services.VeiculoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class VeiculoEditarController {

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

    private Veiculo veiculoOriginal;
    private Runnable aoSalvar;

    public void setVeiculo(Veiculo v) {
        this.veiculoOriginal = v;
        tfAutomovel.setText(v.getCor());
        tfPlaca    .setText(v.getPlaca());
        tfMarca    .setText(v.getMarca());
        tfAno      .setText(String.valueOf(v.getAno()));
        tfKm       .setText(String.valueOf(v.getKm()));
    }

    public void setAoSalvar(Runnable r) { this.aoSalvar = r; }

    @FXML
    private void onConfirmar() {
        lblErro.setText("");
        try {
            String placa     = tfPlaca.getText().trim();
            String automovel = tfAutomovel.getText().trim();
            String marca     = tfMarca.getText().trim();
            int    ano       = Integer.parseInt(tfAno.getText().trim());
            int    km        = Integer.parseInt(tfKm.getText().trim());

            if (placa.isEmpty() || marca.isEmpty() || automovel.isEmpty()) {
                lblErro.setText("Preencha Automóvel, Placa e Marca.");
                return;
            }

            veiculoService.alterarVeiculo(
                    veiculoOriginal.getPlaca(),
                    marca, automovel, placa, ano, km
            );

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
