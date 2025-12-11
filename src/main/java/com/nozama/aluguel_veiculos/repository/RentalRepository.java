package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface RentalRepository extends JpaRepository<Rental, Long> {
    boolean existsByCustomerId(Long customerId);

    @Query("""
    SELECT COUNT(r) > 0 FROM Rental r
    WHERE r.vehicle = :vehicle
      AND r.endDate >= :start
      AND r.startDate <= :end
      AND r.returned = false
""")
    boolean existsActiveConflict(@Param("vehicle") Vehicle vehicle,
                                 @Param("start") LocalDate start,
                                 @Param("end") LocalDate end);

    @Query("""
    SELECT COUNT(r) > 0 FROM Rental r
    WHERE r.vehicle = :vehicle
    AND r.id <> :id
    AND r.endDate >= :start
    AND r.startDate <= :end
""")
    boolean existsConflictExcludingSelf(
            Vehicle vehicle,
            Long id,
            LocalDate start,
            LocalDate end
    );

    @Query("""
    SELECT r FROM Rental r
    WHERE (:customerId IS NULL OR r.customer.id = :customerId)
      AND (:placa IS NULL OR r.vehicle.placa LIKE %:placa%)
      AND (:status IS NULL OR r.status = :status)
      AND (:startDate IS NULL OR r.endDate >= :startDate)
      AND (:endDate IS NULL OR r.endDate <= :endDate)
""")
    Page<Rental> findByFilters(
            @Param("customerId") Long customerId,
            @Param("placa") String placa,
            @Param("status") RentalStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);


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

    @Query("""
        SELECT r FROM Rental r
        WHERE r.vehicle.placa = :placa
        AND DATE(:momento) BETWEEN r.startDate AND COALESCE(r.returnDate, r.endDate)
        """)
    Optional<Rental> findRentalByVehiclePlateAndDateTime(
            @Param("placa") String placa,
            @Param("momento") LocalDateTime momento
    );

    @Query("""
        SELECT COUNT(r) > 0 FROM Rental r
        WHERE r.customer.id = :customerId
        AND r.status IN (:status)
        """)
    boolean existsByCustomerIdAndStatusIn(
            @Param("customerId") Long customerId,
            @Param("status") List<RentalStatus> status
    );

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
        FROM Rental r
        LEFT JOIN r.payments p
        WHERE r.customer.id = :customerId
        AND r.status = com.nozama.aluguel_veiculos.domain.enums.RentalStatus.DEVOLVIDA
        AND (p.id IS NULL OR p.status = com.nozama.aluguel_veiculos.domain.enums.PaymentStatus.PENDENTE)
       """)
    boolean existsDevolvidasNaoPagas(@Param("customerId") Long customerId);

}
