package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Brand;
import com.nozama.aluguel_veiculos.domain.Model;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.dto.ModelRequest;
import com.nozama.aluguel_veiculos.repository.BrandRepository;
import com.nozama.aluguel_veiculos.repository.ModelRepository;
import com.nozama.aluguel_veiculos.repository.VehicleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelController {

    private final ModelRepository repository;
    private final BrandRepository brandRepository;
    private final VehicleRepository vehicleRepository;

    @PostMapping
    public ResponseEntity<?> create (@RequestBody @Valid ModelRequest request) {
        if (repository.existsByNome(request.nome())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Modelo '" + request.nome() + "' já existe!");
        }

        Brand brand = getBrandById(request.brandId());

        Model saved = repository.save(new Model(request, brand));
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Model>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> getById(@PathVariable Integer id) {
        Model model = repository.findById(id).orElse(null);
        if (model == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ModelRequest.update request) {
        Model model = getModelById(id);

        if (request.nome() != null && !request.nome().isBlank()) {
            model.setNome(request.nome());
        }

        boolean brandChanged = false;
        if (request.brandId() != null) {
            Brand brand = getBrandById(request.brandId());

            if (!model.getBrand().getId().equals(request.brandId())) {
                model.setBrand(brand);
                brandChanged = true;
            }
        }

        Model saved = repository.save(model);

        if (brandChanged) {
            List<Vehicle> vehicles = vehicleRepository.findAll().stream()
                    .filter(v -> v.getModel().getId().equals(model.getId()))
                    .toList();

            vehicles.forEach(v -> v.setBrand(model.getBrand()));
            vehicleRepository.saveAll(vehicles);
        }

        return ResponseEntity.ok().body(saved);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Brand getBrandById(Integer id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
    }

    private Model getModelById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado"));
    }
}
