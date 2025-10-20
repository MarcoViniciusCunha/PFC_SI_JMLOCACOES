package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
