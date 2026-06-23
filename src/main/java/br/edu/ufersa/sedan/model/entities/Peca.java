package br.edu.ufersa.sedan.model.entities;

public class Peca {

    private int id;
    private String nome;
    private double preco;
    private String fabricante;

    // Novas propriedades para associar com os dados da tabela
    private String carro;   // Se tiver uma classe Veiculo, pode usar: private Veiculo carro;
    private String cliente; // Se tiver uma classe Cliente, pode usar: private Cliente cliente;

    public Peca() {
        this.id = 0;
        this.nome = "Item Não Identificado";
        this.preco = 0.0;
        this.fabricante = "Genérico";
        this.carro = "Não informado";
        this.cliente = "Não informado";
    }

    // Construtor completo atualizado para receber carro e cliente da tela
    public Peca(int id, String nome, double preco, String fabricante, String carro, String cliente) {
        setId(id);
        setNome(nome);
        setPreco(preco);
        setFabricante(fabricante);
        setCarro(carro);
        setCliente(cliente);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getFabricante() {
        return fabricante;
    }

    // GETTERS E SETTERS das novas propriedades (Obrigatórios para o JavaFX preencher a tabela)
    public String getCarro() {
        return carro;
    }

    public void setCarro(String carro) {
        this.carro = carro;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        this.nome = nome;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        this.preco = preco;
    }

    public void setFabricante(String fabricante) {
        if (fabricante == null || fabricante.isBlank()) {
            throw new IllegalArgumentException("Fabricante inválido");
        }
        this.fabricante = fabricante;
    }

    @Override
    public String toString() {
        return "[ID: " + id + "] " + nome + " - R$" + preco + " (" + fabricante + ")";
    }
}