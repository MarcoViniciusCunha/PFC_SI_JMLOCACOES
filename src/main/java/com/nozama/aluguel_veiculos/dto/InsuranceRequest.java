package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InsuranceRequest(
        @NotNull(message = "Informe o ID da seguradora")
        Integer companyId,

        @NotNull(message = "Informe o valor do seguro")
        BigDecimal valor,

        @NotNull(message = "Informe a validade do seguro")
        LocalDate validade
) {}
