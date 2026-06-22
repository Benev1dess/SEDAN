package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.TipoUsuario;
import br.edu.ufersa.sedan.model.entities.Usuario;
import br.edu.ufersa.sedan.model.services.UsuarioService;
import br.edu.ufersa.sedan.util.Sessao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FuncionarioEditarController {

    @FXML private TextField tfNome;
    @FXML private TextField tfLogin;
    @FXML private PasswordField pfSenha;
    @FXML private TextField tfEmail;
    @FXML private TextField tfCpf;
    @FXML private TextField tfSalario;
    @FXML private ComboBox<TipoUsuario> cbTipo;
    @FXML private Label lblErro;

    private Usuario usuario;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {
        cbTipo.getItems().addAll(TipoUsuario.values());
    }

    public void setUsuario(Usuario usuario) {

        this.usuario = usuario;

        tfNome.setText(usuario.getNome());
        tfLogin.setText(usuario.getLogin());
        pfSenha.setText(usuario.getSenha());

        tfEmail.setText(usuario.getEmail());
        tfCpf.setText(usuario.getCpf());

        cbTipo.setValue(usuario.getTipo());

        if (usuario.getSalario() > 0) {
            tfSalario.setText(String.valueOf(usuario.getSalario()));
        }
    }

    @FXML
    private void onConfirmar() {

        try {

            Usuario atualizado = new Usuario();

            atualizado.setNome(tfNome.getText());
            atualizado.setLogin(tfLogin.getText());
            atualizado.setSenha(pfSenha.getText());

            atualizado.setTipo(cbTipo.getValue());

            if (cbTipo.getValue() == TipoUsuario.ADM) {
                atualizado.setEmail(tfEmail.getText());
            } else {
                atualizado.setCpf(tfCpf.getText());
                atualizado.setSalario(Double.parseDouble(tfSalario.getText()));
            }

            usuarioService.atualizarUsuario(
                    Sessao.getInstance().getUsuario(),
                    usuario.getId(),
                    atualizado
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
