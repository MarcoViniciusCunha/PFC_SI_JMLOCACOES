package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.enums.PaymentStatus;
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

        BigDecimal valorInicial = rental.getPrice();

        Payment pagamento = new Payment(rental, request);
        pagamento.setValor(valorInicial);
        pagamento.setStatus(PaymentStatus.PAGO);
        pagamento.setFormaPagto(request.formaPagto());
        pagamento.setParcelas(request.parcelas());
        pagamento.setData_pagamento(request.dataPagamento());

        return paymentRepository.save(pagamento);
    }

    public Payment gerarPagamentoAposDevolucao(Rental rental) {
        BigDecimal valorTotalFinal = calcularValorTotalComJurosETaxa(rental, BigDecimal.ZERO, rental.getReturnDate());

        BigDecimal totalTolls = rental.getTolls().stream()
                .map(t -> t.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFines = rental.getFines().stream()
                .map(f -> f.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        valorTotalFinal = valorTotalFinal.add(totalTolls).add(totalFines);

        BigDecimal totalPago = paymentRepository.findByRentalId(rental.getId())
                .stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAGO)
                .map(Payment::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal diferenca = valorTotalFinal.subtract(totalPago);

        if (diferenca.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        Payment pagamentoExtra = new Payment();
        pagamentoExtra.setRental(rental);
        pagamentoExtra.setValor(diferenca);
        pagamentoExtra.setStatus(PaymentStatus.PENDENTE);
        pagamentoExtra.setData_pagamento(LocalDate.now());
        pagamentoExtra.setFormaPagto("A DEFINIR");

        return paymentRepository.save(pagamentoExtra);
    }

    public List<Payment> findAll(){
        return paymentRepository.findAll();
    }

    public Page<PaymentResponse> filterPayments(
            LocalDate data,
            String formaPagto,
            String status,
            Long customerId,
            String placa,
            Pageable pageable
    ) {
        PaymentStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            statusEnum = PaymentStatus.fromString(status);
        }

        Page<Payment> payments = paymentRepository.findByFilters(data, formaPagto, statusEnum, customerId, placa, pageable);
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
                    .filter(p -> p.getStatus() == PaymentStatus.PAGO)
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
            payment.setStatus(PaymentStatus.fromString(request.status()));
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
