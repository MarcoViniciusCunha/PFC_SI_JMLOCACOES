package com.nozama.aluguel_veiculos.dto;

public record VehiclePatchRequest(
        String cor,
        String status,
        String descricao,
        Integer idCategoria,
        Integer idSeguro
) {
}
