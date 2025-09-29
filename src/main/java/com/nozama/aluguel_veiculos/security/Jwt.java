package com.nozama.aluguel_veiculos.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // Spring injeta essa classe em outros beans
public class Jwt {

    // Gera uma chave secreta unica com o algoritmo 256
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // gera com base no email
    public String generateToken(String email) {
        //Define tempo de expiração do token
        long expiration = 1000 * 60 * 60;
        return Jwts.builder()
                .setSubject(email) // identificador do usuario
                .setIssuedAt(new Date()) // Data de emissão do token
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Data q expira
                .signWith(secretKey) //Coloca a chave secreta no token
                .compact(); // Constrói o token final em string
    }

    // recupera o email do token
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // verifica se o token è válido
    public boolean validateToken(String token) {
        try {
            getClaims(token); // se n tiver exceção, é valido
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    // auxilia na extração dos claims
    private io.jsonwebtoken.Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // usa a chave para validar a assinatura
                .build()
                .parseClaimsJws(token) // analisa o token e valida assinatura
                .getBody(); // pega o corpo do token
    }

}
