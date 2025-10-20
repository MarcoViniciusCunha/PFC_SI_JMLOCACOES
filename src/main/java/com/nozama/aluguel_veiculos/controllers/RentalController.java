package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import com.nozama.aluguel_veiculos.services.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rental")
public class RentalController {

    private RentalService service;

    public RentalController(RentalService service){
        this.service = service;
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

}
