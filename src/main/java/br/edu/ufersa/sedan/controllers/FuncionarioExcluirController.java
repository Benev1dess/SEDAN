package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Usuario;
import br.edu.ufersa.sedan.model.services.UsuarioService;
import br.edu.ufersa.sedan.util.Sessao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FuncionarioExcluirController {

    @FXML private Label lblMensagem;

    private Usuario usuario;

    private final UsuarioService usuarioService = new UsuarioService();

    public void setUsuario(Usuario usuario) {

        this.usuario = usuario;

        lblMensagem.setText(
                "Deseja realmente excluir o funcionário\n\n"
                        + usuario.getNome()
                        + " ?"
        );
    }

    @FXML
    private void onConfirmar() {

        try {

            usuarioService.excluirUsuario(
                    Sessao.getInstance().getUsuario(),
                    usuario.getId()
            );

            fechar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelar() {
        fechar();
    }

    private void fechar() {
        ((Stage) lblMensagem.getScene().getWindow()).close();
    }
}