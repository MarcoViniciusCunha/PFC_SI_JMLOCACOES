package locacoes.example.demo.controllers;

import com.nozama.aluguel_veiculos.controllers.InsuranceController;
import com.nozama.aluguel_veiculos.domain.Insurance;
import com.nozama.aluguel_veiculos.domain.InsuranceCompany;
import com.nozama.aluguel_veiculos.dto.InsuranceRequest;
import com.nozama.aluguel_veiculos.services.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InsuranceControllerTest {

    @Mock
    private InsuranceService service;

    @InjectMocks
    private InsuranceController controller;

    private Insurance insurance1;
    private Insurance insurance2;
    private InsuranceCompany company1;
    private InsuranceCompany company2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        company1 = new InsuranceCompany(1, "Porto Seguro", "contato@porto.com");
        company2 = new InsuranceCompany(2, "Bradesco Seguros", "contato@bradesco.com");

        insurance1 = new Insurance(1L, company1, BigDecimal.valueOf(1200.00), LocalDate.of(2026, 5, 10));
        insurance2 = new Insurance(2L, company2, BigDecimal.valueOf(980.00), LocalDate.of(2025, 12, 1));
    }

    @Test
    void testCreateInsurance() {
        InsuranceRequest request = new InsuranceRequest(3, BigDecimal.valueOf(1100.00), LocalDate.of(2026, 8, 20));
        InsuranceCompany newCompany = new InsuranceCompany(3, "SulAmérica", "contato@sulamerica.com");
        Insurance created = new Insurance(3L, newCompany, BigDecimal.valueOf(1100.00), LocalDate.of(2026, 8, 20));

        when(service.create(request)).thenReturn(created);

        ResponseEntity<Insurance> response = controller.create(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("SulAmérica", response.getBody().getCompany().getName());
        assertEquals(BigDecimal.valueOf(1100.00), response.getBody().getValor());
    }

    @Test
    void testGetAllInsurance() {
        List<Insurance> list = Arrays.asList(insurance1, insurance2);
        when(service.findAll()).thenReturn(list);

        ResponseEntity<List<Insurance>> response = controller.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetById() {
        when(service.getById(1L)).thenReturn(insurance1);

        ResponseEntity<Insurance> response = controller.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Porto Seguro", response.getBody().getCompany().getName());
    }

    @Test
    void testUpdateInsurance() {
        Map<String, Object> updates = Map.of("valor", BigDecimal.valueOf(1300.00));
        Insurance updated = new Insurance(1L, company1, BigDecimal.valueOf(1300.00), LocalDate.of(2026, 5, 10));

        when(service.update(1L, updates)).thenReturn(updated);

        ResponseEntity<Insurance> response = controller.update(1L, updates);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(1300.00), response.getBody().getValor());
    }

    @Test
    void testDeleteInsurance() {
        doNothing().when(service).deleteByID(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(service, times(1)).deleteByID(1L);
    }
}
