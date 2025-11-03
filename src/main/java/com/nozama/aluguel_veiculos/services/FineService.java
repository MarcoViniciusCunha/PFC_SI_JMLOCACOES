package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Fine;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.FineRequest;
import com.nozama.aluguel_veiculos.repository.FineRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


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
            LocalDate dataBusca = request.dataMulta() != null ? request.dataMulta() : fine.getData_multa();

            Rental rental = rentalRepository.findRentalByVehiclePlateAndDate(
                    request.placa(),
                    dataBusca
            ).orElseThrow(() -> new RuntimeException(
                    "Locação não encontrada com o veículo de placa " + request.placa()
                            + " na data de " + dataBusca
            ));

            fine.setRental(rental);
        }

        return repository.save(fine);
    }

}