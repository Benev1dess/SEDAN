package br.edu.ufersa.sedan.model.entities;

public class Servico {

    private String nome;
    private double preco;

    // =========================================================================
    // CORREÇÃO: Adicionado o construtor vazio que a OrcamentoDAO precisa!
    // Inicializa com valores válidos para evitar problemas com regras de negócio
    // =========================================================================
    public Servico() {
        this.nome = "Serviço Não Definido";
        this.preco = 0.0;
    }

    // Mantido o seu construtor atual com parâmetros
    public Servico(String nome, double preco) {
        setNome(nome);
        setPreco(preco);
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do serviço inválido");
        }
        this.nome = nome;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço do serviço não pode ser negativo");
        }
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Serviço: " + nome + " - R$ " + preco;
    }
}