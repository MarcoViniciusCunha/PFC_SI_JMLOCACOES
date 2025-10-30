package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequest(
        @NotNull(message = "Insira o id da locação")
        Long rentalId,

        @NotNull(message = "Insira a data do pagamento")
        LocalDate dataPagamento,

        @NotNull(message = "Insira o valor do pagamento")
        BigDecimal valor,

        @NotBlank(message = "Insira a forma de pagamento")
        String formaPagto,

        @NotBlank(message = "Insira o status")
        String status,

        Integer parcelas
) {
}
