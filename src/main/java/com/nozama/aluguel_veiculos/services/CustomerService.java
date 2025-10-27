package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerPatchRequest;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CepService cepService;

    public CustomerService(CustomerRepository repository, CepService cepService) {
        this.repository = repository;
        this.cepService = cepService;
    }

    public Customer create(CustomerRequest request){
        Customer customer = new Customer(request);

        if(request.cep() != null && !request.cep().isEmpty()){
            try {
                Map<String, String> endereco = cepService.buscarEndereco(request.cep());
                if (endereco.containsKey("erro")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP inválido");
                }
                customer.setRua((String)endereco.get("logradouro"));
                customer.setCidade((String)endereco.get("localidade"));
                customer.setEstado((String)endereco.get("uf"));
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao buscar endereço");
            }
        }

        try {
            return repository.save(customer);
        } catch (DataIntegrityViolationException e){
            throw new RuntimeException("CNH, CPF ou email já cadatrado!");
        }

    }

    public List<Customer> getAll(){
        return repository.findAll();
    }

    public Customer getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    public List<Customer> getByName(String nome){
       List<Customer> customers = repository.findByNomeContainingIgnoreCase(nome);
       if (customers.isEmpty()){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe cliente com esse nome.");
       }
       return customers;
    }

    public Customer update(Long id, CustomerPatchRequest request){
        Customer existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));

        if (request.cnh() != null) {
            existing.setCnh(request.cnh());
        }
        if (request.nome() != null) {
            existing.setNome(request.nome());
        }
        if (request.cpf() != null) {
            existing.setCpf(request.cpf());
        }
        if (request.email() != null) {
            existing.setEmail(request.email());
        }
        if (request.telefone() != null) {
            existing.setTelefone(request.telefone());
        }
        if (request.numero() != null) {
            existing.setNumero(request.numero());
        }
        if (request.data_nasc() != null) {
            existing.setData_nasc(request.data_nasc());
        }

        if (request.cep() != null) {
            existing.setCep(request.cep());
            try {
                Map<String, String> endereco = cepService.buscarEndereco(request.cep());
                if (endereco.containsKey("erro")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP inválido");
                }
                existing.setRua((String) endereco.get("logradouro"));
                existing.setCidade((String)endereco.get("localidade"));
                existing.setEstado((String)endereco.get("uf"));
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao buscar endereço");
            }
        }

        try {
            return repository.save(existing);
        } catch (DataIntegrityViolationException e){
            throw new RuntimeException("CNH, CPF ou email já cadastrado!");
        }

    }

    public void delete(Long id){
        Customer existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
        repository.delete(existing);
    }

}
