package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.User;
import com.nozama.aluguel_veiculos.dto.UserRequest;
import com.nozama.aluguel_veiculos.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user") //endpoint user
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping //endpoint para criar usúario
    // busca dados do userRequest
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
        try {
            // chama o serviço q cria o usúario
            User user = userService.create(userRequest);
            return ResponseEntity.status(201).body(user); // retorna 201 com o user no body
        } catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage()); // se der erro
        }
    }

    @GetMapping //endpoint para buscar usúario
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers(); // cria lista de user puxando todos usúarios do banco
        return ResponseEntity.status(200).body(users);
    }
}