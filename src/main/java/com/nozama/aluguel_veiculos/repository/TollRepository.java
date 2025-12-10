package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Toll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TollRepository extends JpaRepository<Toll, Long> {
}
