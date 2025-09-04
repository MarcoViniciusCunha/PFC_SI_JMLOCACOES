package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotNull(message = "O nome não pode ser nulo")
        @NotBlank(message = "De um nome para a categoria")
        String nome
) {
}
