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

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer create(CustomerRequest request){
        Customer customer = new Customer(request);
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
        if (request.endereco() != null) {
            existing.setEndereco(request.endereco());
        }
        if (request.data_nasc() != null) {
            existing.setData_nasc(request.data_nasc());
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
