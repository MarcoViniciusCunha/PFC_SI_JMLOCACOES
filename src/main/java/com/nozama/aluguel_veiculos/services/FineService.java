package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.domain.Location;
import com.nozama.aluguel_veiculos.dto.FineRequest;
import com.nozama.aluguel_veiculos.repository.FineRepository;
import com.nozama.aluguel_veiculos.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository repository;
    private final LocationRepository locationRepository;

    public Fine create(FineRequest request) {
        Location location = locationRepository.findById(request.locationId())
                .orElseThrow(() -> new RuntimeException("Locação não encontrada com id: " + request.locationId()));

        Fine fine = new Fine();
        fine.setPlacaVeiculo(request.placaVeiculo());
        fine.setValor(request.valor());
        fine.setLocation(location);
        fine.setDiasAtraso(request.diasAtraso());

        return repository.save(fine);
    }

    public List<Fine> findAll() {
        return repository.findAll();
    }

    public Fine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa não encontrada com id: " + id));
    }

    public void delete(Long id) {
        Fine fine = findById(id);
        repository.delete(fine);
    }
}