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

}
