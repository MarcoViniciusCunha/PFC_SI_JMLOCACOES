package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Integer> {
}
