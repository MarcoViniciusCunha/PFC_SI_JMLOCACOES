package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.UserRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity // mapeada como tabela no bd
@Table(name = "app_user") // nome da tabela usada no bd
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "email") // gera equals e hash baseados em email
public class User {

    // email pk
    @Id
    private String email;

    private String password;

    public User(UserRequest userRequest) {
        this.email = userRequest.email();

        this.password = userRequest.password();

    }
}
