package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.*;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.VehicleFilter;
import com.nozama.aluguel_veiculos.dto.VehiclePatchRequest;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.repository.*;
import com.nozama.aluguel_veiculos.specification.VehicleSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;
    private final InsuranceRepository insuranceRepository;
    private final BrandRepository markRepository;
    private final ColorRepository colorRepository;
    private final ModelRepository modelRepository;

    public VehicleService(VehicleRepository vehicleRepository, CategoryRepository categoryRepository, InsuranceRepository insuranceRepository, BrandRepository markRepository, ColorRepository colorRepository, ModelRepository modelRepository) {
        this.vehicleRepository = vehicleRepository;
        this.categoryRepository = categoryRepository;
        this.insuranceRepository = insuranceRepository;
        this.markRepository = markRepository;
        this.colorRepository = colorRepository;
        this.modelRepository = modelRepository;
    }

    public Vehicle create(VehicleRequest request){
        Category category = categoryRepository.findById(request.idCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));

        Insurance insurance = null;
        if (request.idSeguro() != null){
            insurance = insuranceRepository.findById(request.idSeguro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado"));
        }

        Brand mark = null;
        if (request.idMarca() != null){
            mark = markRepository.findById(request.idMarca())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca não encontrado"));
        }

        Color color = null;
        if (request.idCor() != null){
            color = colorRepository.findById(request.idCor())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor não encontrado"));
        }

        Model model = null;
        if (request.idModelo() != null){
            model = modelRepository.findById(request.idModelo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo não encontrado"));
        }

        Vehicle vehicle = new Vehicle(request, category, insurance, mark, color, model);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    public Vehicle findById(String placa){
        return vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    public List<Vehicle> searchVehicles(VehicleFilter filter) {
        Specification<Vehicle> spec = VehicleSpecification.filter(filter);
        List<Vehicle> vehicles = vehicleRepository.findAll(spec);

        if (vehicles.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículos não encontrados.");
        }
        return vehicles;
    }

    @Transactional
    public Vehicle updateVehicle(String placa, VehiclePatchRequest request) {
        Vehicle vehicle = vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));

        if (request.status() != null){
            vehicle.setStatus(VehicleStatus.valueOf(request.status().toUpperCase()));
        }
        if (request.descricao() != null){
            vehicle.setDescricao(request.descricao());
        }
        if (request.idCategoria() != null){
            Category category = categoryRepository.findById(request.idCategoria())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
            vehicle.setCategory(category);
        }
        if (request.idMarca() != null){
            Brand mark = markRepository.findById(request.idMarca())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca não encontrada."));
            vehicle.setBrand(mark);
        }
        if (request.idSeguro() != null){
            Insurance insurance = insuranceRepository.findById(request.idSeguro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado."));
            vehicle.setInsurance(insurance);
        }
        if (request.idCor() != null){
            Color color = colorRepository.findById(request.idCor())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor não encontrada."));
            vehicle.setColor(color);
        }
        if (request.idModelo() != null){
            Model model = modelRepository.findById(request.idModelo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo não encontrado."));
            vehicle.setModel(model);
        }
        if (request.ano() != null){
            vehicle.setAno(request.ano());
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
