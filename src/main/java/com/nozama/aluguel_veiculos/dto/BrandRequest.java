package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandRequest(
        @NotBlank(message = "Insira o nome da marca.")
        String nome
) {
}
