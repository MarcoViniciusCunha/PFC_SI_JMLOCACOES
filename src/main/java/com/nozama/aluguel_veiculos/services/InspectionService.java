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

    public Inspection create(InspectionRequest request){
        Rental rental = rentalRepository.findById(request.rentalId())
                .orElseThrow(() -> new RuntimeException("Locação não encontrada com id: " + request.rentalId()));

        Inspection inspection = new Inspection(rental, request);

        return inspectionRepository.save(inspection);
    }

    public Inspection findById(Long id){
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com id: " + id));
    }

    public void delete(Long id){
        Inspection inspection = inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com id: " + id));
        inspectionRepository.delete(inspection);
    }
}
