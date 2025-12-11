package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.domain.Toll;
import com.nozama.aluguel_veiculos.dto.TollRequest;
import com.nozama.aluguel_veiculos.dto.TollResponse;
import com.nozama.aluguel_veiculos.dto.TollUpdateRequest;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.repository.TollRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TollService {
    private final TollRepository tollRepository;
    private final RentalRepository rentalRepository;

    public TollResponse create(TollRequest request) {

        Rental rental = rentalRepository.findRentalByVehiclePlateAndDateTime(request.placa(), request.toDateTime())
                .orElseThrow(() -> new EntityNotFoundException("Locação não encontrada"));

        Toll toll = Toll.builder()
                .rodovia(request.rodovia())
                .cidade(request.cidade())
                .valor(request.valor())
                .date(request.toDateTime())
                .rental(rental)
                .build();

        tollRepository.save(toll);

        return toResponse(toll);
    }

    public TollResponse update(Long id, TollUpdateRequest request) {
        Toll toll = tollRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedágio não encontrado"));

        toll.setRodovia(request.rodovia());
        toll.setCidade(request.cidade());
        toll.setValor(request.valor());
        toll.setDate(request.toDateTime());

        tollRepository.save(toll);

        return toResponse(toll);
    }

    public void delete(Long id) {
        if (!tollRepository.existsById(id)) {
            throw new EntityNotFoundException("Pedágio não encontrado");
        }
        tollRepository.deleteById(id);
    }

    public TollResponse getById(Long id) {
        Toll toll = tollRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedágio não encontrado"));
        return toResponse(toll);
    }

    private TollResponse toResponse(Toll toll) {
        return new TollResponse(
                toll.getId(),
                toll.getRodovia(),
                toll.getCidade(),
                toll.getValor(),
                toll.getDate()
        );
    }
}
