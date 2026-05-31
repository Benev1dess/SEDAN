package br.edu.ufersa.sedan.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Endereco {
    private int idEndereco;
    private String rua;
    private String bairro;
    private int num;

    public Endereco(int idEndereco, String rua, String bairro, int num){
        setIdEndereco(idEndereco);
        setRua(rua);
        setBairro(bairro);
        setNum(num);
    }

    public Endereco(){ //Construtor vazio para usar no DAO

    }
    public void setIdEndereco(int id){
        this.idEndereco = id;
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
    public int getIdEndereco(){return this.idEndereco;}


}
