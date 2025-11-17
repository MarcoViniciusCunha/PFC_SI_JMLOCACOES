package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequest(
        @NotNull(message = "Insira o id da locação")
        Long rentalId,

        @NotNull(message = "Insira a data do pagamento")
        LocalDate dataPagamento,

        @NotBlank(message = "Insira a forma de pagamento")
        String formaPagto,

        @NotBlank(message = "Insira o status")
        String status,

        Integer parcelas,

        String descricao,

        @Positive(message = "O juros precisa ser positivo")
        BigDecimal juros

) {
    public record update(
            Long rentalId,
            LocalDate dataPagamento,
            @Positive(message = "O valor precisa ser positivo")
            BigDecimal valor,
            String formaPagto,
            String status,
            Integer parcelas,
            String descricao,
            BigDecimal juros
    ){

    }
}
