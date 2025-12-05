package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.dto.PaymentRequest;
import com.nozama.aluguel_veiculos.dto.PaymentResponse;
import com.nozama.aluguel_veiculos.repository.PaymentRepository;
import com.nozama.aluguel_veiculos.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository repository;
    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody @Valid PaymentRequest request){
        Payment payment = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PaymentResponse.fromEntity(payment));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll(){
        List<Payment> payments = service.findAll();

        List<PaymentResponse> response = payments.stream()
                .map(PaymentResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id){
        Payment payment = service.findById(id);
        return ResponseEntity.ok().body(PaymentResponse.fromEntity(payment));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<PaymentResponse>> filterPayments(
            @RequestParam(required = false) LocalDate data,
            @RequestParam(required = false) String formaPagto,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String placa,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                service.filterPayments(data, formaPagto, status, customerId, placa, pageable)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentResponse> update(@PathVariable Long id, @RequestBody PaymentRequest.update request) {
        Payment payment = service.findById(id);
        Payment updatedPayment = service.update(payment, request);
        return ResponseEntity.ok().body(PaymentResponse.fromEntity(updatedPayment));
    }
}
