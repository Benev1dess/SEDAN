package org.example;
import br.edu.ufersa.sedan.model.entities.*;
import br.edu.ufersa.sedan.model.DAO.*; // Importando as DAOs para testar a persistência direta

import java.util.List;

public class Main {
        public static void main(String []args){

                // Instanciando as DAOs diretamente para testar o fluxo do banco
                ClienteDAO clienteDAO = new ClienteDAO();
                EnderecoDAO enderecoDAO = new EnderecoDAO();

                // 1. O Endereço precisa ser inserido primeiro de forma isolada
                Endereco endereco = new Endereco();
                endereco.setRua("Rua Mamona");
                endereco.setBairro("Centro");
                endereco.setNum(123);

                enderecoDAO.inserir(endereco); // Salva o endereço no banco!
                System.out.println("✅ Endereço salvo!");

                // 2. Criar e salvar o Cliente
                Cliente cliente = new Cliente();
                cliente.setNome("Acsa");
                cliente.setCpf("12345678901"); // CPF limpo
                cliente.setEndereco(endereco);

                clienteDAO.inserir(cliente); // Salva o cliente no banco!
                System.out.println("Cliente salvo!");

                // TESTE 2: Buscar com o CPF EXATAMENTE igual ao que foi inserido
                // Como o método na DAO busca por igualdade estrita, usamos sem pontos:
                List<Cliente> resultadoBusca = clienteDAO.buscarCpf("12345678901");

                if (!resultadoBusca.isEmpty()) {
                        Cliente c = resultadoBusca.get(0); // Pega o cliente encontrado
                        System.out.println("Cliente encontrado: " + c.getNome());

                        // TESTE 4: Alterar o nome usando o objeto que veio do banco (ele já tem o ID correto!)
                        c.setNome("Acsa Nova");
                        clienteDAO.alterar(c);
                        System.out.println(" Nome alterado com sucesso para 'Acsa Nova'!");
                } else {
                        System.out.println("Cliente não encontrado. Verifique o formato do CPF.");
                }

                // TESTE 3: Listar todos
                System.out.println("\n--- Lista de Clientes no Banco ---");
                List<Cliente> todos = clienteDAO.listar();
                for (Cliente cl : todos) {
                        System.out.println("ID: " + cl.getId() + " | Nome: " + cl.getNome() + " | CPF: " + cl.getCpf());
                }
        }
}