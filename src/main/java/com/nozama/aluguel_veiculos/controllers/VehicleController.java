package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.VehicleFilter;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.dto.VehicleResponse;
import com.nozama.aluguel_veiculos.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@RequestBody @Valid VehicleRequest request){
        Vehicle saved = service.create(request);
        VehicleResponse vehicle = VehicleResponse.fromEntity(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAll() {
        List<VehicleResponse> vehicles = service.findAll().stream()
                .map(VehicleResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{placa}")
    public ResponseEntity<VehicleResponse> getByPlaca(@PathVariable String placa){
        Vehicle vehicle = service.findById(placa);
        return ResponseEntity.ok(VehicleResponse.fromEntity(vehicle));
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehicleResponse>> search(
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String status) {

        List<VehicleResponse> vehicles = service.searchVehicles(placa, categoria, brand, color, ano, status);
        return ResponseEntity.ok(vehicles);
    }

    @PatchMapping("/{placa}")
    public ResponseEntity<VehicleResponse> update(@PathVariable String placa, @RequestBody @Valid VehicleRequest.update request){
        Vehicle updated = service.updateVehicle(placa, request);
        VehicleResponse vehicle =  VehicleResponse.fromEntity(updated);
        return ResponseEntity.ok(vehicle);
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> delete(@PathVariable String placa){
        service.delete(placa);
        return ResponseEntity.ok().build();
    }
}
