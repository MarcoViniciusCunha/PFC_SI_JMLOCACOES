package com.nozama.aluguel_veiculos.dto;

import com.nozama.aluguel_veiculos.domain.Fine;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FineResponse(
        Long id,
        RentalInfo rental,
        String descricao,
        BigDecimal valor,
        LocalDate data_multa
) {
    public static FineResponse fromEntity(Fine fine) {
        RentalInfo rentalInfo = new RentalInfo(
                fine.getRental().getId(),
                fine.getRental().getVehicle().getPlaca(),
                fine.getRental().getCustomer().getNome()
        );

        return new FineResponse(
                fine.getId(),
                rentalInfo,
                fine.getDescricao(),
                fine.getValor(),
                fine.getData_multa()
        );
    }

    public static FineResponse fromEntitySummary(Fine fine) {
        RentalInfo rentalInfo = new RentalInfo(
                fine.getRental().getId(),
                fine.getRental().getVehicle().getPlaca(),
                fine.getRental().getCustomer().getNome()
        );

        return new FineResponse(
                fine.getId(),
                null,
                fine.getDescricao(),
                fine.getValor(),
                fine.getData_multa()
        );
    }
}
