package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByNome(String nome);
}
