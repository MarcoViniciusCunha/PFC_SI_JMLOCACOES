package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.VehicleFilter;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.dto.VehicleResponse;
import com.nozama.aluguel_veiculos.services.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@RequestBody @Valid VehicleRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(VehicleResponse.fromEntity(service.create(request)));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAll() {
        return ResponseEntity.ok(
                service.findAll().stream().map(VehicleResponse::fromEntity).toList()
        );
    }

    @GetMapping("/{placa}")
    public ResponseEntity<VehicleResponse> getByPlaca(@PathVariable String placa){
        return ResponseEntity.ok(VehicleResponse.fromEntity(service.findById(placa)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehicleResponse>> search(
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(service.searchVehicles(placa, categoria, brand, model, color, ano, status));
    }

    @GetMapping("/status")
    public ResponseEntity<List<VehicleResponse>> getByStatus(@RequestParam String status){
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @PatchMapping("/{placa}")
    public ResponseEntity<VehicleResponse> update(@PathVariable String placa, @RequestBody @Valid VehicleRequest.update request){
        return ResponseEntity.ok(VehicleResponse.fromEntity(service.updateVehicle(placa, request)));
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> delete(@PathVariable String placa){
        service.delete(placa);
        return ResponseEntity.noContent().build();
    }
}
