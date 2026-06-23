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

        // Soma o preço das peças direto do atributo privado
        if (this.pecas != null) {
            for (Peca p : this.pecas) {
                if (p != null) {
                    total += p.getPreco();
                }
            }
        }

        // Soma o preço os serviços direto do atributo privado
        if (this.servicos != null) {
            for (Servico s : this.servicos) {
                if (s != null) {
                    total += s.getPreco();
                }
            }
        }

        return total;
    }

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
    // --- MÉTODOS AUXILIARES PARA ADAPTAR COM O DAO ---

    // Traduz o objeto Veiculo para a String da placa que o DAO precisa salvar
    public String getPlacaVeiculo() {
        return (this.veiculo != null) ? this.veiculo.getPlaca() : null;
        // Nota: Garanta que na sua classe Veiculo exista o método getPlaca()
    }

    // Atalho para o DAO preencher a placa vinda do banco diretamente na entidadeNOVO
    public void setPlacaVeiculo(String placa) {
        if (this.veiculo == null) {
            this.veiculo = new Veiculo(); // Cria uma instância vazia para injetar a placa
        }
        this.veiculo.setPlaca(placa); // Garanta que exista setPlaca na classe Veiculo
    }

    // Traduz o nome antigo 'getDataOrcamento' do DAO para o seu atributo 'data'
    public LocalDate getDataOrcamento() {
        return this.data;
    }

    // Atalho para o DAO injetar a data vinda do banco
    public void setDataOrcamento(LocalDate dataOrcamento) {
        this.data = dataOrcamento;
    }

    // Traduz o nome 'getListaPecas' do DAO para o seu atributo 'pecas'
    public List<Peca> getListaPecas() {
        return this.pecas;
    }

    // Atalho para o DAO injetar a lista de peças vinda do banco
    public void setListaPecas(List<Peca> listaPecas) {
        this.pecas = listaPecas;
    }
}