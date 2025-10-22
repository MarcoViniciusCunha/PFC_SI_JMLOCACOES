package com.nozama.aluguel_veiculos.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Location location;

    private LocalDate data;

    private Double valor;

    private String formaPagto;

    private String status;

    // Getters e Setters
}
