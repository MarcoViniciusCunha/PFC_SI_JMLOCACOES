package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByNomeContainingIgnoreCase(String nome);
    Optional<Customer> findByCpf(String cpf);
}
