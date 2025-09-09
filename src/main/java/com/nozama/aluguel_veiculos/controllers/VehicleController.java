package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.vehicle.Vehicle;
import com.nozama.aluguel_veiculos.dto.VehiclePatchRequest;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Vehicle> create(@RequestBody @Valid VehicleRequest request){
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{placa}")
    public ResponseEntity<Vehicle> getByPlaca(@PathVariable String placa){
        return ResponseEntity.ok(service.findById(placa));
    }

    @GetMapping("/category")
    public ResponseEntity<List<Vehicle>> getByCategory(@RequestParam String name){
        return ResponseEntity.ok(service.findByCategory(name));
    }

    @PatchMapping("/{placa}")
    public ResponseEntity<Vehicle> update(@PathVariable String placa, @RequestBody @Valid VehiclePatchRequest request){
        Vehicle updated = service.updateVehicle(placa, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Vehicle> delete(@PathVariable String placa){
        service.delete(placa);
        return ResponseEntity.ok().build();
    }
}
