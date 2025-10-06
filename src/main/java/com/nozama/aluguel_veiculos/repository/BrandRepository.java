package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nozama.aluguel_veiculos.domain.Brand;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByNome(String nome);
}
