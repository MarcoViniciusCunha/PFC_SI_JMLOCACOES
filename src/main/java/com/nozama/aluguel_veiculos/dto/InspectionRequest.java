package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InspectionRequest(
        @NotNull(message = "Informe o id da locação.")
        Long rentalId,
        @NotNull(message = "Informe a data da inspeção.")
        LocalDate data_inspecao,
        @NotBlank(message = "Informe a descrição")
        String descricao,
        boolean danificado
) {
    public record update(Long rentalId, LocalDate data_inspecao, String descricao, Boolean danificado){}
}
