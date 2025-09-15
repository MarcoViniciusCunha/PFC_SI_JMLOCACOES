package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.InsuranceRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "insurance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String empresa;

    private BigDecimal valor;

    private LocalDate validade;

    public Insurance(InsuranceRequest request) {
        this.empresa = request.empresa();
        this.valor = request.valor();
        this.validade = request.validade();
    }
}


