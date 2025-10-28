package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Long> { }