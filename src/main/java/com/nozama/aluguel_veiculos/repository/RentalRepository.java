package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface RentalRepository extends JpaRepository<Rental, Long> {
    boolean existsByCustomerId(Long customerId);

    @Query("""
    SELECT r FROM Rental r
    WHERE (:customerId IS NULL OR r.customer.id = :customerId)
    AND (:placa IS NULL OR r.vehicle.placa = :placa)
    AND (:status IS NULL OR r.status = :status)
""")
    Page<Rental> findByFilters(
            @Param("customerId") Long customerId,
            @Param("placa") String placa,
            @Param("status") RentalStatus status,
            Pageable pageable
    );

    @Query("""
    SELECT r FROM Rental r
    WHERE r.status = :status
""")
    List<Rental> findAllByStatus(@Param("status") RentalStatus status);

    @Query("""
            SELECT r FROM Rental r
            WHERE r.vehicle.placa = :placa
            AND :data_multa BETWEEN r.startDate AND r.endDate
            """)
    Optional<Rental> findRentalByVehiclePlateAndDate(
            @Param("placa") String placa,
            @Param("data_multa")LocalDate data_multa
            );


}
