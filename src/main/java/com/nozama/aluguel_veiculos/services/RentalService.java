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
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Rental create(RentalRequest request) {
        Vehicle vehicle = findVehicle(request.placa());
        validateVehicleAvailability(vehicle);

        validateDates(request.startDate(), request.endDate());
        Customer customer = findCustomer(request.cpf());

        BigDecimal price = calculatePrice(
                vehicle.getValorDiario(),
                request.startDate(),
                request.endDate()
        );

        vehicle.setStatus(VehicleStatus.ALUGADO);

        Rental rental = new Rental(vehicle, customer, request);
        rental.setPrice(price);
        rental.setStatus(RentalStatus.ATIVA);

        vehicleRepository.save(vehicle);
        return rentalRepository.save(rental);
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
        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental update(Long id, RentalRequest.update request) {
        Rental rental = findRentalById(id);
        validateRentalEditable(rental);

        if (request.placa() != null) updateVehicle(rental, request.placa());
        if (request.cpf() != null) rental.setCustomer(findCustomer(request.cpf()));
        if (request.startDate() != null) rental.setStartDate(request.startDate());
        if (request.endDate() != null) rental.setEndDate(request.endDate());

        if (rental.getStartDate() != null && rental.getEndDate() != null) {
            validateDates(rental.getStartDate(), rental.getEndDate());
            rental.setPrice(calculatePrice(
                    rental.getVehicle().getValorDiario(),
                    rental.getStartDate(),
                    rental.getEndDate()
            ));
        }

        rental.updateStatus();
        return rentalRepository.save(rental);
    }

    public void deleteById(Long id) {
        Rental rental = findRentalById(id);

        if (!rental.getStartDate().isAfter(LocalDate.now()))
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

        RentalResponse proximaDevolucao = rentals.stream()
                .filter(r -> r.getStatus() == RentalStatus.ATIVA)
                .min(Comparator.comparing(Rental::getEndDate))
                .map(RentalResponse::fromEntity)
                .orElse(null);

        RentalResponse proximaLocacao = rentals.stream()
                .filter(r -> r.getStartDate() != null && r.getStartDate().isAfter(today))
                .min(Comparator.comparing(Rental::getStartDate))
                .map(RentalResponse::fromEntity)
                .orElse(null);

        return new RentalDashboardResponse(
                faturamentoMes,
                reservasAndamento,
                pendencias,
                proximaDevolucao,
                proximaLocacao
        );
    }

    public Page<RentalResponse> listRentals(String cpf, String placa, String status, Pageable pageable) {
        RentalStatus statusEnum = null;

        if (status != null && !status.isBlank()) {
            try {
                statusEnum = RentalStatus.fromString(status);
            } catch (Exception e) {
                throwBadRequest("Status inválido: " + status);
            }
        }

        Page<Rental> rentals = rentalRepository.findByFilters(cpf, placa, statusEnum, pageable);

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

        // Atualiza status e salva no banco
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

        return rental;
    }

    private Vehicle findVehicle(String placa) {
        return vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    private Customer findCustomer(String cpf) {
        return customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    private void validateVehicleAvailability(Vehicle vehicle) {
        if (vehicle.getStatus() != VehicleStatus.DISPONIVEL)
            throwBadRequest("Veículo não disponível.");
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (!end.isAfter(start))
            throwBadRequest("Data final deve ser posterior à inicial.");
    }

    private void validateRentalEditable(Rental rental) {
        if (rental.isReturned())
            throwBadRequest("Não é possível editar uma locação já devolvida.");

        if (!rental.getStartDate().isAfter(LocalDate.now().minusDays(1)))
            throwBadRequest("Locação já iniciada, não pode editar.");
    }

    private void updateVehicle(Rental rental, String placa) {
        Vehicle oldVehicle = rental.getVehicle();
        Vehicle newVehicle = findVehicle(placa);

        validateVehicleAvailability(newVehicle);

        oldVehicle.setStatus(VehicleStatus.DISPONIVEL);
        newVehicle.setStatus(VehicleStatus.ALUGADO);

        vehicleRepository.save(oldVehicle);
        vehicleRepository.save(newVehicle);

        rental.setVehicle(newVehicle);
    }

    private BigDecimal calculatePrice(BigDecimal valorDiario, LocalDate start, LocalDate end) {
        long dias = ChronoUnit.DAYS.between(start, end);
        return valorDiario.multiply(BigDecimal.valueOf(dias)).setScale(2, RoundingMode.HALF_UP);
    }

    private void throwBadRequest(String msg) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}
