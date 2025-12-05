package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}
