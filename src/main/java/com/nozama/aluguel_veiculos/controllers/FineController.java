package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.dto.FineRequest;
import com.nozama.aluguel_veiculos.repository.FineRepository;
import com.nozama.aluguel_veiculos.services.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService service;
    private final FineRepository repository;

    @PostMapping
    public ResponseEntity<Fine> create(@RequestBody @Valid FineRequest request) {
        Fine saved = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Fine>> getAll() {
        List<Fine> fine = repository.findAll();
        return ResponseEntity.ok(fine);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}