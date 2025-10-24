package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("""
            SELECT r FROM Rental r
            WHERE (:cpf is NULL or r.customer.cpf = :cpf)
            AND (:placa is null or r.vehicle.placa = :placa)
            AND (:returned is NULL or r.returned = :returned)
            """)
    Page<Rental> findFiltered(
            @Param("cpf") String cpf,
            @Param("placa") String placa,
            @Param("returned") Boolean returned,
            Pageable pageable
    );
}
