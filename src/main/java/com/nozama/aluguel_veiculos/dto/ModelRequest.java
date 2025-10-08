package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModelRequest(
        @NotBlank(message = "Insira o modelo do veículo.")
        String nome,

        @NotNull(message = "Insira a marca desse modelo.")
        Integer brandId
) {
}
