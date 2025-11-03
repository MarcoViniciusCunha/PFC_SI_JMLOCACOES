package com.nozama.aluguel_veiculos.dto;

import com.nozama.aluguel_veiculos.domain.Inspection;

import java.time.LocalDate;

public record InspectionResponse(
        Long id,
        RentalInfo rental,
        LocalDate data_inspecao,
        String descricao,
        Boolean danificado
) {
    public static InspectionResponse fromEntity(Inspection inspection ) {
        RentalInfo rentalInfo = new RentalInfo(
                inspection.getRental().getId(),
                inspection.getRental().getVehicle().getPlaca(),
                inspection.getRental().getCustomer().getNome()
        );

        return new InspectionResponse(
                inspection.getId(),
                rentalInfo,
                inspection.getData_inspecao(),
                inspection.getDescricao(),
                inspection.isDanificado()
        );
    }

    public static InspectionResponse fromEntitySummary(Inspection inspection ) {
        RentalInfo rentalInfo = new RentalInfo(
                inspection.getRental().getId(),
                inspection.getRental().getVehicle().getPlaca(),
                inspection.getRental().getCustomer().getNome()
        );

        return new InspectionResponse(
                inspection.getId(),
                null,
                inspection.getData_inspecao(),
                inspection.getDescricao(),
                inspection.isDanificado()
        );
    }
}
