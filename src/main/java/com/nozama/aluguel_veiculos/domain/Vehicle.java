package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.VehicleRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    private String placa;

    @ManyToOne
    @JoinColumn(name = "id_mark", nullable = false)
    private Mark mark;

    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private Model model;

    private Integer ano;

    @ManyToOne
    @JoinColumn(name = "id_cor", nullable = false)
    private Color color;

    private String status;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_seguro")
    private Insurance insurance;

    public Vehicle(VehicleRequest request, Category category, Insurance insurance, Mark mark, Color color, Model model){
        this.placa = request.placa();

        this.mark = mark;

        this.model = model;

        this.ano = request.ano();

        this.color = color;

        this.status = request.status();

        this.descricao = request.descricao();

        this.category = category;

        this.insurance = insurance;
    }
}
