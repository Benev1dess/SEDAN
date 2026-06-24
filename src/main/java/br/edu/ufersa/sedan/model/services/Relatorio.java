package br.edu.ufersa.sedan.model.services;

import java.time.LocalDate;

public interface Relatorio {
    // ATUALIZADO: Agora a interface aceita os argumentos de período exigidos
    RelatorioFaturamento gerar(LocalDate dataInicio, LocalDate dataFim);
}