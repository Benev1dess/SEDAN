package br.edu.ufersa.sedan.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Orcamento {

    private Veiculo veiculo;
    private List<Peca> pecas;
    private List<Servico> servicos;
    private LocalDate data;

    public Orcamento(Veiculo veiculo) {
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

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public List<Peca> getPecas() {
        return new ArrayList<>(pecas);
    }

    public List<Servico> getServicos() {
        return new ArrayList<>(servicos);
    }

    public LocalDate getData() {
        return data;
    }
}

