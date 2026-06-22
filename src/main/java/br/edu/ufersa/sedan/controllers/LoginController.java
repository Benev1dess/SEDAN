package br.edu.ufersa.sedan.controllers;

import br.edu.ufersa.sedan.model.entities.Usuario;
import br.edu.ufersa.sedan.model.services.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML private TextField     usuarioField;
    @FXML private PasswordField senhaField;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void handleLogin() {
        String usuario = usuarioField.getText().trim();
        String senha   = senhaField.getText().trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção",
                    "Preencha usuário e senha para continuar.");
            return;
        }

        Usuario logado = usuarioService.fazerLogin(usuario, senha);

        if (logado != null) {
            abrirTelaPrincipal(logado);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro",
                    "Usuário ou senha inválidos.");
        }
    }



    private void abrirTelaPrincipal(Usuario usuarioLogado) {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/br/edu/ufersa/sedan/views/clienteView.fxml")
                    )
            );

            Stage stage = (Stage) usuarioField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Oficina do Seu Zé — " + usuarioLogado.getNome());

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro",
                    "Não foi possível abrir a tela principal: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
