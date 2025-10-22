package com.nozama.aluguel_veiculos.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Return {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Location locacao;

    private LocalDate dataFim;

    private String desc;

}
