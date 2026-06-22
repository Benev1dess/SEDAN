package br.edu.ufersa.sedan.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PecaExcluirController {

    @FXML private Button btnCancelar;
    @FXML private Button btnConfirmar;

    private boolean confirmado = false;

    @FXML
    public void initialize() {
        btnCancelar.setOnAction(e -> fecharJanela());
        btnConfirmar.setOnAction(e -> {
            confirmado = true;
            fecharJanela();
        });
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}