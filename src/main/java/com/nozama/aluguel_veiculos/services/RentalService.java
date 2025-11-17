package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Rental create(RentalRequest request){
        Vehicle vehicle = findVehicle(request.placa());
        validateVehicleAvailability(vehicle);

        validateDates(request.startDate(), request.endDate());

        Customer customer = findCustomer(request.cpf());
        BigDecimal price = calculatePrice(vehicle.getValorDiario(), request.startDate(), request.endDate());

        vehicle.setStatus(VehicleStatus.ALUGADO);
        Rental rental = new Rental(vehicle, customer, request);
        rental.setPrice(price);

        vehicleRepository.save(vehicle);
        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental returnVehicle(Long id){
        Rental rental = findRentalById(id);
        if (rental.isReturned()) throwBadRequest("Veículo já foi devolvido.");

        rental.setReturned(true);
        rental.setReturnDate(LocalDate.now());

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
            rental.setPrice(calculatePrice(rental.getVehicle().getValorDiario(), rental.getStartDate(), rental.getEndDate()));
        }

        return rentalRepository.save(rental);
    }

    public void deleteById(Long id){
        Rental rental = findRentalById(id);

        if (!rental.getStartDate().isAfter(LocalDate.now())) {
            throwBadRequest("Não é possível deletar uma locação já iniciada.");
        }

        if (!rental.isReturned()) rental.getVehicle().setStatus(VehicleStatus.DISPONIVEL);

        rentalRepository.deleteById(id);
    }

    public List<RentalResponse> getAll(){
        return rentalRepository.findAll()
                .stream()
                .map(RentalResponse::fromEntityBasic)
                .toList();
    }

    public Page<RentalResponse> listRentals(String cpf, String placa, String status, Pageable pageable) {
        LocalDate today = LocalDate.now();
        Page<Rental> rentals;

        switch (status == null ? "" : status.toUpperCase()) {
            case "DEVOLVIDA" -> rentals = rentalRepository.findFiltered(cpf, placa, true, pageable);
            case "ATIVA" -> rentals = rentalRepository.findAtivas(cpf, placa, today, pageable);
            case "ATRASADA" -> rentals = rentalRepository.findAtrasadas(cpf, placa, today, pageable);
            default -> rentals = rentalRepository.findFiltered(cpf, placa, null, pageable);
        }

        return rentals.map(RentalResponse::fromEntityBasic);
    }

    public Rental findRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Locação não encontrada."));
    }

    private Vehicle findVehicle(String placa) {
        return vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    private Customer findCustomer(String cpf) {
        return customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    private void validateVehicleAvailability(Vehicle vehicle){
        if (vehicle.getStatus() != VehicleStatus.DISPONIVEL) throwBadRequest("Veículo não disponível para aluguel.");
    }

    private void validateDates(LocalDate start, LocalDate end){
        if (!end.isAfter(start)) throwBadRequest("A data final deve ser posterior à data inicial.");
    }

    private void validateRentalEditable(Rental rental){
        if (rental.isReturned()) throwBadRequest("Não é possível editar uma locação já devolvida.");
        if (!rental.getStartDate().isAfter(LocalDate.now().minusDays(1))) throwBadRequest("Não é possível alterar uma locação já iniciada.");
    }

    private void updateVehicle(Rental rental, String placa){
        Vehicle oldVehicle = rental.getVehicle();
        Vehicle newVehicle = findVehicle(placa);
        validateVehicleAvailability(newVehicle);

        oldVehicle.setStatus(VehicleStatus.DISPONIVEL);
        vehicleRepository.save(oldVehicle);

        newVehicle.setStatus(VehicleStatus.ALUGADO);
        rental.setVehicle(newVehicle);
    }

    private BigDecimal calculatePrice(BigDecimal valorDiario, LocalDate start, LocalDate end){
        long dias = ChronoUnit.DAYS.between(start, end);
        return valorDiario.multiply(BigDecimal.valueOf(dias)).setScale(2, RoundingMode.HALF_UP);
    }

    private void throwBadRequest(String msg){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}
