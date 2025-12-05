package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.CustomerController;
import com.nozama.aluguel_veiculos.domain.Customer;
import com.nozama.aluguel_veiculos.dto.CustomerRequest;
import com.nozama.aluguel_veiculos.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService service;

    @InjectMocks
    private CustomerController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAll() {
        Customer c1 = new Customer();
        c1.setId(1L);
        c1.setNome("Alice");

        Customer c2 = new Customer();
        c2.setId(2L);
        c2.setNome("Bob");

        when(service.getAll()).thenReturn(List.of(c1, c2));

        ResponseEntity<?> response = controller.getAll();

        System.out.println("GetAll returned count: " + ((List<?>) response.getBody()).size());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, ((List<?>) response.getBody()).size());
    }

    @Test
    void testDelete() {
        doNothing().when(service).delete(1L);

        ResponseEntity<?> response = controller.delete(1L);

        System.out.println("Delete returned: " + response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).delete(1L);
    }
}
