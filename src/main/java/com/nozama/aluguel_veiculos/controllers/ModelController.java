package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Brand;
import com.nozama.aluguel_veiculos.domain.Model;
import com.nozama.aluguel_veiculos.dto.ModelPatchRequest;
import com.nozama.aluguel_veiculos.dto.ModelRequest;
import com.nozama.aluguel_veiculos.repository.BrandRepository;
import com.nozama.aluguel_veiculos.repository.ModelRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/model")
public class ModelController {

    private final ModelRepository repository;
    private final BrandRepository brandRepository;

    public ModelController(ModelRepository repository, BrandRepository brandRepository) {
        this.repository = repository;
        this.brandRepository = brandRepository;
    }

    @PostMapping
    public ResponseEntity<?> create (@RequestBody @Valid ModelRequest request) {
        if (repository.existsByNome(request.nome())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Modelo '" + request.nome() + "' já existe!");
        }

        Optional<Brand> brand = brandRepository.findById(request.brandId());
        if (brand.isEmpty()) {
            return ResponseEntity.badRequest().body("Marca não encontrada.");
        }

        Model model = new Model(request, brand.get());
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

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ModelPatchRequest request) {
        Optional<Model> existing = repository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modelo não encontrado.");
        }

        Model model = existing.get();

        if (request.nome() != null && !request.nome().isBlank()) {
            model.setNome(request.nome());
        }

        if (request.brandId() != null) {
            Optional<Brand> brand = brandRepository.findById(request.brandId());
            if (brand.isEmpty()) return ResponseEntity.badRequest().body("Marca não encontrada.");
            model.setBrand(brand.get());
        }

        Model saved = repository.save(model);
        return ResponseEntity.ok().body(saved);
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
