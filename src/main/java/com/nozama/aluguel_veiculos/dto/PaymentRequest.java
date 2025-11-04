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

        @Positive(message = "O valor precisa ser positivo")
        @NotNull(message = "Insira o valor do pagamento")
        BigDecimal valor,

        @NotBlank(message = "Insira a forma de pagamento")
        String formaPagto,

        @NotBlank(message = "Insira o status")
        String status,

        Integer parcelas,

        String descricao
) {
    public record update(
            Long rentalId,
            LocalDate dataPagamento,
            @Positive(message = "O valor precisa ser positivo")
            BigDecimal valor,
            String formaPagto,
            String status,
            Integer parcelas,
            String descricao
    ){

    }
}
