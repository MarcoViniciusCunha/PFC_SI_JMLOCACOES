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
                             boolean returned) {
}
