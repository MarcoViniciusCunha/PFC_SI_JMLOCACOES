package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> { }