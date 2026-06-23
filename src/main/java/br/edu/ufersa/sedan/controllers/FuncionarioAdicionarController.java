package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.TipoUsuario;
import br.edu.ufersa.sedan.model.entities.Usuario;
import br.edu.ufersa.sedan.model.services.UsuarioService;
import br.edu.ufersa.sedan.util.Sessao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FuncionarioAdicionarController {

    @FXML private TextField tfNome;
    @FXML private TextField tfLogin;
    @FXML private PasswordField pfSenha;
    @FXML private TextField tfEmail;
    @FXML private TextField tfCpf;
    @FXML private TextField tfSalario;
    @FXML private ComboBox<TipoUsuario> cbTipo;
    @FXML private Label lblErro;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {
        cbTipo.getItems().addAll(TipoUsuario.values());
        cbTipo.setValue(TipoUsuario.FUNCIONARIO);
    }

    @FXML
    private void onConfirmar() {
        try {

            Usuario usuario = new Usuario();

            usuario.setNome(tfNome.getText());
            usuario.setLogin(tfLogin.getText());
            usuario.setSenha(pfSenha.getText());

            usuario.setTipo(cbTipo.getValue());

            if (cbTipo.getValue() == TipoUsuario.ADM) {
                usuario.setEmail(tfEmail.getText());
            } else {
                usuario.setCpf(tfCpf.getText());
                usuario.setSalario(Double.parseDouble(tfSalario.getText()));
            }

            usuarioService.registrarUsuario(
                    Sessao.getInstance().getUsuario(),
                    usuario
            );

            fechar();

        } catch (Exception e) {
            lblErro.setText(e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        fechar();
    }

    private void fechar() {
        ((Stage) tfNome.getScene().getWindow()).close();
    }
}