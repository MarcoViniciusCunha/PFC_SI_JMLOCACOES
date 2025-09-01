package com.nozama.aluguel_veiculos.repository;

import com.nozama.aluguel_veiculos.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

// acessa a tabela app_user
// extends de JpaRepository para usar os métodos pré prontos
public interface UserRepository extends JpaRepository<User, String> {
    // busca usúario pelo email
    User findByEmail(String email);
}
