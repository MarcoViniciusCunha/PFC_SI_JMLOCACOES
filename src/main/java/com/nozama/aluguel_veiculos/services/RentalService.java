package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final VehicleService vehicleService;

    public RentalService(RentalRepository rentalRepository, VehicleRepository vehicleRepository, CustomerRepository customerRepository, VehicleService vehicleService){
        this.rentalRepository =  rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
        this.vehicleService = vehicleService;
    }

    @Transactional
    public Rental create(RentalRequest request){
        Vehicle vehicle = vehicleRepository.findById(request.placa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));

        if (vehicle.getStatus() != VehicleStatus.DISPONIVEL){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veículo não disponível para aluguel.");
        }

        vehicle.setStatus(VehicleStatus.ALUGADO);

        Customer customer = customerRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));

        Rental rental = new Rental(vehicle, customer, request);

        Rental saved = rentalRepository.save(rental);

        vehicleRepository.save(vehicle);
        return saved;
    }

    @Transactional
    public Rental returnVehicle(Long id){
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aluguel não encontrado."));

        if (rental.isReturned()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veículo já foi devolvido.");
        }

        rental.setReturned(true);
        rental.setReturnDate(LocalDate.now());

        Vehicle vehicle = rental.getVehicle();
        vehicle.setStatus(VehicleStatus.DISPONIVEL);

        return rentalRepository.save(rental);
    }
}
