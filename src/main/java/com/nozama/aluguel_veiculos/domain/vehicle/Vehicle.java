package com.nozama.aluguel_veiculos.domain.vehicle;

import com.nozama.aluguel_veiculos.domain.category.Category;
import com.nozama.aluguel_veiculos.domain.insurence.Insurance;
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

    private String marca;

    private String modelo;

    private Integer ano;

    private String cor;

    private String status;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_seguro")
    private Insurance insurance;

    public Vehicle(VehicleRequest request, Category category, Insurance insurance){
        this.placa = request.placa();

        this.marca = request.marca();

        this.modelo = request.modelo();

        this.ano = request.ano();

        this.cor = request.cor();

        this.status = request.status();

        this.descricao = request.descricao();

        this.category = category;

        this.insurance = insurance;
    }
}
