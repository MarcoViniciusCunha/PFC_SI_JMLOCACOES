package com.nozama.aluguel_veiculos.domain;

import jakarta.persistence.*;

@Entity
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placaVeiculo;

    private Double valor;

    @ManyToOne
    private Location location;

    private Integer diasAtraso;

    // Getters e Setters
}
