package com.nozama.aluguel_veiculos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity // segurança web do spring security
public class SecurityConfig {

    // codificar senhas com bcrypt e mandar para o banco
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // config de segurança da rotas
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // desabilita o csrf
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**", "/login").permitAll() // define rotas user e login n precisam autenticar
                        .anyRequest().authenticated() // resto das rotas precisa de autenticação
                );
        return http.build(); // constrói e retorna filtro de segurança
    }
}
