package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleRequest(
        @NotBlank(message = "Informe a placa do veículo.")
        String placa,

        @NotNull(message = "Informe a marca do veículo.")
        Integer idMarca,

        @NotNull(message = "Informe a marca do veículo.")
        Integer idModelo,

        @NotNull(message = "Informe o ano do veículo.")
        Integer ano,

        @NotNull(message = "Informe a cor do veículo.")
        Integer idCor,

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
