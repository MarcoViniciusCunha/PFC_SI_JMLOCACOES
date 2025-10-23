package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FineRequest(
        @NotBlank(message = "Informe a placa do veículo.")
        String placaVeiculo,

        @NotNull(message = "Informe o valor da multa.")
        @Positive(message = "O valor da multa deve ser positivo.")
        Double valor,

        @NotNull(message = "Informe o ID da locação.")
        Long locationId,

        @NotNull(message = "Informe os dias de atraso.")
        @Positive(message = "Os dias de atraso devem ser positivos.")
        Integer diasAtraso
) {}