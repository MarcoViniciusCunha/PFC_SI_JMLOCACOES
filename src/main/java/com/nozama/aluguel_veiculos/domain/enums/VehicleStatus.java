package com.nozama.aluguel_veiculos.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VehicleStatus {
    DISPONIVEL("Disponível"),
    ALUGADO("Alugado"),
    MANUTENCAO("Manutenção");

    private final String displayName;

    VehicleStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

}
