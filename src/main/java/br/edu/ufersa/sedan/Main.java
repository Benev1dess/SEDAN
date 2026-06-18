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
                URL fxmlUrl = getClass().getResource("/br/edu/ufersa/sedan/views/veiculoView.fxml");

                if (fxmlUrl == null) {
                        throw new IllegalStateException(
                                " Verifique se o arquivo está em " +
                                        "src/main/resources/br/edu/ufersa/sedan/views/loginView.fxml e se o projeto foi recompilado."
                        );
                }

                Parent root = FXMLLoader.load(fxmlUrl);
                stage.setScene(new Scene(root));
                stage.setTitle("Sedan Project - cliente");
                stage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}