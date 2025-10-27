package com.nozama.aluguel_veiculos.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CepService {

    private final String viaCepUrl;

    public CepService(@Value("${viacep.url}") String viaCepUrl){
        this.viaCepUrl = viaCepUrl;
    }

    public Map<String, String> buscarEndereco(String cep){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(viaCepUrl, Map.class, cep);
    }
}
