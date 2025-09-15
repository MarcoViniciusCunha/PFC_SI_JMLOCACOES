package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Model;
import com.nozama.aluguel_veiculos.dto.ModelRequest;
import com.nozama.aluguel_veiculos.repository.ModelRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/model")
public class ModelController {

    private final ModelRepository repository;

    public ModelController(ModelRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Model> create (@RequestBody @Valid ModelRequest request) {
        Model model = new Model(request);
        Model saved = repository.save(model);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Model>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> getById(@PathVariable Integer id) {
        Optional<Model> existing = repository.findById(id);
        return existing.isPresent() ? ResponseEntity.ok().body(existing.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Model> update(@PathVariable Integer id, @RequestBody ModelRequest request) {
        Optional<Model> existing = repository.findById(id);
        if (existing.isPresent()) {
            Model exist = existing.get();
            exist.setNome(request.nome());
            Model saved = repository.save(exist);
            return ResponseEntity.ok().body(saved);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<Model> existing = repository.findById(id);
        if (existing.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
