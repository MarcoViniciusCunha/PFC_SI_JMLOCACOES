package com.nozama.aluguel_veiculos.config;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.converter.CryptoConverter;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Base64;

//@Component
public class EncryptExistingCustomers {
    private final CustomerRepository repo;
    private final CryptoConverter cryptoConverter = new CryptoConverter();

    public EncryptExistingCustomers(CustomerRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void run() {
        System.out.println(">> Atualizando criptografia e hashes dos clientes existentes...");

        var customers = repo.findAll();

        for (Customer c : customers) {

            if (c.getCpf() != null) {
                try {
                    // tentar descriptografar
                    String originalCpf = cryptoConverter.convertToEntityAttribute(c.getCpf());
                    c.setCpf(originalCpf);
                } catch (Exception e) {
                    // se não conseguir descriptografar, dado já está puro
                    c.setCpf(c.getCpf());
                }
            }

            // CNH
            if (c.getCnh() != null) {
                try {
                    String originalCnh = cryptoConverter.convertToEntityAttribute(c.getCnh());
                    c.setCnh(originalCnh);
                } catch (Exception e) {
                    c.setCnh(c.getCnh());
                }
            }

            repo.save(c);
        }

        System.out.println(">> Clientes atualizados com sucesso!");
    }

    private boolean isBase64(String value) {
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
