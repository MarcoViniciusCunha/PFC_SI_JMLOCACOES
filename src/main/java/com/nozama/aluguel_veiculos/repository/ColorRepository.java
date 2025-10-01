package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Integer> {
    boolean existsByNome(String nome);
}
