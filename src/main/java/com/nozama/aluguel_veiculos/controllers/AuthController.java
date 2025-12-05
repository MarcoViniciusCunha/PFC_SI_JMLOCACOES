package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.security.Jwt;
import com.nozama.aluguel_veiculos.domain.User;
import com.nozama.aluguel_veiculos.dto.LoginResponse;
import com.nozama.aluguel_veiculos.dto.UserRequest;
import com.nozama.aluguel_veiculos.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Jwt jwt;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username());

        if (user == null || !passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nome de usuário ou senha inválidos.");
        }

        String token = jwt.generateToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
