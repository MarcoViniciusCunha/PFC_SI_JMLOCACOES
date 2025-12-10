package com.nozama.aluguel_veiculos.dto;

import com.nozama.aluguel_veiculos.domain.Toll;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TollResponse(
        Long id,
        String rodovia,
        String cidade,
        BigDecimal valor,
        LocalDateTime date
) {
    public static TollResponse fromEntitySummary(Toll toll) {
        return new TollResponse(
                toll.getId(),
                toll.getRodovia(),
                toll.getCidade(),
                toll.getValor(),
                toll.getDate()
        );
    }
}
