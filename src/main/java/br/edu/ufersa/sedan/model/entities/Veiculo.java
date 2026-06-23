package br.edu.ufersa.sedan.model.entities;

import java.util.List;
import java.util.ArrayList;

public class Veiculo {
    private int id;
    private String marca;
    private String cor;
    private String placa;
    private int ano;
    private int km;
    private Cliente dono;
    private List<Servico> servicos = new ArrayList<>();

    // Construtor
    public Veiculo(int id, String marca, String cor, String placa, int ano, int km, List<Servico> servicos){
        setId(id);
        setMarca(marca);
        setCor(cor);
        setPlaca(placa);
        setAno(ano);
        setKm(km);
        setServicos(servicos);
    }
    public Veiculo() { // pra usar no DAO
    }

    // Set e Get
    public void setMarca(String marca){
        if (marca != null && !marca.trim().isEmpty()) {
            this.marca = marca;
        }
    }
    public void setCor(String cor){
        if (cor != null && !cor.trim().isEmpty()) {
            this.cor = cor;
        }
    }
    public void setPlaca(String placa){
        if (placa != null && !placa.trim().isEmpty()) {
            this.placa = placa;
        }
    }
    public void setAno(int ano){
        if (ano >= 1886) { // primeiro carro da história
            this.ano = ano;
        }
    }
    public void setKm(int km){
        if (km >= 0) {
            this.km = km;
        }
    }
    public void setDono(Cliente dono) { //protected para não ser possível alterar o dono livremente
        this.dono = dono;
    }

    public void setServicos(List<Servico> servicos) {
        if (servicos == null) {
            this.servicos = new ArrayList<>();
        } else {
            this.servicos = new ArrayList<>(servicos);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }
    public String getCor() {
        return cor;
    }
    public String getPlaca() {
        return placa;
    }
    public int getAno() {
        return ano;
    }
    public int getKm() {
        return km;
    }
    public Cliente getDono() {
        return dono;
    }
    public List<Servico> getServicos() {
        return new ArrayList<>(servicos);
    }

    public int getId() {
        return id;
    }
}
