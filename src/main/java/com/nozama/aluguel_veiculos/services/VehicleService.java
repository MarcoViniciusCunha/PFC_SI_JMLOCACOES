package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.category.Category;
import com.nozama.aluguel_veiculos.domain.insurence.Insurance;
import com.nozama.aluguel_veiculos.domain.vehicle.Vehicle;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.repository.CategoryRepository;
import com.nozama.aluguel_veiculos.repository.InsuranceRepository;
import com.nozama.aluguel_veiculos.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;
    private final InsuranceRepository insuranceRepository;

    public VehicleService(VehicleRepository vehicleRepository, CategoryRepository categoryRepository, InsuranceRepository insuranceRepository) {
        this.vehicleRepository = vehicleRepository;
        this.categoryRepository = categoryRepository;
        this.insuranceRepository = insuranceRepository;
    }

    public Vehicle create(VehicleRequest request){
        Category category = categoryRepository.findById(request.idCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));

        Insurance insurance = null;
        if (request.idSeguro() != null){
            insurance = insuranceRepository.findById(request.idSeguro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado"));
        }

        Vehicle vehicle = new Vehicle(request, category, insurance);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    public Vehicle findById(String placa){
        return vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    public void delete(String placa){
        if (!vehicleRepository.existsById(placa)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado.");
        }
        vehicleRepository.deleteById(placa);
    }

}
