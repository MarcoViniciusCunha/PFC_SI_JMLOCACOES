package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Category;
import com.nozama.aluguel_veiculos.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> { }