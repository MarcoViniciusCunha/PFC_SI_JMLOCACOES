package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,String>, JpaSpecificationExecutor<Vehicle> {
}
