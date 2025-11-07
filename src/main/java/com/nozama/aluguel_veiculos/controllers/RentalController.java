package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import com.nozama.aluguel_veiculos.dto.RentalResponse;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.services.RentalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/{id}")
    public ResponseEntity<Rental> update(@PathVariable Long id, @RequestBody RentalRequest.update request){
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

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getById(@PathVariable Long id){
        Rental rental = service.findRentalById(id);
        return ResponseEntity.ok(RentalResponse.fromEntity(rental));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<RentalResponse>> filter (@RequestParam(required = false) String cpf,
                                                        @RequestParam(required = false) String placa,
                                                        @RequestParam(required = false) String status,
                                                        Pageable pageable){
        Page<RentalResponse> rentals = service.listRentals(cpf, placa, status, pageable);
        return ResponseEntity.ok(rentals);
    }

}
