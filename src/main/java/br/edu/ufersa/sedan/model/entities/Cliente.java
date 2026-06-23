package br.edu.ufersa.sedan.model.entities;

import java.util.List;
import java.util.ArrayList;

public class Cliente {

    // Atributos
    private int id;
    private String nome;
    private String cpf;
    private Endereco endereco;
    private List<Veiculo> veiculos = new ArrayList<>(); // No plural, já que um cliente pode ter vários veículos

    public Cliente() { //Construtor vazio para ser usado dentro do DAO
    }

    // Construtor
    public Cliente(int id, String nome, String cpf, Endereco endereco, List<Veiculo> veiculos) {
        setId(id);
        setNome(nome);
        setCpf(cpf);
        setEndereco(endereco);
        setVeiculos(veiculos);
    }

    // Set
    public void setNome(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome;
        }
    }

    public void setCpf(String cpf) {
        if (cpf != null && cpf.length() == 11) {
            this.cpf = cpf;
        }
    }

    public void setEndereco(Endereco endereco) {
        if (endereco != null) {
            this.endereco = endereco;
        }
    }

    public void setVeiculos(List<Veiculo> veiculos) { // Cliente seta veículos pois cada cliente pode ter vários veículos, assim não se torna necessário cadastrar um cliente novamente para cadastrar veículos
        if (veiculos != null) {
            this.veiculos = new ArrayList<>();
            for (Veiculo v : veiculos) {
                if (v != null) {
                    this.veiculos.add(v);
                    v.setDono(this);
                }
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void addVeiculo(Veiculo v) {
        if (v != null && !veiculos.contains(v)) {
            veiculos.add(v);
            v.setDono(this);
        }
    }

    public void removeVeiculo(Veiculo v) {
        if (v != null && veiculos.remove(v)) {
            v.setDono(null);
        }
    }

    // get
    public String getNome() {
        return this.nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public List<Veiculo> getVeiculos() {
        return new ArrayList<>(veiculos);
    }
}
