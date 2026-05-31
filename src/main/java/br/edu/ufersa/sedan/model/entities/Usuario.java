package br.edu.ufersa.sedan.model.entities;

public class Usuario {
    private int id;
    private String nome;
    private String login;
    private String senha;
    private String email;
    private String cpf;
    private double salario;
    private TipoUsuario tipo;

    /**
     * Construtor Vazio / Padrão
     * OBRIGATÓRIO para a camada DAO conseguir instanciar o objeto
     * e preencher os dados via seleções do banco de dados (ResultSet).
     */
    public Usuario() {
    }

    /**
     * Construtor completo utilizando ID como int.
     * Ideal para quando você já recebe o objeto montado das telas ou controladores.
     */
    public Usuario(int id, String nome, String login, String senha, String email, String cpf, double salario, String tipo) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.cpf = cpf;
        this.salario = salario;

        // Faz o mapeamento da String para o Enum de forma segura
        setTipoString(tipo);

        // Dispara validações de negócio baseadas no tipo de conta
        validarCamposPorTipo();
    }

    /**
     * Valida os dados obrigatórios dependendo da função do usuário no Sedan.
     */
    public void validarCamposPorTipo() {
        if (this.tipo == TipoUsuario.FUNCIONARIO) {
            if (this.cpf == null || this.cpf.isBlank()) {
                throw new IllegalArgumentException("Erro: Funcionários precisam obrigatoriamente de um CPF.");
            }
            if (this.salario < 0) {
                throw new IllegalArgumentException("Erro: O salário do funcionário não pode ser negativo.");
            }
        } else if (this.tipo == TipoUsuario.ADM) {
            if (this.email == null || this.email.isBlank()) {
                throw new IllegalArgumentException("Erro: Administradores precisam obrigatoriamente de um e-mail.");
            }
            // Isola o ADM de atributos de funcionários limpando os campos
            this.cpf = null;
            this.salario = 0.0;
        }
    }

    // ==================== GETTERS E SETTERS ====================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    /**
     * Permite atualizar o tipo do usuário enviando uma String de qualquer formato (Ex: "adm", "Funcionario")
     */
    public void setTipoString(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Erro: O tipo do usuário não pode ser nulo ou vazio.");
        }
        try {
            this.tipo = TipoUsuario.valueOf(tipo.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Erro: Tipo inválido! Use apenas 'ADM' ou 'FUNCIONARIO'.");
        }
    }

    @Override
    public String toString() {
        if (tipo == TipoUsuario.ADM) {
            return "[ID: " + id + "] Encarregado ADM: " + nome + " | Login: " + login + " | Email: " + email;
        } else {
            return "[ID: " + id + "] Funcionário Operacional: " + nome + " | Login: " + login + " | CPF: " + cpf + " | Salário: R$ " + salario;
        }
    }
}