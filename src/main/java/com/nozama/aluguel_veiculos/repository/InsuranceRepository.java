package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}
