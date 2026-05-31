package br.edu.ufersa.sedan.model.entities;

public class Peca {

    private int id;
    private String nome;
    private double preco;
    private String fabricante;

    // =========================================================================
    // CORREÇÃO: Adicionado o construtor vazio que a OrcamentoDAO precisa!
    // Usamos valores padrão válidos para não quebrar suas regras dos setters
    // =========================================================================
    public Peca() {
        this.id = 0;
        this.nome = "Item Não Identificado";
        this.preco = 0.0;
        this.fabricante = "Genérico";
    }

    // Mantido o seu construtor completo com parâmetros
    public Peca(int id, String nome, double preco, String fabricante) {
        setId(id);
        setNome(nome);
        setPreco(preco);
        setFabricante(fabricante);
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