package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.FineRequest;
import com.nozama.aluguel_veiculos.repository.FineRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository repository;
    private final RentalRepository rentalRepository;

    public Fine create(FineRequest request) {
        Rental rental = rentalRepository.findRentalByVehiclePlateAndDate(
                request.placa(),
                request.dataMulta()
                )
                .orElseThrow(() -> new RuntimeException("Locação não encontrada com o veículo de placa " + request.placa()
                        + " na data de " + request.dataMulta()
                ));

        Fine fine = new Fine(rental, request);

        return repository.save(fine);
    }

    public Fine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa não encontrada com id: " + id));
    }

    public void delete(Long id) {
        Fine fine = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa não encontrada com id: " + id));
        repository.delete(fine);
    }
}