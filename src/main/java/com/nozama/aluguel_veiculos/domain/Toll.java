package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.TollRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Toll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rodovia;
    private String cidade;
    private BigDecimal valor;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

}
