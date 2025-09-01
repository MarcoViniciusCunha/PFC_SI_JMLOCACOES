package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Email è obrigatório") String email,

        @NotBlank(message = "Senha è obrigatória") String password
) {
}
