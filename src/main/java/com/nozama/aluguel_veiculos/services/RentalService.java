package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Vehicle;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.RentalRequest;
import com.nozama.aluguel_veiculos.dto.RentalResponse;
import com.nozama.aluguel_veiculos.dto.RentalUpdateRequest;
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

import java.time.LocalDate;
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

    @Transactional
    public Rental update(Long id, RentalUpdateRequest request){
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Locação não encontrada."));

        if (rental.isReturned()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível editar uma locação já devolvida.");
        }

        if (!rental.getStartDate().isAfter(LocalDate.now().minusDays(1))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível alterar uma locação já iniciada.");
        }

        if(request.startDate() != null) rental.setStartDate(request.startDate());
        if(request.endDate() != null) rental.setEndDate(request.endDate());
        if(request.price() != null) rental.setPrice(request.price());

        if (request.placa() != null){
            Vehicle oldVehicle = rental.getVehicle();

            Vehicle newVehicle = vehicleRepository.findById(request.placa())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veículo não encontrado."));

            if (newVehicle.getStatus() != VehicleStatus.DISPONIVEL){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Veículo não disponível para aluguel.");
            }

            oldVehicle.setStatus(VehicleStatus.DISPONIVEL);
            vehicleRepository.save(oldVehicle);

            newVehicle.setStatus(VehicleStatus.ALUGADO);

            rental.setVehicle(newVehicle);
        }

        if (request.cpf() != null){
            Customer customer = customerRepository.findByCpf(request.cpf())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente nâo encontrado."));
            rental.setCustomer(customer);
        }

        return rentalRepository.save(rental);
    }

    public void deleteById(Long id){
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Locação não encontrada."));

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


        Boolean returned = null;
        if (status != null) {
            switch (status.toUpperCase()) {
                case "DEVOLVIDA" -> returned = true;
                case "ATIVA" -> returned = false;
            }
        }

        Page<Rental> rentals = rentalRepository.findFiltered(cpf, placa, returned, pageable);

        // Mapeia cada Rental para RentalResponse completo
        List<RentalResponse> filtered = rentals.stream()
                .map(RentalResponse::fromEntityBasic)
                .filter(rr -> status == null || rr.status().equalsIgnoreCase(status))
                .toList();

        return new PageImpl<>(filtered, pageable, filtered.size());
    }
}
