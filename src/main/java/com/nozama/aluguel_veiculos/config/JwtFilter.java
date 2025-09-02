package com.nozama.aluguel_veiculos.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private Jwt jwt; // classe q gera tokens


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
        throws ServletException, IOException {

        // pega o cabeçalho authorization de onde o token vem
        String authHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        // verifica se o cabeçalho existe e se começa com bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // remove o bearer
            email = jwt.getEmailFromToken(token); // extrai email do token
        }

        // se o token troxe email e o user ainda n esta autenticado
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // valida se o token é valido
            if (jwt.validateToken(token)) {
                // cria uma autenticação baseada no email
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, null);
                // adiciona detalhes da requisição na autenticação
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // coloca a autenticação no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // continua o fluxo da req
        filterChain.doFilter(request, response);
    }
}
