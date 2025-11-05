package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.FineRequest;
import com.nozama.aluguel_veiculos.repository.FineRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository repository;
    private final RentalRepository rentalRepository;

    public Fine create(FineRequest request) {
        String placa = request.placa();
        LocalDate dataMulta = request.dataMulta();
        Rental rental = findRental(placa, dataMulta);

        return repository.save(new Fine(rental, request));
    }

    public Fine findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Multa não encontrada com id: " + id));
    }

    public void delete(Long id) {
        Fine fine = findById(id);
        repository.delete(fine);
    }

    public Fine update(Fine fine, FineRequest.update request) {
        if (request.dataMulta() != null && !request.dataMulta().equals(fine.getData_multa())) {
            fine.setData_multa(request.dataMulta());
        }
        if (request.descricao() != null && !request.descricao().equals(fine.getDescricao())) {
            fine.setDescricao(request.descricao());
        }
        if (request.valor() != null && !request.valor().equals(fine.getValor())) {
            fine.setValor(request.valor());
        }
        if (request.placa() != null && !request.placa().equals(fine.getRental().getVehicle().getPlaca())) {
            LocalDate dataMulta = request.dataMulta() != null ? request.dataMulta() : fine.getData_multa();
            String placa = request.placa();

            Rental rental = findRental(placa, dataMulta);

            fine.setRental(rental);
        }

        return repository.save(fine);
    }

    private Rental findRental(String placa, LocalDate dataMulta) {
        return rentalRepository.findRentalByVehiclePlateAndDate(
                        placa,
                        dataMulta
                )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Locação não encontrada com o veículo de placa " + placa
                        + " na data de " + dataMulta
                ));
    }
}