package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,String>, JpaSpecificationExecutor<Vehicle> {
    List<Vehicle> findByStatus(VehicleStatus status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Vehicle v SET v.status = :status WHERE v.placa = :placa")
    void updateStatusByPlaca(@Param("placa") String placa, @Param("status") VehicleStatus status);

}
