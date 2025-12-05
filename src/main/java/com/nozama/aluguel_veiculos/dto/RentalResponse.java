package com.nozama.aluguel_veiculos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nozama.aluguel_veiculos.domain.Rental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RentalResponse(
        Long id,
        String placa,
        String modelo,
        String vehicleMarca,
        Integer vehicleAno,
        String customerName,
        String customerCnh,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate returnedDate,
        BigDecimal price,
        boolean returned,
        String status,
        List<FineResponse> fines,
        List<InspectionResponse> inspections,
        List<PaymentResponse> payments
) {

    public static RentalResponse fromEntity(Rental rental) {

        List<FineResponse> fineResponses = rental.getFines().stream()
                .map(FineResponse::fromEntitySummary)
                .toList();

        List<InspectionResponse> getInspections = rental.getInspections().stream()
                .map(InspectionResponse::fromEntitySummary)
                .toList();

        List<PaymentResponse> paymentResponses  = rental.getPayments().stream()
                .map(PaymentResponse::fromEntitySummary)
                .toList();

        return new RentalResponse(
                rental.getId(),
                rental.getVehicle().getPlaca(),
                rental.getVehicle().getModel().getNome(),
                rental.getVehicle().getModel().getBrand().getNome(),
                rental.getVehicle().getAno(),
                rental.getCustomer().getNome(),
                CustomerResponse.maskCnh(rental.getCustomer().getCnh()),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getReturnDate(),
                rental.getPrice(),
                rental.isReturned(),
                rental.getStatus().getDisplayName(),
                fineResponses,
                getInspections,
                paymentResponses
        );
    }

    public static RentalResponse fromEntityBasic(Rental rental) {
        return new RentalResponse(
                rental.getId(),
                rental.getVehicle().getPlaca(),
                rental.getVehicle().getModel().getNome(),
                null, // vehicleMarca
                null, // vehicleAno
                rental.getCustomer().getNome(),
                null, // customerCnh
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getReturnDate(),
                rental.getPrice(),
                rental.isReturned(),
                rental.getStatus().getDisplayName(),
                null, // fines
                null, // inspections
                null  // payments
        );
    }

    private static String calcularStatus(boolean returned, LocalDate endDate) {
        if (returned) return "DEVOLVIDA";
        if (endDate.isBefore(LocalDate.now())) return "ATRASADA";
        return "ATIVA";
    }

    public static String calcularStatus(Rental rental) {
        return calcularStatus(rental.isReturned(), rental.getEndDate());
    }
}
