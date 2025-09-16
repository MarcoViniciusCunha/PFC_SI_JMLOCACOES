package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.CategoryRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String descricao;

    public Category(CategoryRequest categoryRequest) {

        this.nome = categoryRequest.nome();
        this.descricao = categoryRequest.descricao();

    }
}
