package com.nozama.aluguel_veiculos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CustomerRequest(

        @NotBlank(message = "Informe a cnh.")
        String cnh,

        @NotBlank(message = "Informe o nome.")
        String nome,

        @NotBlank(message = "Informe o cpf.")
        String cpf,

        @NotBlank(message = "Informe o email.")
        String email,

        @NotBlank(message = "Informe o telefone.")
        String telefone,

        @NotBlank(message = "Informe o endereço.")
        String cep,

        @NotBlank(message = "Informe o número da residência")
        String numero,

        String rua,

        String cidade,

        String estado,

        @NotNull(message = "Informe a data de nascimento.")
        LocalDate data_nasc

) {
    public record update(
            String cnh,
            String nome,
            String cpf,
            String email,
            String telefone,
            String cep,
            String numero,
            String rua,
            String cidade,
            String estado,
            LocalDate data_nasc
    ) {
    }

}
