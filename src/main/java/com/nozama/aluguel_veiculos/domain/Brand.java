package com.nozama.aluguel_veiculos.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nozama.aluguel_veiculos.dto.BrandRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Model> modelos;

    public Brand(BrandRequest request) {
        this.nome = request.nome();
    }
}
