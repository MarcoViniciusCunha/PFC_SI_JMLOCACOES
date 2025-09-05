package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,String> {
    List<Vehicle> findByCategoryId(Integer categoryId);
    List<Vehicle> findByInsuranceId(Integer InsuranceId);
}
