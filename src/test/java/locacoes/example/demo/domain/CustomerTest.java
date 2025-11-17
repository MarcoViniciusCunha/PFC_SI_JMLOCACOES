package locacoes.example.demo.domain;

import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {

    @Test
    void testCustomerConstructorAndGetters() {
        CustomerRequest request = new CustomerRequest(
                "123456789", "Murillo", "123.456.789-00",
                "murillo@email.com", "11999999999",
                "01234-567", "123", "Rua Teste",
                "Cidade Teste", "Estado Teste",
                LocalDate.of(1995, 5, 12)
        );

        Customer customer = new Customer(request);
        customer.setId(1L);

        assertEquals(1L, customer.getId());
        assertEquals("Murillo", customer.getNome());
        assertEquals("123456789", customer.getCnh());

        System.out.println("Customer ID: " + customer.getId());
        System.out.println("Customer Nome: " + customer.getNome());
        System.out.println("Customer CNH: " + customer.getCnh());
        System.out.println("Customer Email: " + customer.getEmail());
        System.out.println("Customer Data Nasc: " + customer.getData_nasc());
    }

    @Test
    void testCustomerSetters() {
        Customer customer = new Customer();
        customer.setId(2L);
        customer.setNome("João");
        customer.setCnh("987654321");

        assertEquals(2L, customer.getId());
        assertEquals("João", customer.getNome());
        assertEquals("987654321", customer.getCnh());

        System.out.println("Customer atualizado ID: " + customer.getId());
        System.out.println("Customer atualizado Nome: " + customer.getNome());
        System.out.println("Customer atualizado CNH: " + customer.getCnh());
    }
}
