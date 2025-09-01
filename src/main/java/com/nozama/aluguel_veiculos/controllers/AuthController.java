package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.config.Jwt;
import com.nozama.aluguel_veiculos.domain.user.User;
import com.nozama.aluguel_veiculos.dto.LoginResponse;
import com.nozama.aluguel_veiculos.dto.UserRequest;
import com.nozama.aluguel_veiculos.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login") // endpoints /login
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Jwt jwt;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, Jwt jwt){
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwt = jwt;
    }

    @PostMapping //Post para autenticação
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest loginRequest) {
        // busca no bd pelo email informado
        User user = userRepository.findByEmail(loginRequest.email());

        // se user n existe ou senha n é igual retorna erro 401 e mensagem
        if(user == null || !passwordEncoder.matches(loginRequest.password(), user.getPassword())){
            return ResponseEntity.status(401).body("Email ou senha inválidos.");
        }

        // gera token com base no email
        String token = jwt.generateToken(user.getEmail());
        // retorna o token com resposta 200 0k
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
