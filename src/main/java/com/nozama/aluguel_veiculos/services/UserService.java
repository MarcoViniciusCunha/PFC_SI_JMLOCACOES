package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.User;
import com.nozama.aluguel_veiculos.dto.UserRequest;
import com.nozama.aluguel_veiculos.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // indica ser um service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // criar novo user no sistema
    // recebe dados via UserRequest
    public User create(UserRequest userRequest) {
        if (userRepository.count() > 0){
            throw new RuntimeException("Já existe um usúario cadastrado. Não é permitido criar outro.");
        }

        User user = new User();
        user.setUsername(userRequest.username());
        //  codifica a senha para salva-la
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        // retorna user criado
        return  userRepository.save(user);
    }

    // retorna lista de todos usúarios cadastrados
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // busca user pelo username
    public User getUserByEmail(String username) {
        return userRepository.findByUsername(username);
    }
}
