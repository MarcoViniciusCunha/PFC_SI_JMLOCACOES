package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.vehicle.Vehicle;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{placa}")
    public ResponseEntity<Vehicle> delete(@PathVariable String placa){
        service.delete(placa);
        return ResponseEntity.ok().build();
    }
}
