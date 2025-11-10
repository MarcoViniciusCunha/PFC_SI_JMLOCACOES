package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import com.nozama.aluguel_veiculos.services.CepService;
import com.nozama.aluguel_veiculos.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CepService cepService;

    @InjectMocks
    private CustomerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- Teste do CREATE ---
    @Test
    void deveCriarClienteComCepValido() {
        CustomerRequest request = mock(CustomerRequest.class);
        when(request.cep()).thenReturn("01001000");
        when(cepService.buscarEndereco("01001000")).thenReturn(Map.of(
                "logradouro", "Praça da Sé",
                "localidade", "São Paulo",
                "uf", "SP"
        ));

        Customer expected = new Customer(request);
        when(repository.save(any(Customer.class))).thenReturn(expected);

        Customer actual = service.create(request);

        System.out.println("✅ Expected: " + expected);
        System.out.println("✅ Actual:   " + actual);

        assertEquals(expected, actual, "O cliente retornado deve ser igual ao esperado");
        verify(repository).save(any(Customer.class));
        verify(cepService).buscarEndereco("01001000");
    }

    @Test
    void deveLancarExcecaoAoCriarClienteComCepInvalido() {
        CustomerRequest request = mock(CustomerRequest.class);
        when(request.cep()).thenReturn("00000000");
        when(cepService.buscarEndereco("00000000")).thenThrow(new RuntimeException("Erro ao buscar endereço"));

        Exception exception = assertThrows(ResponseStatusException.class, () -> service.create(request));

        System.out.println("❌ Expected: ResponseStatusException");
        System.out.println("❌ Actual:   " + exception.getClass().getSimpleName());
    }

    // --- Teste do GET ALL ---
    @Test
    void deveRetornarTodosClientes() {
        List<Customer> expected = List.of(new Customer(), new Customer());
        when(repository.findAll()).thenReturn(expected);

        List<Customer> actual = service.getAll();

        System.out.println("✅ Expected size: " + expected.size());
        System.out.println("✅ Actual size:   " + actual.size());

        assertEquals(expected.size(), actual.size());
        verify(repository).findAll();
    }

    // --- Teste do GET BY ID ---
    @Test
    void deveRetornarClientePorId() {
        Customer expected = new Customer();
        when(repository.findById(1L)).thenReturn(Optional.of(expected));

        Customer actual = service.getById(1L);

        System.out.println("✅ Expected: " + expected);
        System.out.println("✅ Actual:   " + actual);

        assertEquals(expected, actual);
        verify(repository).findById(1L);
    }

    @Test
    void deveLancarExcecaoSeClienteNaoExistirPorId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Exception e = assertThrows(ResponseStatusException.class, () -> service.getById(1L));

        System.out.println("❌ Expected: ResponseStatusException");
        System.out.println("❌ Actual:   " + e.getClass().getSimpleName());
    }

    // --- Teste do DELETE ---
    @Test
    void deveDeletarCliente() {
        Customer existing = new Customer();
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        System.out.println("✅ Expected: delete() chamado com o cliente existente");
        verify(repository).delete(existing);
    }
}
