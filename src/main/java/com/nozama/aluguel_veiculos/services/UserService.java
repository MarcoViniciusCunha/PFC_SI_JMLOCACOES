package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.User;
import com.nozama.aluguel_veiculos.dto.UserRequest;
import com.nozama.aluguel_veiculos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(UserRequest userRequest) {
        if (userRepository.count() > 0){
            throw new RuntimeException("Já existe um usúario cadastrado. Não é permitido criar outro.");
        }

        User user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        return  userRepository.save(user);
    }

    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) user.setPassword(null);
        return user;
    }
}
