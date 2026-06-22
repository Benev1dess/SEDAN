package br.edu.ufersa.sedan.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import br.edu.ufersa.sedan.model.entities.Peca;
import br.edu.ufersa.sedan.model.services.PecaService;

public class PecaAdicionarController {

    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private TextField txtFabricante;

    @FXML private ComboBox<String> cbCliente;
    @FXML private ComboBox<String> cbAutomovel;

    @FXML private Button btnCancelar;
    @FXML private Button btnConfirmar;

    // Removemos o uso da lista compartilhada para evitar conflito com o método atualizarTabelaDoBanco()
    private final PecaService pecaService = new PecaService();

    public void setListaPecas(ObservableList<Peca> lista) {
        // Mantido apenas por compatibilidade se outras classes chamarem, mas não vamos mexer nela aqui
    }

    @FXML
    public void initialize() {
        cbCliente.getItems().addAll("João Silva", "Maria Oliveira", "Carlos Souza");
        cbAutomovel.getItems().addAll("ABC-1234 (Gol)", "XYZ-9876 (Onix)", "KJS-4421 (Civic)");

        btnCancelar.setOnAction(e -> fecharJanela(btnCancelar));
        btnConfirmar.setOnAction(e -> salvar());
    }

    private void salvar() {
        String nome = txtNome.getText();
        String precoStr = txtPreco.getText();
        String fabricante = txtFabricante.getText();

        String cliente = cbCliente.getValue() != null ? cbCliente.getValue() : "Não Informado";
        String carro = cbAutomovel.getValue() != null ? cbAutomovel.getValue() : "Não Informado";

        if (nome == null || nome.isBlank() || fabricante == null || fabricante.isBlank()) {
            System.out.println("Erro: Campos obrigatórios não preenchidos.");
            return;
        }

        try {
            double preco = 0.0;
            if (precoStr != null && !precoStr.isBlank()) {
                preco = Double.parseDouble(precoStr.trim().replace(",", "."));
            }

            Peca novaPeca = new Peca();
            novaPeca.setNome(nome);
            novaPeca.setPreco(preco);
            novaPeca.setFabricante(fabricante);
            novaPeca.setCarro(carro);
            novaPeca.setCliente(cliente);

            // Tenta salvar de verdade no banco de dados
            pecaService.adicionar(novaPeca);

            // Se salvou com sucesso, fecha a janela com segurança
            fecharJanela(btnConfirmar);

        } catch (NumberFormatException ex) {
            System.out.println("Erro: Preço inválido.");
        } catch (Exception ex) {
            System.out.println("Erro crítico ao salvar no banco de dados:");
            ex.printStackTrace();
        }
    }

    private void fecharJanela(Button botao) {
        Stage stage = (Stage) botao.getScene().getWindow();
        stage.close();
    }
}