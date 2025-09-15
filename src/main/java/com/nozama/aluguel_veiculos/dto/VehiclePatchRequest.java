package com.nozama.aluguel_veiculos.dto;

public record VehiclePatchRequest(
        Integer idMark,
        String cor,
        String status,
        String descricao,
        Integer idCategoria,
        Integer idSeguro
) {
}
