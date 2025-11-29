package com.nozama.aluguel_veiculos.controllers;

import com.nozama.aluguel_veiculos.services.CepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cep")
@RequiredArgsConstructor
public class CepController {

    private final CepService cepService;

    @GetMapping("/{cep}")
    public ResponseEntity<?> buscar(@PathVariable String cep) {
        Map<String, Object> endereco = cepService.buscarEndereco(cep);

        if (endereco.containsKey("erro")) {
            return ResponseEntity.badRequest().body(Map.of("erro", "CEP inválido"));
        }

        return ResponseEntity.ok(endereco);
    }
}
