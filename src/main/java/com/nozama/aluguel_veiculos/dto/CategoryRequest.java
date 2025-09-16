package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "De um nome para a categoria")
        String nome,

        String descricao
) {
}
