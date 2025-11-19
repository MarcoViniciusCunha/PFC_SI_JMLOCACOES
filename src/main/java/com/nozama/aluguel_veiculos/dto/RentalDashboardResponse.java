package com.nozama.aluguel_veiculos.dto;

import java.math.BigDecimal;

public record RentalDashboardResponse(
        BigDecimal faturamentoMes,
        long reservasAndamento,
        long pendencias,
        RentalResponse proximaDevolucao,
        RentalResponse proximaLocacao
) {
}
