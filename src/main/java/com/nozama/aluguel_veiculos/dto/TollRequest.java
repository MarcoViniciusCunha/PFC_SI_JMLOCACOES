package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TollRequest(
        @NotBlank(message = "Informe a placa do veículo")
        String placa,

        @NotBlank(message = "A rodovia é obrigatória")
        String rodovia,

        @NotBlank(message = "A cidade é obrigatória")
        String cidade,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotBlank(message = "A data é obrigatória")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$",
                message = "A data deve seguir o formato yyyy-MM-ddTHH:mm"
        )
        String date

) {
    public LocalDateTime toDateTime() {
        return LocalDateTime.parse(date);
    }
}
