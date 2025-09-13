package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleRequest(
        @NotBlank(message = "Informe a placa do veículo.")
        String placa,

        @NotBlank(message = "Informe a marca do veículo.")
        String marca,

        @NotBlank(message = "Informe o modelo do veículo.")
        String modelo,

        @NotNull(message = "Informe o ano do veículo.")
        Integer ano,

        @NotBlank(message = "Informe a cor do veículo.")
        String cor,

        @NotBlank(message = "Informe o status do veículo.")
        String status,

        @NotBlank(message = "De uma descrição mais detalhada sobre o veículo")
        String descricao,

        @NotNull(message = "Informe a categoria do veículo.")
        Integer idCategoria,

        @NotNull(message = "Informe o seguro do veículo.")
        Integer idSeguro
) {
}
