package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Integer> {
    Optional<InsuranceCompany> findByNameIgnoreCase(String name);
}
