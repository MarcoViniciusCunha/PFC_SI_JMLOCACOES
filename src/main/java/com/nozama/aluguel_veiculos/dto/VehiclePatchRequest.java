package com.nozama.aluguel_veiculos.dto;

public record VehiclePatchRequest(
        Integer idMark,
        Integer idCor,
        Integer idModelo,
        String status,
        String descricao,
        Integer idCategoria,
        Integer idSeguro
) {
}
