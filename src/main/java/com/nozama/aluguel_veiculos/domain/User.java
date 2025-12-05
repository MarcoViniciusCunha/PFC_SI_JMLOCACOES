package com.nozama.aluguel_veiculos.domain;

import com.nozama.aluguel_veiculos.dto.UserRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
public class User {

    @Id
    private String username;

    private String password;

    public User(UserRequest userRequest) {
        this.username = userRequest.username();
        this.password = userRequest.password();

    }
}
