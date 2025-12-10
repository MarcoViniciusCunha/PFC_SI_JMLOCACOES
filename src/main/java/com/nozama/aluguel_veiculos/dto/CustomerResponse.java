package com.nozama.aluguel_veiculos.dto;

import com.nozama.aluguel_veiculos.domain.Customer;

public record CustomerResponse(
        Long id,
        String nome,
        String cpfMasked,
        String cnhMasked,
        String email,
        String telefone,
        String rua,
        String cidade,
        String estado,
        String cep,
        String numero,
        String data_nasc,
        boolean ativo   // 👈 novo campo
) {
    public static CustomerResponse from(Customer c){
        return new CustomerResponse(
                c.getId(),
                c.getNome(),
                maskCpf(c.getCpf()),
                maskCnh(c.getCnh()),
                c.getEmail(),
                c.getTelefone(),
                c.getRua(),
                c.getCidade(),
                c.getEstado(),
                c.getCep(),
                c.getNumero(),
                c.getData_nasc() != null ? c.getData_nasc().toString() : null,
                c.isAtivo()
        );
    }

    private static String maskCpf(String cpf) {
        if (cpf == null) return null;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11) return cpf;

        String middle = cpf.substring(6, 9);
        String last = cpf.substring(9);
        return "***.***." + middle + "-" + last;
    }

    public static String maskCnh(String cnh){
        if(cnh == null || cnh.length() < 4) return null;
        String last4 = cnh.substring(cnh.length()-4);
        return "******" + last4;
    }
}
