package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.domain.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByRentalId(Long rentalId);

    @Query("""
    SELECT p FROM Payment p
    WHERE (:data IS NULL OR p.data_pagamento = :data)
      AND (:formaPagto IS NULL OR p.formaPagto = :formaPagto)
      AND (:status IS NULL OR p.status = :status)
      AND (:customerId IS NULL OR p.rental.customer.id = :customerId)
      AND (:placaVeiculo IS NULL OR p.rental.vehicle.placa LIKE %:placaVeiculo%)
""")
    Page<Payment> findByFilters(
            @Param("data") LocalDate data,
            @Param("formaPagto") String formaPagto,
            @Param("status") PaymentStatus status,
            @Param("customerId") Long customerId,
            @Param("placaVeiculo") String placa,
            Pageable pageable
    );

    @Query("""
    SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END
    FROM Payment p
    WHERE p.rental.customer.id = :customerId
    AND p.status = com.nozama.aluguel_veiculos.domain.enums.PaymentStatus.PENDENTE
""")
    boolean existsPendentesByCustomerId(@Param("customerId") Long customerId);



}