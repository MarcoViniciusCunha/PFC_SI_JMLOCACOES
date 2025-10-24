package com.nozama.aluguel_veiculos.dto;

import java.time.LocalDate;

public record RentalUpdateRequest(
        String placa,

        String cpf,

        LocalDate startDate,

        LocalDate endDate,

        Double price
) {
}
