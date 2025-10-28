package com.nozama.aluguel_veiculos.repository;


import com.nozama.aluguel_veiculos.domain.Return;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnRepository extends JpaRepository<Return, Long> { }