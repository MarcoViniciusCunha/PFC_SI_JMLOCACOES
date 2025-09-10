package com.nozama.aluguel_veiculos.dto;
import java.time.LocalDate;

public record CustomerPatchRequest(
        String cnh,

        String nome,

        String cpf,

        String email,

        String telefone,

        String endereco,

        LocalDate data_nasc
) {
}
