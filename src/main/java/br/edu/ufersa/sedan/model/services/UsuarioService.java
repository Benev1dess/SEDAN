package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.DAO.UsuarioDAO;
import br.edu.ufersa.sedan.model.entities.Usuario;
import br.edu.ufersa.sedan.model.entities.TipoUsuario;
import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public UsuarioService() {

    }

    public void registrarUsuario(Usuario quemEstaCadastrando, Usuario novoUsuario) {
        verificarSeEAdmin(quemEstaCadastrando, "cadastrar novos usuários");


        if (buscarPorLogin(novoUsuario.getLogin()) != null) {
            throw new IllegalArgumentException("Erro: Já existe um usuário com o login '" + novoUsuario.getLogin() + "'.");
        }


        List<Usuario> todosUsuarios = usuarioDAO.listar();

        if (novoUsuario.getTipo() == TipoUsuario.ADM) {
            for (Usuario u : todosUsuarios) {
                if (u.getEmail() != null && u.getEmail().equalsIgnoreCase(novoUsuario.getEmail())) {
                    throw new IllegalArgumentException("Erro: Já existe um ADM com o e-mail '" + novoUsuario.getEmail() + "'.");
                }
            }
        } else {
            for (Usuario u : todosUsuarios) {
                if (u.getCpf() != null && u.getCpf().equals(novoUsuario.getCpf())) {
                    throw new IllegalArgumentException("Erro: Já existe um funcionário com o CPF '" + novoUsuario.getCpf() + "'.");
                }
            }
        }


        usuarioDAO.inserir(novoUsuario);
        System.out.println("Sucesso: " + novoUsuario.getTipo() + " cadastrado com sucesso no banco de dados!");
    }

    public Usuario buscarPorId(int id) {

        for (Usuario u : usuarioDAO.listar()) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public Usuario buscarPorLogin(String login) {

        for (Usuario u : usuarioDAO.listar()) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() {

        return usuarioDAO.listar();
    }

    public void atualizarUsuario(Usuario quemEstaAlterando, int idAlvo, Usuario dadosNovos) {

        verificarSeEAdmin(quemEstaAlterando, "atualizar usuários");


        Usuario usuarioExistente = buscarPorId(idAlvo);
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Erro: Usuário com ID " + idAlvo + " não encontrado.");
        }


        Usuario loginOcupado = buscarPorLogin(dadosNovos.getLogin());
        if (loginOcupado != null && loginOcupado.getId() != idAlvo) {
            throw new IllegalArgumentException("Erro: O login '" + dadosNovos.getLogin() + "' já está em uso.");
        }

        usuarioExistente.setNome(dadosNovos.getNome());
        usuarioExistente.setLogin(dadosNovos.getLogin());
        usuarioExistente.setSenha(dadosNovos.getSenha());
        usuarioExistente.setTipo(dadosNovos.getTipo());

        if (dadosNovos.getTipo() == TipoUsuario.FUNCIONARIO) {
            usuarioExistente.setCpf(dadosNovos.getCpf());
            usuarioExistente.setSalario(dadosNovos.getSalario());
            usuarioExistente.setEmail(null);
        } else {
            usuarioExistente.setEmail(dadosNovos.getEmail());
            usuarioExistente.setCpf(null);
            usuarioExistente.setSalario(0.0);
        }

        usuarioDAO.alterar(usuarioExistente);
        System.out.println("Sucesso: Usuário de ID " + idAlvo + " atualizado no banco por " + quemEstaAlterando.getNome());
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


        usuarioDAO.deletar(usuarioExistente);
        System.out.println("Sucesso: Usuário de ID " + idAlvo + " removido do banco por " + quemEstaExcluindo.getNome());
    }

    public Usuario fazerLogin(String login, String senha) {

        Usuario usuario = buscarPorLogin(login);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            System.out.println("\n[LOGIN] " + usuario.getNome() + " logado com sucesso via MySQL!");
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