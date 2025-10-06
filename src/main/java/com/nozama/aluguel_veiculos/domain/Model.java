package com.nozama.aluguel_veiculos.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nozama.aluguel_veiculos.dto.ModelRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonBackReference
    private Brand brand;

    public Model(ModelRequest request, Brand brand) {
        this.nome = request.nome();
        this.brand = brand;
    }
}
