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
        Rental rental = rentalRepository.findById(request.rentalId())
                .orElseThrow(() -> new RuntimeException("Locação não encontrada com id: " + request.rentalId()));

        Payment payment = new Payment(rental, request);

        return paymentRepository.save(payment);
    }

    public Payment findById(Long id){
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrada com id: " + id));
    };

    public void delete(Long id){
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrada com id: " + id));
        paymentRepository.delete(payment);
    }
}
