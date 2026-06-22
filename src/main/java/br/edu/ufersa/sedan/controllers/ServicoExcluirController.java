package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Servico;
import br.edu.ufersa.sedan.model.services.ServicoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ServicoExcluirController {

    public static Stage stage; // Mantendo o padrão que você usou em veículo

    @FXML private Label lblMensagem;

    private final ServicoService servicoService = new ServicoService();
    private Servico servicoParaExcluir;
    private Runnable aoExcluir;

    public void setAoExcluir(Runnable aoExcluir) {
        this.aoExcluir = aoExcluir;
    }

    public void setServico(Servico servico) {
        this.servicoParaExcluir = servico;
        if (servico != null) {
            lblMensagem.setText("Deseja realmente excluir o serviço:\n\"" + servico.getNome() + "\"?");
        }
    }

    @FXML
    private void onConfirmar() {
        if (servicoParaExcluir != null) {
            try {
                servicoService.deletar(servicoParaExcluir.getNome());
                if (aoExcluir != null) {
                    aoExcluir.run();
                }
                onCancelar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onCancelar() {
        if (stage != null) {
            stage.close();
        }
    }
}