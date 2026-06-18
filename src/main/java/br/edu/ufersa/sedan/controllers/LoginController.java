package br.edu.ufersa.sedan.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private void handleLogin() {
        String usuario = usuarioField.getText().trim();
        String senha = senhaField.getText().trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Atenção", "Preencha usuário e senha para continuar.");
            return;
        }

        // TODO: chamar o service/DAO de autenticação (model.services / model.DAO)
        boolean credenciaisValidas = autenticar(usuario, senha);

        if (credenciaisValidas) {
            System.out.println("Login realizado com sucesso: " + usuario);
            // TODO: abrir a tela principal do sistema
        } else {
            mostrarAlerta("Erro", "Usuário ou senha inválidos.");
        }
    }

    @FXML
    private void handleRegistro() {
        // TODO: abrir a tela de cadastro de novo usuário
        System.out.println("Abrindo tela de registro...");
    }

    private boolean autenticar(String usuario, String senha) {
        // Lógica de autenticação real deve ser implementada aqui,
        // delegando para uma classe em model.services, por exemplo.
        return true;
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

