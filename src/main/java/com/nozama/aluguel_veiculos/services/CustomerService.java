package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CepService cepService;
    private final RentalRepository rentalRepository;

    public Customer create(CustomerRequest request){
        Customer customer = new Customer(request);
        fillAddress(customer, request.cep());
        try {
            return repository.save(customer);
        } catch (DataIntegrityViolationException e){
            throw new RuntimeException("CNH, CPF ou email já cadastrado!");
        }
    }

    public List<Customer> getAll(){
        return repository.findAll();
    }

    public Customer getById(Long id){
        return findByIdOrThrow(id);
    }

    public List<Customer> getByName(String nome){
       List<Customer> customers = repository.findByNomeContainingIgnoreCase(nome);
       if (customers.isEmpty()){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe cliente com esse nome.");
       }
       return customers;
    }

    public Customer update(Long id, CustomerRequest.update request){
        Customer existing = findByIdOrThrow(id);

        if(request.cnh() != null) existing.setCnh(request.cnh());
        if(request.nome() != null) existing.setNome(request.nome());
        if(request.cpf() != null) existing.setCpf(request.cpf());
        if(request.email() != null) existing.setEmail(request.email());
        if(request.telefone() != null) existing.setTelefone(request.telefone());
        if(request.numero() != null) existing.setNumero(request.numero());
        if(request.data_nasc() != null) existing.setData_nasc(request.data_nasc());
        if(request.cep() != null){
            existing.setCep(request.cep());
            fillAddress(existing, request.cep());
        }

        try {
            return repository.save(existing);
        } catch (DataIntegrityViolationException e){
            throw new RuntimeException("CNH, CPF ou email já cadastrado!");
        }
    }

    public void delete(Long id) {
        Customer existing = findByIdOrThrow(id);

        boolean possuiLocacoes = rentalRepository.existsByCustomerId(id);
        if (possuiLocacoes) {
            throw new IllegalStateException("Cliente possui locações e não pode ser excluído");
        }

        repository.delete(existing);
    }

    private Customer findByIdOrThrow(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    private void fillAddress(Customer customer, String cep){
        if(cep != null && !cep.isEmpty()){
            try {
                Map<String, String> endereco = cepService.buscarEndereco(cep);
                if(endereco.containsKey("erro")){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP inválido");
                }
                customer.setRua(endereco.get("logradouro"));
                customer.setCidade(endereco.get("localidade"));
                customer.setEstado(endereco.get("uf"));
            } catch(Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao buscar endereço");
            }
        }
    }

}
