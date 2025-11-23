package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.*;
import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import com.nozama.aluguel_veiculos.dto.VehicleFilter;
import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import com.nozama.aluguel_veiculos.dto.VehicleResponse;
import com.nozama.aluguel_veiculos.repository.*;
import com.nozama.aluguel_veiculos.specification.VehicleSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CategoryRepository categoryRepository;
    private final InsuranceRepository insuranceRepository;
    private final BrandRepository brandRepository;
    private final ColorRepository colorRepository;
    private final ModelRepository modelRepository;

    public Vehicle create(VehicleRequest request){
        Category category = findCategory(request.idCategoria());
        Insurance insurance = findOptional(insuranceRepository, request.idSeguro());
        Brand brand = findOptional(brandRepository, request.idMarca());
        Color color = findOptional(colorRepository, request.idCor());
        Model model = findOptional(modelRepository, request.idModelo());

        return vehicleRepository.save(new Vehicle(request, category, insurance, brand, color, model));
    }

    public List<Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    public Vehicle findById(String placa){
        return vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    public List<VehicleResponse> searchVehicles(
            String placa, String categoria, String brand, String model,
            String color, Integer ano, String status) {

        VehicleFilter filter = new VehicleFilter();
        filter.setPlaca(placa);
        filter.setIdCategoria(parseInteger(categoria));
        filter.setIdMarca(parseInteger(brand));
        filter.setIdModelo(parseInteger(model));
        filter.setIdCor(parseInteger(color));
        filter.setAno(ano);

        if (status != null && !status.isEmpty()) {
            try {
                filter.setStatus(VehicleStatus.fromString(status));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + status);
            }
        }

        Specification<Vehicle> spec = VehicleSpecification.filter(filter);
        List<Vehicle> vehicles = vehicleRepository.findAll(spec);

        if (vehicles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículos não encontrados.");
        }

        return vehicles.stream().map(VehicleResponse::fromEntity).toList();
    }

    public List<VehicleResponse> findByStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O status é obrigatório.");
        }

        VehicleStatus statusEnum;
        try {
            statusEnum = VehicleStatus.fromString(status);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + status);
        }

        List<Vehicle> vehicles = vehicleRepository.findByStatus(statusEnum);

        if (vehicles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum veículo encontrado com status: " + status);
        }

        return vehicles.stream()
                .map(VehicleResponse::fromEntity)
                .toList();
    }

    @Transactional
    public Vehicle updateVehicle(String placa, VehicleRequest.update request) {
        Vehicle vehicle = findById(placa);

        if (request.status() != null) vehicle.setStatus(VehicleStatus.valueOf(request.status().toUpperCase()));
        if (request.descricao() != null) vehicle.setDescricao(request.descricao());
        if (request.ano() != null) vehicle.setAno(request.ano());
        if (request.idCategoria() != null) vehicle.setCategory(findCategory(request.idCategoria()));
        if (request.idMarca() != null) vehicle.setBrand(findBrand(request.idMarca()));
        if (request.idCor() != null) vehicle.setColor(findColor(request.idCor()));
        if (request.idModelo() != null) vehicle.setModel(findModel(request.idModelo()));
        if (request.idSeguro() != null) vehicle.setInsurance(findInsurance(request.idSeguro()));

        return vehicleRepository.save(vehicle);
    }

    public void delete(String placa){
        if (!vehicleRepository.existsById(placa)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado.");
        }
        vehicleRepository.deleteById(placa);
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetro inválido: " + value);
        }
    }

    private <T, ID> T findOptional(JpaRepository<T, ID> repo, ID id) {
        if (id == null) return null;
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado."));
    }

    private Category findCategory(Integer id) { return findOptional(categoryRepository, id); }
    private Brand findBrand(Integer id) { return findOptional(brandRepository, id); }
    private Color findColor(Integer id) { return findOptional(colorRepository, id); }
    private Model findModel(Integer id) { return findOptional(modelRepository, id); }
    private Insurance findInsurance(Long id) { return findOptional(insuranceRepository, id); }

}
