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
        Long id = request.rentalId();
        Rental rental = findRental(id);
        Inspection inspection = new Inspection(rental, request);

        return inspectionRepository.save(inspection);
    }

    public Inspection findById(Long id){
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com id: " + id));
    }

    public void delete(Long id){
        Inspection inspection = findById(id);
        inspectionRepository.delete(inspection);
    }

    public Inspection update(Inspection inspection, InspectionRequest.update request) {
        if (request.rentalId() != null && !request.rentalId().equals(inspection.getRental().getId())) {
            Long id = request.rentalId();
            Rental rental = findRental(id);
            inspection.setRental(rental);
        }
        if (request.data_inspecao() != null && !request.data_inspecao().equals(inspection.getData_inspecao())) {
            inspection.setData_inspecao(request.data_inspecao());
        }
        if (request.descricao() != null && !request.descricao().equals(inspection.getDescricao())) {
            inspection.setDescricao(request.descricao());
        }
        if (request.danificado() != null && request.danificado() != inspection.isDanificado()) {
            inspection.setDanificado(request.danificado());
        }

        return inspectionRepository.save(inspection);
    }

    private Rental findRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspeção não encontrada com id:" + id));
    }

}
