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

        if (Boolean.TRUE.equals(endereco.get("erro"))) {
            return ResponseEntity.badRequest().body(
                    Map.of("erro", endereco.get("mensagem"))
            );
        }

        return ResponseEntity.ok(endereco);
    }
}
