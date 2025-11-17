package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.CustomerController;
import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CustomerService customerService;
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        customerService = Mockito.mock(CustomerService.class);
        customerController = new CustomerController(customerService);
    }

    @Test
    void testGetAllCustomers() {
        Customer expected = new Customer(1L, "123456789", "Murillo", "12345678901",
                "murillo@email.com", "11999999999", "01234567", "123", "Rua A",
                "Cidade B", "Estado C", LocalDate.of(2000, 1, 1));

        when(customerService.getAll()).thenReturn(List.of(expected));

        ResponseEntity<List<Customer>> response = customerController.getAll();
        List<Customer> actualList = response.getBody();

        System.out.println("testGetAllCustomers -> Response: " + actualList);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, actualList.size());
        assertEquals(expected.getNome(), actualList.get(0).getNome());
    }

    @Test
    void testGetCustomerById() {
        Customer expected = new Customer(1L, "123456789", "Murillo", "12345678901",
                "murillo@email.com", "11999999999", "01234567", "123", "Rua A",
                "Cidade B", "Estado C", LocalDate.of(2000, 1, 1));

        when(customerService.getById(1L)).thenReturn(expected);

        ResponseEntity<Customer> response = customerController.getById(1L);
        Customer actual = response.getBody();

        System.out.println("testGetCustomerById -> Response: " + actual);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected.getNome(), actual.getNome());
    }

    @Test
    void testGetByName() {
        Customer expected = new Customer(1L, "123456789", "Murillo", "12345678901",
                "murillo@email.com", "11999999999", "01234567", "123", "Rua A",
                "Cidade B", "Estado C", LocalDate.of(2000, 1, 1));

        when(customerService.getByName("Murillo")).thenReturn(List.of(expected));

        ResponseEntity<List<Customer>> response = customerController.getByName("Murillo");
        List<Customer> actualList = response.getBody();

        System.out.println("testGetByName -> Response: " + actualList);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, actualList.size());
        assertEquals(expected.getNome(), actualList.get(0).getNome());
    }

    @Test
    void testUpdateCustomer() {
        CustomerRequest.update updateRequest = new CustomerRequest.update(
                "123456789", "Murillão", "12345678901",
                "murillo@email.com", "11999999999", "01234567",
                "123", "Rua A", "Cidade B", "Estado C",
                LocalDate.of(2000, 1, 1)
        );

        Customer updated = new Customer(1L, "123456789", "Murillão", "12345678901",
                "murillo@email.com", "11999999999", "01234567", "123", "Rua A",
                "Cidade B", "Estado C", LocalDate.of(2000, 1, 1));

        when(customerService.update(eq(1L), any(CustomerRequest.update.class))).thenReturn(updated);

        ResponseEntity<Customer> response = customerController.update(1L, updateRequest);
        Customer actual = response.getBody();

        System.out.println("testUpdateCustomer -> Response: " + actual);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Murillão", actual.getNome());
    }

    @Test
    void testDeleteCustomer() {
        doNothing().when(customerService).delete(1L);

        ResponseEntity<Void> response = customerController.delete(1L);

        System.out.println("testDeleteCustomer -> Status: " + response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());
        verify(customerService, times(1)).delete(1L);
    }
}
