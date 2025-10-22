package com.nozama.aluguel_veiculos.domain;

import jakarta.persistence.*;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;

    private String veiculo;

    // Outros campos relevantes da locação...

    // Getters e Setters
}
