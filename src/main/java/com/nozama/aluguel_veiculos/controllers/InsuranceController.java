package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Insurance;
import com.nozama.aluguel_veiculos.dto.InsuranceRequest;
import com.nozama.aluguel_veiculos.services.InsuranceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    private final InsuranceService service;

    public InsuranceController(InsuranceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Insurance> create(@RequestBody @Valid InsuranceRequest request){
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Insurance>> getAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Insurance> update(@PathVariable int id, @RequestBody Map<String, Object> update) {
        return ResponseEntity.ok(service.update(id, update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Insurance> delete(@PathVariable int id) {
        service.deleteByID(id);
        return ResponseEntity.ok().build();
    }
}
