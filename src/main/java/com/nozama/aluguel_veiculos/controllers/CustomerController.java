package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.dto.CustomerResponse;
import com.nozama.aluguel_veiculos.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request){
        Customer c = service.create(request);
        return ResponseEntity.ok(CustomerResponse.from(c));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll(){
        var list = service.getAll().stream()
                .map(CustomerResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> getByName(@RequestParam String nome){
        var list = service.getByName(nome).stream()
                .map(CustomerResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(CustomerResponse.from(service.getById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id,@RequestBody CustomerRequest.update request){
        return ResponseEntity.ok(CustomerResponse.from(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
