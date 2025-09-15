package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.category.Category;
import com.nozama.aluguel_veiculos.domain.insurence.Insurance;
import com.nozama.aluguel_veiculos.domain.vehicle.Vehicle;
import com.nozama.aluguel_veiculos.dto.VehiclePatchRequest;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.repository.CategoryRepository;
import com.nozama.aluguel_veiculos.repository.InsuranceRepository;
import com.nozama.aluguel_veiculos.repository.MarkRepository;
import com.nozama.aluguel_veiculos.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.nozama.aluguel_veiculos.domain.mark.Mark;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;
    private final InsuranceRepository insuranceRepository;
    private final MarkRepository markRepository;

    public VehicleService(VehicleRepository vehicleRepository, CategoryRepository categoryRepository, InsuranceRepository insuranceRepository, MarkRepository markRepository) {
        this.vehicleRepository = vehicleRepository;
        this.categoryRepository = categoryRepository;
        this.insuranceRepository = insuranceRepository;
        this.markRepository = markRepository;
    }

    public Vehicle create(VehicleRequest request){
        Category category = categoryRepository.findById(request.idCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));

        Insurance insurance = null;
        if (request.idSeguro() != null){
            insurance = insuranceRepository.findById(request.idSeguro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado"));
        }

        Mark mark = null;
        if (request.idMark() != null){
            mark = markRepository.findById(request.idMark())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca não encontrado"));
        }

        Vehicle vehicle = new Vehicle(request, category, insurance, mark);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    public Vehicle findById(String placa){
        return vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    public List<Vehicle> findByCategory(String name) {
        List<Vehicle> vehicles = vehicleRepository.findByCategory_Nome(name);

        if (vehicles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículos não encontrados.");
        }

        return vehicles;
    }

    @Transactional
    public Vehicle updateVehicle(String placa, VehiclePatchRequest request) {
        Vehicle vehicle = vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));

        if (request.cor() != null){
            vehicle.setCor(request.cor());
        }
        if (request.status() != null){
            vehicle.setStatus(request.status());
        }
        if (request.descricao() != null){
            vehicle.setDescricao(request.descricao());
        }
        if (request.idCategoria() != null){
            Category category = categoryRepository.findById(request.idCategoria())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
            vehicle.setCategory(category);
        }
        if (request.idMark() != null){
            Mark mark = markRepository.findById(request.idMark())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca não encontrada."));
            vehicle.setMark(mark);
        }
        if (request.idSeguro() != null){
            Insurance insurance = insuranceRepository.findById(request.idSeguro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado."));
            vehicle.setInsurance(insurance);
        }

        return vehicleRepository.save(vehicle);
    }

    public void delete(String placa){
        if (!vehicleRepository.existsById(placa)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado.");
        }
        vehicleRepository.deleteById(placa);
    }

}
