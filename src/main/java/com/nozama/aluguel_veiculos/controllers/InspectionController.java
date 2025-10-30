package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Inspection;
import com.nozama.aluguel_veiculos.dto.InspectionRequest;
import com.nozama.aluguel_veiculos.repository.InspectionRepository;
import com.nozama.aluguel_veiculos.services.InspectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inspection")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService service;
    private final InspectionRepository inspectionRepository;

    @PostMapping
    public ResponseEntity<Inspection> create(@RequestBody @Valid InspectionRequest request){
        Inspection inspection = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(inspection);
    }

    @GetMapping
    public ResponseEntity<List<Inspection>> getAll(){
        return ResponseEntity.ok(inspectionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inspection> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
