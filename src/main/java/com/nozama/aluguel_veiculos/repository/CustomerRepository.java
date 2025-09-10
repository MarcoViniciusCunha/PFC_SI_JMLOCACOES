package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
