package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.PaymentRequest;
import com.nozama.aluguel_veiculos.repository.PaymentRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;

    public Payment create(PaymentRequest request){
        Long id = request.rentalId();
        Rental rental = findRental(id);

        Payment payment = new Payment(rental, request);

        return paymentRepository.save(payment);
    }

    public Payment findById(Long id){
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrada com id: " + id));
    };

    public void delete(Long id){
        Payment payment = findById(id);
        paymentRepository.delete(payment);
    }

    public Payment update(Payment payment, PaymentRequest.update request){
        if (request.rentalId() != null && !request.rentalId().equals(payment.getRental().getId())) {
            Long id = request.rentalId();
            Rental rental = findRental(id);
            payment.setRental(rental);
        }
        if (request.dataPagamento() != null && !request.dataPagamento().equals(payment.getData_pagamento())) {
            payment.setData_pagamento(request.dataPagamento());
        }
        if (request.valor() != null && !request.valor().equals(payment.getValor())) {
            payment.setValor(request.valor());
        }
        if (request.formaPagto() != null && !request.formaPagto().equals(payment.getFormaPagto())) {
            payment.setFormaPagto(request.formaPagto());
        }
        if (request.status() != null && !request.status().equals(payment.getStatus())) {
            payment.setStatus(request.status());
        }
        if (request.parcelas() != null && !request.parcelas().equals(payment.getParcelas())) {
            payment.setParcelas(request.parcelas());
        }
        if (request.descricao() != null && !request.descricao().equals(payment.getDescricao())) {
            payment.setDescricao(request.descricao());
        }

        return paymentRepository.save(payment);
    }

    private Rental findRental (Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locação não encontrada com id: " + id));
    }
}
