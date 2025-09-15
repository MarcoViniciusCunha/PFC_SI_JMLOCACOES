package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record MarkRequest(
        @NotBlank(message = "Insira o nome da marca.")
        String nome
) {
}
