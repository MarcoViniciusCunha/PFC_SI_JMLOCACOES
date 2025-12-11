package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.domain.enums.PaymentStatus;
import com.nozama.aluguel_veiculos.domain.enums.PaymentStatusConverter;
import com.nozama.aluguel_veiculos.dto.PaymentRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    private LocalDate data_pagamento;

    private BigDecimal valor;

    private String formaPagto;

    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus status;

    private Integer parcelas;

    private String descricao;

    public Payment(Rental rental, PaymentRequest request){
        this.rental = rental;
        this.data_pagamento = request.dataPagamento();
        this.valor = rental.getPrice();
        this.formaPagto = request.formaPagto();
        this.status = request.status() != null ? PaymentStatus.fromString(request.status()) : PaymentStatus.PENDENTE;
        this.parcelas = request.parcelas();
        this.descricao = request.descricao();
    }

}
