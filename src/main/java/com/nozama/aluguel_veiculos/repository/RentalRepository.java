package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;


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

    @Query("""
            SELECT r FROM Rental r
            WHERE r.vehicle.placa = :placa
            AND :data_multa BETWEEN r.startDate AND r.endDate
            """)
    Optional<Rental> findRentalByVehiclePlateAndDate(
            @Param("placa") String placa,
            @Param("data_multa")LocalDate data_multa
            );

    @Query("""
    SELECT r FROM Rental r
    WHERE (:cpf IS NULL OR r.customer.cpf = :cpf)
      AND (:placa IS NULL OR r.vehicle.placa = :placa)
      AND r.returned = false
      AND r.endDate >= :hoje
""")
    Page<Rental> findAtivas(
            @Param("cpf") String cpf,
            @Param("placa") String placa,
            @Param("hoje") LocalDate hoje,
            Pageable pageable
    );

    @Query("""
    SELECT r FROM Rental r
    WHERE (:cpf IS NULL OR r.customer.cpf = :cpf)
      AND (:placa IS NULL OR r.vehicle.placa = :placa)
      AND r.returned = false
      AND r.endDate < :hoje
""")
    Page<Rental> findAtrasadas(
            @Param("cpf") String cpf,
            @Param("placa") String placa,
            @Param("hoje") LocalDate hoje,
            Pageable pageable
    );

}
