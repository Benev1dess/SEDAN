package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Veiculo;
import br.edu.ufersa.sedan.model.services.VeiculoService;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class VeiculoExcluirController {

    private final VeiculoService veiculoService = new VeiculoService();

    private Veiculo veiculo;
    private Runnable aoExcluir;

    public static Stage stage;

    public void setVeiculo(Veiculo v)     { this.veiculo    = v; }
    public void setAoExcluir(Runnable r)  { this.aoExcluir  = r; }

    @FXML
    private void onConfirmar() {
        if (veiculo != null) {
            veiculoService.removerVeiculo(veiculo.getPlaca());
            if (aoExcluir != null) aoExcluir.run();
        }
        fechar();
    }

    @FXML
    private void onCancelar() { fechar(); }

    private void fechar() {
        if (stage != null) {
            stage.close();
        }
    }
}