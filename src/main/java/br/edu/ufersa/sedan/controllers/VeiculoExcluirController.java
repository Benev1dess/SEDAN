package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Veiculo;
import br.edu.ufersa.sedan.model.services.VeiculoService;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class VeiculoExcluirController {

    private final VeiculoService veiculoService = new VeiculoService();

    private Veiculo veiculo;
    private Runnable aoExcluir;

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
        // Fecha qualquer janela que contenha este controller
        // (pegamos o stage a partir de um nó qualquer — usamos o stage direto)
        Stage stage = VeiculoExcluirController.stage;
        if (stage != null) stage.close();
    }

    // Helper estático para passar o Stage antes do showAndWait
    public static Stage stage;
}