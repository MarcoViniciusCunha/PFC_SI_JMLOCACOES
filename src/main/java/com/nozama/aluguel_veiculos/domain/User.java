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
@EqualsAndHashCode(of = "username") // gera equals e hash baseados em email
public class User {

    // email pk
    @Id
    private String username;

    private String password;

    public User(UserRequest userRequest) {
        this.username = userRequest.username();

        this.password = userRequest.password();

    }
}
