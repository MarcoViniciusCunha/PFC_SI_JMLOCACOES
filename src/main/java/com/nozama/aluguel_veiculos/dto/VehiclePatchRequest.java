package com.nozama.aluguel_veiculos.dto;

public record VehiclePatchRequest(
        Integer idMarca,
        Integer idCor,
        Integer idModelo,
        String status,
        String descricao,
        Integer idCategoria,
        Integer idSeguro,
        Integer ano
) {
}
