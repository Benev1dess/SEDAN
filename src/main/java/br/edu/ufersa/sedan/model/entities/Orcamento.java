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

    public Orcamento() {
        this.pecas = new ArrayList<>();
        this.servicos = new ArrayList<>();
        this.data = LocalDate.now();
    }

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
        double total = 0.0;

        if (this.pecas != null) {
            for (Peca p : this.pecas) {
                if (p != null) {
                    total += p.getPreco();
                }
            }
        }

        if (this.servicos != null) {
            for (Servico s : this.servicos) {
                if (s != null) {
                    total += s.getPreco();
                }
            }
        }

        return total;
    }

    // ── SOBREESCRITA DO TOSTRING PARA O COMBOBOX ──────────────────────
    @Override
    public String toString() {
        String placa = (this.veiculo != null) ? this.veiculo.getPlaca() : "Sem Placa";
        String cliente = (this.veiculo != null && this.veiculo.getDono() != null)
                ? this.veiculo.getDono().getNome() : "Sem Cliente";

        // Retorna um formato limpo e profissional para o Sr. Zezé identificar na interface
        return "Orçamento Nº " + this.id + " [" + placa + " - " + cliente + "]";
    }

    // ── GETTERS E SETTERS Padrão ──────────────────────────────────────
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
    public void setPecas(List<Peca> pecas) { this.pecas = pecas; }
    public void setServicos(List<Servico> servicos) { this.servicos = servicos; }
    public void setData(LocalDate data) { this.data = data; }

    public Veiculo getVeiculo() { return veiculo; }
    public List<Peca> getPecas() { return pecas; }
    public List<Servico> getServicos() { return servicos; }
    public LocalDate getData() { return data; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // ── MÉTODOS COMPATIBILIDADE DAO ───────────────────────────────────
    public String getPlacaVeiculo() {
        return (this.veiculo != null) ? this.veiculo.getPlaca() : null;
    }

    public void setPlacaVeiculo(String placa) {
        if (this.veiculo == null) {
            this.veiculo = new Veiculo();
        }
        this.veiculo.setPlaca(placa);
    }

    public java.util.List<Servico> getListaServicos() {
        return this.servicos;
    }

    public void setListaServicos(List<Servico> listaServicos) {
        this.servicos = listaServicos;
    }

    public LocalDate getDataOrcamento() { return this.data; }
    public void setDataOrcamento(LocalDate dataOrcamento) { this.data = dataOrcamento; }
    public List<Peca> getListaPecas() { return this.pecas; }
    public void setListaPecas(List<Peca> listaPecas) { this.pecas = listaPecas; }
}