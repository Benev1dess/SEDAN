package br.edu.ufersa.sedan.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Orcamento {

    private int id;
    private Veiculo veiculo;
    private List<Peca> pecas;
    private List<Servico> servicos;
    private LocalDate data;

    // =========================================================================
    // CORREÇÃO: 1. Adicionado o construtor vazio que a OrcamentoDAO precisa!
    // =========================================================================
    public Orcamento() {
        this.pecas = new ArrayList<>();
        this.servicos = new ArrayList<>();
        this.data = LocalDate.now(); // Garante que mesmo vazio ele comece com a data de hoje
    }

    // 2. Mantido o seu construtor atual com parâmetros
    public Orcamento(int id, Veiculo veiculo) {
        this.id = id;
        this.veiculo = veiculo;
        this.pecas = new ArrayList<>();
        this.servicos = new ArrayList<>();
        this.data = LocalDate.now();
    }

    public void adicionarPeca(Peca p) {
        pecas.add(p);
    }

    public void adicionarServico(Servico s) {
        servicos.add(s);
    }

    public double calcularTotal() {
        double total = 0;

        for (Peca p : pecas) {
            total += p.getPreco();
        }

        for (Servico s : servicos) {
            total += s.getPreco();
        }

        return total;
    }

    // =========================================================================
    // CORREÇÃO: 3. Adicionados os métodos SETTERS que a OrcamentoDAO usa para popular o objeto
    // =========================================================================
    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public void setPecas(List<Peca> pecas) {
        this.pecas = pecas;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    // =========================================================================
    // Getters e Setters que você já tinha criado
    // =========================================================================
    public Veiculo getVeiculo() {
        return veiculo;
    }

    public List<Peca> getPecas() {
        // Retorna a lista real para a DAO conseguir ler os itens, ou mantém a cópia segura
        return pecas;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public LocalDate getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}