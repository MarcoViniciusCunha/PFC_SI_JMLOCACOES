package com.nozama.aluguel_veiculos.dto;

import java.math.BigDecimal;
import java.util.List;

public record RentalDashboardResponse(
        BigDecimal faturamentoMes,
        long reservasAndamento,
        long pendencias,
        List<RentalResponse> proximaDevolucao,
        List<RentalResponse> proximaLocacao
) {
}
