package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.repository.CustomerRepository;
import com.nozama.aluguel_veiculos.repository.RentalRepository;
import com.nozama.aluguel_veiculos.services.CepService;
import com.nozama.aluguel_veiculos.services.CustomerService;
import com.nozama.aluguel_veiculos.utils.HashUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CepService cepService;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private CustomerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarClienteComCepValido() {

        try (MockedStatic<HashUtils> mockHash = Mockito.mockStatic(HashUtils.class)) {

            mockHash.when(() -> HashUtils.hmacSha256Base64(anyString()))
                    .thenReturn("hash-falso");

            CustomerRequest request = mock(CustomerRequest.class);

            when(request.nome()).thenReturn("Fulano da Silva");
            when(request.email()).thenReturn("teste@gmail.com");
            when(request.cpf()).thenReturn("12345678901");
            when(request.cnh()).thenReturn("12345678901");
            when(request.telefone()).thenReturn("11999999999");
            when(request.numero()).thenReturn("100");
            when(request.data_nasc()).thenReturn(LocalDate.of(2000, 1, 1));
            when(request.cep()).thenReturn("01001000");
            when(request.rua()).thenReturn(null);

            when(cepService.buscarEndereco("01001000")).thenReturn(Map.of(
                    "logradouro", "Praça da Sé",
                    "localidade", "São Paulo",
                    "uf", "SP"
            ));

            Customer salvo = new Customer(request);

            when(repository.save(any(Customer.class))).thenReturn(salvo);

            Customer resultado = service.create(request);

            assertNotNull(resultado);
            assertEquals("12345678901", resultado.getCpf());
            assertEquals("teste@gmail.com", resultado.getEmail());
        }
    }


    @Test
    void deveLancarExcecaoAoCriarClienteComCepInvalido() {
        try (MockedStatic<HashUtils> mockHash = Mockito.mockStatic(HashUtils.class)) {

            mockHash.when(() -> HashUtils.hmacSha256Base64(anyString()))
                    .thenReturn("hash-falso");

            CustomerRequest request = mock(CustomerRequest.class);

            when(request.email()).thenReturn("teste@gmail.com");
            when(request.nome()).thenReturn("Fulano");
            when(request.cpf()).thenReturn("12345678901");
            when(request.cnh()).thenReturn("12345678901");
            when(request.telefone()).thenReturn("11999999999");
            when(request.numero()).thenReturn("10");
            when(request.data_nasc()).thenReturn(LocalDate.of(2000,1,1));

            when(request.cep()).thenReturn("00000000");

            when(cepService.buscarEndereco("00000000"))
                    .thenThrow(new RuntimeException("CEP inválido"));

            assertThrows(ResponseStatusException.class, () -> service.create(request));
        }
    }


    @Test
    void deveRetornarTodosClientes() {
        List<Customer> lista = List.of(new Customer(), new Customer());

        when(repository.findAll()).thenReturn(lista);

        List<Customer> resultado = service.getAll();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void deveRetornarClientePorId() {
        Customer cliente = new Customer();

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        Customer resultado = service.getById(1L);

        assertNotNull(resultado);
        assertEquals(cliente, resultado);
    }

    @Test
    void deveLancarExcecaoSeClienteNaoExistirPorId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.getById(1L));
    }

    @Test
    void deveDeletarCliente() {
        Customer cliente = new Customer();

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        when(rentalRepository.existsByCustomerId(1L)).thenReturn(false);

        service.delete(1L);

        verify(repository).delete(cliente);
    }
}
