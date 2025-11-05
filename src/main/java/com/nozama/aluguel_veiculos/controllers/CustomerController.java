package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody CustomerRequest request){
        Customer customer = service.create(request);
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> getByName(@RequestParam String nome){
        List<Customer> customers = service.getByName(nome);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody CustomerRequest.update request){
        Customer update = service.update(id, request);
        return ResponseEntity.ok().body(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
