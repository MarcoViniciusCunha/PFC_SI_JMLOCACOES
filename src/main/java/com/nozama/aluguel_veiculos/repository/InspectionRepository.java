package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {}