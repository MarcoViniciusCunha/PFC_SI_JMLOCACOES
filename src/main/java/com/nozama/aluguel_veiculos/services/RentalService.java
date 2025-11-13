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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public RentalService(RentalRepository rentalRepository, VehicleRepository vehicleRepository, CustomerRepository customerRepository){
        this.rentalRepository =  rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Rental create(RentalRequest request){
        Vehicle vehicle = findVehicle(request.placa());

        if (vehicle.getStatus() != VehicleStatus.DISPONIVEL){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veículo não disponível para aluguel.");
        }

        if (!request.endDate().isAfter(request.startDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data final deve ser posterior à data inicial");
        }

        long dias = ChronoUnit.DAYS.between(request.startDate(), request.endDate());

        BigDecimal valorDiario = vehicle.getValorDiario();
        BigDecimal valorTotal = valorDiario.multiply(BigDecimal.valueOf(dias));

        vehicle.setStatus(VehicleStatus.ALUGADO);
        Customer customer = findCustomer(request.cpf());

        Rental rental = new Rental(vehicle, customer, request);
        rental.setPrice(valorTotal);

        Rental saved = rentalRepository.save(rental);
        vehicleRepository.save(vehicle);
        return saved;
    }

    @Transactional
    public Rental returnVehicle(Long id){
        Rental rental = findRentalById(id);
        if (rental.isReturned()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veículo já foi devolvido.");
        }

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

        if (rental.isReturned()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível editar uma locação já devolvida.");
        }

        if (!rental.getStartDate().isAfter(LocalDate.now().minusDays(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível alterar uma locação já iniciada.");
        }

        if (request.placa() != null) {
            Vehicle oldVehicle = rental.getVehicle();
            Vehicle newVehicle = findVehicle(request.placa());

            if (newVehicle.getStatus() != VehicleStatus.DISPONIVEL) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veículo não disponível para aluguel.");
            }

            oldVehicle.setStatus(VehicleStatus.DISPONIVEL);
            vehicleRepository.save(oldVehicle);

            newVehicle.setStatus(VehicleStatus.ALUGADO);
            rental.setVehicle(newVehicle);
        }

        if (request.cpf() != null) {
            Customer customer = findCustomer(request.cpf());
            rental.setCustomer(customer);
        }

        if (request.startDate() != null) rental.setStartDate(request.startDate());
        if (request.endDate() != null) rental.setEndDate(request.endDate());

        if (rental.getStartDate() != null && rental.getEndDate() != null) {
            if (!rental.getEndDate().isAfter(rental.getStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data final deve ser posterior à data inicial.");
            }

            long dias = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
            BigDecimal valorTotal = rental.getVehicle().getValorDiario()
                    .multiply(BigDecimal.valueOf(dias))
                    .setScale(2, RoundingMode.HALF_UP);

            rental.setPrice(valorTotal);
        }

        return rentalRepository.save(rental);
    }


    public void deleteById(Long id){
        Rental rental = findRentalById(id);

        if (!rental.getStartDate().isAfter(LocalDate.now())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível deletar uma locação já iniciada.");
        }
        if (!rental.isReturned()){
            Vehicle vehicle = rental.getVehicle();
            vehicle.setStatus(VehicleStatus.DISPONIVEL);
        }

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

        if (status != null) {
            switch (status.toUpperCase()) {
                case "DEVOLVIDA":
                    rentals = rentalRepository.findFiltered(cpf, placa, true, pageable );
                    break;
                case "ATIVA":
                    rentals = rentalRepository.findAtivas(cpf, placa, today, pageable );
                    break;
                case "ATRASADA" :
                    rentals = rentalRepository.findAtrasadas(cpf, placa, today, pageable );
                    break;
                default:
                    rentals = rentalRepository.findFiltered(cpf, placa, null, pageable );
            }
        } else {
            rentals = rentalRepository.findFiltered(cpf, placa, null, pageable );
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
}
