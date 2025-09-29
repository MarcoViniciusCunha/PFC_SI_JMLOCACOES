package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.BrandRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mark")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    public Brand(BrandRequest request) {
        this.nome = request.nome();
    }
}
