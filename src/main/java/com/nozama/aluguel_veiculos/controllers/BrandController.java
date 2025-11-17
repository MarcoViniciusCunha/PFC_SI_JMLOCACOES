package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Model;
import com.nozama.aluguel_veiculos.dto.BrandRequest;
import com.nozama.aluguel_veiculos.repository.BrandRepository;
import com.nozama.aluguel_veiculos.repository.ModelRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nozama.aluguel_veiculos.domain.Brand;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandRepository repository;
    private final ModelRepository modelRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid BrandRequest request) {
        if (repository.existsByNome(request.nome())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Marca '" + request.nome() + "' já existe!");
        }
        Brand saved = repository.save(new Brand(request));
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Brand>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> findById(@PathVariable Integer id) {
        Optional<Brand> brand  = repository.findById(id);
        if (brand.isPresent()) {
            return ResponseEntity.ok().body(brand.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{brandId}/models")
    public ResponseEntity<List<Model>> getModelsByBrand(@PathVariable Integer brandId) {
        List<Model> models = modelRepository.findByBrandId(brandId);
        return ResponseEntity.ok(models);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> update(@PathVariable Integer id, @RequestBody Brand brand) {
        Optional<Brand> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            Brand exist = existingOpt.get();
            exist.setNome(brand.getNome());
            Brand saved = repository.save(exist);
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
