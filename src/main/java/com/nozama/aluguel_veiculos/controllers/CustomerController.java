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
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        Customer c = service.create(request);
        return ResponseEntity.ok(CustomerResponse.from(c));
    }

    // Listar todos clientes ativos
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll() {
        var list = service.getAllAtivos().stream()
                .map(CustomerResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    // Listar todos clientes ordenados (ativos e inativos)
    @GetMapping("/all-ordered")
    public ResponseEntity<List<CustomerResponse>> getAllOrdered() {
        var list = service.getAllPrincipal().stream()
                .map(CustomerResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    // Buscar clientes ativos pelo nome
    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> getByName(@RequestParam String nome) {
        var list = service.getByNameAtivos(nome).stream()
                .map(CustomerResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    // Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(CustomerResponse.from(service.getById(id)));
    }

    // Atualizar cliente
    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id,
                                                   @RequestBody CustomerRequest.update request) {
        return ResponseEntity.ok(CustomerResponse.from(service.update(id, request)));
    }

    // Excluir cliente (anônimo)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // Desativar cliente
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok().build();
    }

    // Ativar cliente
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.ok().build();
    }
}
