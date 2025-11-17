package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByRentalId(Long rentalId);
}