package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FineRequest(
        @NotBlank(message = "Informe a placa do veículo")
        String placa,

        @NotBlank(message = "Informe a descrição da multa")
        String descricao,

        @NotNull(message = "Informe o valor da multa.")
        @Positive(message = "O valor da multa deve ser positivo.")
        BigDecimal valor,

        @NotNull(message = "Informe o dia da multa.")
        LocalDate dataMulta
) {
    public record update(
            String placa,
            String descricao,
            @Positive(message = "O valor da multa deve ser positivo.")
            BigDecimal valor,
            LocalDate dataMulta
    ){}
}