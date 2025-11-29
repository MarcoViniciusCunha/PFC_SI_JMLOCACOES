package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.domain.converter.CryptoConverter;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.utils.HashUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cpf_hash"}),
                @UniqueConstraint(columnNames = {"cnh_hash"}),
                @UniqueConstraint(columnNames = {"email"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = CryptoConverter.class)
    private String cnh;

    @Column(name = "cnh_hash")
    private String cnhHash;

    private String nome;

    @Convert(converter = CryptoConverter.class)
    private String cpf;

    @Column(name = "cpf_hash")
    private String cpfHash;

    private String email;

    private String telefone;

    private String cep;
    private String numero;

    private String rua;
    private String cidade;
    private String estado;

    private LocalDate data_nasc;

    public Customer(CustomerRequest request) {
        this.cnh = request.cnh();
        this.nome = request.nome();
        this.cpf = request.cpf();
        this.email = request.email();
        this.telefone = request.telefone();
        this.cep = request.cep();
        this.numero = request.numero();
        this.rua = request.rua();
        this.cidade = request.cidade();
        this.estado = request.estado();
        this.data_nasc = request.data_nasc();

        this.cpfHash = HashUtils.hmacSha256Base64(this.cpf);
        this.cnhHash = HashUtils.hmacSha256Base64(this.cnh);
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
        this.cpfHash = cpf != null ? HashUtils.hmacSha256Base64(cpf) : null;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
        this.cnhHash = cnh != null ? HashUtils.hmacSha256Base64(cnh) : null;
    }
}
