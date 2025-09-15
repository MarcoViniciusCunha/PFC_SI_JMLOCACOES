package com.nozama.aluguel_veiculos.domain.mark;

import com.nozama.aluguel_veiculos.dto.MarkRequest;
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
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    public Mark(MarkRequest request) {
        this.nome = request.nome();
    }
}
