package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Fine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface FineRepository extends JpaRepository<Fine, Long> {
    @Query("""
    SELECT f FROM Fine f
    WHERE (:placa IS NULL OR f.rental.vehicle.placa LIKE %:placa%)
      AND (:cpf IS NULL OR f.rental.customer.cpf LIKE %:cpf%)
      AND (:dataInicial IS NULL OR f.data_multa >= :dataInicial)
      AND (:dataFinal IS NULL OR f.data_multa <= :dataFinal)
""")
    Page<Fine> search(
            String placa,
            String cpf,
            LocalDate dataInicial,
            LocalDate dataFinal,
            Pageable pageable
    );


}