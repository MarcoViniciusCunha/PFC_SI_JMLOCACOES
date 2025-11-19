package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.PaymentRequest;
import com.nozama.aluguel_veiculos.dto.PaymentResponse;
import com.nozama.aluguel_veiculos.repository.PaymentRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;

    public Payment create(PaymentRequest request) {
        Rental rental = findRental(request.rentalId());

        var payments = paymentRepository.findByRentalId(rental.getId());

        BigDecimal juros = request.juros() != null ? request.juros() : BigDecimal.ZERO;

        BigDecimal valorTotalAtual = calcularValorTotalComJurosETaxa(rental, juros, request.dataPagamento());

        BigDecimal valorPago = payments.stream()
                .filter(p -> "PAGO".equalsIgnoreCase(p.getStatus()))
                .map(Payment::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var pagamentoBase = payments.stream()
                .filter(p -> "PAGO".equalsIgnoreCase(p.getStatus()))
                .findFirst();

        var pagamentoPendente = payments.stream()
                .filter(p -> "PENDENTE".equalsIgnoreCase(p.getStatus()))
                .findFirst();

        if (valorPago.compareTo(valorTotalAtual) >= 0 && pagamentoPendente.isEmpty()) {
            throw new RuntimeException("Todas as pendências desta locação já foram pagas.");
        }

        pagamentoPendente.ifPresent(paymentRepository::delete);

        Payment novoPagamento = new Payment(rental, request);

        BigDecimal valorCalculado = valorTotalAtual;

        if (pagamentoBase.isPresent()) {
            BigDecimal valorBase = pagamentoBase.get().getValor();
            valorCalculado = valorCalculado.subtract(valorBase);

            if (valorCalculado.compareTo(BigDecimal.ZERO) < 0) {
                valorCalculado = BigDecimal.ZERO;
            }
        }

        novoPagamento.setValor(valorCalculado);
        novoPagamento.setStatus(request.status());
        novoPagamento.setFormaPagto(request.formaPagto());
        novoPagamento.setParcelas(request.parcelas());
        novoPagamento.setData_pagamento(request.dataPagamento());

        return paymentRepository.save(novoPagamento);
    }

    public List<Payment> findAll(){
        return paymentRepository.findAll();
    }

    public Page<PaymentResponse> filterPayments(
            LocalDate data,
            String formaPagto,
            String status,
            String cpf,
            String placa,
            Pageable pageable
    ) {
        String normalizedStatus = status == null ? "" : status.toUpperCase();

        Page<Payment> payments;

        switch (normalizedStatus) {
            case "PAGO" ->
                    payments = paymentRepository.findByFilters(data, formaPagto, "PAGO", cpf, placa, pageable);
            case "PENDENTE" ->
                    payments = paymentRepository.findByFilters(data, formaPagto, "PENDENTE", cpf, placa, pageable);
            default ->
                    payments = paymentRepository.findByFilters(data, formaPagto, null, cpf, placa, pageable);
        }

        return payments.map(PaymentResponse::fromEntity);
    }

    public Payment findById(Long id){
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com id: " + id));
    }

    public void delete(Long id){
        Payment payment = findById(id);
        paymentRepository.delete(payment);
    }

    public Payment update(Payment payment, PaymentRequest.update request){

        if (request.rentalId() != null && !request.rentalId().equals(payment.getRental().getId())) {
            Rental rental = findRental(request.rentalId());
            payment.setRental(rental);
        }

        if (request.dataPagamento() != null) {
            payment.setData_pagamento(request.dataPagamento());
        }

        if (request.juros() != null) {
            Rental rental = payment.getRental();
            var payments = paymentRepository.findByRentalId(rental.getId());
            var pagamentoBase = payments.stream()
                    .filter(p -> "PAGO".equalsIgnoreCase(p.getStatus()))
                    .findFirst();

            BigDecimal valorCalculado = calcularValorTotalComJurosETaxa(rental, request.juros(), payment.getData_pagamento());

            if (pagamentoBase.isPresent()) {
                BigDecimal valorBase = pagamentoBase.get().getValor();
                valorCalculado = valorCalculado.subtract(valorBase);

                if (valorCalculado.compareTo(BigDecimal.ZERO) < 0) {
                    valorCalculado = BigDecimal.ZERO;
                }
            }

            payment.setValor(valorCalculado);
        }

        if (request.formaPagto() != null) {
            payment.setFormaPagto(request.formaPagto());
        }

        if (request.status() != null) {
            payment.setStatus(request.status());
        }

        if (request.parcelas() != null) {
            payment.setParcelas(request.parcelas());
        }

        if (request.descricao() != null) {
            payment.setDescricao(request.descricao());
        }

        return paymentRepository.save(payment);
    }

    private Rental findRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locação não encontrada com id: " + id));
    }

    private BigDecimal calcularValorTotalComJurosETaxa(Rental rental, BigDecimal juros, LocalDate dataPagamento) {

        BigDecimal valorBase = rental.getPrice();
        BigDecimal multaTotal = BigDecimal.ZERO;
        BigDecimal jurosTotal = BigDecimal.ZERO;

        if (rental.getReturnDate() != null && rental.getReturnDate().isAfter(rental.getEndDate())) {
            long diasAtrasoDevolucao = java.time.temporal.ChronoUnit.DAYS.between(
                    rental.getEndDate(), rental.getReturnDate()
            );
            BigDecimal valorDiario = valorBase.divide(
                    BigDecimal.valueOf(java.time.temporal.ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate())),
                    2, RoundingMode.HALF_UP
            );
            multaTotal = valorDiario.multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(diasAtrasoDevolucao));
        }

        if (dataPagamento != null && dataPagamento.isAfter(rental.getEndDate()) && juros != null && juros.compareTo(BigDecimal.ZERO) > 0) {
            long diasAtrasoPagamento = java.time.temporal.ChronoUnit.DAYS.between(rental.getEndDate(), dataPagamento);
            jurosTotal = valorBase.multiply(juros).multiply(BigDecimal.valueOf(diasAtrasoPagamento));
        }

        return valorBase.add(multaTotal).add(jurosTotal).setScale(2, RoundingMode.HALF_UP);
    }
}
