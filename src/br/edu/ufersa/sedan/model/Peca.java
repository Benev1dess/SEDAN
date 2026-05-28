package br.edu.ufersa.sedan.model;

public class Peca {

    private String nome;
    private double preco;
    private String fabricante;

    public Peca(String nome, double preco, String fabricante) {
        setNome(nome);
        setPreco(preco);
        setFabricante(fabricante);
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

    public String toString() {
        return nome + " - R$" + preco + " (" + fabricante + ")";
    }
}