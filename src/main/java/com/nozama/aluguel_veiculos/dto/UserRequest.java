package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Nome de usuário è obrigatório")
        String username,
        @NotBlank(message = "Senha è obrigatória")
        String password
) {
}
