package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Inspection;
import com.nozama.aluguel_veiculos.domain.Rental;
import com.nozama.aluguel_veiculos.dto.InspectionRequest;
import com.nozama.aluguel_veiculos.repository.InspectionRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InspectionService {

    private final InspectionRepository inspectionRepository;
    private final RentalRepository rentalRepository;

    public Inspection create(InspectionRequest request) {
        Rental rental = findRental(request.rentalId());
        return inspectionRepository.save(new Inspection(rental, request));
    }

    public Inspection findById(Long id){
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com id: " + id));
    }

    public void delete(Long id) {
        inspectionRepository.delete(findById(id));
    }

    public Inspection update(Inspection inspection, InspectionRequest.update request) {
        if (request.rentalId() != null && !request.rentalId().equals(inspection.getRental().getId())) {
            inspection.setRental(findRental(request.rentalId()));
        }
        if (request.data_inspecao() != null) inspection.setData_inspecao(request.data_inspecao());
        if (request.descricao() != null) inspection.setDescricao(request.descricao());
        if (request.danificado() != null) inspection.setDanificado(request.danificado());

        return inspectionRepository.save(inspection);
    }

    private Rental findRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com id:" + id));
    }

}
