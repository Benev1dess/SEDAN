package br.edu.ufersa.sedan.util;

import br.edu.ufersa.sedan.model.entities.Usuario;

public class Sessao {
    private static Sessao instance; // Única instância (Singleton)
    private Usuario usuarioLogado;

    private Sessao() {} // Construtor privado

    public static Sessao getInstance() {
        if (instance == null) {
            instance = new Sessao();
        }
        return instance;
    }

    public void setUsuario(Usuario u) { this.usuarioLogado = u; }
    public Usuario getUsuario() { return usuarioLogado; }
}
