package com.nozama.aluguel_veiculos.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RentalStatus {
    NAO_INICIADA("Não iniciada"),
    ATIVA("Ativa"),
    ATRASADA("Atrasada"),
    DEVOLVIDA("Devolvida");

    private final String displayName;

    RentalStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static RentalStatus fromString(String value) {
        for (RentalStatus status : RentalStatus.values()) {
            if (status.name().equalsIgnoreCase(value) ||
                    status.getDisplayName().equalsIgnoreCase(value)) {

                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + value);
    }
}
