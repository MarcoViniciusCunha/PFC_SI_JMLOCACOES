package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.RentalDashboardResponse;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import com.nozama.aluguel_veiculos.dto.RentalResponse;
import com.nozama.aluguel_veiculos.services.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService service;

    @PostMapping
    public ResponseEntity<Rental> create(@RequestBody @Valid RentalRequest request){
        return ResponseEntity.ok(service.create(request));
    }

    @PatchMapping("/return/{id}")
    public ResponseEntity<Rental> returnVehicle(@PathVariable Long id){
        return ResponseEntity.ok(service.returnVehicle(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Rental> update(@PathVariable Long id, @RequestBody RentalRequest.update request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RentalResponse>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(RentalResponse.fromEntity(service.findRentalById(id)));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<RentalResponse>> filter (
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) String status,
            Pageable pageable){
        return ResponseEntity.ok(service.listRentals(cpf, placa, status, pageable));
    }

    @GetMapping("/dashboard/info")
    public RentalDashboardResponse getRentalDashboard() {
        return service.getRentalDashboard();
    }

    @GetMapping("/status")
    public ResponseEntity<List<RentalResponse>> getRentalStatus(@RequestParam String status){
        return ResponseEntity.ok(service.findRentalByStatus(status));
    }

}
