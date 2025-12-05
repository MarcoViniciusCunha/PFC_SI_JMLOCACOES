package locacoes.example.demo.domain;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.utils.HashUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

class CustomerTest {

    @Test
    void testCustomerConstructorAndGetters() {

        try (MockedStatic<HashUtils> mockHash = Mockito.mockStatic(HashUtils.class)) {

            mockHash.when(() -> HashUtils.hmacSha256Base64(anyString()))
                    .thenReturn("hash-falso");

            CustomerRequest request = new CustomerRequest(
                    "123456789", "Murillo", "123.456.789-00",
                    "murillo@email.com", "11999999999",
                    "01234-567", "123", "Rua Teste",
                    "Cidade Teste", "Estado Teste",
                    LocalDate.of(1995, 5, 12)
            );

            Customer customer = new Customer(request);
            customer.setId(1L);

            assertEquals("Murillo", customer.getNome());
            assertEquals("123456789", customer.getCnh()); // número limpo
        }
    }

    @Test
    void testCustomerSetters() {

        try (MockedStatic<HashUtils> mockHash = Mockito.mockStatic(HashUtils.class)) {

            mockHash.when(() -> HashUtils.hmacSha256Base64(anyString()))
                    .thenReturn("hash-falso");

            Customer c = new Customer();

            c.setId(2L);
            c.setNome("João");
            c.setCnh("987654321");

            assertEquals("987654321", c.getCnh());
        }
    }
}
