package org.example;
import br.edu.ufersa.sedan.model.entities.*;
import br.edu.ufersa.sedan.model.services.*;

public class Main {
    public static void main(String []args){

            ClienteService service = new ClienteService();

            Endereco endereco = new Endereco();
            endereco.setRua("Rua Mamona");
            endereco.setBairro("Centro");
            endereco.setNum(123);

            Cliente cliente = new Cliente();
            cliente.setNome("Acsa");
            cliente.setCpf("12345678901");
            cliente.setEndereco(endereco);

            // TESTE 1: inserir
            service.cadastrarCliente(cliente);

            // TESTE 2: buscar
            Cliente c = service.buscarPorCpf("123.456.789-01");
            System.out.println(c.getNome());

            // TESTE 3: listar
            System.out.println(service.listarClientes());

            // TESTE 4: alterar
            service.alterarNome("12345678901", "Acsa Nova");

    }
}
