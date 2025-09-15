package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record ColorRequest(
        @NotBlank(message = "Insira o nome da cor.")
        String nome
) {
}
