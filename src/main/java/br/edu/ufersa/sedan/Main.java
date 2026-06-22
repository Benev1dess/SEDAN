package br.edu.ufersa.sedan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

        @Override
        public void start(Stage stage) throws Exception {
                // ALTERADO: Mudamos de loginView.fxml para pecaView.fxml aqui na String
                URL fxmlUrl = getClass().getResource("/br/edu/ufersa/sedan/views/pecaView.fxml");

                if (fxmlUrl == null) {
                        throw new IllegalStateException(
                                "O arquivo pecaView.fxml não foi encontrado. " +
                                        "Verifique se o arquivo está em src/main/resources/br/edu/ufersa/sedan/views/pecaView.fxml"
                        );
                }

                Parent root = FXMLLoader.load(fxmlUrl);
                stage.setScene(new Scene(root, 1024, 720)); // Definido o tamanho para encaixar o layout
                stage.setTitle("Oficina do Seu Zé - Gerenciamento de Peças");
                stage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}