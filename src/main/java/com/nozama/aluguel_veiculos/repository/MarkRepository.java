package com.nozama.aluguel_veiculos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nozama.aluguel_veiculos.domain.Mark;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
}
