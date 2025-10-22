package com.nozama.aluguel_veiculos.domain;

import jakarta.persistence.*;

@Entity
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Location location;

    @OneToOne
    private Fine fine;

    @OneToOne
    private Payment payment;

    private String status;
}
