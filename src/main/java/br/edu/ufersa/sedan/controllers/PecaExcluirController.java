package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.services.PecaService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PecaExcluirController {

    @FXML private Label lblDetalhes;

    private Peca pecaAlvo;
    private PecaService pecaService;
    private Runnable aoDeletar;
    private Stage palco;

    public void setTarget(Peca p, PecaService ps, Runnable callback, Stage stage) {
        this.pecaAlvo = p;
        this.pecaService = ps;
        this.aoDeletar = callback;
        this.palco = stage;

        lblDetalhes.setText(p.getNome() + " (" + p.getFabricante() + ")");
    }

    @FXML
    private void onConfirmar() {
        try {
            if (pecaAlvo != null) {
                pecaService.deletar(pecaAlvo);
                if (aoDeletar != null) aoDeletar.run();
            }
            fechar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void onCancelar() { fechar(); }
    private void fechar() { palco.close(); }
}