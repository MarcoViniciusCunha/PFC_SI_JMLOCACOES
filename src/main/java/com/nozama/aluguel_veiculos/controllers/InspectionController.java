package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.domain.Inspection;
import com.nozama.aluguel_veiculos.dto.InspectionRequest;
import com.nozama.aluguel_veiculos.dto.InspectionResponse;
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
    public ResponseEntity<InspectionResponse> create(@RequestBody @Valid InspectionRequest request){
        Inspection inspection = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(InspectionResponse.fromEntity(inspection));
    }

    @GetMapping
    public ResponseEntity<List<InspectionResponse>> getAll(){
        List<Inspection> inspections = inspectionRepository.findAll();

        List<InspectionResponse> response = inspections.stream()
                .map(InspectionResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionResponse> getById(@PathVariable Long id){
        Inspection inspection = service.findById(id);

        return ResponseEntity.ok(InspectionResponse.fromEntity(inspection));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InspectionResponse> update(@PathVariable Long id, @RequestBody InspectionRequest.update request) {
        Inspection inspection = service.findById(id);
        Inspection updatedInspection = service.update(inspection, request);
        return ResponseEntity.ok(InspectionResponse.fromEntity(updatedInspection));
    }
}
