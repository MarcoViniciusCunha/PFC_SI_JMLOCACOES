package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.dto.VehicleFilter;
import com.nozama.aluguel_veiculos.dto.VehiclePatchRequest;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        Vehicle saved = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{placa}")
    public ResponseEntity<Vehicle> getByPlaca(@PathVariable String placa){
        return ResponseEntity.ok(service.findById(placa));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Vehicle>> search(
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String status) {

        VehicleFilter filter = new VehicleFilter();
        filter.setPlaca(placa);
        filter.setIdCategoria(categoria != null ? Integer.valueOf(categoria) : null);
        filter.setIdMarca(brand != null ? Integer.valueOf(brand) : null);
        filter.setIdCor(color != null ? Integer.valueOf(color) : null);
        filter.setAno(ano);
        filter.setStatus(status != null ? status.toUpperCase() : null);

        List<Vehicle> vehicles = service.searchVehicles(filter);
        return ResponseEntity.ok(vehicles);
    }

    @PatchMapping("/{placa}")
    public ResponseEntity<Vehicle> update(@PathVariable String placa, @RequestBody @Valid VehiclePatchRequest request){
        Vehicle updated = service.updateVehicle(placa, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> delete(@PathVariable String placa){
        service.delete(placa);
        return ResponseEntity.ok().build();
    }
}
