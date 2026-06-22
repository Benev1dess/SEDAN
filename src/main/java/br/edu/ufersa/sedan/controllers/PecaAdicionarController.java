package br.edu.ufersa.sedan.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import br.edu.ufersa.sedan.model.entities.Peca;

public class PecaAdicionarController {

    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private TextField txtFabricante;

    @FXML private ComboBox<String> cbCliente;
    @FXML private ComboBox<String> cbAutomovel;

    @FXML private Button btnCancelar;
    @FXML private Button btnConfirmar;

    // Referência da lista observável mapeada no PecaController principal
    private ObservableList<Peca> listaPecasDoMetodoPrincipal;

    /**
     * Método para receber a lista controlada pela TableView principal.
     */
    public void setListaPecas(ObservableList<Peca> lista) {
        this.listaPecasDoMetodoPrincipal = lista;
    }

    @FXML
    public void initialize() {
        // Popula provisoriamente os seletores para testes visuais.
        // TODO: Substituir pelas chamadas dos seus serviços reais (ex: clienteService.listar())
        cbCliente.getItems().addAll("João Silva", "Maria Oliveira", "Carlos Souza");
        cbAutomovel.getItems().addAll("ABC-1234 (Gol)", "XYZ-9876 (Onix)", "KJS-4421 (Civic)");

        // Ação do Botão CANCELAR - Apenas fecha o modal sem alterar nada
        btnCancelar.setOnAction(e -> fecharJanela(btnCancelar));

        // Ação do Botão CONFIRMAR - Valida, converte e adiciona na tabela
        btnConfirmar.setOnAction(e -> {
            String nome = txtNome.getText();
            String precoStr = txtPreco.getText();
            String fabricante = txtFabricante.getText();

            // Pega o valor selecionado ou define um padrão caso esteja nulo
            String cliente = cbCliente.getValue() != null ? cbCliente.getValue() : "Não Informado";
            String carro = cbAutomovel.getValue() != null ? cbAutomovel.getValue() : "Não Informado";

            // Validação simples de interface
            if (nome == null || nome.isBlank() || fabricante == null || fabricante.isBlank()) {
                System.out.println("Erro: Campos obrigatórios (Nome e Fabricante) não preenchidos.");
                return;
            }

            if (listaPecasDoMetodoPrincipal != null) {
                try {
                    // Trata string vazia ou formata para double padrão
                    double preco = 0.0;
                    if (precoStr != null && !precoStr.isBlank()) {
                        preco = Double.parseDouble(precoStr.trim().replace(",", "."));
                    }

                    // Instanciação limpa utilizando o construtor padrão, evitando problemas com IDs
                    Peca novaPeca = new Peca();
                    novaPeca.setNome(nome);
                    novaPeca.setPreco(preco);
                    novaPeca.setFabricante(fabricante);
                    novaPeca.setCarro(carro);
                    novaPeca.setCliente(cliente);

                    // TODO: Aqui você chama o seu service para persistir no banco se desejar:
                    // pecaService.salvar(novaPeca);

                    // Adiciona na lista do controlador principal (atualiza as colunas visualmente na hora!)
                    listaPecasDoMetodoPrincipal.add(novaPeca);

                    // Fecha a tela de pop-up após o sucesso
                    fecharJanela(btnConfirmar);

                } catch (NumberFormatException ex) {
                    System.out.println("Erro: Formato de preço inválido. Digite apenas números.");
                } catch (IllegalArgumentException ex) {
                    // Captura as exceções que você jogou dentro dos métodos set da sua entidade Peca
                    System.out.println("Erro de validação da Entidade: " + ex.getMessage());
                }
            }
        });
    }

    /**
     * Método auxiliar para fechar o estágio (Stage) atual baseado em um nó da cena.
     */
    private void fecharJanela(Button botao) {
        Stage stage = (Stage) botao.getScene().getWindow();
        stage.close();
    }
}