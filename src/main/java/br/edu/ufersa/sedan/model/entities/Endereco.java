package br.edu.ufersa.sedan.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Endereco {
    private String rua;
    private String bairro;
    private int num;

    public Endereco(String rua, String bairro, int num){
        setRua(rua);
        setBairro(bairro);
        setNum(num);
    }

    public void setRua(String rua){
        if (rua != null && !rua.trim().isEmpty()) {
            this.rua = rua;
        }
    }
    public void setBairro(String bairro) {
        if (bairro != null && !bairro.trim().isEmpty()){
            this.bairro = bairro;
        }
    }
    public void setNum(int num){
        if (num >= 0) {
            this.num = num;
        }
    }
    public String getRua(){
        return this.rua;
    }
    public String getBairro(){
        return this.bairro;
    }
    public int getNum(){
        return this.num;
    }

    public static class Orcamento {

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
}
