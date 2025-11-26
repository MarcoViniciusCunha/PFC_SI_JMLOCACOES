package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.dto.FineRequest;
import com.nozama.aluguel_veiculos.dto.FineResponse;
import com.nozama.aluguel_veiculos.repository.FineRepository;
import com.nozama.aluguel_veiculos.services.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService service;
    private final FineRepository repository;

    @PostMapping
    public ResponseEntity<FineResponse> create(@RequestBody @Valid FineRequest request) {
        Fine fine = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(FineResponse.fromEntity(fine));
    }

    @GetMapping
    public ResponseEntity<List<FineResponse>> getAll() {
        List<Fine> fines = repository.findAll();

        List<FineResponse> response = fines.stream()
                .map(FineResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineResponse> getById(@PathVariable Long id) {
        Fine fine = service.findById(id);
        return ResponseEntity.ok(FineResponse.fromEntity(fine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FineResponse> update(@PathVariable Long id, @RequestBody FineRequest.update request) {
        Fine fine = service.findById(id);
        Fine updatedFine = service.update(fine, request);
        return ResponseEntity.ok(FineResponse.fromEntity(updatedFine));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FineResponse>> search(
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) LocalDate dataInicial,
            @RequestParam(required = false) LocalDate dataFinal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<FineResponse> result = service.search(
                placa,
                customerId,
                dataInicial,
                dataFinal,
                pageable
        ).map(FineResponse::fromEntitySummary);

        return ResponseEntity.ok(result);
    }


}