package com.nozama.aluguel_veiculos.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CepService {

    @Value("${viacep.url}")
    private String viaCepUrl;

    public Map<String, Object> buscarEndereco(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            return Map.of("erro", true, "mensagem", "CEP inválido");
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(viaCepUrl.replace("{cep}", cep), Map.class);
        } catch (Exception e) {
            return Map.of("erro", true, "mensagem", "Erro ao consultar ViaCEP");
        }
    }
}
