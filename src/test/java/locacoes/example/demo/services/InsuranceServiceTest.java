package locacoes.example.demo.services;

import com.nozama.aluguel_veiculos.domain.Insurance;
import com.nozama.aluguel_veiculos.dto.InsuranceRequest;
import com.nozama.aluguel_veiculos.repository.InsuranceRepository;
import com.nozama.aluguel_veiculos.services.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsuranceServiceTest {

    @Mock
    private InsuranceRepository insuranceRepository;

    @InjectMocks
    private InsuranceService insuranceService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInsurance() {
        InsuranceRequest request = new InsuranceRequest("Porto Seguro", new BigDecimal("500.00"), LocalDate.now().plusYears(1));
        Insurance insurance = new Insurance(request);

        when(insuranceRepository.save(any(Insurance.class))).thenReturn(insurance);

        Insurance created = insuranceService.create(request);

        assertNotNull(created);
        assertEquals("Porto Seguro", created.getEmpresa());
        System.out.println("✅ Teste de criação passou! Seguro criado: " + created.getEmpresa());
    }

    @Test
    void testFindAllInsurance() {
        List<Insurance> insurances = List.of(
                new Insurance(new InsuranceRequest("SulAmérica", new BigDecimal("400.00"), LocalDate.now().plusYears(1))),
                new Insurance(new InsuranceRequest("Porto Seguro", new BigDecimal("600.00"), LocalDate.now().plusYears(2)))
        );

        when(insuranceRepository.findAll()).thenReturn(insurances);

        List<Insurance> result = insuranceService.findAll();

        assertEquals(2, result.size());
        System.out.println("📋 Teste de listagem passou! Total de seguros encontrados: " + result.size());
    }

    @Test
    void testGetByIdFound() {
        Insurance insurance = new Insurance(new InsuranceRequest("Bradesco", new BigDecimal("300.00"), LocalDate.now().plusYears(1)));

        when(insuranceRepository.findById(1)).thenReturn(Optional.of(insurance));

        Insurance result = insuranceService.getById(1);

        assertEquals("Bradesco", result.getEmpresa());
        System.out.println("🔎 Teste de busca passou! Seguro encontrado: " + result.getEmpresa());
    }

    @Test
    void testGetByIdNotFound() {
        when(insuranceRepository.findById(999)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> insuranceService.getById(999));
        System.out.println("⚠️  Teste de busca falhou como esperado: " + exception.getReason());
    }

    @Test
    void testUpdateInsurance() {
        Insurance insurance = new Insurance(new InsuranceRequest("Antiga", new BigDecimal("200.00"), LocalDate.now()));
        when(insuranceRepository.findById(1)).thenReturn(Optional.of(insurance));
        when(insuranceRepository.save(any(Insurance.class))).thenAnswer(i -> i.getArguments()[0]);

        Map<String, Object> updates = new HashMap<>();
        updates.put("empresa", "Nova Empresa");
        updates.put("valor", "999.99");
        updates.put("validade", LocalDate.now().plusYears(2).toString());

        Insurance updated = insuranceService.update(1, updates);

        assertEquals("Nova Empresa", updated.getEmpresa());
        assertEquals(new BigDecimal("999.99"), updated.getValor());
        System.out.println("🔁 Teste de atualização passou! Novo valor: " + updated.getValor());
    }

    @Test
    void testDeleteInsuranceSuccess() {
        when(insuranceRepository.existsById(1)).thenReturn(true);

        insuranceService.deleteByID(1);

        verify(insuranceRepository, times(1)).deleteById(1);
        System.out.println("🗑️  Teste de exclusão passou! Seguro removido com sucesso.");
    }

    @Test
    void testDeleteInsuranceNotFound() {
        when(insuranceRepository.existsById(999)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> insuranceService.deleteByID(999));
        System.out.println("⚠️  Teste de exclusão falhou como esperado: " + exception.getReason());
    }
}
