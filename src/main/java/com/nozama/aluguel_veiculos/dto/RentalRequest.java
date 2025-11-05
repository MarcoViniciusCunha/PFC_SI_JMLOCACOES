package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RentalRequest(
        @NotBlank(message = "Informe a placa do veículo.")
        String placa,

        @NotNull(message = "Insira o cpf do cliente")
        String cpf,

        @NotNull(message = "Insira a data inicial")
        LocalDate startDate,

        @NotNull(message = "Insira a data final")
        LocalDate endDate,

        @NotNull(message = "Insira o valor")
        Double price

) {
    public record update(
            String placa,

            String cpf,

            LocalDate startDate,

            LocalDate endDate,

            Double price
    ) {
    }
}
