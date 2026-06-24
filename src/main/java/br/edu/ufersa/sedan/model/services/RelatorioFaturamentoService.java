package br.edu.ufersa.sedan.model.services;

import br.edu.ufersa.sedan.model.entities.OrdemServico;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioFaturamentoService extends RelatorioService implements Relatorio {

    @Override
    public RelatorioFaturamento gerar(LocalDate dataInicio, LocalDate dataFim) {
        // Chamando diretamente o método herdado da classe mãe (Superclasse)
        List<OrdemServico> todasFaturadas = super.obterOrdensFaturadas();

        // Aplica o filtro de período exigido pelo requisito do professor
        List<OrdemServico> ordensFiltradas = todasFaturadas.stream()
                .filter(os -> os.getData() != null &&
                        !os.getData().isBefore(dataInicio) &&
                        !os.getData().isAfter(dataFim))
                .collect(Collectors.toList());

        // Reutiliza as regras de negócio matemáticas herdadas de RelatorioService
        double totalFaturado = super.calcularFaturamentoTotal(ordensFiltradas);
        double totalPecas    = super.calcularTotalPecas(ordensFiltradas);
        double totalServicos = super.calcularTotalServicos(ordensFiltradas);
        long clientesUnicos  = super.contarClientesUnicos(ordensFiltradas);

        // Retorna o DTO com os resultados consolidados
        return new RelatorioFaturamento(
                totalFaturado,
                totalPecas,
                totalServicos,
                clientesUnicos,
                ordensFiltradas
        );
    }
}