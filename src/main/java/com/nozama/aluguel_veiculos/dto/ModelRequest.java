package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record ModelRequest(
        @NotBlank(message = "Insira o modelo do veículo.")
        String nome
) {
}
