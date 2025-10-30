package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.FineRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    private String descricao;

    private BigDecimal valor;

    private LocalDate data_multa;

    public Fine (Rental rental, FineRequest fineRequest) {
        this.rental = rental;
        this.descricao = fineRequest.descricao();
        this.valor = fineRequest.valor();
        this.data_multa = fineRequest.dataMulta();
    }

}
