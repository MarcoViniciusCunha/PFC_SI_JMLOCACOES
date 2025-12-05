package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.InsuranceCompany;
import com.nozama.aluguel_veiculos.dto.InsuranceCompanyRequest;
import com.nozama.aluguel_veiculos.repository.InsuranceCompanyRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class InsuranceCompanyController {

    private final InsuranceCompanyRepository repository;

    @PostMapping
    public ResponseEntity<InsuranceCompany> create(@RequestBody @Valid InsuranceCompanyRequest request) {
        InsuranceCompany existing = repository.findByNameIgnoreCase(request.name()).orElse(null);
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }

        InsuranceCompany company = new InsuranceCompany(request);
        repository.save(company);
        return ResponseEntity.ok(company);
    }

    @GetMapping
    public ResponseEntity<List<InsuranceCompany>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsuranceCompany> findById(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
