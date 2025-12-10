package com.nozama.aluguel_veiculos.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.domain.enums.PaymentStatus;
import com.nozama.aluguel_veiculos.domain.enums.RentalStatus;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import com.nozama.aluguel_veiculos.repository.PaymentRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.utils.HashUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CepService cepService;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    // -------------------- MÉTODOS PÚBLICOS --------------------

    public Customer create(CustomerRequest request) {
        String cpfHash = HashUtils.hmacSha256Base64(request.cpf());
        repository.findByCpfHash(cpfHash).ifPresent(c -> {
            throw new DataIntegrityViolationException("CPF já cadastrado");
        });

        Customer customer = new Customer(request);

        if (request.rua() == null || request.rua().isBlank()) {
            fillAddress(customer, request.cep());
        }

        try {
            return repository.save(customer);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("CNH, CPF ou email já cadastrado!");
        }
    }

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public Customer getById(Long id) {
        return findByIdOrThrow(id);
    }

    public List<Customer> getByName(String nome) {
        List<Customer> customers = repository.findByNomeContainingIgnoreCase(nome);
        if (customers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe cliente com esse nome.");
        }
        return customers;
    }

    public Customer update(Long id, CustomerRequest.update request) {
        Customer existing = findByIdOrThrow(id);

        if (request.cpf() != null) {
            if (request.cpf().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF não pode ser vazio");
            }
            String cleanCpf = request.cpf().replaceAll("\\D", "");
            if (!cleanCpf.matches("\\d{11}")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve ter 11 dígitos");
            }
            String cpfHash = HashUtils.hmacSha256Base64(cleanCpf);
            repository.findByCpfHash(cpfHash).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new DataIntegrityViolationException("CPF já cadastrado");
                }
            });
            existing.setCpf(cleanCpf);
        }

        if (request.cnh() != null) {
            if (request.cnh().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNH não pode ser vazia");
            }
            String cleanCnh = request.cnh().replaceAll("\\D", "");
            if (!cleanCnh.matches("\\d{11}")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNH deve ter 11 dígitos");
            }
            existing.setCnh(cleanCnh);
        } else if (existing.getCnh() == null || existing.getCnh().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNH não pode ser nula");
        }

        if (request.nome() != null) {
            if (request.nome().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome não pode ser vazio");
            }
            existing.setNome(request.nome());
        }

        if (request.email() != null) {
            if (request.email().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email não pode ser vazio");
            }
            String emailLower = request.email().toLowerCase();
            repository.findByEmail(emailLower).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new DataIntegrityViolationException("Email já cadastrado");
                }
            });
            existing.setEmail(emailLower);
        }

        if (request.telefone() != null) {
            String cleanTel = request.telefone().replaceAll("\\D", "");
            if (!cleanTel.matches("\\d{10,11}")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone inválido");
            }
            existing.setTelefone(cleanTel);
        }

        if (request.cep() != null) {
            if (request.cep().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP não pode ser vazio");
            }
            existing.setCep(request.cep());
            fillAddress(existing, request.cep());
        }

        if (request.numero() != null) {
            if (request.numero().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número não pode ser vazio");
            }
            existing.setNumero(request.numero());
        }

        if (request.data_nasc() != null) {
            existing.setData_nasc(request.data_nasc());
        }

        return repository.save(existing);
    }



    public void delete(Long id) {
        Customer existing = findByIdOrThrow(id);

        if (possuiLocacoesAtivas(id)) {
            throw new IllegalStateException("Cliente possui locações ativas/atrasadas/não iniciadas e não pode ser excluído.");
        }

        if (possuiLocacoesNaoPagas(id)) {
            throw new IllegalStateException("Cliente possui locações devolvidas não pagas ou pagamentos pendentes e não pode ser excluído.");
        }

        existing.setNome("Cliente Removido");
        existing.setEmail("anon" + existing.getId() + "@cliente-removido.com");
        existing.setTelefone("");
        existing.setCnh(null);
        existing.setRua("");
        existing.setCep("");
        existing.setNumero("");
        existing.setEstado("");
        existing.setAtivo(false);

        repository.save(existing);
    }



    public void desativar(Long id) {
        Customer existing = findByIdOrThrow(id);

        if (possuiLocacoesAtivas(id)) {
            throw new IllegalStateException("Cliente possui locações ativas/atrasadas/não iniciadas.");
        }

        if (possuiLocacoesNaoPagas(id)) {
            throw new IllegalStateException("Cliente possui locações devolvidas não pagas ou pagamentos pendentes.");
        }

        existing.setAtivo(false);
        repository.save(existing);
    }


    public void ativar(Long id) {
        Customer existing = findByIdOrThrow(id);
        if (existing.isAtivo()) return;
        existing.setAtivo(true);
        repository.save(existing);
    }

    // -------------------- MÉTODOS AUXILIARES --------------------
    private Customer findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    private void fillAddress(Customer customer, String cep) {
        if (cep != null && !cep.isEmpty()) {
            try {
                Map<String, Object> endereco = cepService.buscarEndereco(cep);
                if (endereco.containsKey("erro")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP inválido");
                }
                customer.setRua((String) endereco.get("logradouro"));
                customer.setCidade((String) endereco.get("localidade"));
                customer.setEstado((String) endereco.get("uf"));
            } catch (RestClientException e) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de CEP indisponível");
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao buscar endereço");
            }
        }
    }

    public List<Customer> getAllAtivos() {
        return repository.findAllByAtivoTrue();
    }

    public List<Customer> getByNameAtivos(String nome) {
        return repository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
    }

    public List<Customer> getAllPrincipal() {
        return repository.findAllByOrderByAtivoDescNomeAsc();
    }

    // -------------------- MÉTODOS DE VERIFICAÇÃO --------------------


    private boolean possuiLocacoesAtivas(Long customerId) {
        return rentalRepository.existsByCustomerIdAndStatusIn(
                customerId,
                List.of(RentalStatus.ATIVA, RentalStatus.ATRASADA, RentalStatus.NAO_INICIADA)
        );
    }

    private boolean possuiLocacoesNaoPagas(Long customerId) {
        return rentalRepository.existsDevolvidasNaoPagas(customerId);
    }
}
