package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.dto.BrandRequest;
import com.nozama.aluguel_veiculos.repository.BrandRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nozama.aluguel_veiculos.domain.Brand;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/brand")
public class BrandController {

    private final BrandRepository repository;

    public BrandController(BrandRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Brand> create(@RequestBody @Valid BrandRequest request) {
        Brand mark = new Brand(request);
        Brand saved = repository.save(mark);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Brand>> findAll() {
        List<Brand> marks = repository.findAll();
        return ResponseEntity.ok().body(marks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> findById(@PathVariable Integer id) {
        Optional<Brand> existing = repository.findById(id);
        if (existing.isPresent()) {
            return ResponseEntity.ok().body(existing.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> update(@PathVariable Integer id, @RequestBody Brand mark) {
        Optional<Brand> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            Brand existing = existingOpt.get();
            existing.setNome(mark.getNome());
            Brand saved = repository.save(existing);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer  id) {
        Optional<Brand> existing = repository.findById(id);
        if (existing.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
