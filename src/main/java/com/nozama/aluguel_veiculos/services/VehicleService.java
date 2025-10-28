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
        // Busca a categoria pelo ID enviado na requisição
        Category category = categoryRepository.findById(request.idCategoria())
                // Se não encontrar, lança erro 404 (Categoria não encontrada)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));

        // Declara o seguro como nulo inicialmente
        Insurance insurance = null;
        // Se o ID do seguro for informado, busca no banco de dados
        if (request.idSeguro() != null){
            insurance = insuranceRepository.findById(request.idSeguro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado"));
        }

        // Declara a marca como nula inicialmente
        Brand mark = null;
        // Busca a marca se o ID for informado
        if (request.idMarca() != null){
            mark = markRepository.findById(request.idMarca())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca não encontrado"));
        }

        // Declara a cor como nula inicialmente
        Color color = null;
        // Busca a cor se o ID for informado
        if (request.idCor() != null){
            color = colorRepository.findById(request.idCor())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor não encontrado"));
        }

        // Declara o modelo como nulo inicialmente
        Model model = null;
        // Busca o modelo se o ID for informado
        if (request.idModelo() != null){
            model = modelRepository.findById(request.idModelo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo não encontrado"));
        }

        // Cria o objeto veículo com os dados validados e relacionados
        Vehicle vehicle = new Vehicle(request, category, insurance, mark, color, model);
        // Salva o veículo no banco de dados
        return vehicleRepository.save(vehicle);
    }

    // Retorna todos os veículos cadastrados no sistema
    public List<Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    // Busca um veículo específico pela placa
    public Vehicle findById(String placa){
        return vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    // Pesquisa veículos com filtros dinâmicos (marca, modelo, categoria etc.)
    public List<Vehicle> searchVehicles(VehicleFilter filter) {
        // Pesquisa veículos com filtros dinâmicos (marca, modelo, categoria etc.)
        Specification<Vehicle> spec = VehicleSpecification.filter(filter);
        // Busca todos os veículos que correspondem à especificação
        List<Vehicle> vehicles = vehicleRepository.findAll(spec);

        if (vehicles.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículos não encontrados.");
        }
        return vehicles;
    }

    // Atualiza os dados de um veículo específico
    @Transactional // Garante que a atualização seja feita de forma segura no banco de dados
    public Vehicle updateVehicle(String placa, VehiclePatchRequest request) {
        // Busca o veículo pela placa
        Vehicle vehicle = vehicleRepository.findById(placa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));

        // Atuazila Status, se enviado
        if (request.status() != null){
            vehicle.setStatus(VehicleStatus.valueOf(request.status().toUpperCase()));
        }
        // Atualiza a descrição, se enviada
        if (request.descricao() != null){
            vehicle.setDescricao(request.descricao());
        }
        // Atualiza a categoria, se enviada
        if (request.idCategoria() != null){
            Category category = categoryRepository.findById(request.idCategoria())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
            vehicle.setCategory(category);
        }
        // Atualiza a marca, se enviada
        if (request.idMarca() != null){
            Brand mark = markRepository.findById(request.idMarca())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca não encontrada."));
            vehicle.setBrand(mark);
        }
        // Atualiza o seguro, se enviado
        if (request.idSeguro() != null){
            Insurance insurance = insuranceRepository.findById(request.idSeguro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seguro não encontrado."));
            vehicle.setInsurance(insurance);
        }
        // Atualiza a cor, se enviada
        if (request.idCor() != null){
            Color color = colorRepository.findById(request.idCor())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cor não encontrada."));
            vehicle.setColor(color);
        }
        // Atualiza o modelo, se enviado
        if (request.idModelo() != null){
            Model model = modelRepository.findById(request.idModelo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo não encontrado."));
            vehicle.setModel(model);
        }
        // Atualiza o ano, se enviado
        if (request.ano() != null){
            vehicle.setAno(request.ano());
        }
        // Salva as alterações no banco
        return vehicleRepository.save(vehicle);
    }

    // Deleta um veículo do banco de dados
    public void delete(String placa){
        // Verifica se o veículo existe antes de tentar deletar
        if (!vehicleRepository.existsById(placa)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado.");
        }
        // Deleta o registro pelo ID (placa)
        vehicleRepository.deleteById(placa);
    }

}
