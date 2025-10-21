package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import com.nozama.aluguel_veiculos.dto.RentalResponse;
import com.nozama.aluguel_veiculos.dto.RentalUpdateRequest;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.services.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental")
public class RentalController {

    private RentalService service;
    private RentalRepository repository;

    public RentalController(RentalService service, RentalRepository repository){
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Rental> create(@RequestBody @Valid RentalRequest request){
        Rental saved = service.create(request);
        return ResponseEntity.ok().body(saved);
    }

    @PatchMapping("/return/{id}")
    public ResponseEntity<Rental> returnVehicle(@PathVariable Long id){
        Rental saved = service.returnVehicle(id);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Rental> update(@PathVariable Long id, @RequestBody RentalUpdateRequest request){
        Rental update = service.update(id, request);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RentalResponse>> getAll(){
        List<RentalResponse> rentals = service.getAll();
        return ResponseEntity.ok(rentals);
    }

}
