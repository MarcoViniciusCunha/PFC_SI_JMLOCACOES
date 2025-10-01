package com.nozama.aluguel_veiculos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nozama.aluguel_veiculos.domain.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByNome(String nome);
}
