package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.InspectionRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "inspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    private LocalDate data_inspecao;

    private String descricao;

    private boolean danificado = false;

    public Inspection(Rental rental, InspectionRequest request){
        this.rental = rental;
        this.data_inspecao = request.data_inspecao();
        this.descricao = request.descricao();
    }
}
