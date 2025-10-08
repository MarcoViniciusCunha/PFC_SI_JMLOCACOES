package com.nozama.aluguel_veiculos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleFilter {
    String placa;

    Integer idMarca;

    Integer idModelo;

    Integer ano;

    Integer idCor;

    String status;

    Integer idCategoria;

    Integer idSeguro;
}
