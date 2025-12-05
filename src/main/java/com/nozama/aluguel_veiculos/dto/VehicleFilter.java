package com.nozama.aluguel_veiculos.dto;

import com.nozama.aluguel_veiculos.domain.enums.VehicleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleFilter {
    private String placa;
    private Integer idMarca;
    private Integer idModelo;
    private Integer ano;
    private Integer idCor;
    private VehicleStatus status;
    private Integer idCategoria;
    private Integer idSeguro;
}
