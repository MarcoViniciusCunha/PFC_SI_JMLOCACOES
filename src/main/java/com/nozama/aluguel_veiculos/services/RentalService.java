package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.Payment;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.PaymentStatus;
import com.nozama.aluguel_veiculos.domain.enums.RentalStatus;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.RentalDashboardResponse;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import com.nozama.aluguel_veiculos.dto.RentalResponse;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleService vehicleService;
    private final PaymentService paymentService;

    @Transactional
    public Rental create(RentalRequest request) {
        validateDates(request.startDate(), request.endDate());

        Vehicle vehicle = findVehicle(request.placa());
        Customer customer = findCustomer(request.customerId());

        boolean conflito = rentalRepository.existsActiveConflict(
                vehicle,
                request.startDate(),
                request.endDate()
        );

        if (conflito) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O veículo já possui uma locação nesse período.");
        }

        validateVehicleAvailability(vehicle);

        Rental rental = new Rental(vehicle, customer, request);
        rental.setPrice(calculatePrice(
                vehicle.getValorDiario(),
                request.startDate(),
                request.endDate()
        ));

        rentalRepository.save(rental);
        vehicleService.atualizarStatusDoVeiculo(vehicle);

        return rental;
    }

    @Transactional
    public Rental returnVehicle(Long id) {
        Rental rental = findRentalById(id);

        if (rental.isReturned())
            throwBadRequest("Veículo já devolvido.");

        rental.setReturned(true);
        rental.setReturnDate(LocalDate.now());
        rental.setStatus(RentalStatus.DEVOLVIDA);

        Vehicle vehicle = rental.getVehicle();
        vehicle.setStatus(VehicleStatus.DISPONIVEL);
        vehicleRepository.save(vehicle);

        rentalRepository.save(rental);
        Payment pagamentoExtra = paymentService.gerarPagamentoAposDevolucao(rental);
        if (pagamentoExtra != null) {
            System.out.println("Pagamento extra gerado: " + pagamentoExtra.getValor());
        }
        return rental;
    }

    @Transactional
    public Rental update(Long id, RentalRequest.update request) {
        Rental rental = findRentalById(id);
        validateRentalEditable(rental);

        if (request.placa() != null) updateVehicle(rental, request.placa());
        if (request.customerId() != null) rental.setCustomer(findCustomer(request.customerId()));
        if (request.startDate() != null) rental.setStartDate(request.startDate());
        if (request.endDate() != null) rental.setEndDate(request.endDate());

        validateDates(rental.getStartDate(), rental.getEndDate());

        boolean conflito = rentalRepository.existsConflictExcludingSelf(
                rental.getVehicle(),
                rental.getId(),
                rental.getStartDate(),
                rental.getEndDate()
        );

        if (conflito) {
            throwBadRequest("O veículo já possui uma locação nesse período.");
        }

        rental.setPrice(calculatePrice(
                rental.getVehicle().getValorDiario(),
                rental.getStartDate(),
                rental.getEndDate()
        ));

        rental.updateStatus();
        return rentalRepository.save(rental);
    }

    public void deleteById(Long id) {
        Rental rental = findRentalById(id);

        if (!rental.getStartDate().isAfter(LocalDate.now().minusDays(1)))
            throwBadRequest("Não é possível deletar uma locação já iniciada.");

        if (!rental.isReturned())
            rental.getVehicle().setStatus(VehicleStatus.DISPONIVEL);

        rentalRepository.deleteById(id);
    }

    public List<RentalResponse> getAll() {
        List<Rental> rentals = rentalRepository.findAll();
        rentals.forEach(Rental::updateStatus);
        rentalRepository.saveAll(rentals);
        return rentals.stream().map(RentalResponse::fromEntityBasic).toList();

    }

    public RentalDashboardResponse getRentalDashboard() {
        LocalDate today = LocalDate.now();
        List<Rental> rentals = rentalRepository.findAll();
        rentals.forEach(Rental::updateStatus);
        rentalRepository.saveAll(rentals);

        BigDecimal faturamentoMes = rentals.stream()
                .flatMap(r -> r.getPayments().stream())
                .filter(p -> p.getData_pagamento() != null)
                .filter(p -> p.getStatus() == PaymentStatus.PAGO)
                .filter(p ->
                        p.getData_pagamento().getMonth() == today.getMonth()
                                && p.getData_pagamento().getYear() == today.getYear()
                )
                .map(Payment::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long reservasAndamento = rentals.stream()
                .filter(r -> r.getStatus() == RentalStatus.ATIVA)
                .count();

        long pendencias = rentals.stream()
                .filter(r -> r.getStatus() == RentalStatus.ATRASADA)
                .count();

// Próximas devoluções
        List<RentalResponse> proximaDevolucao = rentals.stream()
                .filter(r -> r.getStatus() == RentalStatus.ATIVA)
                .filter(r -> r.getEndDate() != null && !r.getEndDate().isBefore(today)) // hoje ou depois
                .collect(Collectors.groupingBy(Rental::getEndDate))
                .entrySet().stream()
                .min(Map.Entry.comparingByKey()) // pega a menor data
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList())
                .stream()
                .map(RentalResponse::fromEntity)
                .toList();

// Próximas locações
        List<RentalResponse> proximaLocacao = rentals.stream()
                .filter(r -> r.getStartDate() != null && !r.getStartDate().isBefore(today)) // hoje ou depois
                .collect(Collectors.groupingBy(Rental::getStartDate))
                .entrySet().stream()
                .min(Map.Entry.comparingByKey()) // pega a menor data
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList())
                .stream()
                .map(RentalResponse::fromEntity)
                .toList();

        return new RentalDashboardResponse(
                faturamentoMes,
                reservasAndamento,
                pendencias,
                proximaDevolucao,
                proximaLocacao
        );
    }

    public Page<RentalResponse> listRentals(
            Long customerId,
            String placa,
            String status,
            Boolean aVencer,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {

        RentalStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = RentalStatus.fromString(status);
            } catch (Exception e) {
                throwBadRequest("Status inválido: " + status);
            }
        }

        LocalDate today = LocalDate.now();
        if (Boolean.TRUE.equals(aVencer)) {
            if (startDate == null) startDate = today;
        }

        Page<Rental> rentals = rentalRepository.findByFilters(
                customerId, placa, statusEnum, startDate, endDate, pageable
        );

        rentals.forEach(Rental::updateStatus);
        rentalRepository.saveAll(rentals.getContent());

        return rentals.map(RentalResponse::fromEntityBasic);
    }

    public List<RentalResponse> findRentalByStatus(String status) {
        RentalStatus statusEnum;

        try {
            statusEnum = RentalStatus.fromString(status);
        } catch (Exception e) {
            throwBadRequest("Status inválido: " + status);
            return null;
        }

        List<Rental> rentals = rentalRepository.findAllByStatus(statusEnum);

        if (rentals.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma locação encontrada.");

        rentals.forEach(Rental::updateStatus);
        rentalRepository.saveAll(rentals);

        return rentals.stream()
                .map(RentalResponse::fromEntityBasic)
                .toList();
    }


    public Rental findRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Locação não encontrada."));

        rental.updateStatus();
        rentalRepository.save(rental);

        vehicleService.atualizarStatusDoVeiculo(rental.getVehicle());

        return rental;
    }

    private Vehicle findVehicle(String placa) {
        return vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    private Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    private void validateVehicleAvailability(Vehicle vehicle) {
        if (vehicle.getStatus() == VehicleStatus.MANUTENCAO) {
            throwBadRequest("Veículo está em manutenção e não pode ser alugado.");
        }

        if (vehicle.getStatus() == VehicleStatus.ALUGADO) {
            throwBadRequest("Veículo já está alugado e não pode ser reservado.");
        }

        if (vehicle.getStatus() != VehicleStatus.DISPONIVEL) {
            throwBadRequest("Veículo não está disponível para locação.");
        }
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start.isBefore(LocalDate.now())) {
            throwBadRequest("A data de início não pode estar no passado.");
        }

        if (end.isBefore(LocalDate.now())) {
            throwBadRequest("A data de término não pode estar no passado.");
        }

        if (!end.isAfter(start)) {
            throwBadRequest("Data final deve ser posterior à inicial.");
        }
    }

    private void validateRentalEditable(Rental rental) {
        if (rental.isReturned())
            throwBadRequest("Não é possível editar uma locação já devolvida.");

        if (!rental.getStartDate().isAfter(LocalDate.now()))
            throwBadRequest("Locação já iniciada, não pode editar.");
    }

    private void updateVehicle(Rental rental, String placa) {
        Vehicle oldVehicle = rental.getVehicle();
        Vehicle newVehicle = findVehicle(placa);

        validateVehicleAvailability(newVehicle);

        oldVehicle.setStatus(VehicleStatus.DISPONIVEL);

        vehicleRepository.save(oldVehicle);
        rental.setVehicle(newVehicle);
        vehicleService.atualizarStatusDoVeiculo(newVehicle);

    }

    private BigDecimal calculatePrice(BigDecimal valorDiario, LocalDate start, LocalDate end) {
        long dias = ChronoUnit.DAYS.between(start, end);
        return valorDiario.multiply(BigDecimal.valueOf(dias)).setScale(2, RoundingMode.HALF_UP);
    }

    private void throwBadRequest(String msg) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}