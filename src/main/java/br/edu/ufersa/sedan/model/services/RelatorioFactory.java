package br.edu.ufersa.sedan.model.services;

public class RelatorioFactory {

    // O Factory Method clássico
    public static Relatorio obterRelatorio(String tipoRelatorio) {
        if (tipoRelatorio == null) {
            return null;
        }

        // Avalia o tipo solicitado. Fácil de expandir adicionando novos 'case' no futuro
        switch (tipoRelatorio.toUpperCase()) {
            case "FATURAMENTO":
                return new RelatorioFaturamentoService();
            // case "ESTOQUE":
            //     return new RelatorioEstoqueService(); <-- Exemplo de expansão futura
            default:
                throw new IllegalArgumentException("Tipo de relatório desconhecido: " + tipoRelatorio);
        }
    }
}