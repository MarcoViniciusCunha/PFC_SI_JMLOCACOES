package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Color;
import com.nozama.aluguel_veiculos.dto.ColorRequest;
import com.nozama.aluguel_veiculos.repository.ColorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/color")
@RequiredArgsConstructor
public class ColorController {

    private final ColorRepository repository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid ColorRequest request) {
        if (repository.existsByNome(request.nome())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cor '" + request.nome() + "' já existe!");
        }
        return ResponseEntity.ok(repository.save(new Color(request)));
    }

    @GetMapping
    public ResponseEntity<List<Color>> getAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Color> getById(@PathVariable Integer id) {
        Optional<Color> color = repository.findById(id);
        if (color.isPresent()) {
            return ResponseEntity.ok(color.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Color> update(@PathVariable Integer id, @RequestBody Color color) {
        Optional<Color> existing = repository.findById(id);
        if (existing.isPresent()) {
            Color exist = existing.get();
            exist.setNome(color.getNome());
            Color saved = repository.save(exist);
            return ResponseEntity.ok().body(saved);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<Color> existing = repository.findById(id);
        if (existing.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
