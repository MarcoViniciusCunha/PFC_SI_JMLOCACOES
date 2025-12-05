package com.nozama.aluguel_veiculos.dto;

import com.nozama.aluguel_veiculos.domain.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponse(
        Long id,
        RentalInfo rental,
        LocalDate dataPagamento,
        BigDecimal valor,
        String formaPagto,
        String status,
        Integer parcelas
) {
        public static PaymentResponse fromEntity(Payment payment) {
            RentalInfo rentalInfo = new RentalInfo(
                    payment.getRental().getId(),
                    payment.getRental().getVehicle().getPlaca(),
                    payment.getRental().getCustomer().getNome()
            );

            return new PaymentResponse(
                    payment.getId(),
                    rentalInfo,
                    payment.getData_pagamento(),
                    payment.getValor(),
                    payment.getFormaPagto(),
                    payment.getStatus().name(),
                    payment.getParcelas()
            );
    }

    public static PaymentResponse fromEntitySummary(Payment payment) {
        RentalInfo rentalInfo = new RentalInfo(
                payment.getRental().getId(),
                payment.getRental().getVehicle().getPlaca(),
                payment.getRental().getCustomer().getNome()
        );

        return new PaymentResponse(
                payment.getId(),
                null,
                payment.getData_pagamento(),
                payment.getValor(),
                payment.getFormaPagto(),
                payment.getStatus().name(),
                payment.getParcelas()
        );
    }
}
