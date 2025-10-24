package com.nozama.aluguel_veiculos.dto;

import java.time.LocalDate;

public record RentalResponse(Long id,
                             String placa,
                             String modelo,
                             String customerName,
                             LocalDate startDate,
                             LocalDate endDate,
                             LocalDate returnedDate,
                             Double price,
                             boolean returned,
                             String status) {
    public RentalResponse(Long id,
                          String placa,
                          String modelo,
                          String customerName,
                          LocalDate startDate,
                          LocalDate endDate,
                          LocalDate returnedDate,
                          Double price,
                          boolean returned) {
        this(
                id,
                placa,
                modelo,
                customerName,
                startDate,
                endDate,
                returnedDate,
                price,
                returned,
                calcularStatus(returned, endDate));
    }

    private static String calcularStatus(boolean returned, LocalDate endDate) {
        if (returned) return "DEVOLVIDA";
        if (endDate.isBefore(LocalDate.now())) return "ATRASADA";
        return "ATIVA";
    }
}
