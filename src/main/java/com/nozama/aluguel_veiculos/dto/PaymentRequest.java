package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.*;

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
        @Pattern(regexp = "PAGO|PENDENTE|CANCELADO", message = "Status inválido")
        String status,

        Integer parcelas,

        String descricao,

        @PositiveOrZero(message = "O juros não pode ser negativo")
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
