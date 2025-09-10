package com.nozama.aluguel_veiculos.domain.customer;

import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cnh;

    private String nome;

    private String cpf;

    private String email;

    private String telefone;

    private String endereco;

    private LocalDate data_nasc;

    public Customer (CustomerRequest request) {
        this.cnh = request.cnh();
        this.nome = request.nome();
        this.cpf = request.cpf();
        this.email = request.email();
        this.telefone = request.telefone();
        this.endereco = request.endereco();
        this.data_nasc = request.data_nasc();
    }
}
