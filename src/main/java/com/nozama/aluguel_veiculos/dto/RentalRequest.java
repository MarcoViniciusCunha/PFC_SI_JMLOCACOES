package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RentalRequest(
        @NotBlank(message = "Informe a placa do veículo.")
        String placa,
        @NotNull(message = "Informe o ID do cliente.")
        Long customerId,
        @NotNull(message = "Insira a data inicial")
        LocalDate startDate,
        @NotNull(message = "Insira a data final")
        LocalDate endDate
) {
    public record update(
            String placa,
            Long customerId,
            LocalDate startDate,
            LocalDate endDate
    ) {
    }
}
