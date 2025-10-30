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
    public record RentalInfo(
            Long id,
            String placa,
            String customerNome
    ) {}
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
                    payment.getStatus(),
                    payment.getParcelas()
            );
    }
}
