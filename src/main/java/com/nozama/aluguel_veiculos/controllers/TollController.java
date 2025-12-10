package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.dto.TollRequest;
import com.nozama.aluguel_veiculos.dto.TollResponse;
import com.nozama.aluguel_veiculos.dto.TollUpdateRequest;
import com.nozama.aluguel_veiculos.services.TollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/toll")
@RequiredArgsConstructor
public class TollController {
    private final TollService tollService;

    @PostMapping
    public ResponseEntity<TollResponse> create(@RequestBody @Valid TollRequest request) {
        return ResponseEntity.ok(tollService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TollResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid TollUpdateRequest request
    ) {
        return ResponseEntity.ok(tollService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TollResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tollService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tollService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
