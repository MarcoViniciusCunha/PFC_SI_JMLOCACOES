package com.nozama.aluguel_veiculos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "location")
@Getter
@Setter
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cnh_cliente", nullable = false)
    private String cnhCliente;

    @Column(name = "placa_veiculo", nullable = false)
    private String placaVeiculo;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_est_fim", nullable = false)
    private LocalDate dataEstFim;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    public Location() {
    }

    public Location(String cnhCliente, String placaVeiculo, LocalDate dataInicio, LocalDate dataEstFim, Double valorTotal) {
        this.cnhCliente = cnhCliente;
        this.placaVeiculo = placaVeiculo;
        this.dataInicio = dataInicio;
        this.dataEstFim = dataEstFim;
        this.valorTotal = valorTotal;
    }
}
