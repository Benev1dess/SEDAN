package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.entities.Usuario;
import br.edu.ufersa.sedan.model.entities.TipoUsuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private List<Usuario> bancoUsuarios = new ArrayList<>();
    private int proximoId = 1; // Gerador de ID automático

    public UsuarioService() {
        // Cadastra o primeiro ADM padrão para você conseguir iniciar o sistema
        // ID: 1, Nome, Login, Senha, Email, CPF (null), Salário (0.0), Tipo
        bancoUsuarios.add(new Usuario(proximoId++, "Admin Geral", "admin", "1234", "admin@sedan.com", null, 0.0, "adm"));
    }


    public void registrarUsuario(Usuario quemEstaCadastrando, Usuario novoUsuario) {

        verificarSeEAdmin(quemEstaCadastrando, "cadastrar novos usuários");

        if (buscarPorLogin(novoUsuario.getLogin()) != null) {
            throw new IllegalArgumentException("Erro: Já existe um usuário com o login '" + novoUsuario.getLogin() + "'.");
        }

        if (novoUsuario.getTipo() == TipoUsuario.ADM) {
            if (buscarPorEmail(novoUsuario.getEmail()) != null) {
                throw new IllegalArgumentException("Erro: Já existe um ADM com o e-mail '" + novoUsuario.getEmail() + "'.");
            }
        } else {
            if (buscarPorCpf(novoUsuario.getCpf()) != null) {
                throw new IllegalArgumentException("Erro: Já existe um funcionário com o CPF '" + novoUsuario.getCpf() + "'.");
            }
        }

        novoUsuario.setId(proximoId++);
        bancoUsuarios.add(novoUsuario);
        System.out.println("Sucesso: " + novoUsuario.getTipo() + " cadastrado com ID " + novoUsuario.getId());
    }

    public Usuario buscarPorId(int id) {
        for (Usuario u : bancoUsuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }


    public Usuario buscarPorLogin(String login) {
        for (Usuario u : bancoUsuarios) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                return u;
            }
        }
        return null;
    }

    private Usuario buscarPorCpf(String cpf) {
        for (Usuario u : bancoUsuarios) {
            if (u.getCpf() != null && u.getCpf().equals(cpf)) return u;
        }
        return null;
    }

    private Usuario buscarPorEmail(String email) {
        for (Usuario u : bancoUsuarios) {
            if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        return this.bancoUsuarios;
    }


    public void atualizarUsuario(Usuario quemEstaAlterando, int idAlvo, Usuario dadosNovos) {
        verificarSeEAdmin(quemEstaAlterando, "atualizar usuários");

        Usuario usuarioExistente = buscarPorId(idAlvo);
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Erro: Usuário com ID " + idAlvo + " não encontrado.");
        }

        // Se mudou o login, verifica se o novo já não está em uso por outro ID
        Usuario loginOcupado = buscarPorLogin(dadosNovos.getLogin());
        if (loginOcupado != null && loginOcupado.getId() != idAlvo) {
            throw new IllegalArgumentException("Erro: O login '" + dadosNovos.getLogin() + "' já está em uso.");
        }

        // Atualiza as informações básicas
        usuarioExistente.setNome(dadosNovos.getNome());
        usuarioExistente.setLogin(dadosNovos.getLogin());
        usuarioExistente.setSenha(dadosNovos.getSenha());
        usuarioExistente.setTipo(dadosNovos.getTipo());

        // Atualiza os campos específicos baseados no novo tipo
        if (dadosNovos.getTipo() == TipoUsuario.FUNCIONARIO) {
            usuarioExistente.setCpf(dadosNovos.getCpf());
            usuarioExistente.setSalario(dadosNovos.getSalario());
            usuarioExistente.setEmail(null); // Limpa campo de ADM
        } else {
            usuarioExistente.setEmail(dadosNovos.getEmail());
            usuarioExistente.setCpf(null);     // Limpa campos de funcionário
            usuarioExistente.setSalario(0.0);
        }

        System.out.println("Sucesso: Usuário de ID " + idAlvo + " atualizado por " + quemEstaAlterando.getNome());
    }


    public void excluirUsuario(Usuario quemEstaExcluindo, int idAlvo) {
        verificarSeEAdmin(quemEstaExcluindo, "excluir usuários");

        Usuario usuarioExistente = buscarPorId(idAlvo);
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Erro: Usuário com ID " + idAlvo + " não encontrado.");
        }

        if (quemEstaExcluindo.getId() == idAlvo) {
            throw new IllegalArgumentException("Erro: Operação inválida. Você não pode excluir a si mesmo.");
        }

        bancoUsuarios.remove(usuarioExistente);
        System.out.println("Sucesso: Usuário de ID " + idAlvo + " removido por " + quemEstaExcluindo.getNome());
    }


    public Usuario fazerLogin(String login, String senha) {
        Usuario usuario = buscarPorLogin(login);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            System.out.println("\n[LOGIN] " + usuario.getNome() + " logado com sucesso!");
            return usuario;
        }
        System.out.println("\n[LOGIN FALHOU] Usuário ou senha incorretos.");
        return null;
    }

    private void verificarSeEAdmin(Usuario usuario, String acao) {
        if (usuario == null || usuario.getTipo() != TipoUsuario.ADM) {
            throw new SecurityException("Acesso negado: Apenas administradores podem " + acao + ".");
        }
    }
}