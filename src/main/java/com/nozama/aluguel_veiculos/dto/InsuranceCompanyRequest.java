package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record InsuranceCompanyRequest(
        @NotBlank(message = "O nome da seguradora é obrigatório.")
        String name,

        String contact
) {
}
